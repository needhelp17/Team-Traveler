package com.example.teamtraveler.presentation.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamtraveler.R;
import com.example.teamtraveler.presentation.homePage.HomePageActivity;
import com.example.teamtraveler.presentation.signUp.signUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailTextView, passwordTextView;
    Button loginButton,registerButton;
    FirebaseAuth firebaseAuth;
    final static int REQUEST_CODE_CONNEXION=234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextView = (EditText) findViewById(R.id.user_email_login);
        passwordTextView = (EditText) findViewById(R.id.user_password_login);
        loginButton = (Button) findViewById(R.id.btn_connection_login);
        registerButton=(Button) findViewById(R.id.btn_register_login);
        firebaseAuth = FirebaseAuth.getInstance();
        View linearLayoutFormCreateTrip=findViewById(R.id.LinearLayoutLogIn);
        linearLayoutFormCreateTrip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(emailTextView.getWindowToken(), 0);
                return true;
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                login(email,password);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signUpActivity.class));
            }
        });
    }

    private void login(String email,String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.msg_auth_success_login),
                            Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(getApplicationContext(), HomePageActivity.class),REQUEST_CODE_CONNEXION);
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.msg_auth_failed_login),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONNEXION) {
            if(firebaseAuth.getCurrentUser()!=null){
                finish();
            }

        }
    }

}