package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.models.UtilisateurDTO;
import com.groupeisi.com.dondesang_sn.services.UtilisateurService;

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
@RequestMapping("utilisateurs")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @Operation(summary = "Create utilisateur", description = "this endpoint takes input utilisateur and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        try {
            var dto = utilisateurService.createUtilisateur(utilisateurDTO);
            return Response.ok().setPayload(dto).setMessage("utilisateur créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateutilisateur(@Parameter(name = "id", description = "the utilisateur id to updated") @PathVariable("id") Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        utilisateurDTO.setId(id);
        try {
            var dto = utilisateurService.updateUtilisateur(utilisateurDTO);
            return Response.ok().setPayload(dto).setMessage("utilisateur modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read the utilisateur", description = "This endpoint is used to read utilisateur, it takes input id utilisateur")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getutilisateur(@Parameter(name = "id", description = "the type utilisateur id to valid") @PathVariable Long id) {
        try {
            var dto = utilisateurService.getUtilisateur(id);
            return Response.ok().setPayload(dto).setMessage("utilisateur trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllutilisateurs(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = utilisateurService.getAllUtilisateurs(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @Operation(summary = "delete the utilisateur", description = "Delete utilisateur, it takes input id utilisateur")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteutilisateur(@PathVariable("id") Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
