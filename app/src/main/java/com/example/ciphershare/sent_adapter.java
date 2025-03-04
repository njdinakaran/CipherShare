package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class sent_adapter extends RecyclerView.Adapter<sent_adapter.ViewHolder> {

    private Context context;
    private List<new_chat_data> data;

    public sent_adapter(Context context, List<new_chat_data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public sent_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_sent_requests, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull sent_adapter.ViewHolder holder, int position) {
        int reversePosition = data.size() - 1 - position;

        new_chat_data requestData = data.get(reversePosition);

        holder.tvDate.setText(requestData.getDateTime());
        holder.tvSenderPublicKey.setText(requestData.getReceiverpublicKey());
        holder.tvNote.setText(requestData.getNote());
        holder.tvStatus.setText(requestData.getStatus());

        if(requestData.getStatus().equals("Accepted")){
            holder.btnCancelReq.setVisibility(View.GONE);
        }

        Log.d(TAG, "onBindViewHolder: ==="+requestData.getNote()+ requestData.getDateTime());
        holder.btnCancelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("chatRequests").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            String dbdate = childSnapshot.child("dateTime").getValue(String.class);
                            String sname = childSnapshot.child("senderName").getValue(String.class);
                            String semail = childSnapshot.child("senderEmail").getValue(String.class);
                            String rname = childSnapshot.child("receiveremail").getValue(String.class);// Accepting a chat request will reveal your email address to the sender.
                            String gotdat = requestData.getDateTime();

                            String ckey = childSnapshot.getKey();

                            String gotsname = requestData.getSenderName();
                            String gotrname = requestData.getReceiveremail();
                            ///
                            if (gotdat.equals(dbdate) && gotsname.equals(sname)) {
                                // Prompt user for confirmation before accepting

                                AlertDialog.Builder builder = new AlertDialog.Builder(context); // Replace 'context' with your activity or fragment context
                                builder.setTitle("Chat Request Deletion");
                                builder.setMessage("Do you want to Cancel the request?");
                                builder.setPositiveButton("Delete Request", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        childSnapshot.getRef().removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onDataChange: Request deleted successfully for " + childSnapshot.getKey());
                                                        dialog.dismiss();
                                                        notifyDataSetChanged(); // Refresh the view to remove the deleted request
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "onDataChange: Failed to delete request", e);
                                                        // Handle deletion failure (optional)
                                                    }
                                                });
                                    }

                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();// Dismiss the dialog without accepting
                                    }
                                });
                                builder.show();

                                break; // Only process the first matching request
                            }



                            ///

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvSenderName, tvSenderEmail, tvSenderPublicKey, tvNote, tvStatus;
        Button btnCancelReq, btnAcceptReq;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.dispdate);
            tvSenderPublicKey = itemView.findViewById(R.id.dispsenderPK);
            tvNote = itemView.findViewById(R.id.dispnote);
            tvStatus = itemView.findViewById(R.id.dispstatus);
            btnCancelReq = itemView.findViewById(R.id.cancelreqbutton);
        }
    }
}
