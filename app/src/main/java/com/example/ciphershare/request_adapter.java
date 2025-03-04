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
import java.util.Map;

public class request_adapter extends RecyclerView.Adapter<request_adapter.ViewHolder> {

    private Context context;
    private List<new_chat_data> data;

    public request_adapter(Context context, List<new_chat_data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public request_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_requests, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull request_adapter.ViewHolder holder, int position) {
        new_chat_data requestData = data.get(position);

        holder.tvDate.setText(requestData.getDateTime());
        holder.tvSenderName.setText(requestData.getSenderName());
        holder.tvSenderEmail.setText(requestData.getSenderEmail());
        holder.tvSenderPublicKey.setText(requestData.getSenderPublickey());
        holder.tvNote.setText(requestData.getNote());
        holder.tvStatus.setText(requestData.getStatus());


        if(!requestData.getStatus().equals("Pending")){
            holder.btnCancelReq.setVisibility(View.GONE);
            holder.btnAcceptReq.setVisibility(View.GONE);
        }

        Log.d(TAG, "onBindViewHolder: ==="+requestData.getSenderName()+ requestData.getReceiverName());

        holder.btnAcceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Name :"+requestData.getSenderName(), Toast.LENGTH_SHORT).show();

                FirebaseDatabase.getInstance().getReference("chatRequests").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String dbdate = childSnapshot.child("dateTime").getValue(String.class);
                            String sname = childSnapshot.child("senderName").getValue(String.class);
                            String semail = childSnapshot.child("senderEmail").getValue(String.class);
                            String rname = childSnapshot.child("receiveremail").getValue(String.class);// Accepting a chat request will reveal your email address to the sender.
                            String gotdat = requestData.getDateTime();

                            String ckey = childSnapshot.getKey();

                            String gotsname = requestData.getSenderName();
                            String gotrname = requestData.getReceiveremail();

                            if (gotdat.equals(dbdate) && gotsname.equals(sname)) {
                                // Prompt user for confirmation before accepting

                                AlertDialog.Builder builder = new AlertDialog.Builder(context); // Replace 'context' with your activity or fragment context
                                builder.setTitle("Chat Request Confirmation");
                                builder.setMessage("Accepting this chat request will reveal your email address. Are you sure you want to proceed?");
                                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                        String dateTime = dateFormat.format(calendar.getTime());
                                        // Update status to "accepted"
                                        HashMap<String, Object> updateMap = new HashMap<>();
                                        updateMap.put("status", "Accepted");
                                        childSnapshot.getRef().updateChildren(updateMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onDataChange: Status updated successfully for " + childSnapshot.getKey());
                                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("chatconnection").child(ckey);
                                                        userRef.child("date").setValue(dateTime);
                                                        userRef.child("senderemail").setValue(semail);
                                                        userRef.child("receiveremail").setValue(rname);


                                                        DatabaseReference secref = FirebaseDatabase.getInstance().getReference().child("messageData").child(ckey);
                                                        secref.child("usera").setValue(semail);
                                                        secref.child("userb").setValue(rname);

                                                        dialog.dismiss();
                                                        notifyDataSetChanged();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "onDataChange: Failed to update status", e);
                                                        // Handle update failure (optional)
                                                    }
                                                });

                                    }

                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); // Dismiss the dialog without accepting
                                    }
                                });
                                builder.show();

                                break; // Only process the first matching request
                            }
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
            tvSenderName = itemView.findViewById(R.id.dispsendername);
            tvSenderEmail = itemView.findViewById(R.id.dispsenderemail);
            tvSenderPublicKey = itemView.findViewById(R.id.dispsenderPK);
            tvNote = itemView.findViewById(R.id.dispnote);
            tvStatus = itemView.findViewById(R.id.dispstatus);
            btnCancelReq = itemView.findViewById(R.id.cancelreqbutton);
            btnAcceptReq = itemView.findViewById(R.id.acceptreqbutton);
        }
    }
}
