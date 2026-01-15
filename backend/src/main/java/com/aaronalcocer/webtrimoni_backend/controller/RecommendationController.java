package com.aaronalcocer.webtrimoni_backend.controller;

import com.aaronalcocer.webtrimoni_backend.service.RecommendationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<Map<String, Object>> getRecommendations(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Token no detectat, es mostren recomanacions per defecte");
                return recommendationService.getDefaultRecommendations();
            }

            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            System.out.println("Usuari identificat: " + uid);
            
            return recommendationService.getRecommendations(uid);

        } catch (Exception e) {
            System.err.println("Error validant el token: " + e.getMessage());
            return recommendationService.getDefaultRecommendations();
        }
    }
}