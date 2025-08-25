package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.DonneurService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/donneurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class DonneurController {

    private final DonneurService donneurService;

    @Operation(summary = "Create donneur", description = "this endpoint takes input donneur and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createDonneur(@RequestBody DonneurDTO donneurDTO) {
        try {
            System.out.println("=== DONNÉES REÇUES PAR LE BACKEND ===");
            System.out.println("DonneurDTO: " + donneurDTO.toString());
            System.out.println("Nom: " + donneurDTO.getNom());
            System.out.println("Prenom: " + donneurDTO.getPrenom());
            System.out.println("Sexe: " + donneurDTO.getSexe());
            System.out.println("GroupeSanguin: " + donneurDTO.getGroupeSanguin());
            System.out.println("DNI: " + donneurDTO.getDni());
            System.out.println("Telephone: " + donneurDTO.getTelephone());
            System.out.println("Addresse: " + donneurDTO.getAddresse());
            System.out.println("Mdp: " + donneurDTO.getMdp());
            System.out.println("CampagneId: " + donneurDTO.getCampagneId());
            System.out.println("CentreCollecteId: " + donneurDTO.getCentreCollecteId());
            
            var dto = donneurService.createDonneur(donneurDTO);
            System.out.println("=== DONNEUR CRÉÉ AVEC SUCCÈS ===");
            System.out.println("ID généré: " + dto.getId());
            return Response.ok().setPayload(dto).setMessage("donneur créé");
        } catch (Exception ex) {
            System.out.println("=== ERREUR LORS DE LA CRÉATION ===");
            System.out.println("Erreur: " + ex.getMessage());
            ex.printStackTrace();
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateDonneur(@Parameter(name = "id", description = "the donneur id to updated") @PathVariable("id") Long id, @RequestBody DonneurDTO donneurDTO) {
        donneurDTO.setId(id);
        try {
            var dto = donneurService.updateDonneur(donneurDTO);
            return Response.ok().setPayload(dto).setMessage("donneur modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }

    }

    @Operation(summary = "Read the donneur", description = "This endpoint is used to read donneur, it takes input id donneur")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getDonneur(@Parameter(name = "id", description = "the type donneur id to valid") @PathVariable Long id) {
        try {
            var dto = donneurService.getDonneur(id);
            return Response.ok().setPayload(dto).setMessage("donneur trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllDonneurs(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = donneurService.getAllDonneurs(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }


    @Operation(summary = "delete the donneur", description = "Delete donneur, it takes input id donneur")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDonneur(@PathVariable("id") Long id) {
        try {
            donneurService.deleteDonneur(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Login donneur", description = "Authenticate donneur with phone and password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "401", description = "Invalid credentials"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> loginDonneur(@RequestBody Map<String, String> body) {
        try {
            String telephone = body.getOrDefault("telephone", "");
            String motDePasse = body.getOrDefault("motDePasse", "");
            
            System.out.println("=== TENTATIVE DE CONNEXION DONNEUR ===");
            System.out.println("Téléphone: " + telephone);
            System.out.println("Mot de passe fourni: " + (motDePasse.isEmpty() ? "NON" : "OUI"));
            
            var result = donneurService.authenticateDonneur(telephone, motDePasse);
            return result;
        } catch (Exception ex) {
            System.out.println("=== ERREUR CONNEXION DONNEUR ===");
            System.out.println("Erreur: " + ex.getMessage());
            ex.printStackTrace();
            return Response.badRequest().setMessage("Erreur lors de la connexion: " + ex.getMessage());
        }
    }
}
