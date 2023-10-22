package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class productdetails extends AppCompatActivity {
    DatabaseReference EStore;
    EditText quantity;
    TextView PName, PID, PDesc, PCost, PCat;
    ImageView ProductImage;
    public String uid;
    public String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        Intent viewp = getIntent();
        pid = viewp.getStringExtra("pid");
        uid = viewp.getStringExtra("uid");
        Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, ""+pid, Toast.LENGTH_SHORT).show();
        EStore = FirebaseDatabase.getInstance().getReference();
        quantity = findViewById(R.id.PQuantity);
        quantity.setText("1");
        PName = findViewById(R.id.PNAME);
        PID = findViewById(R.id.PID);
        PDesc = findViewById(R.id.PDESC);
        PCost = findViewById(R.id.PCost);
        PCat = findViewById(R.id.PCat);
        ProductImage = findViewById(R.id.PImage);


        DatabaseReference ref = EStore.child("products").child(pid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("productname").getValue(String.class);
                    String desc = snapshot.child("productdesc").getValue(String.class);
                    String cost = snapshot.child("productcost").getValue(String.class);
                    String cat = snapshot.child("productcat").getValue(String.class);
                    String link = snapshot.child("imageURL").getValue(String.class);
                    PName.setText(name);
                    PDesc.setText(desc);
                    PCat.setText(cat);
                    PCost.setText(cost);
                    Glide.with(productdetails.this).load(link).into(ProductImage);
                } else {
                    Toast.makeText(productdetails.this, "Product not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(productdetails.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });



}

    public void addtocart(View view) {
        if (quantity != null) {
            String quantityValue = quantity.getText().toString();
            DatabaseReference cartRef = EStore.child("cart").child(uid);
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(pid)) {
                        int currentQuantity = Integer.parseInt(snapshot.child(pid).getValue(String.class));
                        int newQuantity = currentQuantity + Integer.parseInt(quantityValue);
                        cartRef.child(pid).setValue(String.valueOf(newQuantity));
                    } else {
                        cartRef.child(pid).setValue(quantityValue);
                    }
                    Toast.makeText(productdetails.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(productdetails.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(productdetails.this, "Quantity field is null.", Toast.LENGTH_SHORT).show();
        }
    }
    }
