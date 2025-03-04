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

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.ViewHolder> {

    private Context context;
    private List<KeyData> data;

    public KeyAdapter(Context context, List<KeyData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public KeyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.key_disp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyAdapter.ViewHolder holder, int position) {
        KeyData requestData = data.get(position);

        holder.nkey.setText(requestData.getMykey());
        holder.nnote.setText(requestData.getMynote());
        String rid = requestData.getId();

//        holder.btndelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mydiag = new AlertDialog.Builder(context.getApplicationContext());
//                mydiag.setTitle("Delete Key");
//                mydiag.setMessage("Are you sure you want to delete this key?");
//                mydiag.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context.getApplicationContext(), "Clicked delete", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                mydiag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context.getApplicationContext(), "Clicked no", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                mydiag.show();
//
//
//
//
////                AlertDialog.Builder builder = new AlertDialog.Builder(context);
////                builder.setTitle("Delete Key");
////                builder.setMessage("Are you sure you want to delete this key?");
////                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        // Delete the key from Firebase
////                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("KeyStore");
////                        dbref.child(rid).removeValue()
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void aVoid) {
////                                        Log.d(TAG, "deleteKeyFromFirebase: Key deleted successfully for " + rid);
////                                        Toast.makeText(context, "Key deleted!", Toast.LENGTH_SHORT).show();
////                                    }
////                                })
////                                .addOnFailureListener(new OnFailureListener() {
////                                    @Override
////                                    public void onFailure(@NonNull Exception e) {
////                                        Log.w(TAG, "deleteKeyFromFirebase: Failed to delete key", e);
////                                        Toast.makeText(context, "Failed to delete key!", Toast.LENGTH_SHORT).show();
////                                    }
////                                });
////
////                        dialog.dismiss();
////                    }
////                });
////                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.dismiss();
////                    }
////                });
////                builder.show();
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nkey, nnote;
        Button btndelete;

        public ViewHolder(View itemView) {
            super(itemView);

            nnote = itemView.findViewById(R.id.note);
            nkey = itemView.findViewById(R.id.key);
            //btndelete = itemView.findViewById(R.id.deletebutton);

        }
    }
}

