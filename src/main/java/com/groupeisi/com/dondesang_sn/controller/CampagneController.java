package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.CampagneService;
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
@RequestMapping("/campagnes")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Campagnes", description = "API pour la gestion des campagnes de don")
public class CampagneController {

    private final CampagneService campagneService;

    @Operation(summary = "Create campagnes", description = "this endpoint takes input campagnes and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createCampagne(@RequestBody CampagneDTO campagneDTO) {
        try {
            System.out.println("=== CRÉATION CAMPAGNE - DONNÉES REÇUES ===");
            System.out.println("CampagneDTO: " + campagneDTO.toString());
            System.out.println("Nom campagne: " + campagneDTO.getNom_campagne());
            System.out.println("Description: " + campagneDTO.getDescription());
            System.out.println("Date début: " + campagneDTO.getDate_debut());
            System.out.println("Date fin: " + campagneDTO.getDate_fin());
            System.out.println("Nombre de poches: " + campagneDTO.getNbre_de_poche());
            System.out.println("Centre collecte ID: " + campagneDTO.getCentreCollecteId());
            System.out.println("CNTS ID: " + campagneDTO.getCntsId());
            
            var dto = campagneService.createCampagne(campagneDTO);
            
            System.out.println("=== CAMPAGNE CRÉÉE AVEC SUCCÈS ===");
            System.out.println("ID généré: " + dto.getId());
            return Response.ok().setPayload(dto).setMessage("campagnes créé");
        } catch (Exception ex) {
            System.out.println("=== ERREUR CRÉATION CAMPAGNE ===");
            System.out.println("Erreur: " + ex.getMessage());
            ex.printStackTrace();
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Update campagne", description = "Update an existing campagne")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource not found"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateCampagne(@Parameter(name = "id", description = "the campagnes id to updated") @PathVariable("id") Long id, @RequestBody CampagneDTO campagneDTO) {
        campagneDTO.setId(id);
        try {
            var dto = campagneService.updateCampagne(campagneDTO);
            return Response.ok().setPayload(dto).setMessage("campagnes modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }

    }

    @Operation(summary = "Read the campagnes", description = "This endpoint is used to read campagnes, it takes input id campagnes")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getCampagne(@Parameter(name = "id", description = "the type campagnes id to valid") @PathVariable Long id) {
        try {
            var dto = campagneService.getCampagne(id);
            return Response.ok().setPayload(dto).setMessage("campagnes trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Get all campagnes", description = "Get all campagnes with pagination and search parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllCampagnes(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = campagneService.getAllCampagnes(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }


    @Operation(summary = "Delete campagne", description = "Delete campagne, it takes input id campagnes")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampagne(@PathVariable("id") Long id) {
        try {
            campagneService.deleteCampagne(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
