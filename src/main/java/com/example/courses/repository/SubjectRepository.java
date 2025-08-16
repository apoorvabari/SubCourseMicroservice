package com.example.courses.repository;

import com.example.courses.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repository for managing Subject entities with basic CRUD operations
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Find subjects by course ID to support course-subject queries
    List<Subject> findByCourseId(Long courseId);

    // Find a subject by its title to support duplicate checks
    Optional<Subject> findByTitle(String title);
}




//get subject by course id


//sql database for reflection of crud operations 