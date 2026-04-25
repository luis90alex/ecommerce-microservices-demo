package com.microservice.course.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-student")
public interface StudentClient {

    @GetMapping("/searchByCourseId/{id}")
    ResponseEntity<?> findByCourseId(@PathVariable Long courseId);
}
