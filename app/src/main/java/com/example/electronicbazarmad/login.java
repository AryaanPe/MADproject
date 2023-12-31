package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
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

public class login extends AppCompatActivity {
    private DatabaseReference EStore;
    private TextView name,address,title;
    private ImageView userI;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EStore = FirebaseDatabase.getInstance().getReference();
        name=findViewById(R.id.userName);
        address=findViewById(R.id.addres);
        title=findViewById(R.id.titlepage);
        userI = findViewById(R.id.userI);

        Intent login = getIntent();
        username = login.getStringExtra("UserName");
        String tit = login.getStringExtra("title");


        DatabaseReference Ref = EStore.child(tit).child(username);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uname = (snapshot.child("user").getValue().toString() + snapshot.child("userL").getValue().toString());
                String adress = snapshot.child("Address").getValue().toString();
                name.setText(uname);
                address.setText(adress);
                String imageURL = snapshot.child("imageURL").getValue().toString();
                if (!imageURL.isEmpty()) {
                    Glide.with(login.this).load(imageURL).into(userI);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    public void editdetails(View view) {
        Intent edit = new Intent(login.this, EditDetails.class);
        edit.putExtra("title","customer");
        edit.putExtra("id",username);
        startActivity(edit);
    }

    public void browse(View view) {
        Intent browse = new Intent(login.this,BrowseProducts.class);
        browse.putExtra("id",username);
        startActivity(browse);
    }

    public void viewcart(View view) {
        Intent viewcart = new Intent(login.this,cartview.class);
        viewcart.putExtra("id",username);
        startActivity(viewcart);
    }
}