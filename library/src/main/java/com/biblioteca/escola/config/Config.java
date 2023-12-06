package com.biblioteca.escola.config;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.biblioteca.escola.entity.Book;
import com.biblioteca.escola.repository.BookRepository;


@Configuration
public class Config implements CommandLineRunner{
    
    @Autowired        
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {

        Book b1 = new Book(null, "To Kill a Mockingbird");
        Book b2 = new Book(null, "1984");
        Book b3 = new Book(null, "The Great Gatsby");
        Book b4 = new Book(null, "Pride and Prejudice");
        Book b5 = new Book(null, "The Catcher in the Rye");
        Book b6 = new Book(null, "One Hundred Years of Solitude");
        Book b7 = new Book(null, "The Lord of the Rings");
        Book b8 = new Book(null, "Harry Potter and the Sorcerer's Stone");
        Book b9 = new Book(null, "The Chronicles of Narnia");
        Book b10 = new Book(null, "The Hobbit");

        bookRepository.saveAll(Arrays.asList(b1,b2,b3,b4,b5,b6,b7,b8,b9,b10));
    }    
}