package com.example.electronicbazarmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference EStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EStore = FirebaseDatabase.getInstance().getReference();

        String phone = "7506640631";
        String user = "Aryaan";
        String userL = "Peshoton";
        String password = "I049";
        test(phone, user, password, userL);
    }

    public void test(String phone, String user, String password, String userL) {
        EStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.child("manager").child(phone)).exists()){
                    Toast.makeText(MainActivity.this, "This ID already registred", Toast.LENGTH_SHORT).show();

                }else{
                    EStore.child("manager").setValue(phone);
                    DatabaseReference managerRef = EStore.child("manager").child(phone);
                    managerRef.child("user").setValue(user);
                    managerRef.child("userL").setValue(userL);
                    managerRef.child("password").setValue(password);
                    Toast.makeText(MainActivity.this, "Data Set ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error is "+ error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
