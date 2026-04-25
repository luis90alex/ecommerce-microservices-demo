package com.microservice.course.mapper;

import com.microservice.course.dto.client.StudentDto;
import com.microservice.course.dto.response.StudentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentMapper {

    public StudentResponse toResponse(StudentDto studentDto) {
        StudentResponse response = new StudentResponse();
        response.setName(studentDto.getName());
        response.setLastName(studentDto.getLastName());
        return response;
    }

    public List<StudentResponse> toResponseList(List<StudentDto> studentDtoList) {
        return studentDtoList.stream()
                .map(this::toResponse)
                .toList();
    }
}