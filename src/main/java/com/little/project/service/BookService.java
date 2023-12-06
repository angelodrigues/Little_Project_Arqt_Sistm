package com.little.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.little.project.entities.Book;
import com.little.project.repositories.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public List<Book> findAll(){
        return repository.findAll();
    }
}