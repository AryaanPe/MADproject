package com.example.electronicbazarmad;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInM extends AppCompatActivity {
    private DatabaseReference EStore;
    private TextView name,MID,title;
    private ImageView userI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_m);
        EStore = FirebaseDatabase.getInstance().getReference();
        name=findViewById(R.id.userName);
        MID=findViewById(R.id.MID);
        title=findViewById(R.id.titlepage);
        userI = findViewById(R.id.userI);

        Intent login = getIntent();
        String username = login.getStringExtra("UserName");
        String password = login.getStringExtra("Password");
        boolean status = login.getBooleanExtra("manager",false);
        if(status == true){
            title.setText("manager");
        }else {
            title.setText("customer");
        }
        DatabaseReference Ref = EStore.child(title.getText().toString()).child(username);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uname = (snapshot.child("user").getValue().toString() +" " + snapshot.child("userL").getValue().toString());
                name.setText(uname);
                MID.setText(username);
                String imageURL = snapshot.child("imageURL").getValue().toString();
                if (!imageURL.isEmpty()) {
                    Glide.with(LogInM.this).load(imageURL).into(userI);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addprod(View view) {
        String ID = MID.getText().toString();
        Intent Products = new Intent(this, addproducts.class);
        Products.putExtra("id",ID);
        startActivity(Products);
    }
    public void usersM(View view) {
        String mid = MID.getText().toString();
        Intent Users = new Intent(this,ViewUsersM.class);
        Users.putExtra("MID",mid);
        startActivity(Users);

    }


}