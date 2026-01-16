package com.aaronalcocer.webtrimoni_backend.controller;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.aaronalcocer.webtrimoni_backend.dto.FavoriteRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/favorite")
@CrossOrigin
public class FavoriteController {

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/add")
  public ResponseEntity<?> addFavorit(
      @RequestBody FavoriteRequest request,
      @RequestHeader("Authorization") String authHeader) {

    try {
      // Extreiem token del header
      String token = authHeader.replace("Bearer ", "");
      // Verifiquem el token
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

      String uid = decodedToken.getUid();

      // Accés a la base de dades
      Firestore db = FirestoreClient.getFirestore();

      // Afegim el ID del patrimoni a l'array de preferits si no hi és
      db.collection("users")
          .document(uid)
          .update("preferits",
              FieldValue.arrayUnion(request.getIdPatrimoni()));

      return ResponseEntity.ok(
          Map.of("status", "ok", "idPatrimoni", request.getIdPatrimoni()));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
          .status(401)
          .body(Map.of("error", "No autoritzat"));
    }
  }

  @PostMapping("/remove")
  public ResponseEntity<?> removeFavorite(
      @RequestHeader("Authorization") String authorization,
      @RequestBody FavoriteRequest request) {
    try {
      String token = authorization.replace("Bearer ", "");

      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
      String uid = decodedToken.getUid();

      Long idPatrimoni = request.getIdPatrimoni();

      if (idPatrimoni == null) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "idPatrimoni obligatori"));
      }

      Firestore db = FirestoreClient.getFirestore();

      db.collection("users")
          .document(uid)
          .update("preferits", FieldValue.arrayRemove(idPatrimoni));

      return ResponseEntity.ok(
          Map.of("message", "Eliminat de favorits", "id", idPatrimoni));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(401)
          .body(Map.of("error", "Error al eliminar: " + e.getMessage()));
    }
  }

  @GetMapping("/state")
  public ResponseEntity<?> checkFavoriteState(
      @RequestParam Long idPatrimoni,
      @RequestHeader("Authorization") String authHeader) {
    try {
      String token = authHeader.replace("Bearer ", "");
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
      String uid = decodedToken.getUid();

      Firestore db = FirestoreClient.getFirestore();
      DocumentSnapshot userDoc = db.collection("users").document(uid).get().get();

      List<Long> preferits = (List<Long>) userDoc.get("preferits");
      boolean exists = preferits.contains(idPatrimoni);

      return ResponseEntity.ok(Map.of("isFavorite", exists));
    } catch (Exception e) {
      return ResponseEntity.status(401).body(Map.of("isFavorite", false));
    }
  }

  @GetMapping("/getfavorites")
  public List<JsonNode> getUserFavorites(@RequestHeader("Authorization") String authHeader) {
    List<JsonNode> favoriteDetails = new ArrayList<>();
    try {
      String token = authHeader.replace("Bearer ", "");
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
      String uid = decodedToken.getUid();

      Firestore db = FirestoreClient.getFirestore();
      DocumentSnapshot userDoc = db.collection("users").document(uid).get().get();

      if (userDoc.exists() && userDoc.contains("preferits")) {
        List<?> rawList = (List<?>) userDoc.get("preferits");

        for (Object item : rawList) {
          String id = String.valueOf(item);
          try {
            String url = "https://do.diba.cat/api/dataset/patrimoni_cultural/camp-id/" + id;
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode resp = objectMapper.readTree(jsonResponse);
            JsonNode elements = resp.path("elements");

            if (elements.isArray() && !elements.isEmpty()) {
              ObjectNode node = (ObjectNode) elements.get(0);
              node.put("id", id); // Aseguramos que el ID se llame 'id' para el front
              favoriteDetails.add(node);
            }
          } catch (Exception e) {
            System.err.println("No se pudo obtener información del favorito: " + id);
          }
        }
      }
    } catch (Exception e) {
      System.err.println("Error en getFavorites: " + e.getMessage());
    }
    return favoriteDetails;
  }
}
