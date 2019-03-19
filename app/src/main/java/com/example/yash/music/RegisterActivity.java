package com.example.yash.music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText email_user,password_user,confirm_password,name;
    Button register;
    TextView login;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar();
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email_user=findViewById(R.id.email_user);
        password_user=findViewById(R.id.password_user);
        confirm_password=findViewById(R.id.confirm_password);
        login=findViewById(R.id.login);
        name=findViewById(R.id.name_user);
        register=findViewById(R.id.register);
        auth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInput(email_user,password_user,confirm_password))
                {
                    register(email_user.getText().toString(),password_user.getText().toString(),name.getText().toString());
                }
            }
        });
    }
    public boolean CheckInput(EditText email,EditText password,EditText confirm_password)
    {
        boolean status=true;
        if(email.getText().toString().isEmpty())
        {
            status=false;
            email.setError("Email Needed");
        }
        else if(!email.getText().toString().matches("([\\w]+[@][\\w]+[.][A-z]+)"))
        {
            status=false;
            email.setError("Invalid Email");
        }
        if(password.getText().toString().isEmpty())
        {
            status=false;
            password.setError("Password Needed");
        }
        else if(password.getText().toString().length()<6)
        {
            status=false;
            password.setError("Minimum Length must be 6");
        }
        if(confirm_password.getText().toString().isEmpty())
        {
            status=false;
            confirm_password.setError("Please Confirm Password");
        }
        else if(!confirm_password.getText().toString().equals(password.getText().toString()))
        {
            status=false;
            confirm_password.setError("Enter correct password");
        }
        return status;
    }
    private void register(String email, String password, final String name)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    firebaseUser=auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid=firebaseUser.getUid();

                    reference=FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("Id",userid);
                    hashMap.put("Name",name);

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
