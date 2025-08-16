package com.example.courses;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.example.courses.model.Course;
import com.example.courses.model.Subject;
import com.example.courses.repository.CourseRepository;
import com.example.courses.repository.SubjectRepository;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.courses.client")
public class CourseServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(CourseRepository courseRepository, SubjectRepository subjectRepository) {
        return args -> {
            try {
                createCourseWithSubjects(courseRepository, subjectRepository, "DBDA",
                        List.of("Data Structures", "Database Systems", "Machine Learning", "Web Technologies", "Cloud Computing"));
                createCourseWithSubjects(courseRepository, subjectRepository, "Embedded",
                        List.of("Microcontrollers", "RTOS", "Embedded C", "IoT Systems"));
                createCourseWithSubjects(courseRepository, subjectRepository, "DAC",
                        List.of("Java Programming", "Spring Boot", "Hibernate", "REST APIs"));
                createCourseWithSubjects(courseRepository, subjectRepository, "Electronics",
                        List.of("Analog Circuits", "Digital Electronics", "VLSI Design", "Signal Processing"));
                logger.info("Database initialized successfully with courses and subjects.");
            } catch (Exception e) {
                logger.error("Failed to initialize database: {}", e.getMessage());
                throw e;
            }
        };
    }

    private void createCourseWithSubjects(CourseRepository courseRepository, SubjectRepository subjectRepository,
                                         String courseName, List<String> subjectTitles) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        if (subjectTitles == null || subjectTitles.isEmpty()) {
            throw new IllegalArgumentException("Subject titles cannot be null or empty");
        }

        Optional<Course> existingCourse = courseRepository.findByName(courseName);
        if (existingCourse.isPresent()) {
            logger.info("Course {} already exists, skipping creation.", courseName);
            return;
        }

        Course course = new Course();
        course.setName(courseName);

        List<Subject> subjects = subjectTitles.stream()
                .filter(title -> title != null && !title.trim().isEmpty())
                .map(title -> {
                    Subject subject = new Subject();
                    subject.setTitle(title);
                    subject.setCourse(course);
                    return subject;
                })
                .toList();

        course.setSubjects(subjects);
        courseRepository.save(course);
    }
}