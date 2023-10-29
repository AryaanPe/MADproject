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

public class EditDetails extends AppCompatActivity {
    private DatabaseReference EStore;
    private EditText user, pass, FName, LName, Address;
    private TextView Status;

    private ImageView userimg;
    private StorageReference storageReference;

    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        EStore = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        Intent edit = getIntent();
        String username = edit.getStringExtra("id");
        DatabaseReference ref = EStore.child("customer").child(username);
        user = findViewById(R.id.PhoneNum);
        pass = findViewById(R.id.Pass);
        FName = findViewById(R.id.FirstName);
        LName = findViewById(R.id.LastName);
        Address = findViewById(R.id.Address);
        userimg = findViewById(R.id.userimg);
        user.setText(username);
    }

    public void savechanges(View view) {
        EStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = user.getText().toString();
                String first = FName.getText().toString();
                String last = LName.getText().toString();
                String loc = Address.getText().toString();
                String password = pass.getText().toString();

                if (username.isEmpty() || password.isEmpty() || first.isEmpty() || last.isEmpty() || imguri == null) {
                    Toast.makeText(EditDetails.this, "Please fill in all required fields and select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                                Toast.makeText(EditDetails.this, "Details Updated", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getimage(View view) {
        Intent GetImage = new Intent();
        GetImage.setType("image/*");
        GetImage.setAction(GetImage.ACTION_GET_CONTENT);
        startActivityForResult(GetImage, 49);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 49 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                imguri = data.getData();
                userimg.setImageURI(imguri);
            }
        }
    }
}
