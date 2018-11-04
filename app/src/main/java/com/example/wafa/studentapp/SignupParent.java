package com.example.wafa.studentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupParent extends AppCompatActivity implements View.OnClickListener{

    EditText studentName , studentEmail , studentPassword , studentPhone , studentusername ;
    Button signup;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    private ProgressDialog progressDialog;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_parent);


        studentName = (EditText) findViewById( R.id.editNS);
        studentEmail= (EditText) findViewById(R.id.editES);
        studentPassword = (EditText) findViewById(R.id.editPS);
        studentPhone = (EditText) findViewById(R.id.editPHS);
        studentusername = (EditText) findViewById(R.id.editstd) ;
        signup = (Button) findViewById(R.id.btnS);

        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();

        signup.setOnClickListener(this);
        auth= FirebaseAuth.getInstance();
    }
    public void Account(View v){
        Intent i = new Intent(this, LoginParent.class);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){

        }
    }

    public void register(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request, please wait...");

        progressDialog.show();

        final String name =  studentName.getText().toString().trim();
        final String username = studentusername.getText().toString().trim();
        final String email = studentEmail.getText().toString().trim();
        final String password = studentPassword.getText().toString().trim();
        final String phone = studentPhone.getText().toString().trim();
        final String image = "default";
        final  String thumb_image = "default";

       // final Parent parent= new Parent(name,email,password,phone ,username);
        //final Student student = new Student(name,email,password,phone ,username);

       // Query usernameQuery  = FirebaseDatabase.getInstance().getReference().child("Parents").equalTo(username);
        //Query std = FirebaseDatabase.getInstance().getReference().child("Student").equalTo(username);


/**

        if(parent.getUsername().equals(student.getUsername()))
        {
            Toast.makeText(getApplicationContext(), " done ", Toast.LENGTH_SHORT).show();


        }

      

        if(!parent.getUsername().equals(student.getUsername())) {


            studentusername.setError(" ##### ");
            studentusername.requestFocus();
            return;
        }

**/
        if(name.isEmpty()){
            studentName.setError(" invalid name");
            studentName.requestFocus();
            return;
        }
        if(username.isEmpty()){
            studentusername.setError(" error email");
            studentusername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            studentEmail.setError(" error email");
            studentEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            studentEmail.setError("  invalid email");
            studentEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            studentPassword.setError("blank");
            studentPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            studentPassword.setError(" password at least 6 charecter");
            studentPassword.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            studentPhone.setError(" empty");
            studentPhone.requestFocus();
            return;
        }
        if(phone.length() != 10){

            studentPhone.setError(" atleast 10 number");
            studentPhone.requestFocus();
            return;

        }





        firebaseDatabase = FirebaseDatabase.getInstance();

        ref= firebaseDatabase.getReference().child("Parents");





        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    final  String id= firebaseUser.getUid();

                    User parent= new User(name,email,password,phone ,username , image , thumb_image, id);

                    HashMap<String, String> userMap = new HashMap<>();

                    userMap.put("name", name);
                    userMap.put("username" , username);
                    userMap.put("email" , email);
                    userMap.put("password" , password);
                    userMap.put("phone" , phone);
                    userMap.put("id" ,id);
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");


                    FirebaseDatabase.getInstance().getReference("Parents").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {


                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext()," Parent signup successflly" , Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext() , ParentInfo.class);
                                startActivity(i);

                            }
                            else {
                                progressDialog.dismiss();
                                if( task.getException() instanceof FirebaseAuthUserCollisionException)
                                {
                                    Toast.makeText(getApplicationContext()," You are already signin " , Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), " Please try again", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    });
                }
                else{
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "an error accoured , Please try again", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnS:
                register();

                break;


        }

    }


}
