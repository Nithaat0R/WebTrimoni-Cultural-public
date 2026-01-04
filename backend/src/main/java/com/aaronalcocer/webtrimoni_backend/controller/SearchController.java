package com.aaronalcocer.webtrimoni_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
// Aquest controlador retornarà els patrimonis cercats a través del web.
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Generem un endpoint
    @GetMapping("/api/search")
    public ArrayNode getElementsBySearch(@RequestParam String search) {
        String url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-all-like/" + search;
        try {

            String jsonString = restTemplate.getForObject(url, String.class);
            //Generem un arbre de nodes a partir del JSON per poder manipular facilment
            JsonNode root = objectMapper.readTree(jsonString);
            //Accedim a elements ja que la resta del JSON és informació de l'API
            ArrayNode elements = (ArrayNode) root.path("elements");
            return elements;

        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createArrayNode();
        }
    }
}
