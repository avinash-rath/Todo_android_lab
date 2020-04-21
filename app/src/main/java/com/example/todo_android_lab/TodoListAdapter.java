package com.example.todo_android_lab;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

        public DataHolder(@NonNull final View itemView) {
            super(itemView);
            card_element_description = itemView.findViewById(R.id.card_element_description);
            card_element_title = itemView.findViewById(R.id.card_element_title);
            card_element_timestamp = itemView.findViewById(R.id.card_element_timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), String.valueOf(list.get(getAdapterPosition()).getId()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(itemView.getContext(), DynamicActivity.class);
                    intent.putExtra("docId", list.get(getAdapterPosition()).getId());
                    intent.putExtra("title", list.get(getAdapterPosition()).getTitle());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
