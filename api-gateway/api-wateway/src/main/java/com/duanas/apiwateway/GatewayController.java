package com.duanas.apiwateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@RestController
public class GatewayController {

    @Value("${services.auth}")
    private String authService;

    @Value("${services.tramites}")
    private String tramitesService;

    @Value("${services.reportes}")
    private String reportesService;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/auth/**")
    public ResponseEntity<String> proxyAuth(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, authService);
    }

    @RequestMapping("/tramites/**")
    public ResponseEntity<String> proxyTramites(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, tramitesService);
    }

    @RequestMapping("/reportes/**")
    public ResponseEntity<String> proxyReportes(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, reportesService);
    }

    private ResponseEntity<String> proxy(HttpServletRequest request, String body, String serviceUrl) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String url = serviceUrl + path + (query != null ? "?" + query : "");

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            List<String> values = Collections.list(request.getHeaders(name));
            headers.put(name, values);
        }

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(url, method, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"Servicio no disponible: " + e.getMessage() + "\"}");
        }
    }
}