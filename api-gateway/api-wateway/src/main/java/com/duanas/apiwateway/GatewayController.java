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

    @Value("${services.sag}")
    private String sagService;

    @Value("${services.pdi}")
    private String pdiService;

    private final RestTemplate restTemplate = new RestTemplate();

    // ms-auth expone sus endpoints bajo /auth, igual que el gateway -> no hay que reescribir nada.
    @RequestMapping("/auth/**")
    public ResponseEntity<String> proxyAuth(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, authService, "/auth", "/auth");
    }

    // ms-tramites expone sus endpoints bajo /api/v1 (p.ej. /api/v1/vehiculos).
    // El gateway recibe /tramites/vehiculos y debe reenviar a /api/v1/vehiculos.
    @RequestMapping("/tramites/**")
    public ResponseEntity<String> proxyTramites(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, tramitesService, "/tramites", "/api/v1");
    }

    // ms-reportes expone sus endpoints bajo /api/v1/reportes (context-path /api/v1 + @RequestMapping /reportes).
    // El gateway recibe /reportes/... y debe reenviar a /api/v1/reportes/...
    @RequestMapping("/reportes/**")
    public ResponseEntity<String> proxyReportes(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, reportesService, "/reportes", "/api/v1/reportes");
    }

    // ms-sag expone sus endpoints bajo /api/v1/sag (context-path /api/v1 + @RequestMapping /sag).
    // El gateway recibe /sag/... y debe reenviar a /api/v1/sag/...
    @RequestMapping("/sag/**")
    public ResponseEntity<String> proxySag(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, sagService, "/sag", "/api/v1/sag");
    }

    // ms-pdi expone sus endpoints bajo /api/v1/pdi (context-path /api/v1 + @RequestMapping /pdi).
    // El gateway recibe /pdi/... y debe reenviar a /api/v1/pdi/...
    @RequestMapping("/pdi/**")
    public ResponseEntity<String> proxyPdi(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(request, body, pdiService, "/pdi", "/api/v1/pdi");
    }

    /**
     * @param stripPrefix   prefijo tal como llega al gateway (ej "/tramites")
     * @param targetPrefix  prefijo real que espera el microservicio (ej "/api/v1")
     */
    private ResponseEntity<String> proxy(HttpServletRequest request, String body, String serviceUrl,
                                          String stripPrefix, String targetPrefix) {
        String path = request.getRequestURI();
        String rest = path.startsWith(stripPrefix) ? path.substring(stripPrefix.length()) : path;
        String query = request.getQueryString();
        String url = serviceUrl + targetPrefix + rest + (query != null ? "?" + query : "");

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            // El header Host original no debe reenviarse, RestTemplate arma el suyo.
            if (name.equalsIgnoreCase("host") || name.equalsIgnoreCase("content-length")) continue;
            List<String> values = Collections.list(request.getHeaders(name));
            headers.put(name, values);
        }

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(url, method, entity, String.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            // Reenvía el código y body de error real del microservicio (400, 404, etc.)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"Servicio no disponible: " + e.getMessage() + "\"}");
        }
    }
}
