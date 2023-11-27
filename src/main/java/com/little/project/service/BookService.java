package com.little.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.little.project.entities.Book;
import com.little.project.repositories.BookRepository;

public class BookService {
    @Autowired
    private BookRepository repository;

    public List<Book> findAll(){
        return repository.findAll();
    }

}