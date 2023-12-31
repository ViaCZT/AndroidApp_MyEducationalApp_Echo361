package com.example.echo361.LayoutActivity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.echo361.Course;
import com.example.echo361.Database.FirebaseDAOImpl;
import com.example.echo361.Database.FirebaseDataCallback;
import com.example.echo361.Factory.Student;
import com.example.echo361.Factory.Teacher;
import com.example.echo361.R;
import com.example.echo361.Search.CourseAVLtree;
import com.example.echo361.Search.Search;
import com.example.echo361.util.ToastUtil;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author Yuan Li u7550484
 * Delete courses
 */
public class AdminDeletionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deletion);

        EditText editText = (EditText) findViewById(R.id.ed_searchCourse);
        TextView textView = (TextView) findViewById(R.id.tx_deletedCourse);
        ListView listView = (ListView) findViewById(R.id.list_courseList);
        Button buttonDelete = (Button) findViewById(R.id.btn_delete);
        Button buttonSearch = (Button) findViewById(R.id.btn_searchCourse);
        FirebaseDAOImpl firebaseDAOImpl = FirebaseDAOImpl.getInstance();



        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ArrayList<String > list1 = new ArrayList<>();
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list1);
                listView.setAdapter(arrayAdapter);
                            //get the course Admin want to delete
                            String courseCode = String.valueOf(textView.getText());


                            int courseID = Integer.parseInt(courseCode.substring(4,8));
                            //get all courses of same code from firebase
                            firebaseDAOImpl.getData(courseCode.substring(0,4)+"Tree", null, new FirebaseDataCallback<String>() {

                                @Override
                                public void onDataReceived(String data) {
                            Gson gson = new Gson();
                            CourseAVLtree courseAVLtree = gson.fromJson(data,CourseAVLtree.class);
                            //delete course
                            courseAVLtree = courseAVLtree.delete(courseID);
                            FirebaseDAOImpl firebaseDAO = FirebaseDAOImpl.getInstance();
                            //store
                            firebaseDAO.storeData(courseCode.substring(0,4)+"Tree",null,gson.toJson(courseAVLtree));
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                }
                            });
                            //get students and delete the target course from theirs course list and store
                            firebaseDAOImpl.getData("Students", null, new FirebaseDataCallback<ArrayList<HashMap<String, Object>>>() {
                                @Override
                                public void onDataReceived(ArrayList<HashMap<String, Object>> students) {
                                    ArrayList<Student> storeStudents = new ArrayList<>();
                                    for (HashMap<String, Object> hashMap1 : students
                                            ) {
                                        Student student = new Student((String) hashMap1.get("userName"),(String)hashMap1.get("passWord"),(ArrayList<String>) hashMap1.get("courses"));
                                        boolean has = false;
                                        //check if students has the target course and delete it
                                        for (String course: student.getCourses()) {
                                            if (course.equals(courseCode.substring(0,8))){
                                                has = true;
                                            }
                                        }
                                        if (has){
                                            student.getCourses().remove(courseCode.substring(0,8));
                                        }
                                        storeStudents.add(student);
                                    }
                                    //store
                                    FirebaseDAOImpl firebaseDAO = FirebaseDAOImpl.getInstance();
                                    firebaseDAO.storeData("Students",null,storeStudents);
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                }
                            });
                            //delete the target course from teacher
                            firebaseDAOImpl.getData("Teachers", null, new FirebaseDataCallback<ArrayList<HashMap<String, Object>>>() {
                                @Override
                                public void onDataReceived(ArrayList<HashMap<String, Object>> teachers) {
                                    ArrayList<Teacher> storeTeachers = new ArrayList<>();
                                    for (HashMap<String, Object> hashMap1 : teachers
                                    ) {

                                        Teacher teacher = new Teacher((String) hashMap1.get("userName"),(String)hashMap1.get("passWord"),(ArrayList<String>) hashMap1.get("courses"));
                                        boolean has = false;
                                        if (teacher.getCourses()!=null){
                                            for (String course: teacher.getCourses()) {
                                                if (course.equals(courseCode.substring(0,8))){
                                                    has = true;
                                                }
                                            }
                                            if (!has){
                                                storeTeachers.add(teacher);
                                            }
                                        }
                                    }
                                    //store
                                    FirebaseDAOImpl firebaseDAO = FirebaseDAOImpl.getInstance();
                                    firebaseDAO.storeData("Teachers",null,storeTeachers);
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                }
                            });

                        }
                    });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = String.valueOf(editText.getText());
//                String courseCode = String.valueOf(editText.getText());


                if (!(input.isEmpty())) {

                    Search Search = new Search();
                    // get course code and college code
                    String[] courseinfo = Search.inputToCourse(input);
                    String collegeCode = courseinfo[0];
                    String courseCode = courseinfo[1];
                    ArrayList<String> allCollegeCode = new ArrayList<>();
                    ArrayList<String> list = new ArrayList<>();

                    if (!(collegeCode.equals(""))){
                        allCollegeCode = Search.getCollege(String.valueOf(collegeCode));
                    }else{
                        for (Course.CODE i : Course.CODE.values()) {
                            allCollegeCode.add(String.valueOf(i));
                        }
                    }
                    for (String i : allCollegeCode){
                        firebaseDAOImpl.getData(i+"Tree", null, new FirebaseDataCallback<String>() {

                            @Override
                            public void onDataReceived(String data) {
                                Gson gson = new Gson();
                                CourseAVLtree courseAVLtree = gson.fromJson(data,CourseAVLtree.class);
                                ArrayList<Course> courselist = new ArrayList<>();
                                courselist = Search.courseListFilted(courseAVLtree, false, false, false, false, courseCode);
                                for (Course c :courselist) {
                                    list.add(c.getTitle() +"-"+ c.getDelivery()+ "-"+c.getCareer());
                                }
                                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list);
                                listView.setAdapter(arrayAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        textView.setText(list.get(position));
                                    }
                                });
                            }
                            @Override
                            public void onError(DatabaseError error) {
                            }
                        });

                    }

                }else{
                    Context context = getApplicationContext();
                    ToastUtil.showMsg(AdminDeletionActivity.this, "Input can not be empty.");
                }
            }
        });
    }
}