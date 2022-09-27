package com.dev.vickyeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {

    EditText inputFirstname, inputLastname, input_Email,inputCellphone, input_password, inputConfirm_password;
    Button btnSignup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth myAuth;
    FirebaseUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstname=findViewById(R.id.inputFirstname);
        inputLastname=findViewById(R.id.inputLastname);
        input_Email=findViewById(R.id.input_Email);
        inputCellphone=findViewById(R.id.inputCellphone);
        input_password=findViewById(R.id.input_password);
        inputConfirm_password=findViewById(R.id.inputConfirm_password);
        btnSignup=findViewById(R.id.btnSignup);
        progressDialog=new ProgressDialog(this);
        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCredentials();
            }
        });
    }

    private void AuthCredentials(){
        String firstName=inputFirstname.getText().toString();
        String lastName=inputLastname.getText().toString();
        String email=input_Email.getText().toString();
        String cellphone=inputCellphone.getText().toString();
        String password=input_password.getText().toString();
        String comfirmPassword=inputConfirm_password.getText().toString();

        if (firstName.isEmpty()){
            showError(inputFirstname, "Enter first Name");
        }
        else if (lastName.isEmpty()){
            showError(inputLastname, "Enter last Name");
        }
        else if (email.isEmpty() || !email.matches(emailPattern)){
            showError(input_Email, "Invalid email");
        }
        else if (cellphone.isEmpty()){
            showError(inputCellphone, "Enter cellphone number");
        }
        else if (password.isEmpty() || password.length() < 8){
            showError(input_password, "Password should 8 character or more");
        }
        else if (comfirmPassword.isEmpty() || !password.equals(comfirmPassword)){
            showError(inputConfirm_password, "Password does not match");
        }
        else {
            progressDialog.setMessage("Please wait while Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(registerActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(registerActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(registerActivity.this, loginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }
}