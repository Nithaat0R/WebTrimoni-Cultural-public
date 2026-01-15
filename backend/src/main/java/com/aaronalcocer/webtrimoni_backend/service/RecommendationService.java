package com.aaronalcocer.webtrimoni_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private PatrimoniCatalogService catalogService;

    public List<Map<String, Object>> getRecommendations(String uid) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            // Obtenim les dades de l'usuari
            Map<String, Integer> myRatings = getLatestRatings(uid, db);
            System.out.println("L'usuari (" + uid + ") té les següents reviews: " + myRatings);

            if (myRatings.isEmpty()) {
                System.out.println("L'usuari no té puntuacions, es mostrarà els default");
                return getPopularityModel(db);
            }

            // Obtenim la  de la resta
            List<QueryDocumentSnapshot> allUsers = db.collection("users").get().get().getDocuments();
            Map<String, Double> weightedScores = new HashMap<>();
            Map<String, Double> similaritySums = new HashMap<>();

            for (QueryDocumentSnapshot otherUser : allUsers) {
                if (otherUser.getId().equals(uid))
                    continue;

                Map<String, Integer> otherRatings = getLatestRatings(otherUser.getId(), db);
                double sim = calculateCosineSimilarity(myRatings, otherRatings);

                // !!! LOG DE SIMILITUD
                System.out.println("Comparant amb " + otherUser.getId() + " | Similitud: " + sim + " | Vots: "
                        + otherRatings.keySet());

                if (sim > 0.1) // CANVIAR EL BAREMO MÉS ENDAVANT {
                    for (Map.Entry<String, Integer> entry : otherRatings.entrySet()) {
                        String idPat = entry.getKey();
                        int rating = entry.getValue();

                        if (!myRatings.containsKey(idPat)) {
                            weightedScores.put(idPat, weightedScores.getOrDefault(idPat, 0.0) + (rating * sim));
                            similaritySums.put(idPat, similaritySums.getOrDefault(idPat, 0.0) + sim);
                        }
                    }
                }

            // CREUAR AMB EL CATALEG --> CANVIAR PER NO HAVER-HO D'UTILITZAR
            List<Map<String, Object>> finalResults = weightedScores.entrySet().stream()
                    .map(e -> {
                        String id = e.getKey();
                        double finalScore = e.getValue() / similaritySums.get(id);

                        Map<String, Object> info = catalogService.getById(id);

                        if (info != null) {
                            Map<String, Object> res = new HashMap<>(info);
                            res.put("score_recomendacion", finalScore);
                            return res;
                        } else {
                            // !!! LOG DE ERROR DE CATÁLOGO
                            System.err.println("⚠️ ALERTA: L'ID " + id
                                    + " ha estat recomanat però NO EXISTEIX al catàleg de 5000!");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .sorted((a, b) -> Double.compare((double) b.get("score_recomendacion"),
                            (double) a.get("score_recomendacion")))
                    .limit(10)
                    .collect(Collectors.toList());

            System.out.println("✅ Resultats finals enviats al Front: " + finalResults.size());
            return finalResults;

        } catch (Exception e) {
            System.err.println("❌ Error en el motor de recomanació:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Funció per a obtenir només la ultima puntuació de cada patrimoni, així no tindrem duplicats en els patrimonis.
    private Map<String, Integer> getLatestRatings(String userId, Firestore db) throws Exception {
        DocumentSnapshot doc = db.collection("users").document(userId).get().get();
        List<Map<String, Object>> comments = (List<Map<String, Object>>) doc.get("comentaris");
        Map<String, Integer> latest = new HashMap<>();

        if (comments != null) {
            // Ordenamos por fecha (String ISO) para que el último put sobreescriba a los
            // anteriores
            comments.sort(Comparator.comparing(c -> (String) c.get("data")));
            for (Map<String, Object> c : comments) {
                latest.put(String.valueOf(c.get("id_patrimoni")), ((Number) c.get("puntuacio")).intValue());
            }
        }
        return latest;
    }

    // Sistema de calcul de similaritat basada en el collaborative-filtering
    private double calculateCosineSimilarity(Map<String, Integer> user1, Map<String, Integer> user2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        boolean shared = false;

        for (String id : user1.keySet()) {
            double r1 = user1.get(id);
            if (user2.containsKey(id)) {
                dotProduct += r1 * user2.get(id);
                shared = true;
            }
            norm1 += Math.pow(r1, 2);
        }
        for (int r2 : user2.values()) {
            norm2 += Math.pow(r2, 2);
        }

        if (!shared || norm1 == 0 || norm2 == 0)
            return 0.0;
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // Model dels més popular per mostrar-lo quan no tenim compte o reviews al compte.
    private List<Map<String, Object>> getPopularityModel(Firestore db) throws Exception {
        // Encara s'ha de desenvolupar
        return catalogService.getAll().stream().limit(10).collect(Collectors.toList());
    }

    // Recomendacions default per posar-les abans, FUNCIÓ TEMPORAL, CAL ELIMINAR-LA.
    public List<Map<String, Object>> getDefaultRecommendations() {
        List<Map<String, Object>> mockList = new ArrayList<>();

        // Patrimonio 1
        mockList.add(createMockPatrimony("1", "Sagrada Família", "Barcelona",
                "https://images.unsplash.com/photo-1583779791512-eeccdee5c5dd?w=500"));

        // Patrimonio 2
        mockList.add(createMockPatrimony("2", "Monestir de Montserrat", "Monistrol de Montserrat",
                "https://images.unsplash.com/photo-1514332512351-460309995162?w=500"));

        // Patrimonio 3
        mockList.add(createMockPatrimony("3", "Teatre-Museu Dalí", "Figueres",
                "https://images.unsplash.com/photo-1627918331181-450f385f06ca?w=500"));

        // Patrimonio 4
        mockList.add(createMockPatrimony("4", "Anfiteatre Romà", "Tarragona",
                "https://images.unsplash.com/photo-1618386371720-7243918a3832?w=500"));

        // Patrimonio 5
        mockList.add(createMockPatrimony("5", "Pont de Besalú", "Besalú",
                "https://images.unsplash.com/photo-1502444330042-d1a1ddf9bb5b?w=500"));

        return mockList;
    }

    // Funció temporal per a la d'adalt
    private Map<String, Object> createMockPatrimony(String id, String titol, String municipi, String imgUrl) {
        Map<String, Object> p = new HashMap<>();
        p.put("codi_element", id);
        p.put("titol", titol);
        p.put("municipi_nom", municipi);
        // Tu frontend hace split("|")[0], así que le pasamos el formato esperado
        p.put("images", List.of(imgUrl + "|"));
        return p;
    }
}