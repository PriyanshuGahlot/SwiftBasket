package com.example.swiftbasket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ExtraDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_details);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        EditText nameInp = findViewById(R.id.nameInp);
        EditText phoneInp = findViewById(R.id.phoneInp);
        EditText addressInp = findViewById(R.id.addressInp);

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(view -> {
            String name = nameInp.getText().toString();
            String phone = phoneInp.getText().toString();
            String address = addressInp.getText().toString();
            HashMap<String,String> details = new HashMap<>();
            details.put("name",name);
            details.put("phone",phone);
            details.put("address",address);
            details.put("cartCount","0");
            if(name.length()>0 && phone.length()==10 && address.length()>0){
                reference.child("users").child(auth.getUid()).setValue(details);
                Toast.makeText(this, "Welcome "+name, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
            }else {
                if(name.length()==0) Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
                else if(phone.length()!=10) Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                else if(address.length()==0) Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
            }
        });

    }
}