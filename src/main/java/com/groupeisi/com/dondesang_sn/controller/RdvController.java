package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.RdvService;
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
@RequestMapping("/rdv")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RdvController {
    private final RdvService rdvService;

    @Operation(summary = "Create Rdv", description = "this endpoint takes input Rdv and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createRdv(@RequestBody RdvDTO rdvDTO) {
        try {
            var dto = rdvService.createRdv(rdvDTO);
            return Response.ok().setPayload(dto).setMessage("Rdv créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateRdv(@Parameter(name = "id", description = "the Rdv id to updated") @PathVariable("id") Long id, @RequestBody RdvDTO rdvDTO) {
        rdvDTO.setId(id);
        try {
            var dto = rdvService.updateRdv(rdvDTO);
            return Response.ok().setPayload(dto).setMessage("Rdv modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }

    }

    @Operation(summary = "Read the Rdv", description = "This endpoint is used to read Rdv, it takes input id Rdv")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getRdv(@Parameter(name = "id", description = "the type Rdv id to valid") @PathVariable Long id) {
        try {
            var dto = rdvService.getRdv(id);
            return Response.ok().setPayload(dto).setMessage("Rdv trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllRdvs(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = rdvService.getAllRdvs(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @GetMapping("/donneur/{donneurId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getRdvsByDonneur(@PathVariable Long donneurId) {
        try {
            var rdvs = rdvService.getRdvsByDonneur(donneurId);
            return Response.ok().setPayload(rdvs).setMessage("RDV du donneur trouvés");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }


    @Operation(summary = "Rdv the agent", description = "Delete Rdv, it takes input id Rdv")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRdv(@PathVariable("id") Long id) {
        try {
            rdvService.deleteRdv(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
