package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.CNTSDTO;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.services.CNTSService;
import com.groupeisi.com.dondesang_sn.services.CampagneService;
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
@RequestMapping("cnts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CNTSController {

    private final CNTSService cntsService;

    @Operation(summary = "Create campagnes", description = "this endpoint takes input campagnes and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createCNTS(@RequestBody CNTSDTO cntsDTO) {
        try {
            var dto = cntsService.createCNTS(cntsDTO);
            return Response.ok().setPayload(dto).setMessage("campagnes créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateCNTS(@Parameter(name = "id", description = "the campagnes id to updated") @PathVariable("id") Long id, @RequestBody CNTSDTO cntsdto) {
        cntsdto.setId(id);
        try {
            var dto = cntsService.updateCNTS(cntsdto);
            return Response.ok().setPayload(dto).setMessage("campagnes modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }

    }

    @Operation(summary = "Read the campagnes", description = "This endpoint is used to read campagnes, it takes input id campagnes")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getCNTS(@Parameter(name = "id", description = "the type campagnes id to valid") @PathVariable Long id) {
        try {
            var dto = cntsService.getCNTS(id);
            return Response.ok().setPayload(dto).setMessage("campagnes trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Read all Budget", description = "It takes input param of the page and returns this list related")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllCNTSs(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = cntsService.getAllCNTSs(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }


    @Operation(summary = "create the campagnes", description = "Delete campagnes, it takes input id campagnes")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCNTS(@PathVariable("id") Long id) {
        try {
            cntsService.deleteCNTS(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
