package com.example.courses.controller;

import com.example.courses.model.Course;
import com.example.courses.model.Subject;
import com.example.courses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // Get a single course by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    // Get all subjects for a specific course
    @GetMapping("/{courseId}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsByCourseId(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return course != null ? ResponseEntity.ok(course.getSubjects()) : ResponseEntity.notFound().build();
    }

    // Get a specific subject by its ID within a specific course
    @GetMapping("/{courseId}/subjects/{subjectId}")
    public ResponseEntity<Subject> getSubjectByCourseAndSubjectId(
            @PathVariable Long courseId,
            @PathVariable Long subjectId) {
        Subject subject = courseService.getSubjectByCourseAndSubjectId(courseId, subjectId);
        return subject != null ? ResponseEntity.ok(subject) : ResponseEntity.notFound().build();
    }

    // Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@Validated @RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCourse.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdCourse);
    }

    // Add a subject to a course
    @PostMapping("/{courseId}/subjects")
    public ResponseEntity<Subject> addSubjectToCourse(
            @PathVariable Long courseId,
            @Validated @RequestBody Subject subjectDetails) {
        Subject newSubject = courseService.addSubjectToCourse(courseId, subjectDetails);
        if (newSubject != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{subjectId}")
                    .buildAndExpand(newSubject.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newSubject);
        }
        return ResponseEntity.notFound().build();
    }

    // Update an existing course
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @Validated @RequestBody Course courseDetails) {
        Course updatedCourse = courseService.updateCourse(id, courseDetails);
        return updatedCourse != null ? ResponseEntity.ok(updatedCourse) : ResponseEntity.notFound().build();
    }

    // Update a subject in a course
    @PutMapping("/{courseId}/subjects/{subjectId}")
    public ResponseEntity<Subject> updateSubjectInCourse(
            @PathVariable Long courseId,
            @PathVariable Long subjectId,
            @Validated @RequestBody Subject subjectDetails) {
        Subject updatedSubject = courseService.updateSubjectInCourse(courseId, subjectId, subjectDetails);
        return updatedSubject != null ? ResponseEntity.ok(updatedSubject) : ResponseEntity.notFound().build();
    }

    // Delete a course by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        boolean deleted = courseService.deleteCourse(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Delete a subject from a course
    @DeleteMapping("/{courseId}/subjects/{subjectId}")
    public ResponseEntity<Void> deleteSubjectInCourse(
            @PathVariable Long courseId,
            @PathVariable Long subjectId) {
        boolean deleted = courseService.deleteSubjectInCourse(courseId, subjectId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}