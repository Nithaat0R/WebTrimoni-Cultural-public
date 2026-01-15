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
    public ArrayNode getFilter(@RequestParam String filtre, @RequestParam String data) {

        try {
            String url = "";

            switch (filtre) {
                case ("comarca"):
                    url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-rel_comarca/" + data;
                    break;
                case ("segle"):
                    url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-centuria-like/" + data;
                    break;
                case ("estil"):
                    url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-estil-like/" + data;
                    break;
            }
            String jsonString = restTemplate.getForObject(url, String.class);
            // Generem un arbre de nodes a partir del JSON per poder manipular facilment
            JsonNode root = objectMapper.readTree(jsonString);
            // Accedim a elements ja que la resta del JSON és informació de l'API
            ArrayNode elements = (ArrayNode) root.path("elements");

            return elements;
        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createArrayNode();
        }
    }

}
