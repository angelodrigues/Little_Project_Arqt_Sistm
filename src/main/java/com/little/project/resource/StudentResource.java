package com.little.project.resource;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.little.project.entities.Book;
import com.little.project.entities.Course;
import com.little.project.entities.Student;
import com.little.project.service.StudentService;

@RestController
@RequestMapping(value = "/students")
public class StudentResource {

    @Autowired
    private StudentService service;

    @GetMapping
    public ResponseEntity<List<Student>> findAll(){
        List<Student> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Student> findById(@PathVariable Long id){
        Student obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }
    //COURSES
    @GetMapping(value = "/{id}/courses")
    public ResponseEntity<Set<Course>> findCoursesById(@PathVariable Long id){
        Set<Course> courses = service.findCoursesById(id);
        return ResponseEntity.ok().body(courses);
    }

    @PostMapping(value = "/{id}/courses/{courseId}")
    public ResponseEntity<Student> addCourseToStudent(@PathVariable Long id, @PathVariable Long courseId){
        Student updatedStudent = service.addCourseToStudent(id, courseId);
        return ResponseEntity.ok().body(updatedStudent);
    }

    @DeleteMapping(value = "/{id}/courses/{courseId}")
    public ResponseEntity<Student> removeCourseFromStudent(@PathVariable Long id, @PathVariable Long courseId) {
        Student updatedStudent = service.removeCourseFromStudent(id, courseId);
        return ResponseEntity.ok().body(updatedStudent);
    }
    //BOOKS
    @GetMapping(value = "/{id}/books")
    public ResponseEntity<Set<Book>> getRentedBooks(@PathVariable Long id) {
        Set<Book> rentedBooks = service.getRentedBooks(id);
        return ResponseEntity.ok().body(rentedBooks);
    }

    @PostMapping(value = "/{id}/books/{bookId}")
    public ResponseEntity<Student> rentBook(@PathVariable Long id, @PathVariable Long bookId) {
        Student updatedStudent = service.rentBook(id, bookId);
        return ResponseEntity.ok().body(updatedStudent);
    }

    @DeleteMapping(value = "/{id}/books/{bookId}")
    public ResponseEntity<Student> returnBook(@PathVariable Long id, @PathVariable Long bookId) {
        Student updatedStudent = service.returnBook(id, bookId);
        return ResponseEntity.ok().body(updatedStudent);
    }
}
