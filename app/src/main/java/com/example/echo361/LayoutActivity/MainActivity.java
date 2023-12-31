package com.example.echo361.LayoutActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echo361.Database.FirebaseDAOImpl;
import com.example.echo361.R;
import com.example.echo361.util.ToastUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The MainActivity is for login feature.
 * There are three roles: Students, Teacher and Admin.
 *
 * @author Zetian Chen, u7564812
 * @author Yuan Li u7550484
 * @author Zihan Ai, u7528678
 */

public class MainActivity extends AppCompatActivity {

    private DatabaseReference studentsReference;
    private DatabaseReference teachersReference;
    private DatabaseReference adminReference;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getBaseContext());

//         //initial course, student, and teacher data in firebase
//        FirebaseDAOImpl firebaseDAOImpl = FirebaseDAOImpl.getInstance();
//        try {
//            firebaseDAOImpl.initialCoursesData(getApplicationContext());
//            firebaseDAOImpl.initialTeacherData(getApplicationContext());
//            firebaseDAOImpl.initialStudentData(getApplicationContext());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        Button mBtnLogin = findViewById(R.id.btn_login);
        EditText editText1 = findViewById(R.id.ed_user);
        EditText editText2 = findViewById(R.id.ed_password);

        studentsReference = FirebaseDatabase.getInstance().getReference("Students");


        teachersReference = FirebaseDatabase.getInstance().getReference("Teachers");
        adminReference = FirebaseDatabase.getInstance().getReference("Admin");

        mBtnLogin.setOnClickListener(view -> {
            String inputUsername = editText1.getText().toString();
            String inputPassword = editText2.getText().toString();

            if (!inputUsername.isEmpty() && !inputPassword.isEmpty()) {
                // search student node
                studentsReference.orderByChild("userName").equalTo(inputUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean userFound = false;
//                        Log.d(TAG, "Data snapshot: " + dataSnapshot.toString());
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                            Log.d(TAG, "Checking node");
                            String storedPassword = userSnapshot.child("passWord").getValue(String.class);
                            if (storedPassword != null && storedPassword.equals(inputPassword)) {
                                userFound = true;
                                // login successfully, go to student page
                                String studentName = userSnapshot.child("userName").getValue(String.class);
                                String studentID = userSnapshot.child("passWord").getValue(String.class);
                                // Get course list
                                GenericTypeIndicator<List<String>> typeIndicator = new GenericTypeIndicator<List<String>>() {};
                                List<String> coursesList = userSnapshot.child("courses").getValue(typeIndicator);
//                                Log.d(TAG, coursesList.get(0));
                                Intent intent = new Intent(MainActivity.this, StudentMainpageActivity.class);
                                intent.putExtra("student_name", studentName);
                                intent.putExtra("student_id", studentID);
                                intent.putExtra("uid",inputPassword);
                                intent.putStringArrayListExtra("courses_list", new ArrayList<>(Objects.requireNonNull(coursesList)));
                                MainActivity.this.startActivity(intent);
                                break;
                            }
                        }

                        if (!userFound) {
                            // Query teacher child nodes
                            teachersReference.orderByChild("userName").equalTo(inputUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    boolean userFound = false;
//                                    Log.d(TAG, "Data snapshot: " + dataSnapshot.toString());
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                                        Log.d(TAG, "Checking node");
                                        String storedPassword = userSnapshot.child("passWord").getValue(String.class);
                                        if (storedPassword != null && storedPassword.equals(inputPassword)) {
                                            userFound = true;
                                            // Teacher Login successful
                                            GenericTypeIndicator<List<String>> typeIndicator = new GenericTypeIndicator<List<String>>() {};
                                            List<String> coursesList = userSnapshot.child("courses").getValue(typeIndicator);
                                            String course = (coursesList != null) ? coursesList.get(0) : null;
                                            Log.d(TAG,"get course"+course);
                                            String teacherName = userSnapshot.child("userName").getValue(String.class);
                                            Intent intent = new Intent(MainActivity.this, CourseMainpageActivity.class);
                                            intent.putExtra("teacher_name", teacherName);
                                            intent.putExtra("courseName",course);
                                            intent.putExtra("uid",inputPassword);
                                            intent.putExtra("is_teacher",true);
                                            MainActivity.this.startActivity(intent);
                                            break;
                                        }
                                    }

                                    if (!userFound) { // Query Admin child nodes
                                        adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                boolean userFound = false;
                                                String storedUsername = dataSnapshot.child("userName").getValue(String.class);
                                                String storedPassword = dataSnapshot.child("passWord").getValue(String.class);
                                                if (storedPassword != null && storedUsername != null
                                                        && storedUsername.equals(inputUsername) && storedPassword.equals(inputPassword)){
                                                    userFound = true;
                                                    // Admin Login successful
                                                    Intent intent = new Intent(getApplicationContext(), AdminDeletionActivity.class);
                                                    startActivity(intent);
                                                }
                                                // Failded login, with toast.
                                                if (!userFound) {
                                                    ToastUtil.showMsg(MainActivity.this, "Login failed: No user found with username " + inputUsername + " and password " + inputPassword);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    Log.w(TAG, "loadTeacher:onCancelled", databaseError.toException());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.w(TAG, "loadStudent:onCancelled", databaseError.toException());
                    }
                });
            } else {
                ToastUtil.showMsg(MainActivity.this, "Username and password cannot be empty");
            }
        });
    }

}
