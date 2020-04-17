package com.example.todo_android_lab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    List<TodoModel> todoList = new ArrayList<>();
    FirebaseFirestore db;
    private TodoListAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("items")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
//                            List<TodoModel> a = queryDocumentSnapshots.toObjects(TodoModel.class);
//                            modelArrayAdapter = new ModelListAdapter(getApplicationContext(), a);
//                            listView.setAdapter(modelArrayAdapter);
                            Log.i("updated", String.valueOf(queryDocumentSnapshots.getDocuments()));
                        }
                    }

                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);

        final Context context = this;

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("listStr", String.valueOf(task.getResult()));
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        HashMap hm = (HashMap) queryDocumentSnapshot.getData();
                        todoList.add(new TodoModel(queryDocumentSnapshot.getId(),
                                String.valueOf(hm.get("description")),
                                String.valueOf(hm.get("title")),
                                String.valueOf(hm.get("timestamp"))));
                        Log.i("Printing", String.valueOf(hm.get("title")));
                        adapter = new TodoListAdapter(todoList);
                        recycler_view.setAdapter(adapter);
                        recycler_view.setLayoutManager(new GridLayoutManager(context, 2));
                    }
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateItemActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
