package com.aaronalcocer.webtrimoni_backend.service;

// Anotaciones de Spring
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Collection;


// SERVEI PER GUARDAR UN CATALEG, VULL CANVIAR-LA PER NO HAVER DE DESCARREGAR-LO
@Service
public class PatrimoniCatalogService {

    private Map<String, Map<String, Object>> cataleg = new HashMap<>();
    private final String BASE_URL = "https://do.diba.cat/api/dataset/patrimoni_cultural/";

    public Collection<Map<String, Object>> getCatalegCompleto() {
        // Si ya lo descargamos antes, lo devolvemos al instante
        if (!cataleg.isEmpty()) {
            return cataleg.values();
        }

        // Si esta buit, el descarreguem per primera vegada
        synchronized (this) { // Evita que si dos usuaris entran a la vegada, es descarregui dos cops
            if (cataleg.isEmpty()) {
                descargarDatos(70000);
            }
        }
        return cataleg.values();
    }

    private void descargarDatos(int max) {
        RestTemplate restTemplate = new RestTemplate();
        int step = 1000;
        int inicio = 1;

        System.out.println("Descarregant catàleg...");

        while (inicio <= max) {
            try {
                String url = BASE_URL + "pag-ini/" + inicio + "/pag-fi/" + (inicio + step - 1);
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                List<Map<String, Object>> elements = (List<Map<String, Object>>) response.get("elements");

                if (elements == null || elements.isEmpty())
                    break;

                for (Map<String, Object> p : elements) {
                    cataleg.put(p.get("id").toString(), p);
                }
                inicio += step;
            } catch (Exception e) {
                break;
            }
        }
        System.out.println("Cataleg carregat.");
    }

    public Map<String, Object> getById(String id) {
        if (cataleg.isEmpty())
            getCatalegCompleto();
        return cataleg.get(id);
    }

    public Collection<Map<String, Object>> getAll() {
        return cataleg.values();
    }
}