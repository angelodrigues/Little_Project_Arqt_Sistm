package com.little.project.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.little.project.entities.Book;
import com.little.project.entities.Course;
import com.little.project.entities.Discipline;
import com.little.project.entities.Student;
import com.little.project.repositories.BookRepository;
import com.little.project.repositories.CourseRepository;
import com.little.project.repositories.DisciplineRepository;
import com.little.project.repositories.StudentRepository;
import com.little.project.service.exceptions.ResourceNotFoundException;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired CourseRepository courseRepository;

    @Autowired BookRepository bookRepository;

    @Autowired DisciplineRepository disciplineRepository;

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(Long id) {
        Optional<Student> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Set<Course> findCoursesById(Long id) {
        Optional<Student> studentOptional = repository.findById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return student.getCourses();
        } else {
            return Collections.emptySet();
        }
    }

    public Student addCourseToStudent(Long studentId, Long courseId) {
        Optional<Student> studentOptional = repository.findById(studentId);
    
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Course> existingCourses = student.getCourses();
                
            if (existingCourses.stream().anyMatch(course -> course.getId().equals(courseId))) {
                throw new IllegalArgumentException("O aluno já está matriculado neste curso.");
            }
    
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + courseId));
    
            existingCourses.add(course);
            student.setCourses(existingCourses);
    
            return repository.save(student);
        } else {
            throw new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId);
        }
    }
    

    public Student removeCourseFromStudent(Long studentId, Long courseId) {
        Optional<Student> studentOptional = repository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Course> courses = student.getCourses();

            courses.removeIf(course -> course.getId().equals(courseId));

            student.setCourses(courses);

            return repository.save(student);
        } else {
            throw new ResourceNotFoundException(studentId);
        }
    }

    public Student rentBook(Long studentId, Long bookId) {
        Optional<Student> studentOptional = repository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Book> rentedBooks = student.getBooks();

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com o ID: " + bookId));

            if (rentedBooks.contains(book)) {
                throw new IllegalArgumentException("O aluno já alugou este livro.");
            }
            
            rentedBooks.add(book);
            student.setBooks(rentedBooks);

            return repository.save(student);
        } else {
            throw new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId);
        }
    }

    public Student returnBook(Long studentId, Long bookId) {
        Optional<Student> studentOptional = repository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Book> rentedBooks = student.getBooks();

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com o ID: " + bookId));

            if (!rentedBooks.contains(book)) {
                throw new IllegalArgumentException("O aluno não alugou este livro.");
            }

            rentedBooks.remove(book);
            student.setBooks(rentedBooks);

            return repository.save(student);
        } else {
            throw new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId);
        }
    }

    public Set<Book> getRentedBooks(Long studentId) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId));

        return student.getBooks();
    }

    public Set<Discipline> getEnrolledDisciplines(Long studentId) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId));

        return student.getDisciplines();
    }

    public Student enrollInDiscipline(Long studentId, Long disciplineId) {
        Optional<Student> studentOptional = repository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Discipline> enrolledDisciplines = student.getDisciplines();

            Discipline discipline = disciplineRepository.findById(disciplineId)
                    .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID: " + disciplineId));

            if (enrolledDisciplines.contains(discipline)) {
                throw new IllegalArgumentException("O aluno já está matriculado nesta disciplina.");
            }

            enrolledDisciplines.add(discipline);
            student.setDisciplines(enrolledDisciplines);

            return repository.save(student);
        } else {
            throw new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId);
        }
    }

    public Student cancelEnrollmentInDiscipline(Long studentId, Long disciplineId) {
        Optional<Student> studentOptional = repository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Discipline> enrolledDisciplines = student.getDisciplines();

            enrolledDisciplines.removeIf(discipline -> discipline.getId().equals(disciplineId));

            student.setDisciplines(enrolledDisciplines);

            return repository.save(student);
        } else {
            throw new ResourceNotFoundException("Aluno não encontrado com o ID: " + studentId);
        }
    }
}
