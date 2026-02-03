package com.aaronalcocer.webtrimoni_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.regex.Pattern;

@RestController
public class FilterController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/api/filtre")
    public ArrayNode getFilter(@RequestParam String filtre, @RequestParam String data) {
        try {
            String url = "";
            switch (filtre) {
                case "comarca":
                    url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-rel_comarca/" + data;
                    break;
                case "ambit":
                    url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-ambit/" + data;
                    break;
                case "estil":
                    url = "https://do.diba.cat/api/dataset/patrimoni_cultural/ord-titol/asc/camp-estil-like/" + data;
                    break;
            }

            String jsonString = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode elements = (ArrayNode) root.path("elements");
            
            if ("estil".equals(filtre) || "segle".equals(filtre)) {
                ArrayNode filteredElements = objectMapper.createArrayNode();

                for (JsonNode node : elements) {
                    String valorCamp = node.path(filtre).asText();
                    
                    if (isExactWordMatch(valorCamp, data)) {
                        filteredElements.add(node);
                    }
                }
                return filteredElements;
            }

            return elements;

        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createArrayNode();
        }
    }

    // Evitar confusions amb estils que tenen nom arrels d'altres (Modern/Modernisme)
    private boolean isExactWordMatch(String text, String target) {
        if (text == null || target == null || target.isEmpty()) {
            return false;
        }

        String escapedTarget = Pattern.quote(target.toLowerCase());
        
        String regex = ".*\\b" + escapedTarget + "\\b.*";

        return text.toLowerCase().matches(regex);
    }
}