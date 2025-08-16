package com.example.courses.test;

import com.example.courses.client.CourseFeignClient;
import com.example.courses.model.Course;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feign-test")
public class FeignClientTestController {

    @Autowired
    private CourseFeignClient courseFeignClient;

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCoursesViaFeign() {
        try {
            return ResponseEntity.ok(courseFeignClient.getAllCourses());
        } catch (FeignException ex) {
            int status = ex.status();
            if (status == -1) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(null); // or return a custom error DTO
            }
            return ResponseEntity.status(status).build();
        }
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseByIdViaFeign(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseFeignClient.getCourseById(id));
        } catch (FeignException ex) {
            int status = ex.status();
            if (status == -1) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(null); // or return a custom error DTO
            }
            return ResponseEntity.status(status).build();
        }
    }
}
