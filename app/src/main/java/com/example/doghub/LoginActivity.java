package com.example.doghub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    //declaring variables
    EditText emailEt, passEt;
    Button register_btn, login_btn;
    CheckBox checkBox;
    ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //equating the variables with their respective IDs
        emailEt = findViewById(R.id.login_email_et);
        passEt = findViewById(R.id.login_password_et);
        register_btn = findViewById(R.id.login_to_signup);
        login_btn = findViewById(R.id.button_login);
        checkBox = findViewById(R.id.login_checkbox);
        progressBar = findViewById(R.id.progressbar_login);
        //initializing Firebase Authentication
        mAuth = FirebaseAuth.getInstance();


        //implementing the checkbox
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //isChecked means if the checkbox is checked, it will show the actual characters of the password
                //if not checked, password remains hidden
                if (isChecked) {
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    // confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //  confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }

        });

        //setting up the register button
        //sending it from LoginActivity page to RegisterActivity page
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //setting up the login button
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();

                //checking is the email and password fields are not null
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    progressBar.setVisibility(View.VISIBLE);

                    //sending the email and password to be authenticated by firebase
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if authentication was successful, implement sendtoMain() method
                            if (task.isSuccessful()) {
                                sendtoMain();

                                //if the email or password is not authenticated
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //if the fields are null, send error toast message
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //this method takes you from the LoginActivity page to the MainActivity page
    private void sendtoMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}