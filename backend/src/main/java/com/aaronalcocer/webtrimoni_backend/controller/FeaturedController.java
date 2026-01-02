package com.aaronalcocer.webtrimoni_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
//Aaquest test obtindrà els 3 primers patrimonis de tot el dataset
public class FeaturedController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Generem un endpoint
    @GetMapping("/api/featured")
    public ArrayNode getFirstThreeElements() {
        //Definim la URL de l'API 
        String url = "https://do.diba.cat/api/dataset/patrimoni_cultural";

        try {

            //Agafem el JSON en format string
            String jsonString = restTemplate.getForObject(url, String.class);
            //Generm un arbre de nodes a partir del JSON per poder manipular facilment
            JsonNode root = objectMapper.readTree(jsonString);
            //Accedim a elements ja que la resta del JSON és informació de l'API
            JsonNode elements = root.path("elements");
            //Transformem en un Array per treballar amb ell
            ArrayNode result = objectMapper.createArrayNode();
            //Mostrem només 3 per no petar el buffer
            for (int i = 0; i < Math.min(10, elements.size()); i++) {
                result.add(elements.get(i));
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createArrayNode();
        }
    }
}
