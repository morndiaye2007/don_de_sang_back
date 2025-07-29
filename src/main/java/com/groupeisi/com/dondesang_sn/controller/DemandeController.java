package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.DemandeDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.DemandeService;

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
@RequestMapping("demandes")
@RequiredArgsConstructor
@CrossOrigin("*")
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

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllDemandes(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = demandeService.getAllDemandes(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }
    @Operation(summary = "delete the demande", description = "Delete demande, it takes input id demande")
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
