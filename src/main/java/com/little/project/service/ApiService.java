package com.little.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.little.project.entities.Book;
import com.little.project.entities.Course;
import com.little.project.entities.Student;
import com.little.project.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Value("${api.url}")
    private String studentApiUrl;

    @Value("${api.biblioteca}")
    private String bookApiUrl;

    public Set<Student> fetchStudents() {
        Set<Student> students = new HashSet<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(studentApiUrl, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                students = parseStudentsFromApiResponse(responseBody);
                printStudentsData(students);
            }
        } catch (HttpClientErrorException.NotFound e) {
            handleNotFoundError("alunos", e);
        } catch (Exception e) {
            handleApiError("alunos", e);
        }

        return students;
    }

    public Set<Book> fetchBooks() {
        Set<Book> books = new HashSet<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(bookApiUrl, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                books = parseBooksFromApiResponse(responseBody);
            }
        } catch (HttpClientErrorException.NotFound e) {
            handleNotFoundError("livros", e);
        } catch (Exception e) {
            handleApiError("livros", e);
        }

        return books;
    }

    private Set<Student> parseStudentsFromApiResponse(String responseBody) throws JsonProcessingException {
        Set<Student> students = new HashSet<>();
    
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
    
        for (JsonNode node : jsonNode) {
            long id = node.get("id").asLong();
            String name = node.get("nome").asText();
    
            JsonNode cursoNode = node.get("curso");
    
            if (cursoNode != null && !cursoNode.isNull()) {
                String courseName = cursoNode.asText();
    
                if ("Serviço Social".equals(courseName)) {
                    Set<Course> courses = new HashSet<>();
                    Course existingCourse = courseRepository.findByName(courseName);
    
                    if (existingCourse != null) {
                        courses.add(existingCourse);
                    } else {
                        Course newCourse = new Course(null, courseName);
                        courseRepository.save(newCourse);
                        courses.add(newCourse);
                    }
    
                    System.out.println("ID: " + id + ", Nome: " + name + ", Cursos: " + courses);
                    Student student = new Student(id, name, courses);
                    students.add(student);
                }
            }
        }
    
        return students;
    }

    private Set<Book> parseBooksFromApiResponse(String responseBody) throws JsonProcessingException {
        Set<Book> books = new HashSet<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        for (JsonNode node : jsonNode) {
            long id = node.get("id").asLong();
            String name = node.get("name").asText();

            Book book = new Book(id, name);
            books.add(book);
        }

        return books;
    }

    private void printStudentsData(Set<Student> students) {
        for (Student student : students) {
            System.out.println("Student ID: " + student.getId());
            System.out.println("Student Name: " + student.getName());
            System.out.println("Course Name: " + student.getCourses());
            System.out.println("----------------------");
        }
    }


    private void handleApiError(String resourceName, Exception e) {
        System.out.println("Ocorreu um erro na API de " + resourceName + ". Mensagem: " + e.getMessage());
    }

    private void handleNotFoundError(String resourceName, HttpClientErrorException.NotFound e) {
        System.out.println("Recurso não encontrado na API de " + resourceName + ". Mensagem: " + e.getMessage());
    }
}
