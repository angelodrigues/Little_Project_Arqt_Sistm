package com.little.project.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.little.project.entities.Course;
import com.little.project.entities.Student;
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

    @Value("${api.url}")
    private String apiUrl;

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Set<Student> students = new HashSet<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

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

                studentRepository.saveAll(students);

                students.forEach(s -> System.out.println("ID: " + s.getId() + ", Nome: " + s.getName() + ", Cursos: " + s.getCourses()));
            } else {
                System.out.println("Falha ao obter dados da URL. Código de resposta: " + responseEntity.getStatusCode());
            }
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Recurso não encontrado. Mensagem: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro. Mensagem: " + e.getMessage());
        }
    }
}
