package com.little.project.config;

import com.little.project.entities.Book;
import com.little.project.entities.Student;
import com.little.project.repositories.BookRepository;
import com.little.project.repositories.DisciplineRepository;
import com.little.project.repositories.StudentRepository;
import com.little.project.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Set;

@Configuration
@Profile("test")
public class Config implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Override
    public void run(String... args) throws Exception {
        Set<Student> studentsFromApi = apiService.fetchStudents();
        updateStudentsInDatabase(studentsFromApi);

        Set<Book> booksFromApi = apiService.fetchBooks();
        updateBooksInDatabase(booksFromApi);
    }

    private void updateStudentsInDatabase(Set<Student> students) {        
        List<Student> existingStudents = studentRepository.findAll();
        existingStudents.removeAll(students);
        studentRepository.deleteAll(existingStudents);
    
        studentRepository.saveAll(students);    
        students.forEach(s -> System.out.println("API Alunos - ID: " + s.getId() + ", Nome: " + s.getName() + ", Cursos: " + s.getCourses()));
    }

    private void updateBooksInDatabase(Set<Book> books) {        
        List<Book> existingBooks = bookRepository.findAll();
        existingBooks.removeAll(books);
        bookRepository.deleteAll(existingBooks);
        
        bookRepository.saveAll(books);
        books.forEach(b -> System.out.println("API Livros - ID: " + b.getId() + ", Nome: " + b.getName()));
    }

    private void updateDisciplineCourses(){
        
    }
}
