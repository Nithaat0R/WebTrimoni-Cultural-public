package com.aaronalcocer.webtrimoni_backend.controller;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.aaronalcocer.webtrimoni_backend.dto.CommentRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
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
      // Extreiem token del header
      String token = authHeader.replace("Bearer ", "");
      // Verifiquem el token
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

      String uid = decodedToken.getUid();

      // Accés a la base de dades
      Firestore db = FirestoreClient.getFirestore();

      // Obtenim nom d'usuari
      DocumentReference userRef = db.collection("users").document(uid);
      ApiFuture<DocumentSnapshot> future = userRef.get();
      DocumentSnapshot userDoc = future.get();

      if (!userDoc.exists()) {
        return ResponseEntity
            .status(404)
            .body(Map.of("error", "Usuari no trobat"));
      }

      String nomUsuari = userDoc.getString("nom_usuari");

      // Creem el objecte de comentari per afegir-lo a les reviews
      Map<String, Object> comentariPatrimoni = new HashMap<>();
      comentariPatrimoni.put("idUsuari", uid);
      comentariPatrimoni.put("nomUsuari", nomUsuari);
      comentariPatrimoni.put("id_patrimoni", request.getIdPatrimoni());
      comentariPatrimoni.put("comentari", request.getComment());
      comentariPatrimoni.put("puntuacio", request.getRating());
      comentariPatrimoni.put("data", java.time.Instant.now().toString());

      // Creem el objecte de comentari per afegir-lo als comentaris de users
      Map<String, Object> comentariUsers = new HashMap<>();
      comentariUsers.put("id_patrimoni", request.getIdPatrimoni());
      comentariUsers.put("comentari", request.getComment());
      comentariUsers.put("puntuacio", request.getRating());
      comentariUsers.put("data", java.time.Instant.now().toString());

      //FALTA AFEGIR A COMENTARIS

      DocumentReference docRef = db.collection("reviews").document(request.getIdPatrimoni().toString());
      DocumentSnapshot doc = docRef.get().get();

      if (doc.exists()) {
        docRef.update("comentaris", FieldValue.arrayUnion(comentariPatrimoni));
      } else {
        docRef.set(Map.of("comentaris", Arrays.asList(comentariPatrimoni)));
      }

      return ResponseEntity.ok(Map.of("status", "ok", "idPatrioni", request.getIdPatrimoni()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
          .status(401)
          .body(Map.of("error", "No autoritzat"));
    }
  }
  @GetMapping("/load")
  public ResponseEntity<?> loadCommentsPatrimoni(@RequestParam String id) {
    try {
      // Accés a la base de dades
      Firestore db = FirestoreClient.getFirestore();

      // Obtenim nom d'usuari
      DocumentReference userRef = db.collection("reviews").document(id);
      ApiFuture<DocumentSnapshot> future = userRef.get();
      DocumentSnapshot userDoc = future.get();

      if (!userDoc.exists()) {
        return ResponseEntity
            .status(404)
            .body(Map.of("error", "Patrimoni no trobat"));
      }

      return ResponseEntity.ok(Map.of("status", "ok", "idPatrimoni", request.getIdPatrimoni()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
          .status(401)
          .body(Map.of("error", "No autoritzat"));
    }
  }

}
