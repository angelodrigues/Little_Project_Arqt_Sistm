package com.little.project.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.little.project.entities.Discipline;
import com.little.project.service.DisciplineService;

@RestController
@RequestMapping(value = "/disciplines")
public class DisciplineResource {
    
    @Autowired
    private DisciplineService service;

    @GetMapping
    public ResponseEntity<List<Discipline>> findAll(){
        List<Discipline> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<Discipline> findByName(@PathVariable String name) {
        Discipline discipline = service.findByName(name);
        if (discipline != null) {
            return ResponseEntity.ok().body(discipline);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}