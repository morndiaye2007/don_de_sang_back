package com.groupeisi.com.dondesang_sn.controller;

import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.models.StockSangDTO;
import com.groupeisi.com.dondesang_sn.services.StockSangService;
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
@RequestMapping("stockSang")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Stock Sang", description = "API pour la gestion du stock de sang")
public class StockSangController {

    private final StockSangService stockSangService;

    @Operation(summary = "Create Stock sang", description = "this endpoint takes input Stock sang and saves it")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Object> createStockSang(@RequestBody StockSangDTO stockSangDTO) {
        try {
            var dto = stockSangService.createStockSang(stockSangDTO);
            return Response.ok().setPayload(dto).setMessage("Stock sang créé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Update Stock sang", description = "Update an existing Stock sang")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource not found"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> updateStockSang(@Parameter(name = "id", description = "the Stock sang id to updated") @PathVariable("id") Long id, @RequestBody StockSangDTO stockSangDTO) {
        stockSangDTO.setId(id);
        try {
            var dto = stockSangService.updateStockSang(stockSangDTO);
            return Response.ok().setPayload(dto).setMessage("Stock sang modifié");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }

    }

    @Operation(summary = "Read the Stock sang", description = "This endpoint is used to read Stock sang, it takes input id Stock sang")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource access does not exist"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getStockSang(@Parameter(name = "id", description = "the type Stock sang id to valid") @PathVariable Long id) {
        try {
            var dto = stockSangService.getStockSang(id);
            return Response.ok().setPayload(dto).setMessage("Stock sang trouvé");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Get all StockSang", description = "Get all StockSang with pagination and search parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getAllStockSangs(@RequestParam Map<String, String> searchParams, Pageable pageable) {
        var page = stockSangService.getAllStockSangs(searchParams, pageable);
        Response.PageMetadata metadata = Response.PageMetadata.builder().number(page.getNumber()).totalElements(page.getTotalElements()).size(page.getSize()).totalPages(page.getTotalPages()).build();
        return Response.ok().setPayload(page.getContent()).setMetadata(metadata);
    }


    @Operation(summary = "Get stock statistics by blood group", description = "Get aggregated stock statistics grouped by blood type")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("/statistiques/groupes-sanguins")
    @ResponseStatus(HttpStatus.OK)
    public Response<Object> getStockStatistiquesByGroupeSanguin() {
        try {
            var statistiques = stockSangService.getStockStatistiquesByGroupeSanguin();
            return Response.ok().setPayload(statistiques).setMessage("Statistiques de stock par groupe sanguin récupérées");
        } catch (Exception ex) {
            return Response.badRequest().setMessage(ex.getMessage());
        }
    }

    @Operation(summary = "Delete Stock sang", description = "Delete Stock sang, it takes input id Stock sang")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content"), @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"), @ApiResponse(responseCode = "404", description = "Resource not found"), @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockSang(@PathVariable("id") Long id) {
        try {
            stockSangService.deleteStockSang(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
