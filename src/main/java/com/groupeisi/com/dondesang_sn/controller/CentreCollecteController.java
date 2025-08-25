package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.CentreCollecteDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.CentreCollecteService;
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
@RequestMapping("/CentreCollecte")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CentreCollecteController {
    private final CentreCollecteService centreCollecteService;

    @Operation(summary = "Create CentreCollecte", description = "this endpoint takes input CentreCollecte and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createCentreCollecte(@RequestBody CentreCollecteDTO centreCollecteDTO) {
        try {
            var dto = centreCollecteService.createCentreCollecte(centreCollecteDTO);
            return Response.ok().setPayload(dto).setMessage("CentreCollecte créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateCentreCollecte(@Parameter(name = "id", description = "the CentreCollecte id to updated") @PathVariable("id") Long id, @RequestBody CentreCollecteDTO centreCollecteDTO) {
        centreCollecteDTO.setId(id);
        try {
            var dto = centreCollecteService.updateCentreCollecte(centreCollecteDTO);
            return Response.ok().setPayload(dto).setMessage("CentreCollecte modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read the CentreCollecte", description = "This endpoint is used to read CentreCollecte, it takes input id CentreCollecte")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getCentreCollecte(@Parameter(name = "id", description = "the type CentreCollecte id to valid") @PathVariable Long id) {
        try {
            var dto = centreCollecteService.getCentreCollecte(id);
            return Response.ok().setPayload(dto).setMessage("CentreCollecte trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllCentreCollectes(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = centreCollecteService.getAllCentreCollectes(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @Operation(summary = "delete the CentreCollecte", description = "Delete CentreCollecte, it takes input id CentreCollecte")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCentreCollecte(@PathVariable("id") Long id) {
        try {
            centreCollecteService.deleteCentreCollecte(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
