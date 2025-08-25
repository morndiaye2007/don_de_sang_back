package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.configurations.JwtService;
import com.groupeisi.com.dondesang_sn.entity.UtilisateurEntity;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> login(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "");
        String password = body.getOrDefault("password", "");

        return utilisateurRepository.findByEmail(email)
                .filter(u -> passwordMatches(password, u.getMdp()))
                .map(user -> {
                    String token = jwtService.generateToken(user.getEmail(), Map.of(
                            "userId", user.getId(),
                            "role", user.getRole() != null ? user.getRole().getNom_role() : ""
                    ));
                    return Response.ok().setPayload(Map.of(
                            "token", token,
                            "user", Map.of(
                                    "id", user.getId(),
                                    "nom", user.getNom(),
                                    "prenom", user.getPrenom(),
                                    "email", user.getEmail(),
                                    "role", user.getRole() != null ? user.getRole().getNom_role() : ""
                            )
                    ));
                })
                .orElseGet(() -> Response.wrongCredentials().setMessage("Identifiants invalides"));
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> me(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.unauthorized().setMessage("Token manquant");
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractSubject(token);
        return utilisateurRepository.findByEmail(email)
                .map(user -> Response.ok().setPayload(Map.of(
                        "id", user.getId(),
                        "nom", user.getNom(),
                        "prenom", user.getPrenom(),
                        "email", user.getEmail(),
                        "role", user.getRole() != null ? user.getRole().getNom_role() : ""
                )))
                .orElseGet(() -> Response.notFound().setMessage("Utilisateur non trouvé"));
    }

    private boolean passwordMatches(String raw, String stored) {
        return stored != null && passwordEncoder.matches(raw, stored);
    }
}


