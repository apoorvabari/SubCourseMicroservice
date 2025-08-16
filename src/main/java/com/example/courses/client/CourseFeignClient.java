package com.example.courses.client;

import com.example.courses.model.Course;

import org.springframework.cloud.openfeign.FeignClient; // Correct import
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "course-service", url = "http://localhost:8081") // Defines the Feign client for the course service
public interface CourseFeignClient {

    @GetMapping("/api/courses")
    List<Course> getAllCourses();

    @GetMapping("/api/courses/{id}")
    Course getCourseById(@PathVariable("id") Long id);
    
    @GetMapping("/names")
    List<String> getNames();
    
    @Component
	abstract
    class CourseFeignFallback implements CourseFeignClient {
        @Override
        public List<String> getNames() {
            return List.of("Fallback Name");
        }
    }
}