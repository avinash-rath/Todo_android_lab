package com.example.todo_android_lab;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ModelListAdapter extends ArrayAdapter<Model> {

    public ModelListAdapter(Context context, List<Model> modelList) {
        super(context, 0, modelList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Model model = getItem(position);
        Log.d("getView() called","Item number : "+position);
            if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_element, parent,
                    false);
            }
            TextView titleTextView = convertView.findViewById(R.id.card_element_title);
            TextView timestampTextView = convertView.findViewById(R.id.card_element_timestamp);
            TextView descriptionTextView = convertView.findViewById(R.id.card_element_description);

            titleTextView.setText(model.getTitle());
            timestampTextView.setText(model.getTimestamp());
            descriptionTextView.setText(model.getDescription());

        return convertView;

    }
}
