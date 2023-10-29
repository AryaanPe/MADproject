package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addproducts extends AppCompatActivity {
    private TextView managerId;
    private EditText productid, productname, productcat, productdesc, productcost;
    private ImageView productimg;
    private StorageReference storageReference;
    private DatabaseReference EStore;

    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproducts);
        managerId = findViewById(R.id.MID);
        productid = findViewById(R.id.productid);
        productname = findViewById(R.id.productname);
        productcat = findViewById(R.id.productcat);
        productdesc = findViewById(R.id.productdesc);
        productcost = findViewById(R.id.productcost);
        productimg = findViewById(R.id.productimg);
        EStore = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        Intent addp = getIntent();
        String username = addp.getStringExtra("id");
        managerId.setText(username);
    }

    public void getimage(View view) {
        Intent GetImage = new Intent();
        GetImage.setType("image/*");
        GetImage.setAction(GetImage.ACTION_GET_CONTENT);
        startActivityForResult(GetImage, 49);
    }

    public void pushproduct(View view) {
        String managerIdText = managerId.getText().toString();
        String productidText = productid.getText().toString();
        String productnameText = productname.getText().toString();
        String productcatText = productcat.getText().toString();
        String productdescText = productdesc.getText().toString();
        String productcostText = productcost.getText().toString();

        if (productidText.isEmpty() || productnameText.isEmpty() || productcatText.isEmpty() ||
                productdescText.isEmpty() || productcostText.isEmpty() || imguri == null) {
            Toast.makeText(addproducts.this, "Provide all details", Toast.LENGTH_SHORT).show();
            return;
        }

        EStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("products").child(productidText).exists()) {
                    Toast.makeText(addproducts.this, "Product ID " + productidText + " already exists", Toast.LENGTH_SHORT).show();
                } else {
                    StorageReference imageRef = storageReference.child("products/" + productidText + ".jpg");
                    imageRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference prod = EStore.child("products").child(productidText);
                                    prod.child("productname").setValue(productnameText);
                                    prod.child("productcat").setValue(productcatText);
                                    prod.child("productdesc").setValue(productdescText);
                                    prod.child("productcost").setValue(productcostText);
                                    prod.child("AddedBy").setValue(managerIdText);
                                    prod.child("imageURL").setValue(uri.toString());
                                    Toast.makeText(addproducts.this, "Product Successfully Added", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 49 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                imguri = data.getData();
                productimg.setImageURI(imguri);
            }
        }
    }
}
