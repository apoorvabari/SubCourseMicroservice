package com.example.courses.service;

import com.example.courses.model.Course;
import com.example.courses.model.Subject;
import com.example.courses.repository.CourseRepository;
import com.example.courses.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // --- GET Operations ---

    // Retrieve all courses from the database
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Retrieve a course by its ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }

    // Retrieve all subjects for a specific course
    public List<Subject> getSubjectsByCourseId(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found with id: " + courseId);
        }
        return subjectRepository.findByCourseId(courseId);
    }

    // Retrieve a specific subject by its ID within a specific course
    public Subject getSubjectByCourseAndSubjectId(Long courseId, Long subjectId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found with id: " + courseId);
        }
        return subjectRepository.findById(subjectId)
                .filter(subject -> subject.getCourse() != null && subject.getCourse().getId().equals(courseId))
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + subjectId + " for course id: " + courseId));
    }

    // --- POST Operations ---

    // Create a new course, ensuring no duplicate names
    public Course createCourse(Course course) {
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        courseRepository.findByName(course.getName())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Course with name '" + course.getName() + "' already exists");
                });
        if (course.getSubjects() != null) {
            course.getSubjects().forEach(subject -> subject.setCourse(course));
        }
        return courseRepository.save(course);
    }

    // Add a subject to an existing course
    public Subject addSubjectToCourse(Long courseId, Subject subjectDetails) {
        if (subjectDetails.getTitle() == null || subjectDetails.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject title cannot be null or empty");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        subjectDetails.setCourse(course);
        course.addSubject(subjectDetails);
        courseRepository.save(course);
        return subjectDetails;
    }

    // --- PUT Operations ---

    // Update an existing course
    public Course updateCourse(Long id, Course courseDetails) {
        if (courseDetails.getName() == null || courseDetails.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        courseRepository.findByName(courseDetails.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Course with name '" + courseDetails.getName() + "' already exists");
                    }
                });
        course.setName(courseDetails.getName());
        if (courseDetails.getSubjects() != null) {
            course.setSubjects(courseDetails.getSubjects());
        } else {
            course.setSubjects(null);
        }
        return courseRepository.save(course);
    }

    // Update a subject in a course
    public Subject updateSubjectInCourse(Long courseId, Long subjectId, Subject subjectDetails) {
        if (subjectDetails.getTitle() == null || subjectDetails.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject title cannot be null or empty");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        Subject subject = subjectRepository.findById(subjectId)
                .filter(s -> s.getCourse() != null && s.getCourse().getId().equals(courseId))
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + subjectId + " for course id: " + courseId));
        subject.setTitle(subjectDetails.getTitle());
        courseRepository.save(course);
        return subject;
    }

    // --- DELETE Operations ---

    // Delete a course by its ID
    public boolean deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        courseRepository.deleteById(id);
		return false;
		
    }

    // Delete a subject from a course
    public boolean deleteSubjectInCourse(Long courseId, Long subjectId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
        boolean removed = course.getSubjects().removeIf(subject -> subject.getId().equals(subjectId));
        if (!removed) {
            throw new EntityNotFoundException("Subject not found with id: " + subjectId + " for course id: " + courseId);
        }
        courseRepository.save(course);
		return false;
    }
}