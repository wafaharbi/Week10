package com.example.wafa.studentapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentUpadate extends AppCompatActivity {

    FirebaseAuth auth;
    TextView key;
    DatabaseReference ref;
    FirebaseUser u;
    EditText studentName, studentEmail, studentPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_upadate);


        key = (TextView) findViewById(R.id.stdIDp);

        studentName = (EditText) findViewById(R.id.updateNamep);
        studentEmail = (EditText) findViewById(R.id.updateEmailp);
        studentPhone = (EditText) findViewById(R.id.updatePhonep);

        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();


        ref = FirebaseDatabase.getInstance().getReference().child("Parents").child(u.getUid());


        studentName.setText(getIntent().getStringExtra("name"));
        studentEmail.setText(getIntent().getStringExtra("email"));
        studentPhone.setText(getIntent().getStringExtra("phone"));


    }

        public  void update(View v){




            String n = studentName.getText().toString().trim();
            String e = studentEmail.getText().toString().trim();
            String p = studentPhone.getText().toString().trim();


            final Parent user =new Parent(n,e,p);


            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    // here is update code

                    // ref.child("Student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);


                    dataSnapshot.getRef().child("name").setValue(studentName.getText().toString());
                    dataSnapshot.getRef().child("email").setValue(studentEmail.getText().toString());
                    dataSnapshot.getRef().child("phone").setValue(studentPhone.getText().toString());

                    Toast.makeText(getApplicationContext() , " data was updated" , Toast.LENGTH_SHORT).show();

                    ParentUpadate.this.finish();

                    Intent i =new Intent(getApplicationContext(), ParentInfo.class);
                    startActivity(i);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }






    }
