package com.little.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.little.project.entities.Discipline;
import com.little.project.repositories.DisciplineRepository;
import com.little.project.service.exceptions.ResourceNotFoundException;

@Service
public class DisciplineService {
    
    @Autowired
    private DisciplineRepository repository;

    public List<Discipline> findAll(){
        return repository.findAll();
    }

    public Discipline findById(Long id){
        Optional<Discipline> obj = repository.findById(id);
        return obj.orElseThrow(()->new ResourceNotFoundException(id));
    }

    public Discipline findByName(String name){
        return repository.findByName(name);
    }
}