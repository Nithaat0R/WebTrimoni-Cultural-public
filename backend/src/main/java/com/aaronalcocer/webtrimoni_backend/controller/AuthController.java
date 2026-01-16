package com.aaronalcocer.webtrimoni_backend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.aaronalcocer.webtrimoni_backend.dto.RegisterRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(request.getEmail())
                    .setPassword(request.getPassword());

            UserRecord user = FirebaseAuth.getInstance().createUser(createRequest);

            Firestore db = FirestoreClient.getFirestore();

            List<Map<String, Object>> comentaris = new ArrayList<>();

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getUid());
            userData.put("nom_usuari", request.getUsername());
            userData.put("preferits", new ArrayList<>());
            userData.put("comentaris", comentaris);
            userData.put("email", user.getEmail());
            userData.put("createdAt", System.currentTimeMillis());

            db.collection("users")
                    .document(user.getUid())
                    .set(userData);

            return ResponseEntity.ok(Map.of(
                    "uid", user.getUid(),
                    "email", user.getEmail()));

        } catch (FirebaseAuthException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(HttpServletRequest request) {

        Object uid = request.getAttribute("uid");

        if (uid == null) {
            return ResponseEntity.status(401).body("Token no procesado");
        }

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {

            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot document = db.collection("users").document(uid).get().get();

            if (!document.exists()) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuari no trobat"));
            }

            List<?> comentaris = (List<?>) document.get("comentaris");

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("nom_usuari", document.getString("nom_usuari"));
            respuesta.put("email", document.getString("email"));

            respuesta.put("numComentaris", (comentaris != null) ? comentaris.size() : 0);

            return ResponseEntity.ok(respuesta);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Sessió caducada"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error en recuperar el perfil"));
        }
    }

}
