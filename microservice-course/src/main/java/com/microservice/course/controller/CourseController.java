package com.microservice.course.controller;

import com.microservice.course.dto.response.StudentsByCourseIdResponse;
import com.microservice.course.entities.Course;
import com.microservice.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<Course> create(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(course));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/search-students/{id}")
    public ResponseEntity<StudentsByCourseIdResponse> findStudentsByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findStudentsByCourseId(id));
    }
}
