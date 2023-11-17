package com.little.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class studentService {

    private final RestTemplate restTemplate;

    @Autowired
    public studentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void consumirMicroservico() {
        String url = "https://rmi6vdpsq8.execute-api.us-east-2.amazonaws.com/msAluno";

        ResponseEntity<String> resposta = restTemplate.getForEntity(url, String.class);

        HttpStatusCode statusCode = resposta.getStatusCode();

        if (statusCode.value() == 200) {
            String corpoResposta = resposta.getBody();
            System.out.println("Resposta do microserviço: " + corpoResposta);
        } else {
            System.out.println("Falha ao chamar o microserviço. Código de status: " + statusCode);
        }
    }
}
