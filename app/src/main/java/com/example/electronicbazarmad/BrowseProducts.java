package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class BrowseProducts extends AppCompatActivity  {
    private RecyclerView recyclerView;
    private productadapter productAdapter;
    private List<products> productList;
    TextView customerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products);
        Intent browse = getIntent();
        String UID = browse.getStringExtra("id");
        Toast.makeText(this, ""+UID, Toast.LENGTH_SHORT).show();
        customerid = findViewById(R.id.CID);
        customerid.setText(UID);
        recyclerView = findViewById(R.id.ReView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String PID = productSnapshot.getKey();
                    String PName = productSnapshot.child("productname").getValue().toString();
                    String PCost = productSnapshot.child("productcost").getValue().toString();
                    products product = new products(PID, PName, PCost);
                    productList.add(product);
                }

                productAdapter = new productadapter(productList, BrowseProducts.this);
                recyclerView.setAdapter(productAdapter);

                productAdapter.setOnItemClickListener(new productadapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(products product) {
                        Intent viewproduct = new Intent(BrowseProducts.this,productdetails.class);
//                        Toast.makeText(BrowseProducts.this, "Browsing" + product.getPID().toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(BrowseProducts.this, "Browsing" + customerid.getText().toString(), Toast.LENGTH_SHORT).show();
                        viewproduct.putExtra("pid",product.getPID().toString());
                        viewproduct.putExtra("uid",UID);
                        startActivity(viewproduct);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BrowseProducts.this, "Cannot Load Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
