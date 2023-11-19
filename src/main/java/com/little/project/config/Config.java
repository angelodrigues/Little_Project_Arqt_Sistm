package com.little.project.config;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

// import com.little.project.repositories.StudentRepository;

@Configuration
@Profile("test")
public class Config implements CommandLineRunner {

    // @Autowired
    // private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
            // URL da qual você quer recuperar os dados
        String apiUrl = "https://rmi6vdpsq8.execute-api.us-east-2.amazonaws.com/msAluno";

        // Cria uma instância do RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Faz a chamada à URL e obtém a resposta como String
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

        // Imprime o conteúdo da resposta no console
        System.out.println("Resposta da URL:\n" + responseEntity.getBody());
    }
}
