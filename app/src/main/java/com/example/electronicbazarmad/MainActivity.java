package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference EStore;
    private EditText user,pass;
    private CheckBox manager;
    private String Status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.PhoneNum);
        pass = findViewById(R.id.Pass);
        manager = findViewById(R.id.mcheck);
        EStore = FirebaseDatabase.getInstance().getReference();
    }
    public void login(View view) {
        if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.getText().toString().length() != 10) {
            Toast.makeText(this, "Please enter a 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (manager.isChecked()) {
            Status = "manager";
        } else {
            Status = "customer";
        }
        final String uname = user.getText().toString();
        final String providedPassword = pass.getText().toString();

        DatabaseReference ref = EStore.child(Status).child(uname);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String storedPassword = snapshot.child("password").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(providedPassword)) {
                        Intent loginIntent = new Intent(MainActivity.this, com.example.electronicbazarmad.login.class);
                        loginIntent.putExtra("UserName", uname);
                        loginIntent.putExtra("Password", providedPassword);
                        loginIntent.putExtra("manager", manager.isChecked());
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error is " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup(View view) {
        if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.getText().toString().length() != 10) {
            Toast.makeText(this, "Please enter a 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent signup = new Intent(this,com.example.electronicbazarmad.signup.class);
        signup.putExtra("UserName",user.getText().toString());
        signup.putExtra("Password",pass.getText().toString());
        signup.putExtra("manager",manager.isChecked());
        startActivityForResult(signup,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                user.setText(data.getStringExtra("user"));
                pass.setText(data.getStringExtra("pass"));
                boolean man = data.getBooleanExtra("man",false);
                if(man==true){
                    manager.setChecked(true);
                }else{
                    manager.setChecked(false);
                }

            }
        }
    }
}

