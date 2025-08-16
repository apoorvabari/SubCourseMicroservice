package com.example.courses.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Course name cannot be null")
    @NotBlank(message = "Course name cannot be empty")
    private String name;

    // Manages subjects with cascade and orphan removal
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Subject> subjects = new ArrayList<>();

    // Default constructor for JPA
    public Course() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    // Handles bidirectional relationship for updating subjects
    public void setSubjects(List<Subject> subjects) {
        this.subjects.clear();
        if (subjects != null) {
            for (Subject subject : subjects) {
                subject.setCourse(this);
                this.subjects.add(subject);
            }
        }
    }

    // Adds a single subject, maintaining bidirectional relationship
    public void addSubject(Subject subject) {
        if (subject != null) {
            subject.setCourse(this);
            this.subjects.add(subject);
        }
    }

    // toString excluding subjects to avoid recursion
    @Override
    public String toString() {
        return "Course{id=" + id + ", name='" + name + "'}";
    }

    // equals and hashCode for entity comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}