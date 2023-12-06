package com.biblioteca.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.escola.entity.Book;

public interface BookRepository extends JpaRepository<Book,Long>{   
}