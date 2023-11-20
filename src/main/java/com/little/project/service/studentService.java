package com.little.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.little.project.entities.Student;
import com.little.project.repositories.StudentRepository;
import com.little.project.service.exceptions.ResourceNotFoundException;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public List<Student> findAll(){
        return repository.findAll();
    }

    public Student findById(Long id){
        Optional<Student> obj = repository.findById(id);
        return obj.orElseThrow(()->new ResourceNotFoundException(id));
    }
}