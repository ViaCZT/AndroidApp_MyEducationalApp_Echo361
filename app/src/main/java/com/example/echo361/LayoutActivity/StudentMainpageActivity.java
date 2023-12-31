package com.example.echo361.LayoutActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.echo361.R;

import java.util.ArrayList;

/**
 * The StudentMainpageActivity class is for implementing the jump to different page for students.
 * Button "Enroll" leads to the course enroll page.
 * Button "Drop" leads to the course drop page.
 * Button "MyCourse" leads to the current course page.
 *
 * @author Yijun Huang, u7564899
 * @author Yitao Zhang, u7504766
 */
public class StudentMainpageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mainpage);

        Intent intent = getIntent();
        ArrayList<String> courses_list= intent.getStringArrayListExtra("courses_list");
        ArrayList<String> courses_list_drop= intent.getStringArrayListExtra("courses_list_drop");
        String studentID= intent.getStringExtra("student_id");
        String uid = intent.getStringExtra("uid");

        Button toEnroll = findViewById(R.id.btn_toEnroll);
        toEnroll.setOnClickListener(view -> {
            Intent intent0 = new Intent(StudentMainpageActivity.this, EnrollActivity.class);
            intent0.putExtra("student_id", studentID);
            StudentMainpageActivity.this.startActivity(intent0);
        });

        Button toDrop = findViewById(R.id.btn_toDrop);
        toDrop.setOnClickListener(view -> {
            Intent intent0 = new Intent(StudentMainpageActivity.this, DropActivity.class);
            intent0.putExtra("student_id", studentID);
            StudentMainpageActivity.this.startActivity(intent0);
        });

        Button ToMyCourse = findViewById(R.id.btn_toMyCourse);
        ToMyCourse.setOnClickListener(view -> {

            if (courses_list_drop == null || courses_list_drop.isEmpty()){
                Intent intent0 = new Intent(StudentMainpageActivity.this, MyCourseActivity.class);
                intent0.putStringArrayListExtra("courses_list",courses_list);
                intent0.putExtra("student_id", studentID);
                intent0.putExtra("uid",uid);
                Log.d("cuurrentid",uid);
                StudentMainpageActivity.this.startActivity(intent0);
            }else{
                Intent intent0 = new Intent(StudentMainpageActivity.this, MyCourseActivity.class);
                intent0.putStringArrayListExtra("courses_list",courses_list_drop);
                intent0.putExtra("student_id", studentID);
                intent0.putExtra("uid",uid);
                Log.d("cuurrentid",uid);
                StudentMainpageActivity.this.startActivity(intent0);
            }
        });
    }

}