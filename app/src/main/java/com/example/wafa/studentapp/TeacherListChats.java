package com.example.wafa.studentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TeacherListChats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list_chats);
    }


    public void studentList(View v){

        Intent i  = new Intent(this , TeacherChatsStudent.class);
        startActivity(i);
    }

    public void parentList(View v){

        Intent i  = new Intent(this , TeacherChatsParent.class);
        startActivity(i);
    }

}
