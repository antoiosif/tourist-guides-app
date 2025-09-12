package gr.tourist_guides.ds.touristguidesapp.api;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.ValidationException;
import gr.tourist_guides.ds.touristguidesapp.dto.ResponseMessageDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionRestController {
    private final RegionService regionService;

    @Operation(
            summary = "Insert a region",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Region created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegionReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Region already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<RegionReadOnlyDTO> insertRegion(@Valid @RequestBody RegionInsertDTO regionInsertDTO,
                                                          BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        RegionReadOnlyDTO regionReadOnlyDTO = regionService.insertRegion(regionInsertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(regionReadOnlyDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(regionReadOnlyDTO);
    }

    @Operation(
            summary = "Update a region",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Region updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegionReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Region not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Region already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RegionReadOnlyDTO> updateRegion(@PathVariable Long id,
                                                          @Valid @RequestBody RegionUpdateDTO regionUpdateDTO,
                                                          BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException, AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        RegionReadOnlyDTO regionReadOnlyDTO = regionService.updateRegion(regionUpdateDTO);
        return ResponseEntity.ok(regionReadOnlyDTO);
    }

    @Operation(
            summary = "Delete a region",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Region deleted",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404", description = "Region not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id)
            throws AppObjectNotFoundException {
        regionService.deleteRegion(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get one region by id",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Region returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegionReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Region not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RegionReadOnlyDTO> getRegionById(@PathVariable Long id)
            throws AppObjectNotFoundException {
        RegionReadOnlyDTO regionReadOnlyDTO = regionService.getRegionById(id);
        return ResponseEntity.ok(regionReadOnlyDTO);
    }

    @Operation(
            summary = "Get one region by name",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Region returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegionReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Region not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<RegionReadOnlyDTO> getRegionByName(@PathVariable String name)
            throws AppObjectNotFoundException {
        RegionReadOnlyDTO regionReadOnlyDTO = regionService.getRegionByName(name);
        return ResponseEntity.ok(regionReadOnlyDTO);
    }

    @Operation(
            summary = "Get all regions",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Regions returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "id": "1", "name": "Κρήτη" },
                                                    { "id": "2", "name": "Αττική" }
                                                ]"""
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<RegionReadOnlyDTO>> getAllRegions() {
        List<RegionReadOnlyDTO> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @Operation(
            summary = "Get all regions sorted by name",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Regions returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "id": "2", "name": "Αττική" },
                                                    { "id": "1", "name": "Κρήτη" }
                                                ]"""
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<List<RegionReadOnlyDTO>> getAllRegionsSortedByName() {
        List<RegionReadOnlyDTO> sortedRegions = regionService.getAllRegionsSortedByName();
        return ResponseEntity.ok(sortedRegions);
    }

    @Operation(
            summary = "Check if the input name already exists in DB",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "If the input name already exists in DB (true/false)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(
                            responseCode = "401", description = "Not Authenticated",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied",
                            content = @Content)
            }
    )
    @GetMapping("/check-name-exists/{name}")
    public ResponseEntity<Boolean> isNameExists(@PathVariable String name) {
        Boolean isNameExists = regionService.isNameExists(name);
        return ResponseEntity.ok(isNameExists);
    }
}
