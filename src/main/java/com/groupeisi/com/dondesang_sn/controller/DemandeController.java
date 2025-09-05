package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.DemandeDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.DemandeService;

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
@RequestMapping("api/demandes")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Demandes", description = "API pour la gestion des demandes de sang")
public class DemandeController {
    private final DemandeService demandeService;

    @Operation(summary = "Create demande", description = "this endpoint takes input demande and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createDemande(@RequestBody DemandeDTO demandeDTO) {
        try {
            var dto = demandeService.createDemande(demandeDTO);
            return Response.ok().setPayload(dto).setMessage("demande créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Update demande", description = "Update an existing demande")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource not found"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateDemande(@Parameter(name = "id", description = "the demande id to updated") @PathVariable("id") Long id, @RequestBody DemandeDTO demandeDTO) {
        demandeDTO.setId(id);
        try {
            var dto = demandeService.updateDemande(demandeDTO);
            return Response.ok().setPayload(dto).setMessage("demande modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read the demande", description = "This endpoint is used to read demande, it takes input id demande")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getDemande(@Parameter(name = "id", description = "the type demande id to valid") @PathVariable Long id) {
        try {
            var dto = demandeService.getDemande(id);
            return Response.ok().setPayload(dto).setMessage("demande trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Get all demandes", description = "Get all demandes with pagination and search parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllDemandes(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = demandeService.getAllDemandes(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }
    @Operation(summary = "Créer demande par médecin", description = "Permet à un médecin de créer une demande de sang")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping("/medecin/{medecinId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createDemandeByMedecin(
            @PathVariable("medecinId") Long medecinId,
            @RequestBody DemandeDTO demandeDTO) {
        try {
            demandeDTO.setMedecinId(medecinId);
            var dto = demandeService.createDemandeByMedecin(demandeDTO);
            return Response.ok().setPayload(dto).setMessage("Demande créée avec succès");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Obtenir demandes par médecin", description = "Récupère toutes les demandes d'un médecin")
    @GetMapping("/medecin/{medecinId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getDemandesByMedecin(@PathVariable("medecinId") Long medecinId) {
        try {
            var demandes = demandeService.getDemandesByMedecin(medecinId);
            return Response.ok().setPayload(demandes).setMessage("Demandes récupérées");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Valider demande", description = "Permet à l'admin de valider une demande")
    @PutMapping("/{id}/valider")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> validerDemande(@PathVariable("id") Long id) {
        try {
            var dto = demandeService.validerDemande(id);
            return Response.ok().setPayload(dto).setMessage("Demande validée avec succès");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Refuser demande", description = "Permet à l'admin de refuser une demande")
    @PutMapping("/{id}/refuser")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> refuserDemande(@PathVariable("id") Long id) {
        try {
            var dto = demandeService.refuserDemande(id);
            return Response.ok().setPayload(dto).setMessage("Demande refusée");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Obtenir l'historique complet des demandes", description = "Récupère toutes les demandes avec détails médecin et hôpital")
    @GetMapping("/historique")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getHistoriqueDemandes(
            @RequestParam(required = false) String hopitalId,
            @RequestParam(required = false) String medecinId,
            @RequestParam(required = false) String statutDemande,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin,
            Pageable pageable) {
        try {
            var page = demandeService.getHistoriqueDemandes(hopitalId, medecinId, statutDemande, dateDebut, dateFin, pageable);
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

    @Operation(summary = "Obtenir les statistiques des demandes", description = "Récupère les statistiques globales des demandes")
    @GetMapping("/statistiques")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getStatistiquesDemandes() {
        try {
            var stats = demandeService.getStatistiquesDemandes();
            return Response.ok().setPayload(stats).setMessage("Statistiques récupérées");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Delete demande", description = "Delete demande, it takes input id demande")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDemande(@PathVariable("id") Long id) {
        try {
            demandeService.deleteDemande(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
