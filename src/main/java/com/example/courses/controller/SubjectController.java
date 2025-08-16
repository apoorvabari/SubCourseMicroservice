package com.example.courses.controller;

import com.example.courses.model.Subject;
import com.example.courses.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.constraints.Positive; 


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@Validated
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // Get all subjects
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    // Get a single subject by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable @Positive Long id) {
        Subject subject = subjectService.getSubjectById(id);
        return subject != null ? ResponseEntity.ok(subject) : ResponseEntity.notFound().build();
    }


    // Create a new subject
    @PostMapping
    public ResponseEntity<Subject> createSubject(@Validated @RequestBody Subject subject) {
        Subject createdSubject = subjectService.createSubject(subject);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSubject.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdSubject);
    }

    // Update an existing subject
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable @Positive Long id,
            @Validated @RequestBody Subject subjectDetails) {
        Subject updatedSubject = subjectService.updateSubject(id, subjectDetails);
        return updatedSubject != null ? ResponseEntity.ok(updatedSubject) : ResponseEntity.notFound().build();
    }

    // Delete a subject by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable @Positive Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}