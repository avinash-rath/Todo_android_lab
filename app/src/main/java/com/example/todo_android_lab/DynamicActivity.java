package com.example.todo_android_lab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class DynamicActivity extends AppCompatActivity {
    private TextView timestamp;
    private EditText title, description;
    private Button editButton;
    HashMap hm;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_create_item);

        DynamicActivity.this.setTitle(getIntent().getStringExtra("title"));

        timestamp = findViewById(R.id.timestamp);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        editButton = findViewById(R.id.editButton);
        final String string = getIntent().getStringExtra("id");

        DocumentReference documentReference = firestore.collection("items").document(string);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    HashMap hmap = (HashMap) documentSnapshot.getData();
                    timestamp.setText(String.valueOf(hmap.get("timestamp")));
                    title.setText(String.valueOf(hmap.get("title")));
                    description.setText(String.valueOf(hmap.get("description")));
                }
            }
        });

//        firestore.collection("items").document(string).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            hm = (HashMap) documentSnapshot.getData();
//                            timestamp.setText(String.valueOf(hm.get("timestamp")));
//                            title.setText(String.valueOf(hm.get("title")));
//                            description.setText(String.valueOf(hm.get("description")));
//                        }
//                    }
//                });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", title.getText().toString());
                map.put("timestamp", timestamp.getText().toString());
                map.put("description", description.getText().toString());
                DocumentReference reference = firestore.collection("items").document(string);

                reference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DynamicActivity.this, "List Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
