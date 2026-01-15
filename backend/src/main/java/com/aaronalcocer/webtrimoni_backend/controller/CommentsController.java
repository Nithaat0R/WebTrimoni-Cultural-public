package com.aaronalcocer.webtrimoni_backend.controller;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.aaronalcocer.webtrimoni_backend.dto.CommentRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentsController {
  @PostMapping("/submit")
  public ResponseEntity<?> submitComment(
      @RequestBody CommentRequest request,
      @RequestHeader("Authorization") String authHeader) {
    try {
      String token = authHeader.replace("Bearer ", "");
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
      String uid = decodedToken.getUid();

      Firestore db = FirestoreClient.getFirestore();

      DocumentReference userRef = db.collection("users").document(uid);
      ApiFuture<DocumentSnapshot> futureUser = userRef.get();
      DocumentSnapshot userDoc = futureUser.get();

      if (!userDoc.exists()) {
        return ResponseEntity.status(404).body(Map.of("error", "Usuari no trobat"));
      }

      String nomUsuari = userDoc.getString("nom_usuari");

      Map<String, Object> comentariPatrimoni = new HashMap<>();
      comentariPatrimoni.put("idUsuari", uid);
      comentariPatrimoni.put("nomUsuari", nomUsuari);
      comentariPatrimoni.put("id_patrimoni", request.getIdPatrimoni());
      comentariPatrimoni.put("comentari", request.getComment());
      comentariPatrimoni.put("puntuacio", request.getRating());
      comentariPatrimoni.put("data", java.time.Instant.now().toString());

      Map<String, Object> comentariUsers = new HashMap<>();
      comentariUsers.put("id_patrimoni", request.getIdPatrimoni());
      comentariUsers.put("comentari", request.getComment());
      comentariUsers.put("puntuacio", request.getRating());
      comentariUsers.put("data", java.time.Instant.now().toString());

      db.collection("users").document(uid).update("comentaris", FieldValue.arrayUnion(comentariUsers));

      int novaPuntuacio = request.getRating();

      DocumentReference docRef = db.collection("reviews").document(request.getIdPatrimoni().toString());
      ApiFuture<DocumentSnapshot> futureDoc = docRef.get(); // Nombre cambiado a futureDoc
      DocumentSnapshot doc = futureDoc.get();

      if (doc.exists()) {
        long totalReviews = doc.contains("total_reviews") ? doc.getLong("total_reviews") : 0;
        long sumaTotalPunts = doc.contains("suma_total_punts") ? doc.getLong("suma_total_punts") : 0;

        long nouTotalReviews = totalReviews + 1;
        long novaSumaTotalPunts = sumaTotalPunts + novaPuntuacio;
        double novaMitjana = (double) novaSumaTotalPunts / nouTotalReviews;

        docRef.update(
            "comentaris", FieldValue.arrayUnion(comentariPatrimoni),
            "total_reviews", nouTotalReviews,
            "suma_total_punts", novaSumaTotalPunts,
            "puntuacio_mitjana", novaMitjana);
      } else {
        Map<String, Object> camposRaiz = new HashMap<>();
        camposRaiz.put("comentaris", Arrays.asList(comentariPatrimoni));
        camposRaiz.put("total_reviews", 1);
        camposRaiz.put("suma_total_punts", (long) novaPuntuacio);
        camposRaiz.put("puntuacio_mitjana", (double) novaPuntuacio);

        docRef.set(camposRaiz);
      }

      return ResponseEntity.ok(Map.of("status", "ok", "idPatrimoni", request.getIdPatrimoni()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(401).body(Map.of("error", "No autoritzat"));
    }
  }
  @GetMapping("/load")
  public ResponseEntity<?> loadCommentsPatrimoni(@RequestParam String id) {
    try {
      Firestore db = FirestoreClient.getFirestore();

      DocumentReference docRef = db.collection("reviews").document(id);
      ApiFuture<DocumentSnapshot> future = docRef.get();
      DocumentSnapshot document = future.get();

      if (document.exists()) {
        List<Map<String, Object>> comments = (List<Map<String, Object>>) document.get("comentaris");

        if (comments == null) {
          return ResponseEntity.ok(new ArrayList<>());
        }

        // Revertim per mostrar els més recents primer
        java.util.Collections.reverse(comments);

        return ResponseEntity.ok(comments);
      } else {
        return ResponseEntity.ok(new ArrayList<>());
      }

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of("error", "Error carregant comentaris"));
    }
  }
}
