package com.example.ccw.firebase;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class StudentList extends ArrayAdapter<Student> {
    private Activity context;
    List<Student> students;

    public StudentList(Activity context, List<Student> students) {
        super(context, R.layout.layout_student_list, students);
        this.context = context;
        this.students = students;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_student_list, null, true);

        TextView Name = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView Matrix = (TextView) listViewItem.findViewById(R.id.textViewMatrix);
        TextView Course = (TextView) listViewItem.findViewById(R.id.textViewCourse);

        Student student = students.get(position);
        Name.setText(student.getName());
        Matrix.setText(student.getMatrix());
        Course.setText(student.getCourse());

        return listViewItem;
    }
}