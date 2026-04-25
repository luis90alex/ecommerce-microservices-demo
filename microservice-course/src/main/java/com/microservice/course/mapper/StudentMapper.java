package com.microservice.course.mapper;

import com.microservice.course.dto.client.StudentDto;
import com.microservice.course.dto.response.StudentResponse;

public class StudentMapper {

    public static StudentResponse toStudentResponse(StudentDto studentDto) {
        StudentResponse response = new StudentResponse();
        response.setName(studentDto.getName());
        response.setLastName(studentDto.getLastName());
        return response;
    }

    public static StudentDto toStudentDto(StudentResponse studentResponse) {
        StudentDto dto = new StudentDto();
        dto.setName(studentResponse.getName());
        dto.setLastName(studentResponse.getLastName());
        return dto;
    }
}
