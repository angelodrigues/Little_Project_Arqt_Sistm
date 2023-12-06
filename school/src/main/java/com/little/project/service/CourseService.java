package com.little.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.little.project.entities.Course;
import com.little.project.repositories.CourseRepository;
import com.little.project.service.exceptions.ResourceNotFoundException;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository repository;

    public List<Course> findAll(){
        return repository.findAll();
    }

    public Course findById(Long id){
        Optional<Course> obj = repository.findById(id);
        return obj.orElseThrow(()->new ResourceNotFoundException(id));
    }

    public Course findByName(String name){
        return repository.findByName(name);
    }
}