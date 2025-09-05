package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.LivraisonDTO;
import com.groupeisi.com.dondesang_sn.services.LivraisonService;
import com.groupeisi.com.dondesang_sn.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
public class LivraisonController {
    
    private final LivraisonService livraisonService;

    @Operation(summary = "Créer une nouvelle livraison")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createLivraison(@RequestBody LivraisonDTO livraisonDTO) {
        try {
            var dto = livraisonService.createLivraison(livraisonDTO);
            return Response.ok().setPayload(dto).setMessage("Livraison créée");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Modifier une livraison")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateLivraison(@PathVariable("id") Long id, @RequestBody LivraisonDTO livraisonDTO) {
        try {
            livraisonDTO.setId(id);
            var dto = livraisonService.updateLivraison(livraisonDTO);
            return Response.ok().setPayload(dto).setMessage("Livraison modifiée");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Obtenir une livraison par ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getLivraison(@PathVariable("id") Long id) {
        try {
            var dto = livraisonService.getLivraison(id);
            return Response.ok().setPayload(dto).setMessage("Livraison trouvée");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Obtenir toutes les livraisons avec pagination et filtres")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllLivraisons(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        try {
            var page = livraisonService.getAllLivraisons(searchParams, pageable);
            Response.PageMetadata metadata = Response.PageMetadata.builder()
                    .number(page.getNumber())
                    .totalElements(page.getTotalElements())
                    .size(page.getSize())
                    .totalPages(page.getTotalPages())
                    .build();
            return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Obtenir les livraisons d'une demande")
    @GetMapping("/demande/{demandeId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getLivraisonsByDemande(@PathVariable("demandeId") Long demandeId) {
        try {
            var livraisons = livraisonService.getLivraisonsByDemande(demandeId);
            return Response.ok().setPayload(livraisons).setMessage("Livraisons trouvées");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Obtenir les livraisons d'un hôpital")
    @GetMapping("/hopital/{hopitalId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getLivraisonsByHopital(@PathVariable("hopitalId") Long hopitalId) {
        try {
            var livraisons = livraisonService.getLivraisonsByHopital(hopitalId);
            return Response.ok().setPayload(livraisons).setMessage("Livraisons trouvées");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Supprimer une livraison")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLivraison(@PathVariable("id") Long id) {
        try {
            livraisonService.deleteLivraison(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
