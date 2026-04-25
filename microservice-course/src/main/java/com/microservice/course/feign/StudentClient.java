package com.microservice.course.feign;

import com.microservice.course.dto.client.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "microservice-student")
public interface StudentClient {

    @GetMapping("/student/searchByCourseId/{id}")
    List<StudentDto> findStudentsByCourseId(@PathVariable("id") Long courseId);
}
