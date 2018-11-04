package com.example.wafa.studentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupTeacher extends AppCompatActivity implements View.OnClickListener {

    EditText studentName, studentEmail, studentPassword, studentPhone;
    Button signup;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    private ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_teacher);

        studentName = (EditText) findViewById(R.id.editNt);
        studentEmail = (EditText) findViewById(R.id.editEt);
        studentPassword = (EditText) findViewById(R.id.editPt);
        studentPhone = (EditText) findViewById(R.id.editPHt);
        signup = (Button) findViewById(R.id.btnt);

        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();

        signup.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();


    }
    public void Account(View v){
        Intent i = new Intent(this, LoginTeacher.class);
        startActivity(i);
    }


    @Override
    protected void onStart() {

        super.onStart();
        if (auth.getCurrentUser() != null) {

        }
    }

    public void register() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request, please wait...");

        progressDialog.show();


        final String name = studentName.getText().toString().trim();
        final String email = studentEmail.getText().toString().trim();
        final String password = studentPassword.getText().toString().trim();
        final String phone = studentPhone.getText().toString().trim();
        final String img = "default";
        final  String thumb_img = "default";


        if (name.isEmpty()) {
            studentName.setError(" invalid name");
            studentName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            studentEmail.setError(" error email");
            studentEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            studentEmail.setError("  invalid email");
            studentEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            studentPassword.setError("blank");
            studentPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            studentPassword.setError(" password at least 6 charecter");
            studentPassword.requestFocus();
            return;
        }


        if (phone.isEmpty()) {
            studentPhone.setError(" empty");
            studentPhone.requestFocus();
            return;
        }
        if (phone.length() != 10) {

            studentPhone.setError(" atleast 10 number");
            studentPhone.requestFocus();
            return;

        }

        firebaseDatabase = FirebaseDatabase.getInstance();

        ref = firebaseDatabase.getReference().child("Teachers");


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    final  String id= firebaseUser.getUid();

                    User teacher = new User(name, email,phone,password,img, thumb_img , id);

                    HashMap<String, String> userMap = new HashMap<>();

                    userMap.put("name", name);
                    userMap.put("email" , email);
                    userMap.put("password" , password);
                    userMap.put("phone" , phone);
                    userMap.put("id" , id);
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");

                    FirebaseDatabase.getInstance().getReference("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), " Teacher signup successflly", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext(), TeacherInfo.class);
                                startActivity(i);

                            } else {
                                progressDialog.dismiss();

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), " You are already signin ", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), " Please try again", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "an error accoured , Please try again", Toast.LENGTH_SHORT).show();

                }
            }
        });


}


        @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnt:
                    register();

                    break;


            }
    }
}
