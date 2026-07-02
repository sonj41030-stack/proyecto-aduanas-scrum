package com.duanas.msauth.controller;

import com.duanas.msauth.model.Usuario;
import com.duanas.msauth.security.JwtUtil;
import com.duanas.msauth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("password");

        return usuarioService.buscarPorUsername(username)
                .filter(u -> u.getHabilitado())
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(u -> {
                    String token = jwtUtil.generarToken(u.getUsername(), u.getRol());
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    response.put("rol", u.getRol());
                    response.put("username", u.getUsername());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas o cuenta deshabilitada")));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PutMapping("/usuarios/{id}/estado")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean estado) {
        usuarioService.habilitarDeshabilitarUsuario(id, estado);
        return ResponseEntity.ok("Estado actualizado correctamente");
    }
}