package com.example.ccw.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {

    private String name;
    private String matrix;
    private String course;

    public Student(){

    }

    public Student(String matrix,String name, String course) {

        this.name = name;
        this.matrix=matrix;
        this.course = course;
    }


    public String getName() {
        return name;
    }

    public String getMatrix() {
        return matrix;
    }

    public String getCourse() {
        return course;
    }
}