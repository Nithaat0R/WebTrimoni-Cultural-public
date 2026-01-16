package com.aaronalcocer.webtrimoni_backend.service;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    public List<Map<String, Object>> getRecommendations(String uid) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            Map<String, Integer> myRatings = getLatestRatings(uid, db);
            
            if (myRatings.isEmpty()) {
                return new ArrayList<>(); 
            }

            List<QueryDocumentSnapshot> allUsers = db.collection("users").get().get().getDocuments();
            Map<String, Double> weightedScores = new HashMap<>();
            Map<String, Double> similaritySums = new HashMap<>();

            for (QueryDocumentSnapshot otherUser : allUsers) {
                if (otherUser.getId().equals(uid)) continue;

                Map<String, Integer> otherRatings = getLatestRatings(otherUser.getId(), db);
                double sim = calculateCosineSimilarity(myRatings, otherRatings);

                if (sim > 0.4) { 
                    for (Map.Entry<String, Integer> entry : otherRatings.entrySet()) {
                        String idPat = entry.getKey();
                        int rating = entry.getValue();

                        if (!myRatings.containsKey(idPat)) {
                            weightedScores.put(idPat, weightedScores.getOrDefault(idPat, 0.0) + (rating * sim));
                            similaritySums.put(idPat, similaritySums.getOrDefault(idPat, 0.0) + sim);
                        }
                    }
                }
            }

            if (weightedScores.isEmpty()) return new ArrayList<>();

            return weightedScores.entrySet().stream()
                    .map(e -> {
                        String id = e.getKey();
                        double finalScore = e.getValue() / similaritySums.get(id);
                        Map<String, Object> res = new HashMap<>();
                        res.put("id", id);
                        res.put("score", finalScore);
                        return res;
                    })
                    .sorted((a, b) -> Double.compare((double) b.get("score"), (double) a.get("score")))
                    .limit(10)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error en motor de recomendación: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Map<String, Integer> getLatestRatings(String userId, Firestore db) throws Exception {
        DocumentSnapshot doc = db.collection("users").document(userId).get().get();
        if (!doc.exists()) return new HashMap<>();
        
        List<Map<String, Object>> comments = (List<Map<String, Object>>) doc.get("comentaris");
        Map<String, Integer> latest = new HashMap<>();

        if (comments != null) {
            for (Map<String, Object> c : comments) {
                latest.put(String.valueOf(c.get("id_patrimoni")), ((Number) c.get("puntuacio")).intValue());
            }
        }
        return latest;
    }

    private double calculateCosineSimilarity(Map<String, Integer> user1, Map<String, Integer> user2) {
        double dotProduct = 0.0, norm1 = 0.0, norm2 = 0.0;
        boolean shared = false;
        for (String id : user1.keySet()) {
            double r1 = user1.get(id);
            if (user2.containsKey(id)) {
                dotProduct += r1 * user2.get(id);
                shared = true;
            }
            norm1 += Math.pow(r1, 2);
        }
        for (int r2 : user2.values()) norm2 += Math.pow(r2, 2);
        if (!shared || norm1 == 0 || norm2 == 0) return 0.0;
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}