package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class chat_with_user extends AppCompatActivity {

    TextView usname;

    FloatingActionButton sendtext;
    RecyclerView myrecycler;
    EditText messageEditText;

    String chatuseremail, receivedrname;


    private MessageAdapter messageAdapter;
    private List<Message> messageList;


    Boolean saved = Boolean.valueOf("False");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_user);
        usname = findViewById(R.id.username_text);
        sendtext = findViewById(R.id.send_button);
        myrecycler = findViewById(R.id.chat_messages_recycler);
        messageEditText = findViewById(R.id.message_edit_text);


        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        myrecycler.setLayoutManager(new LinearLayoutManager(this));
        myrecycler.setAdapter(messageAdapter);



        Intent intent = getIntent();
        if (intent.hasExtra("name") && intent.hasExtra("email")) {
            String username = intent.getStringExtra("name");
            chatuseremail = intent.getStringExtra("email");

            usname.setText(username);
            Log.d("ChatActivity", "Username: " + username + ", Email: " + chatuseremail);
        } else {
            Log.w("ChatActivity", "Username or email extra not found in Intent");
        }

        sendtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {
        final String[] work = {"notdone"};


        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("messageData");
        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childs : snapshot.getChildren()) {
                    String presenta = childs.child("usera").getValue(String.class);
                    String presentb = childs.child("userb").getValue(String.class);
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    Log.d(TAG, "onDataChange: from dbddd" + presenta);
                    Log.d(TAG, "onDataChange: from dbddd" + presentb);
                    Log.d(TAG, "onDataChange: from dbddd" + currentuser);
                    Log.d(TAG, "onDataChange: from dbddd" + chatuseremail);


                    if ((presenta.equals(chatuseremail)
                            && currentuser.equals(presentb)) ||
                            (presenta.equals(currentuser)
                                    && presentb.equals(chatuseremail))) {
                        String loc = childs.getKey();
                        savemessage(loc);
                        saved = Boolean.valueOf(("True"));

                        Log.d(TAG, "onDataChange: matches found" + childs.getKey());
                        break;
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void savemessage(String loc) {
        String messageText = messageEditText.getText().toString().trim();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTime = dateFormat.format(calendar.getTime());

        if(!saved.equals("True")) {
            if (!messageText.isEmpty()) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("messageData").child(loc);
                com.example.ciphershare.Message message = new com.example.ciphershare.Message(messageText, FirebaseAuth.getInstance().getCurrentUser().getEmail(), chatuseremail,dateTime);
                DatabaseReference messageRef = databaseRef.child("messages").push();
                messageRef.setValue(message);
                messageEditText.setText("");

                messageList.clear();
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: i started this first");


        final String[] lockey = new String[1];
        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("messageData");
        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childs : snapshot.getChildren()) {
                    String presenta = childs.child("usera").getValue(String.class);
                    String presentb = childs.child("userb").getValue(String.class);
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    if ((presenta.equals(chatuseremail)
                            && currentuser.equals(presentb)) ||
                            (presenta.equals(currentuser)
                                    && presentb.equals(chatuseremail))) {
                        lockey[0] = childs.getKey();
                        Log.d(TAG, "onDataChange: key found for fetching" + lockey[0]);
                        break;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageList.clear();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("messageData");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()) {
                    if(child.getKey().equals(lockey[0])) {
                        DataSnapshot messagesNode = child.child("messages");
                        for (DataSnapshot messageSnapshot : messagesNode.getChildren()) {
                            String messageText = messageSnapshot.child("message").getValue(String.class);
                            String sendername = messageSnapshot.child("sender").getValue(String.class);
                            String receivername = messageSnapshot.child("receiver").getValue(String.class);
                            String timess = messageSnapshot.child("dateTime").getValue(String.class);


                            Message mydata = new Message(messageText,sendername,receivername,timess);
                            messageList.add(mydata);


                            Log.d(TAG, "Message: " + messageText); // Modify this line to display as needed (e.g., TextView)
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    //    protected void onStart() {
//        super.onStart();
//        final String[] lockey = new String[1];
//        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("messageData");
//        mydb.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot childs : snapshot.getChildren()) {
//                    String presenta = childs.child("usera").getValue(String.class);
//                    String presentb = childs.child("userb").getValue(String.class);
//                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//                    Log.d(TAG, "onDataChange: from dbddd" + presenta);
//                    Log.d(TAG, "onDataChange: from dbddd" + presentb);
//                    Log.d(TAG, "onDataChange: from dbddd" + currentuser);
//                    Log.d(TAG, "onDataChange: from dbddd" + chatuseremail);
//
//
//                    if ((presenta.equals(chatuseremail)
//                            && currentuser.equals(presentb)) ||
//                            (presenta.equals(currentuser)
//                                    && presentb.equals(chatuseremail))) {
//                        lockey[0] = childs.getKey();
//                        Log.d(TAG, "onDataChange: key found for fetching" + lockey[0]);
//                        break;
//                    }
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("messageData").child(lockey[0]).child("messages");
//
//        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                messageList.clear();
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    com.example.ciphershare.Message message = child.getValue(com.example.ciphershare.Message.class);
//                    if (message != null) {
//                        Log.d(TAG, "onDataChange: found message"+message);
//                        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//                        if ((currentUserEmail.equals(message.getSender()) && chatuseremail.equals(message.getReceiver())) ||
//                                (currentUserEmail.equals(message.getReceiver()) && chatuseremail.equals(message.getSender()))) {
//                            messageList.add(message);
//                        }
//                    }
//                }
//                if (messageList.isEmpty()) {
//                    com.example.ciphershare.Message startMessage = new com.example.ciphershare.Message("Start chatting now", "", "", "");
//                    messageList.add(startMessage);
//                }
//                // Reverse the list to display the latest messages first
//                Collections.reverse(messageList);
//                messageAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "onStart: onCancelled", error.toException());
//            }
//        });
//    }
}
