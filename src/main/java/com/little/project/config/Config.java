package com.little.project.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.little.project.entities.Book;
import com.little.project.entities.Course;
import com.little.project.entities.Student;
import com.little.project.repositories.BookRepository;
import com.little.project.repositories.CourseRepository;
import com.little.project.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Profile("test")
public class Config implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BookRepository bookRepository;

    @Value("${api.url}")
    private String studentApiUrl;

    @Value("${api.biblioteca}")
    private String bookApiUrl;

    @Override
    public void run(String... args) throws Exception {
        Set<Student> studentsFromApi = fetchStudentsFromApi(studentApiUrl);
        studentRepository.saveAll(studentsFromApi);

        studentsFromApi.forEach(s -> System.out.println("API Alunos - ID: " + s.getId() + ", Nome: " + s.getName() + ", Cursos: " + s.getCourses()));
        
        Set<Book> booksFromApi = fetchBooksFromApi(bookApiUrl);
        bookRepository.saveAll(booksFromApi);

        booksFromApi.forEach(b -> System.out.println("API Livros - ID: " + b.getId() + ", Nome: " + b.getName()));
    }

    private Set<Student> fetchStudentsFromApi(String url) {
        RestTemplate restTemplate = new RestTemplate();
        Set<Student> students = new HashSet<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                for (JsonNode node : jsonNode) {
                    long id = node.get("id").asLong();
                    String name = node.get("nome").asText();

                    JsonNode cursoNode = node.get("curso");

                    Set<Course> courses = new HashSet<>();

                    if (cursoNode != null && !cursoNode.isNull()) {
                        String courseName = cursoNode.asText();

                        Course existingCourse = courseRepository.findByName(courseName);
                        if (existingCourse != null) {
                            courses.add(existingCourse);
                        } else {
                            Course newCourse = new Course(null, courseName);
                            courseRepository.save(newCourse);
                            courses.add(newCourse);
                        }
                    }

                    System.out.println("ID: " + id + ", Nome: " + name + ", Cursos: " + courses);
                    Student student = new Student(id, name, courses);
                    students.add(student);
                }
            } else {
                System.out.println("Falha ao obter dados da API de alunos. Código de resposta: " + responseEntity.getStatusCode());
            }
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Recurso não encontrado na API de alunos. Mensagem: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro na API de alunos. Mensagem: " + e.getMessage());
        }

        return students;
    }

    private Set<Book> fetchBooksFromApi(String url) {
        RestTemplate restTemplate = new RestTemplate();
        Set<Book> books = new HashSet<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                for (JsonNode node : jsonNode) {
                    long id = node.get("id").asLong();
                    String name = node.get("name").asText();

                    Book book = new Book(id, name);
                    books.add(book);
                }
            } else {
                System.out.println("Falha ao obter dados da API de livros. Código de resposta: " + responseEntity.getStatusCode());
            }
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Recurso não encontrado na API de livros. Mensagem: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro na API de livros. Mensagem: " + e.getMessage());
        }

        return books;
    }
}
