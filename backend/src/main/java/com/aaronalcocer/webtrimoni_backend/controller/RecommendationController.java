package com.aaronalcocer.webtrimoni_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import com.aaronalcocer.webtrimoni_backend.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public List<JsonNode> getRecommendations(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<JsonNode> finalDetailedList = new ArrayList<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) return finalDetailedList;

            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            
            List<Map<String, Object>> recs = recommendationService.getRecommendations(decodedToken.getUid());

            for (Map<String, Object> r : recs) {
                String id = r.get("id").toString();
                String url = "https://do.diba.cat/api/dataset/patrimoni_cultural/camp-id/" + id;
                
                try {
                    JsonNode resp = objectMapper.readTree(restTemplate.getForObject(url, String.class));
                    JsonNode elements = resp.path("elements");
                    if (elements.isArray() && !elements.isEmpty()) {
                        ObjectNode node = (ObjectNode) elements.get(0);
                        node.put("id", id); // Forzamos ID consistente
                        finalDetailedList.add(node);
                    }
                } catch (Exception e) {
                    System.err.println("Error obteniendo detalle para recomendación ID: " + id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDetailedList;
    }
}