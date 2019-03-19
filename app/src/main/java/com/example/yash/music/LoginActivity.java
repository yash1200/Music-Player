package com.example.yash.music;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email_user,password_user;
    Button login;
    TextView sign_up;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar();
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorfav)));

        email_user=findViewById(R.id.email_user);
        password_user=findViewById(R.id.password_user);
        login=findViewById(R.id.login);
        sign_up=findViewById(R.id.sign_up);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();

        if(firebaseUser!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInput(email_user,password_user))
                {
                    auth.signInWithEmailAndPassword(email_user.getText().toString(),password_user.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    public boolean CheckInput(EditText email,EditText password)
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
        return status;
    }
}
