package com.example.ciphershare;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class accepted_adapter extends RecyclerView.Adapter<accepted_adapter.ViewHolder> {

    private Context context;
    public List<accepted_data> data;
    private OnItemClickListener onItemClickListener; // Interface for handling click listener

    public accepted_adapter(Context context, List<accepted_data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public accepted_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avaliable_users, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull accepted_adapter.ViewHolder holder, int position) {
        if (data.isEmpty()) {
            holder.username.setText("Click 'New Chat' to add a user");
        }
        accepted_data requestData = data.get(position);

        holder.username.setText(requestData.getUsername());

        holder.mycard.setOnClickListener(null); // Remove any previously set listener (optional)
        holder.mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position); // Call back to the adapter's onClickListener
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, usersrecent;
        ConstraintLayout mycard;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.cardname);
            mycard = itemView.findViewById(R.id.myusercards);
        }
    }

    // Interface for handling click listener
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Implement setOnItemClickListener to allow setting listener from outside
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
