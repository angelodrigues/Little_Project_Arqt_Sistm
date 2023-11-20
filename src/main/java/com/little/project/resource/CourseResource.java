package com.little.project.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.little.project.entities.Course;
import com.little.project.service.CourseService;

@RestController
@RequestMapping(value = "/courses")
public class CourseResource {
    
    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<List<Course>> findAll(){
        List<Course> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<Course> findByName(@PathVariable String name) {
        Course course = service.findByName(name);
        if (course != null) {
            return ResponseEntity.ok().body(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}