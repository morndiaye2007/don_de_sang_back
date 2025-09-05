package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.MedecinDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.MedecinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/medecins")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Médecins", description = "API pour la gestion des médecins")
public class MedecinController {
    
    private final MedecinService medecinService;

    @Operation(summary = "Connexion médecin", description = "Authentification d'un médecin avec email et mot de passe")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String motDePasse = credentials.get("motDePasse");
            return medecinService.authenticateMedecin(email, motDePasse);
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Créer médecin", description = "Créer un nouveau médecin")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createMedecin(@RequestBody MedecinDTO medecinDTO) {
        try {
            var dto = medecinService.createMedecin(medecinDTO);
            return Response.ok().setPayload(dto).setMessage("Médecin créé avec succès");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateMedecin(@Parameter(name = "id", description = "ID du médecin à modifier") @PathVariable("id") Long id, @RequestBody MedecinDTO medecinDTO) {
        medecinDTO.setId(id);
        try {
            var dto = medecinService.updateMedecin(medecinDTO);
            return Response.ok().setPayload(dto).setMessage("Médecin modifié avec succès");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getMedecin(@Parameter(name = "id", description = "ID du médecin") @PathVariable Long id) {
        try {
            var dto = medecinService.getMedecin(id);
            return Response.ok().setPayload(dto).setMessage("Médecin trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllMedecins(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = medecinService.getAllMedecins(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder()
                .number(page.getNumber())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedecin(@PathVariable("id") Long id) {
        try {
            medecinService.deleteMedecin(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
