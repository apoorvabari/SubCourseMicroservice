package com.example.courses.repository;

import com.example.courses.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository for managing Course entities with basic CRUD operations
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find a course by its name to support duplicate checks
    Optional<Course> findByName(String name);
}