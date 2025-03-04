package com.example.ciphershare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile_view extends AppCompatActivity {
    TextView username,useremail,userpublickey;
    Button copypublic,viewprivatekey;
    DatabaseReference dbref;
    FirebaseAuth mAuth;
    String[] userprivatekey = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);


        useremail = findViewById(R.id.user_email);
        username = findViewById(R.id.user_name);
        userpublickey = findViewById(R.id.user_public_key);
        copypublic = findViewById(R.id.copy_public_key_button);
        viewprivatekey = findViewById(R.id.view_private_key_button);

        final String[] copytext = new String[1];
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            dbref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get user data
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String publicKey = dataSnapshot.child("publicKey").getValue(String.class);
                    userprivatekey[0] = dataSnapshot.child("privateKey").getValue(String.class);

                    // Set the values to TextViews
                    username.setText(name);
                    useremail.setText(email);
                    userpublickey.setText(publicKey);

                    copytext[0] = publicKey;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Toast.makeText(profile_view.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        copypublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copy the text to the clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", copytext[0]);
                clipboardManager.setPrimaryClip(clipData);

                // Show a toast message indicating the text has been copied
                Toast.makeText(profile_view.this, "Public key copied", Toast.LENGTH_SHORT).show();
            }
        });

        viewprivatekey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show dialog to enter password
                AlertDialog.Builder builder = new AlertDialog.Builder(profile_view.this);
                builder.setTitle("Enter Password");

                // Set up the input
                final EditText input = new EditText(profile_view.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        verifyPassword(password);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


// Method to print the private key

    }

    private void verifyPassword(String password) {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

        // Re-authenticate the user
        user.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Re-authentication successful
                        Toast.makeText(profile_view.this, "Re-authentication successful", Toast.LENGTH_SHORT).show();
                        // Now you can display the private key


                        printPrivateKey(userprivatekey[0]);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Re-authentication failed
                        Toast.makeText(profile_view.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to print the private key
    // Method to print the private key
    private void printPrivateKey(String privateKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(profile_view.this);
        builder.setTitle("Private Key");
        builder.setMessage(privateKey);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    // Implement your code to print the private key here
    }
