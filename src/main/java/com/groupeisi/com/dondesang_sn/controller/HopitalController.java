package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.HopitalDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.HopitalService;

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
@RequestMapping("hopitals")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HopitalController {
    private final HopitalService hopitalService;

    @Operation(summary = "Create salaire", description = "this endpoint takes input salaire and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createHopital(@RequestBody HopitalDTO hopitalDTO) {
        try {
            var dto = hopitalService.createHopital(hopitalDTO);
            return Response.ok().setPayload(dto).setMessage("Salaire créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateHopital(@Parameter(name = "id", description = "the salaire id to updated") @PathVariable("id") Long id, @RequestBody HopitalDTO hopitalDTO) {
        hopitalDTO.setId(id);
        try {
            var dto = hopitalService.updateHopital(hopitalDTO);
            return Response.ok().setPayload(dto).setMessage("Salaire modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read the salaire", description = "This endpoint is used to read salaire, it takes input id salaire")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getSalaire(@Parameter(name = "id", description = "the type salaire id to valid") @PathVariable Long id) {
        try {
            var dto = hopitalService.getHopital(id);
            return Response.ok().setPayload(dto).setMessage("Salaire trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllHopitals(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = hopitalService.getAllHopitals(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all-salaire-agent")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllSalairesAgent(@RequestParam Map<String, String> searchParams, Pageable pageable, @RequestParam("idAgent") Long idAgent) {
        var page = hopitalService.getSalairesByAgent(searchParams, pageable, idAgent);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }

    @Operation(summary = "delete the salaire", description = "Delete salaire, it takes input id salaire")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSalaire(@PathVariable("id") Long id) {
        try {
            hopitalService.deleteHopital(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
