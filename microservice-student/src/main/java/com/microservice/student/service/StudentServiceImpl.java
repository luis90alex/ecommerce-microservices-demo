package com.microservice.student.service;

import com.microservice.student.dto.StudentDto;
import com.microservice.student.entities.Student;
import com.microservice.student.persistence.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<StudentDto> findByCourseId(Long courseId) {
        return studentRepository.findByCourseId(courseId).stream()
                .map(student -> {
                    StudentDto dto = new StudentDto();
                    dto.setName(student.getName());
                    dto.setLastName(student.getLastName());
                    dto.setEmail(student.getEmail());
                    dto.setCourseId(student.getCourseId());
                    return dto;
                }).toList();
    }
}
