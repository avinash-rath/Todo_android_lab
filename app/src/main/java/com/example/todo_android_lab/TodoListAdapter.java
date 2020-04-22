package com.example.todo_android_lab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.DataHolder> {
    private List<TodoModel> list;
    DataHolder dataHolder;

    public TodoListAdapter (List<TodoModel> list){
        this.list = list;
    }
    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_element, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        dataHolder = holder;
        dataHolder.card_element_timestamp.setText(list.get(position).getTimestamp());
        dataHolder.card_element_title.setText(list.get(position).getTitle());
        dataHolder.card_element_description.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        TextView card_element_description;
        TextView card_element_title;
        TextView card_element_timestamp;
        Button editButton;
        Button deleteButton;
        public DataHolder(@NonNull final View itemView) {
            super(itemView);
            card_element_description = itemView.findViewById(R.id.card_element_description);
            card_element_title = itemView.findViewById(R.id.card_element_title);
            card_element_timestamp = itemView.findViewById(R.id.card_element_timestamp);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder =
                            new AlertDialog.Builder(itemView.getContext());
                    dialogBuilder.setTitle("Delete")
                            .setMessage("Confirm delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener(

                            ) {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference mDocRef = db.collection("items")
                                            .document(card_element_timestamp.getText()
                                                    .toString()
                                                    +card_element_title.getText().toString());
                                    mDocRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Context context = itemView.getContext();
                                            Toast toast = Toast.makeText(context,"TODO deleted",
                                                    Toast.LENGTH_LONG);
                                            toast.show();
                                            Log.d("firebase","TODO deleted");
                                        }
                                    });
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = dialogBuilder.create();
                    alert.show();
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(itemView.getContext(),CreateItemActivity.class);
                    myIntent.putExtra("ISEDIT",true);
                    myIntent.putExtra("TIMESTAMP",card_element_timestamp.getText().toString());
                    myIntent.putExtra("TITLE", card_element_title.getText());
                    myIntent.putExtra("DESCRIPTION",card_element_description.getText());

                    itemView.getContext().startActivity(myIntent);
                }
            });
        }
    }
}
