package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;

public class ChatPage extends AppCompatActivity implements accepted_adapter.OnItemClickListener {

    LinearLayout addbutton,pending,sentbut;

    private RecyclerView recyclerView;
    private accepted_adapter adapter;
    private List<accepted_data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        addbutton = findViewById(R.id.add_chat_button);
        pending = findViewById(R.id.buttonpending);
        sentbut = findViewById(R.id.buttonsent);
        recyclerView=findViewById(R.id.chat_list_recycler_view);

        dataList = new ArrayList<>();




        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"button clicked",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatPage.this);
                View dialogView = getLayoutInflater().inflate(R.layout.new_chat, null);
                EditText emailBox = dialogView.findViewById(R.id.edittetPK);
                EditText mynote = dialogView.findViewById(R.id.new_note);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnrequest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String myPK = emailBox.getText().toString();
                        String myNote = mynote.getText().toString();
                        if (myPK.isEmpty() && myNote.isEmpty()) {
                            Toast.makeText(ChatPage.this, "Enter all details", Toast.LENGTH_SHORT).show();
                        }
                        // Writing vode to send and save new chat request
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String SName = snapshot.child("name").getValue(String.class);
                                        String SEmail = snapshot.child("email").getValue(String.class);
                                        String Spublickey = snapshot.child("publicKey").getValue(String.class);
                                        Log.d(TAG, "onDataChange: user detailssss"+SName+"  "+SEmail+"    "+Spublickey);
                                        //Checking whther entered pk is valid or not
                                        FirebaseDatabase.getInstance().getReference("users").orderByChild("publicKey").equalTo(myPK).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                        String recName = userSnapshot.child("name").getValue(String.class);
                                                        String recEmail = userSnapshot.child("email").getValue(String.class);
                                                        Log.d(TAG, "onDataChange: receiver info: " + recName + "  " + recEmail);
                                                        //Storing chat requests
                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                                                        DatabaseReference chatRef = database.getReference("chatRequests");
                                                        Calendar calendar = Calendar.getInstance();
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                                        String dateTime = dateFormat.format(calendar.getTime());
                                                        String cstatus = "Pending";
                                                        new_chat_data chatData = new new_chat_data(SName, SEmail, Spublickey, myPK, recName, recEmail, dateTime, myNote, cstatus);

                                                        FirebaseDatabase.getInstance().getReference("chatRequests").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                boolean isRequestFound = false; // Initialize to false

                                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                                                            String rec = childSnapshot.child("receiverpublicKey").getValue(String.class);
                                                            String sender = childSnapshot.child("senderPublickey").getValue(String.class);

                                                            if (Spublickey.equals(sender) && myPK.equals(rec)) {
                                                                Toast.makeText(ChatPage.this, "Request sent already", Toast.LENGTH_SHORT).show();
                                                                isRequestFound = true; // Set to true if a match is found
                                                                break; // Exit loop if request found
                                                            }
                                                        }
                                                                if (!isRequestFound) { // Assuming a flag to track if a match is found
                                                            chatRef.push().setValue(chatData)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(ChatPage.this, "Chat Request sent successfully", Toast.LENGTH_SHORT).show();
                                                                            dialog.dismiss();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(ChatPage.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }

                                                                }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }
                                                } else {
                                                    Toast.makeText(ChatPage.this, "No user found with this public key", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle any errors
                                            }
                                        });
                                        //



                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle any errors
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

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), View_pending_request.class);
                startActivity(m);
               // Toast.makeText(ChatPage.this, "Viewing pending approval", Toast.LENGTH_SHORT).show();
            }
        });

        sentbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), view_sent_requests.class);
                startActivity(m);
            //    Toast.makeText(ChatPage.this, "View sent request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(this, "Hello welocome to chat", Toast.LENGTH_SHORT).show();

        dataList.clear();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();

            Log.d(TAG, "onStart: =="  + email);

            FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childsnap : snapshot.getChildren()){
                        if(email.equals(childsnap.child("email").getValue(String.class))){
                            String useremail = childsnap.child("email").getValue(String.class);
                            acceptedchats(useremail);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

        }


    }

    private void acceptedchats(String useremail) {
        adapter = new accepted_adapter(getApplicationContext(), dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setOnItemClickListener(this); // Set 'this' activity as the listener


        dataList.clear();
        adapter.notifyDataSetChanged();



        FirebaseDatabase.getInstance().getReference("chatRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childsnap : snapshot.getChildren()){
                    if(useremail.equals(childsnap.child("senderEmail").getValue(String.class))){
                        if(childsnap.child("status").getValue(String.class).equals("Accepted")) {
                            Log.d(TAG, "onDataChange: Found chats receive names" + childsnap.child("receiverName").getValue(String.class));
                            String uname = childsnap.child("receiverName").getValue(String.class);
                            String uemail = childsnap.child("receiveremail").getValue(String.class);
                            String myr ="No recent";
                            Log.d(TAG, "onDataChange: sending to adapter"+uemail);

                            accepted_data mydata = new accepted_data(uname,myr,uemail);
                            dataList.add(mydata);
                        }
                    }
                    if((useremail.equals(childsnap.child("receiveremail").getValue(String.class)))){
                        if(childsnap.child("status").getValue(String.class).equals("Accepted")) {
                            Log.d(TAG, "onDataChange: Found chats receive names" + childsnap.child("receiverName").getValue(String.class));
                            String uname = childsnap.child("senderName").getValue(String.class);
                            String uemail = childsnap.child("senderEmail").getValue(String.class);
                            String myr ="No recent";
                            Log.d(TAG, "onDataChange: sending to adapter"+uemail);

                            accepted_data mydata = new accepted_data(uname,myr,uemail);
                            dataList.add(mydata);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        accepted_data clickedUserData = adapter.data.get(position);
        String userEmail = clickedUserData.getUseremail();
        String userName = clickedUserData.getUsername();

        // Log user data using a logging library of your choice (e.g., Logcat)
        Log.d("ChatPage", "User Clicked: Email - " + userEmail + ", Name - " + userName);

        Intent intent = new Intent(getApplicationContext(),chat_with_user.class);
        intent.putExtra("name",userName);
        intent.putExtra("email",userEmail);
        startActivity(intent);

    }
}