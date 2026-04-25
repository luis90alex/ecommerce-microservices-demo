package com.microservice.course.service;

import com.microservice.course.dto.client.StudentDto;
import com.microservice.course.dto.response.StudentResponse;
import com.microservice.course.dto.response.StudentsByCourseIdResponse;
import com.microservice.course.entities.Course;
import com.microservice.course.feign.StudentClient;
import com.microservice.course.mapper.StudentMapper;
import com.microservice.course.persistence.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentClient studentClient;
    private final StudentMapper studentMapper;

    public CourseServiceImpl(CourseRepository courseRepository, StudentClient studentClient, StudentMapper studentMapper) {
        this.courseRepository = courseRepository;
        this.studentClient = studentClient;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public StudentsByCourseIdResponse findStudentsByCourseId(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));

        List<StudentDto> studentsDtoList;
        try {
            studentsDtoList = studentClient.findStudentsByCourseId(id);
        } catch (Exception e) {
            studentsDtoList = List.of();
        }

        List<StudentResponse> studentResponseList =
                studentMapper.toResponseList(studentsDtoList);

        StudentsByCourseIdResponse response = new StudentsByCourseIdResponse();
        response.setCourseName(course.getName());
        response.setTeacher(course.getTeacher());
        response.setStudents(studentResponseList);

        return response;
    }
}
