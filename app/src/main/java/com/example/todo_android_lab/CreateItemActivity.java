package com.example.todo_android_lab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class CreateItemActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Intent intent;
    String title;
    String timestamp;
    String description;

    public int year, month, dayOfMonth, hour, minute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_create_item);

        if(intent.getBooleanExtra("ISEDIT",false)) {
            EditText title = findViewById(R.id.title_edit);
            title.setText(intent.getStringExtra("TITLE"));
            EditText description = findViewById(R.id.description_edit);
            description.setText(intent.getStringExtra("DESCRIPTION"));

            String timestamp = intent.getStringExtra("TIMESTAMP");
            TextView date = findViewById(R.id.date_id);
            TextView time = findViewById(R.id.time_id);

            date.setText(timestamp.split(" ")[0]);
            time.setText(timestamp.split(" ")[1]);

        };

        Button button = (Button) findViewById(R.id.date_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        final Button timeButton = (Button) findViewById(R.id.time_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setText(intent.getBooleanExtra("ISEDIT",false) ? "Edit TODO" : "Add TODO");
        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    String dateTime = year + "-" + (month+1) + "-" + dayOfMonth + " " + hour + ":" + minute + ":00.00";
                    Timestamp timestamp = Timestamp.valueOf(dateTime);

                    EditText titleView = (EditText) findViewById(R.id.title_edit);
                    EditText descriptionView = (EditText) findViewById(R.id.description_edit);
                    String title = titleView.getText().toString();
                    String description = descriptionView.getText().toString();

                    if (!title.equals("") || !description.equals("")) {


                        Map<String, Object> dataToPush = new HashMap<String, Object>();
                        dataToPush.put("title", title);
                        dataToPush.put("description", description);
                        dataToPush.put("timestamp", timestamp.toString());

                        String documentName = timestamp.toString() + title;

                DocumentReference mDocRef = FirebaseFirestore.getInstance()
                        .collection("items").document(documentName);
                mDocRef.set(dataToPush).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context,"TODO added", Toast.LENGTH_LONG);
                        toast.show();
                        Log.d("firebase","TODO added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context,"TODO not added"+e.toString(),
                                Toast.LENGTH_LONG);
                        toast.show();
                        Log.d("firebase","TODO not added",e);
                    }
                });
                        finish();
                    } else {
                        Toast.makeText(CreateItemActivity.this, "Title or description should not be empty", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("No input", "No input given", e);
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                    finish();
                }
                if(intent.getBooleanExtra("ISEDIT",false)) {
                    title = intent.getStringExtra("TITLE");
                    timestamp = intent.getStringExtra("TIMESTAMP");
                    description = intent.getStringExtra("DESCRIPTION");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference mDocRef = db.collection("items")
                            .document(timestamp+title);
                    mDocRef.delete();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // TODO: pick date from here and add it to the model for sending to FireBase
        String currentDateString = DateFormat.getDateInstance().format((c.getTime()));

        TextView textView = (TextView) findViewById(R.id.date_id);
        textView.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        TextView textView = (TextView) findViewById(R.id.time_id);
        textView.setText(hourOfDay + " : " + minute);
    }
}
