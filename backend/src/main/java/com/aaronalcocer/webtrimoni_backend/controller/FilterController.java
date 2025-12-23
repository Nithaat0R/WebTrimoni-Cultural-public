package com.aaronalcocer.webtrimoni_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
// Aquest controlador filtrarà per diferents variables, filtre portara el tipus
// de filtre mentre que el parametre data portara amb que haurem de filtrar.
public class FilterController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Generem un endpoint
    @GetMapping("/api/filtre")
    public ObjectNode getFilter(@RequestParam String filtre, @RequestParam String data) {

        String url = "";

        switch (filtre) {
            case ("comarca"):
                url = "https://do.diba.cat/api/dataset/patrimoni_cultural/camp-rel_comarca/" + data;
            case("estil"):
        }

        
        try {
            String jsonString = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonString);

            ArrayNode elements = (ArrayNode) root.path("elements");

            if (elements.isEmpty()) {
                return objectMapper.createObjectNode();
            }

            return (ObjectNode) elements.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createObjectNode();
        }

    }

}
