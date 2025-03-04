package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_pending_request extends AppCompatActivity {

    private RecyclerView recyclerView;
    private request_adapter adapter;
    private List<new_chat_data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pending_request);

        recyclerView = findViewById(R.id.requestRecycler);  // Assuming you have a RecyclerView with id 'recycler_view' in your layout

        // Prepare data (replace with your data fetching logic)
        dataList = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String myownpk = snapshot.child("publicKey").getValue(String.class);
                        Log.d(TAG, "onDataChange: my own puclick keyee" + myownpk);

                        FirebaseDatabase.getInstance().getReference("chatRequests").orderByChild("receiverpublicKey").equalTo(myownpk).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot usersnapshot : snapshot.getChildren()){
                                        String sendername = usersnapshot.child("senderName").getValue(String.class);
                                        String senderemail = usersnapshot.child("senderEmail").getValue(String.class);
                                        String sentdate = usersnapshot.child("dateTime").getValue(String.class);
                                        String note =usersnapshot.child("note").getValue(String.class);
                                        String sstatus = usersnapshot.child("status").getValue(String.class);
                                        String senderpk = usersnapshot.child("senderPublickey").getValue(String.class);
                                        String recemail = usersnapshot.child("receiverEmail").getValue(String.class);

                                        new_chat_data requestData = new new_chat_data(sendername, senderemail, senderpk, null, null, recemail, sentdate, note, sstatus);

                                            dataList.add(requestData);

                                        // Update adapter after data is added
                                        adapter.notifyDataSetChanged();


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(View_pending_request.this, "Error Fetching", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Add new_chat_data objects to dataList here (considering security aspects when fetching real data)

        adapter = new request_adapter(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}