package com.example.courses.service;

import com.example.courses.model.Subject;
import com.example.courses.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // --- GET Operations ---

    // Retrieve all subjects from the database
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Retrieve a subject by its ID
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + id));
    }

    // --- POST Operations ---

    // Create a new subject, ensuring no duplicate titles
    public Subject createSubject(Subject subject) {
        if (subject.getTitle() == null || subject.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject title cannot be null or empty");
        }
        subjectRepository.findByTitle(subject.getTitle())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Subject with title '" + subject.getTitle() + "' already exists");
                });
        return subjectRepository.save(subject);
    }

    // --- PUT Operations ---

    // Update an existing subject
    public Subject updateSubject(Long id, Subject subjectDetails) {
        if (subjectDetails.getTitle() == null || subjectDetails.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject title cannot be null or empty");
        }
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + id));
        subjectRepository.findByTitle(subjectDetails.getTitle())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Subject with title '" + subjectDetails.getTitle() + "' already exists");
                    }
                });
        subject.setTitle(subjectDetails.getTitle());
        return subjectRepository.save(subject);
    }

    // --- DELETE Operations ---

    // Delete a subject by its ID
    public void deleteSubject(Long id) {
        subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + id));
        subjectRepository.deleteById(id);
    }
}