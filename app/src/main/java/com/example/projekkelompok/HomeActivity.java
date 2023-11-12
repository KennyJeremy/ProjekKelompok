package com.example.projekkelompok;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.projekkelompok.models.ItemModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<ItemModel, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private TextView tvToDo, tvHome, tvProfile;
    private EditText etNew;
    private String onlineUserID;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tvToDo = findViewById(R.id.tvToDo);
        recyclerView = findViewById(R.id.recycler);
        tvHome = findViewById(R.id.tvHome);
        tvProfile = findViewById(R.id.tvProfile);
        View view3 = findViewById(R.id.view3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("todo_items").child(onlineUserID);

        etNew = findViewById(R.id.etNew);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(etNew.getText().toString().trim());
                etNew.setText("");
            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addTask(String task) {
        String id = reference.push().getKey();

        if(TextUtils.isEmpty(task)) {
            Toast.makeText(HomeActivity.this, "Task is required!", Toast.LENGTH_LONG).show();
        } else {
            ItemModel model = new ItemModel(task, id);
            reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Task has been inserted successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ItemModel> options = new FirebaseRecyclerOptions.Builder<ItemModel>()
                .setQuery(reference, ItemModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ItemModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ItemModel model) {
                holder.setTask(model.getTask());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

                return new MyViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvTask;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTask(String task) {
            tvTask = view.findViewById(R.id.tvTask);
            tvTask.setText(task);
        }
    }

}
