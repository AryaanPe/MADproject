package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signup extends AppCompatActivity {
    private DatabaseReference EStore;
    private EditText user,pass,FName,LName,Address;
    private TextView Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xignup);
        EStore = FirebaseDatabase.getInstance().getReference();
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
        user.setText(username);
        pass.setText(password);
        if(status == true){
            Status.setText("Manager");
            Address.setText(null);
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
        if(status == true){
            EStore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if((snapshot.child("manager").child(username).exists())){
                        Toast.makeText(signup.this, "This Manager iD already exits", Toast.LENGTH_SHORT).show();
                    }else{
//                        EStore.child("manager").setValue(username);
                        DatabaseReference mana = EStore.child("manager").child(username);
                        mana.child("password").setValue(password);
                        mana.child("user").setValue(first);
                        mana.child("userL").setValue(last);
                        Toast.makeText(signup.this, "Manager Account Created", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(signup.this, "Error is "+ error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        if(status == false){
            EStore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if((snapshot.child("customer").child(username).exists())){
                        Toast.makeText(signup.this, "This Customer iD already exits", Toast.LENGTH_LONG).show();
                    }else{
//                        EStore.child("customer").setValue(username);
                        DatabaseReference cust = EStore.child("customer").child(username);
                        cust.child("password").setValue(password);
                        cust.child("user").setValue(first);
                        cust.child("userL").setValue(last);
                        cust.child("Address").setValue(loc);
                        Toast.makeText(signup.this, "Customer Account Created", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(signup.this, "Error is "+ error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}