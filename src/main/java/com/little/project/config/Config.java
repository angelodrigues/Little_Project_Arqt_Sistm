package com.little.project.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.little.project.entities.Student;
import com.little.project.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("test")
public class Config implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Value("${api.url}")
    private String apiUrl;
    
    @Override
    public void run(String... args) throws Exception {        

        RestTemplate restTemplate = new RestTemplate();

        List<Student> students = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
        
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                
                for (JsonNode node : jsonNode) {                    
                    JsonNode idNode = node.get("id");
                    long id = (idNode != null && !idNode.isNull()) ? idNode.asLong() : 0;
                    
                    JsonNode nameNode = node.get("nome");
                    String name = (nameNode != null && !nameNode.isNull()) ? nameNode.asText() : "";
                    
                    JsonNode cursoNode = node.get("curso");
                    String curso = (cursoNode != null && !cursoNode.isNull()) ? cursoNode.asText() : "";
                    
                    Student student = new Student(id, name, curso);
                    students.add(student);
                }
                studentRepository.saveAll(students);
                students.forEach(s -> System.out.println("ID: " + s.getId() + ", Nome: " + s.getName() + ", Curso: " + s.getCourse()));
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