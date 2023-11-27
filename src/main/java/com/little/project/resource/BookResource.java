package com.little.project.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.little.project.entities.Book;
import com.little.project.service.BookService;

@RestController
@RequestMapping(value = "/books")
public class BookResource {
    @Autowired
    private BookService service;

    @GetMapping
    public ResponseEntity<List<Book>> findAll(){
        List<Book> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }
}