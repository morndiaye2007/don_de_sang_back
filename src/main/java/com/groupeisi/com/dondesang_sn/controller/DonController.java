package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.DonDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.DonService;
import com.groupeisi.com.dondesang_sn.services.DonationEligibilityService;

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
@RequestMapping("dons")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DonController {

    private final DonService donService;
    private final DonationEligibilityService donationEligibilityService;

    @Operation(summary = "Create don", description = "this endpoint takes input don and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createDon(@RequestBody DonDTO donDTO) {
        try {
            var dto = donService.createDon(donDTO);
            return Response.ok().setPayload(dto).setMessage("don créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateDon(@Parameter(name = "id", description = "the don id to updated") @PathVariable("id") Long id, @RequestBody DonDTO donDTO) {
        donDTO.setId(id);
        try {
            var dto = donService.updateDon(donDTO);
            return Response.ok().setPayload(dto).setMessage("don modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }

    }

    @Operation(summary = "Read the don", description = "This endpoint is used to read don, it takes input id don")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getDon(@Parameter(name = "id", description = "the type don id to valid") @PathVariable Long id) {
        try {
            var dto = donService.getDon(id);
            return Response.ok().setPayload(dto).setMessage("don trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllDons(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = donService.getAllDons(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }


    @GetMapping("/donneur/{donneurId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getDonsByDonneur(@PathVariable Long donneurId) {
        try {
            var dons = donService.getDonsByDonneur(donneurId);
            return Response.ok().setPayload(dons).setMessage("Dons du donneur trouvés");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @GetMapping("/eligibility/{donneurId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> checkEligibility(@PathVariable Long donneurId) {
        try {
            boolean isEligible = donationEligibilityService.isEligibleForDonation(donneurId);
            var nextEligibleDate = donationEligibilityService.getNextEligibleDate(donneurId);
            
            var result = Map.of(
                "eligible", isEligible,
                "nextEligibleDate", nextEligibleDate,
                "message", isEligible ? "Éligible pour un don" : "Doit attendre avant le prochain don"
            );
            
            return Response.ok().setPayload(result).setMessage("Vérification d'éligibilité effectuée");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "don the agent", description = "Delete don, it takes input id don")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAgent(@PathVariable("id") Long id) {
        try {
            donService.deleteDon(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
