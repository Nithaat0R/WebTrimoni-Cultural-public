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
// Aquest controlador retornarà els patrimonis cercats a través del web.
public class InfoPatrimoniController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Generem un endpoint
    @GetMapping("/api/infopatrimoni")
    public ObjectNode getInfoPatrimoni(@RequestParam String id) {

    String url = "https://do.diba.cat/api/dataset/patrimoni_cultural/camp-id/" + id;

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
