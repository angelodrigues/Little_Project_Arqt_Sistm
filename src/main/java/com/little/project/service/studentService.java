package com.little.project.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.little.project.entities.Course;
import com.little.project.entities.Student;
import com.little.project.repositories.StudentRepository;
import com.little.project.service.exceptions.ResourceNotFoundException;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(Long id) {
        Optional<Student> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Set<Course> findCoursesById(Long id) {
        Optional<Student> studentOptional = repository.findById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return student.getCourses();
        } else {
            return Collections.emptySet();
        }
    }

    public Student addCourseToStudent(Long id, Course courseToAdd) {
        Optional<Student> studentOptional = repository.findById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Course> existingCourses = student.getCourses();
            
            if (existingCourses.contains(courseToAdd)) {
                throw new IllegalArgumentException("O aluno já está matriculado neste curso.");
            }

            existingCourses.add(courseToAdd);
            student.setCourses(existingCourses);

            return repository.save(student);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}
