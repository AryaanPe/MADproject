package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private DatabaseReference EStore;
    private TextView name,address,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EStore = FirebaseDatabase.getInstance().getReference();
        name=findViewById(R.id.userName);
        address=findViewById(R.id.addres);
        title=findViewById(R.id.titlepage);
        Intent login = getIntent();
        String username = login.getStringExtra("UserName");
        String password = login.getStringExtra("Password");
        boolean status = login.getBooleanExtra("manager",false);
        if(status == true){
            title.setText("manager");
            address.setVisibility(View.GONE);
        }else {
            title.setText("customer");
        }
        DatabaseReference Ref = EStore.child(title.getText().toString()).child(username);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uname = (snapshot.child("user").getValue().toString() + snapshot.child("userL").getValue().toString());
                String adress = snapshot.child("Address").getValue().toString();
                name.setText(uname);
                address.setText(adress);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}