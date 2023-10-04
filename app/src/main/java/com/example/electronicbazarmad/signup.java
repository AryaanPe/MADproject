package com.example.electronicbazarmad;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class signup extends AppCompatActivity {
    private DatabaseReference EStore;
    private EditText user,pass,FName,LName,Address;
    private TextView Status;

    private ImageView userimg;
    Uri imguri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xignup);
        EStore = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        Intent signup = getIntent();
        String username = signup.getStringExtra("UserName");
        String password = signup.getStringExtra("Password");
        boolean status = signup.getBooleanExtra("manager",false);
        user = findViewById(R.id.PhoneNum);
        pass = findViewById(R.id.Pass);
        FName = findViewById(R.id.FirstName);
        LName = findViewById(R.id.LastName);
        Address = findViewById(R.id.Address);
        Status = findViewById(R.id.status);
        userimg = findViewById(R.id.userimg);
        user.setText(username);
        pass.setText(password);
        if(status == true){
            Status.setText("Manager");
            Address.setVisibility(View.GONE);
        }else {
            Status.setText("Customer");
        }

    }

    public void newacc(View view) {
        String first = FName.getText().toString();
        String last = LName.getText().toString();
        String loc = Address.getText().toString();
        Intent signup = getIntent();
        String username = signup.getStringExtra("UserName");
        String password = signup.getStringExtra("Password");
        boolean status = signup.getBooleanExtra("manager",false);

        if (username.isEmpty() || password.isEmpty() || first.isEmpty() || last.isEmpty()) {
            Toast.makeText(signup.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }



        if(status == true){
            EStore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if((snapshot.child("manager").child(username).exists())){
                        Toast.makeText(signup.this, "This Manager iD already exits", Toast.LENGTH_SHORT).show();
                    }else{
                        StorageReference imageRef = storageReference.child("manager/" + username + ".jpg");
                        imageRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DatabaseReference mana = EStore.child("manager").child(username);
                                        mana.child("password").setValue(password);
                                        mana.child("user").setValue(first);
                                        mana.child("userL").setValue(last);
                                        mana.child("imageURL").setValue(uri.toString());
                                        Toast.makeText(signup.this, "Manager Account Created", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle getting the download URL failure
                                        Toast.makeText(signup.this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle image upload failure
                                Toast.makeText(signup.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(signup.this, "Error is "+ error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        if (status == false) {
            EStore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("customer").child(username).exists()) {
                        Toast.makeText(signup.this, "This Customer ID already exists", Toast.LENGTH_LONG).show();
                    } else {
                        StorageReference imageRef = storageReference.child("customer/" + username + ".jpg");
                        imageRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DatabaseReference cust = EStore.child("customer").child(username);
                                        cust.child("password").setValue(password);
                                        cust.child("user").setValue(first);
                                        cust.child("userL").setValue(last);
                                        cust.child("Address").setValue(loc);
                                        cust.child("imageURL").setValue(uri.toString());
                                        Toast.makeText(signup.this, "Customer Account Created", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(signup.this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(signup.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(signup.this, "Error is " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        Intent Created = new Intent(this,MainActivity.class);
        Created.putExtra("user",username);
        Created.putExtra("pass",password);
        Created.putExtra("man",status);
        setResult(RESULT_OK,Created);
        finish();
    }

    public void getimage(View view) {
        Intent GetImage = new Intent();
        GetImage.setType("image/*");
        GetImage.setAction(GetImage.ACTION_GET_CONTENT);
        startActivityForResult(GetImage,49);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==49&&data!=null&&data.getData()!=null){
            if(resultCode == RESULT_OK){
                imguri = data.getData();
                userimg.setImageURI(imguri);
            };
        };
    }
}
