package com.example.ciphershare;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        int reversePosition = messageList.size() - 1 - position;
      Message message = messageList.get(position);

        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (currentUserEmail.equals(message.getSender())) {
            // Set message view for current user (e.g., right alignment, different background)

            holder.mylayout.setBackgroundResource(R.drawable.message_sent_background);
            holder.mylayout.setGravity(Gravity.getAbsoluteGravity(Gravity.RIGHT,Gravity.RIGHT));

//            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
//            holder.datetime.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
//            holder.sname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        } else {
            // Set message view for received message (e.g., left alignment, different background)

            holder.mycard.setBackgroundResource(R.drawable.message_received_background); // Replace with your drawable resource
            holder.mylayout.setGravity(Gravity.LEFT);

//            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//            holder.sname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//            holder.sname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
        holder.messageTextView.setText(message.getMessage());
        holder.datetime.setText(message.getDateTime());
        holder.sname.setText(message.getSender());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView,sname,datetime;
        CardView mycard;
        LinearLayout mylayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.pmessage);
            mycard=itemView.findViewById(R.id.messagecard);
            sname = itemView.findViewById(R.id.sendername);
            datetime=itemView.findViewById(R.id.messagetime);
            mylayout=itemView.findViewById(R.id.mylinearlayout);
        }
    }
}
