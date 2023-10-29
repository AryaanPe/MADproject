package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class cartview extends AppCompatActivity  {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<carts> cartItemList;
    public String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartview);

        Intent viewcart = getIntent();
        UID = viewcart.getStringExtra("id");

        TextView customerid = findViewById(R.id.CID);
        customerid.setText(UID);

        cartRecyclerView = findViewById(R.id.RecView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("cart").child(UID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    String cartID = cartItemSnapshot.getKey();
                    String quantity = cartItemSnapshot.getValue().toString();
                    fetchProductDetails(cartID, quantity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cartview.this, "Cannot Load Cart Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductDetails(String cartID, String quantity) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products").child(cartID);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                String cartName = productSnapshot.child("productname").getValue().toString();
                String imageURL = productSnapshot.child("imageURL").getValue().toString();
                carts cartItem = new carts(cartID, cartName, imageURL, quantity);
                cartItemList.add(cartItem);
                cartAdapter = new CartAdapter(cartItemList, cartview.this);
                cartRecyclerView.setAdapter(cartAdapter);

                cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(carts cartItem) {
                        Intent viewproduct = new Intent(cartview.this, productdetails.class);
                        viewproduct.putExtra("pid", cartItem.getCartID());
                        viewproduct.putExtra("uid", UID);
                        startActivity(viewproduct);
                    }

                    @Override
                    public void onRemoveButtonClick(carts cartItem) {
                        removeItemFromCart(UID, cartItem.getCartID());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cartview.this, "Cannot Load Product Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeItemFromCart(String uid, String cartID) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("cart").child(uid).child(cartID);
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(cartview.this, "Item Removed From Cart", Toast.LENGTH_SHORT).show();                ;
                Intent refresh = new Intent(cartview.this,cartview.class);
                refresh.putExtra("id", UID);
                startActivity(refresh);
            }
        });
    }

    public void placeolder(View view) {
        Toast.makeText(this, "Order Placed Succesfully", Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UID);
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
            }
        });
    }
}

