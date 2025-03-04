package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Store_Keys extends AppCompatActivity {

    RecyclerView myrecycler;

    KeyAdapter adapter;
    List<KeyData> dataList;
    Button addbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_keys);
        myrecycler = findViewById(R.id.recycler_view);
        addbutton=findViewById(R.id.add_key_button);


        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store_Keys.this);
                View dialogView = getLayoutInflater().inflate(R.layout.new_key, null);
                EditText emailBox = dialogView.findViewById(R.id.edittetPK);
                EditText mynote = dialogView.findViewById(R.id.new_note);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnrequest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String myPK = emailBox.getText().toString();
                        String myNote = mynote.getText().toString();
                        if (myPK.isEmpty() && myNote.isEmpty()) {
                            Toast.makeText(Store_Keys.this, "Enter all details", Toast.LENGTH_SHORT).show();
                        }

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String dateTime = dateFormat.format(calendar.getTime());


                        HashMap<String, Object> keyData = new HashMap<>();
                        keyData.put("privateKey", myPK);
                        keyData.put("note", myNote);
                        keyData.put("DateTime",dateTime);
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            String userEmail = currentUser.getEmail();
                            String cid = currentUser.getUid();

                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("KeyStore").child(cid);
                            dbref.push().setValue(keyData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Store_Keys.this, "Key saved successfully!", Toast.LENGTH_SHORT).show();
                                            // Clear the edit text fields after successful save (optional)
                                            emailBox.getText().clear();
                                            mynote.getText().clear();
                                            dialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "onClick: Failed to save key", e);
                                            Toast.makeText(Store_Keys.this, "Failed to save key!", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }


                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        dataList = new ArrayList<>();
        adapter = new KeyAdapter(getApplicationContext(), dataList);
        myrecycler.setAdapter(adapter);
        myrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        dataList.clear();
        adapter.notifyDataSetChanged();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if user is logged in
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String cid = currentUser.getUid();

            // Get a reference to the user's key storage node
            DatabaseReference userKeyRef = FirebaseDatabase.getInstance().getReference("KeyStore").child(cid);

            // Attach a ValueEventListener to retrieve keys
            userKeyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Clear any existing data in your adapter (optional)
                    // ... (assuming your adapter has a method to clear data)

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Extract key data from each child node
                        String privateKey = childSnapshot.child("privateKey").getValue(String.class);
                        String note = childSnapshot.child("note").getValue(String.class);
                        String keyDateTime = childSnapshot.child("DateTime").getValue(String.class);

                        // Create a KeyData object and add it to the list
                        KeyData keyData = new KeyData(privateKey, note, keyDateTime,cid); // Assuming KeyData class exists
                        dataList.add(keyData);
                    }

                    // Update your RecyclerView adapter with the retrieved key list
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Store_Keys.this, "Failed to retrieve keys!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle scenario if user is not logged in (optional)
            Toast.makeText(Store_Keys.this, "Please sign in to view keys", Toast.LENGTH_SHORT).show();
        }





    }
}