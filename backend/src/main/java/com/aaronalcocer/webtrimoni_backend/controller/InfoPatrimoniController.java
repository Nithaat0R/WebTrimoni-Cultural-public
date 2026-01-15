package com.aaronalcocer.webtrimoni_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class InfoPatrimoniController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/api/infopatrimoni")
    public ObjectNode getInfoPatrimoni(@RequestParam String id) {

        String url = "https://do.diba.cat/api/dataset/patrimoni_cultural/camp-id/" + id;

        try {
            String jsonString = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode elements = (ArrayNode) root.path("elements");

            if (elements.isEmpty()) {
                return objectMapper.createObjectNode();
            }

            JsonNode el = elements.get(0);
            ObjectNode clean = objectMapper.createObjectNode();

            // Netejem els camps posant desconegut si no posa res
            clean.put("titol", getOrUnknown(el, "titol"));
            clean.put("any", getOrUnknown(el, "any"));
            clean.put("centuria", getOrUnknown(el, "centuria"));
            clean.put("ambit", getOrUnknown(el, "ambit"));
            clean.put("tipologia", getOrUnknown(el, "tipologia"));
            clean.put("estil", getOrUnknown(el, "estil"));
            clean.put("estat_conservacio", getOrUnknown(el, "estat_conservacio"));
            clean.put("acces", getOrUnknown(el, "acces"));
            clean.put("titularitat", getOrUnknown(el, "titularitat"));
            clean.put("ubicacio", getOrUnknown(el, "ubicacio"));
            clean.put("municipi_nom", getOrUnknown(el, "municipi_nom"));
            clean.put("descripcio", getOrUnknown(el, "descripcio"));
            clean.put("codi_element", getOrUnknown(el, "codi_element"));
            clean.put("coordenades", getOrUnknown(el, "coordenades"));
            clean.set("images", el.has("images")
                    ? el.get("images")
                    : objectMapper.createArrayNode());

            return clean;

        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createObjectNode();
        }
    }

    // Helper: campo vacío → "Desconegut"
    private String getOrUnknown(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull() || value.asText().trim().isEmpty()) {
            return "Desconegut";
        }
        return value.asText();
    }
}
