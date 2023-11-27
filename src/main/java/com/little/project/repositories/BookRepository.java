package com.little.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.little.project.entities.Book;

public interface BookRepository extends JpaRepository<Book,Long>{
}