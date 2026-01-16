package com.aaronalcocer.webtrimoni_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@CrossOrigin
public class FeaturedController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/api/featured")
    public List<JsonNode> getTopRated() {
        Firestore db = FirestoreClient.getFirestore();
        List<JsonNode> resultList = new ArrayList<>();
        
        try {
            Query topQuery = db.collection("reviews")
                               .orderBy("puntuacio_mitjana", Query.Direction.DESCENDING)
                               .limit(10);
            
            List<QueryDocumentSnapshot> reviewDocs = topQuery.get().get().getDocuments();

            for (QueryDocumentSnapshot doc : reviewDocs) {
                String id = doc.getId().trim();
                Double notaMitjana = doc.getDouble("puntuacio_mitjana");

                String url = "https://do.diba.cat/api/dataset/patrimoni_cultural/camp-id/" + id;
                
                try {
                    String jsonResponse = restTemplate.getForObject(url, String.class);
                    JsonNode root = objectMapper.readTree(jsonResponse);
                    
                    JsonNode elements = root.path("elements");
                    
                    if (elements.isArray() && !elements.isEmpty()) {
                        ObjectNode elementNode = (ObjectNode) elements.get(0);
                        
                        elementNode.put("id", id);
                        elementNode.put("puntuacio_mitjana", notaMitjana);
                        
                        resultList.add(elementNode);
                    }
                } catch (Exception e) {
                    System.err.println("No se pudo encontrar el patrimonio " + id + ".");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}