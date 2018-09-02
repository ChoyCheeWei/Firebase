package com.example.ccw.firebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private EditText studentName,matric,course;
    private DatabaseReference databaseStudent;
    private Button addStudent,Search;
    List<Student> students;
    ListView listViewStudent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.Logout){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Logout your account?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isDestroyed();
                            finish();
                            Intent intent = new Intent(StudentActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        addStudent = (Button) findViewById(R.id.buttonAdd);
        Search = (Button) findViewById(R.id.search);

        studentName = (EditText) findViewById(R.id.editTextName);
        matric = (EditText) findViewById(R.id.editTextMatrix);
        course = (EditText) findViewById(R.id.editTextCourse);

        listViewStudent = (ListView) findViewById(R.id.listView);

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");
        students = new ArrayList<>();

        listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                Student student = students.get(position);
                studentName.setText(student.getName());
                matric.setText(student.getMatrix());
                course.setText(student.getCourse());


                }
                });
        listViewStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int
                    position, long id) {
                showUpdateDeleteDialog(position);
                return false;
                }
                });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudent(v);
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptMatric(v);
            }
        });
    }

    public void Clear(){
        studentName.setText(null);
        matric.setText(null);
        course.setText(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                students.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Student student = postSnapshot.getValue(Student.class);
                    students.add(student);
                }
                StudentList studentAdapter = new StudentList(StudentActivity.this, students);
                listViewStudent.setAdapter(studentAdapter);
            }

      public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void showUpdateDeleteDialog(final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextMatrix = (EditText) dialogView.findViewById(R.id.editTextMatrix);
        final EditText editTextCourse = (EditText) dialogView.findViewById(R.id.editTextCourse);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDelete);

        dialogBuilder.setTitle("");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               final String name = editTextName.getText().toString().trim();
               final String matric = editTextMatrix.getText().toString().trim();
               final String course = editTextCourse.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    editTextName.setError("Name is required");
                }

                if (TextUtils.isEmpty(matric)){
                    editTextMatrix.setError("Matric number is required");
                }

                if (TextUtils.isEmpty(course)){
                    editTextCourse.setError("Course is required");
                }

                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
                    builder.setMessage("Are you sure you want to update this student info?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!TextUtils.isEmpty(name) && (!TextUtils.isEmpty(matric)) && (!TextUtils.isEmpty(course))) {
                                        updateStudent(matric,name,course);
                                        alertDialog.dismiss();
                                        Clear();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"PLEASE FILL DATA",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();

                }

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteStudent(position);
                alertDialog.dismiss();
                Clear();
            }
        });

    }


    public void AddStudent(View v) {
        final String name = studentName.getText().toString();
        final String matrix = matric.getText().toString();
        final String Course = course.getText().toString();

        if ((TextUtils.isEmpty(name))){
            studentName.setError("Name is required");

        }
        if ((TextUtils.isEmpty(matrix))){
            matric.setError("Matric number is required");

        }
        if ((TextUtils.isEmpty(Course))){
            course.setError("Course is required");

        }


        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Want to add a new student info?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student").child(matrix);
                            final Student student = new Student(matrix, name, Course);

                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        databaseReference.setValue(student);
                                        Toast.makeText(getApplication(), "Student added successfully", Toast.LENGTH_LONG).show();
                                        Clear();
                                    }
                                    else {
                                        Toast.makeText(getApplication(), "Student's matric already exist", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            databaseReference.addListenerForSingleValueEvent(eventListener);


                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
                    .show();
        }
    }

    public void DeleteStudent(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete student info?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int ida) {
                        Student student = students.get(position);
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Student").child(student.getMatrix());
                        dR.removeValue();
                        Toast.makeText(StudentActivity.this, "DELETED",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
    private boolean updateStudent(String matrix, String name, String major) {
        //getting the specified student reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Student").child(matrix);

        //updating student
        Student student = new Student(matrix, name, major);
        dR.setValue(student);
        Toast.makeText(getApplicationContext(), "Student Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    public void promptMatric(View v){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.input);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                searchData(userInput.getText().toString());

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void searchData(final String matrix){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        Query query = databaseReference.orderByChild("matrix").equalTo(matrix);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Student student = data.getValue(Student.class);

                    studentName.setText(student.getName());
                    matric.setText(student.getMatrix());
                    course.setText(student.getCourse());
                    Toast.makeText(getApplicationContext(),"Student Found",Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}