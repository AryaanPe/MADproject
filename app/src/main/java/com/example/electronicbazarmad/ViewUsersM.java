package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicbazarmad.users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersM extends AppCompatActivity  {
    private RecyclerView recycleusers;
    private useradapter adapterusers;
    private List<users> userList;
    private TextView MID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_m);
        Intent start = getIntent();
        String mid = start.getStringExtra("MID");
        recycleusers = findViewById(R.id.RView);
        MID = findViewById(R.id.MID);
        MID.setText(mid);
        recycleusers.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot cust : snapshot.child("customer").getChildren()) {
                    String uid = cust.getKey();
                    String ufname = cust.child("user").getValue(String.class);
                    String ulname = cust.child("userL").getValue(String.class);
                    users userdata = new users(uid,ufname,ulname);
                    userList.add(userdata);
                }
                adapterusers = new useradapter(userList,ViewUsersM.this);
                recycleusers.setAdapter(adapterusers);
                adapterusers.setOnItemClickListener(new useradapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(users user) {
                        Intent openuser = new Intent(ViewUsersM.this, login.class);
                        openuser.putExtra("UserName",user.getUid());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewUsersM.this, "Cannot Load Data", Toast.LENGTH_SHORT).show();

            }
        });

    }
}