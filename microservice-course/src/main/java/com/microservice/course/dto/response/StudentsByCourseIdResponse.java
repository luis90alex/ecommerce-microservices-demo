package com.microservice.course.dto.response;

import com.microservice.course.dto.client.StudentDto;

import java.util.List;

public class StudentsByCourseIdResponse {

    private String courseName;
    private String teacher;
    private List<StudentDto> studentDtoList;

    public StudentsByCourseIdResponse() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<StudentDto> getStudentDtoList() {
        return studentDtoList;
    }

    public void setStudentDtoList(List<StudentDto> studentDtoList) {
        this.studentDtoList = studentDtoList;
    }
}
