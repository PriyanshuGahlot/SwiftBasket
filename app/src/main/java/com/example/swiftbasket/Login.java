package com.example.swiftbasket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText emailInp = findViewById(R.id.emailInp);
        EditText pswdImp = findViewById(R.id.pswdInp);

        Button activeBtn = findViewById(R.id.activeBtn);
        TextView passiveBtn = findViewById(R.id.passiveBtn);

        if(auth.getUid()!=null) startActivity(new Intent(Login.this,MainActivity.class));

        View.OnClickListener signUp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button)view).setText("Please wait...");
                String email = emailInp.getText().toString();
                String pswd = pswdImp.getText().toString();
                if(pswd.length()>=4){
                    if(!containsSpecialChar(pswd)) Toast.makeText(Login.this, "Password must contain a special character", Toast.LENGTH_SHORT).show();
                    else {
                        auth.createUserWithEmailAndPassword(email,pswd).addOnFailureListener(runnable -> {
                            //editing needed
                            Toast.makeText(Login.this, "Email not valid", Toast.LENGTH_SHORT).show();
                        }).addOnSuccessListener(runnable -> {
                            startActivity(new Intent(Login.this,ExtraDetails.class));
                        }).addOnFailureListener(runnable -> {
                            ((Button)view).setText("Sign Up");
                        });
                    }
                }else{
                    Toast.makeText(Login.this, "Password should be minimum of 4 characters", Toast.LENGTH_SHORT).show();
                }
            }
        };
        View.OnClickListener login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button)view).setText("Please wait...");
                String email = emailInp.getText().toString();
                String pswd = pswdImp.getText().toString();
                auth.signInWithEmailAndPassword(email,pswd).addOnSuccessListener(runnable -> {
                    Toast.makeText(Login.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,MainActivity.class));
                }).addOnFailureListener(runnable -> {
                    Toast.makeText(Login.this, "Credentials not valid", Toast.LENGTH_SHORT).show();
                    ((Button)view).setText("Login");
                });
            }
        };
        View.OnClickListener switchTask = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!activeBtn.getText().toString().equals("Login")){
                    activeBtn.setText("Login");
                    passiveBtn.setText("or Sign Up?");
                    activeBtn.setOnClickListener(login);
                }else {
                    activeBtn.setText("Sign Up");
                    passiveBtn.setText("or Login?");
                    activeBtn.setOnClickListener(signUp);
                }
            }
        };

        activeBtn.setOnClickListener(signUp);
        passiveBtn.setOnClickListener(switchTask);

    }

    boolean containsSpecialChar(String s){
        HashSet<Character> set = new HashSet<>();
        set.add('!');
        set.add('@');
        set.add('#');
        set.add('$');
        set.add('%');
        set.add('^');
        set.add('&');
        set.add('*');
        for(char c : s.toCharArray()){
            if(set.contains(c)) return true;
        }
        return false;
    }

}