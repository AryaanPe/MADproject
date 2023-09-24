package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
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
    }

    public void signup(View view) {
        Intent signup = new Intent(this,com.example.electronicbazarmad.signup.class);
        signup.putExtra("UserName",user.getText().toString());
        signup.putExtra("Password",pass.getText().toString());
        signup.putExtra("manager",manager.isChecked());
        startActivity(signup);

    }
}
