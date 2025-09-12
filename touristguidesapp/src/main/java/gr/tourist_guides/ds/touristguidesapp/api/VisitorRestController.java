package gr.tourist_guides.ds.touristguidesapp.api;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.ValidationException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.core.filters.VisitorFilters;
import gr.tourist_guides.ds.touristguidesapp.dto.ResponseMessageDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorRestController {
    private final VisitorService visitorService;

    @Operation(
            summary = "Insert a visitor",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Visitor created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VisitorReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error or invalid region",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Visitor already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class)))
            }
    )
    @PostMapping
    public ResponseEntity<VisitorReadOnlyDTO> insertVisitor(@Valid @RequestBody VisitorInsertDTO visitorInsertDTO,
                                                            BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        VisitorReadOnlyDTO visitorReadOnlyDTO = visitorService.insertVisitor(visitorInsertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(visitorReadOnlyDTO.uuid())
                .toUri();
        return ResponseEntity.created(location).body(visitorReadOnlyDTO);
    }

    @Operation(
            summary = "Update a visitor",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Visitor updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VisitorReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error, or invalid region, or visitor is not active",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Visitor not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Visitor already exists",
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
    @PutMapping("/{uuid}")
    public ResponseEntity<VisitorReadOnlyDTO> updateVisitor(@PathVariable String uuid,
                                                            @Valid @RequestBody VisitorUpdateDTO visitorUpdateDTO,
                                                            BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException, AppObjectInvalidArgumentException,
            AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        VisitorReadOnlyDTO visitorReadOnlyDTO = visitorService.updateVisitor(visitorUpdateDTO);
        return ResponseEntity.ok(visitorReadOnlyDTO);
    }

    @Operation(
            summary = "Delete a visitor (soft delete)",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Visitor deleted",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400", description = "Visitor is not active",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Visitor not found",
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
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable String uuid)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException  {
        visitorService.deleteVisitor(uuid);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get one visitor by uuid",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Visitor returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VisitorReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Visitor not found",
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
    @GetMapping("/{uuid}")
    public ResponseEntity<VisitorReadOnlyDTO> getVisitorByUuid(@PathVariable String uuid)
            throws AppObjectNotFoundException {
        VisitorReadOnlyDTO visitorReadOnlyDTO = visitorService.getVisitorByUuid(uuid);
        return ResponseEntity.ok(visitorReadOnlyDTO);
    }

    @Operation(
            summary = "Get visitors filtered and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Visitors returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "createdAt": "...", "updatedAt": "...", "id": "...", ... },
                                                    { "createdAt": "...", "updatedAt": "...", "id": "...", ... }
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
    @PostMapping("/search")
    public ResponseEntity<List<VisitorReadOnlyDTO>> getVisitorsFilteredSorted(
            @Nullable @RequestBody VisitorFilters filters) {
        if (filters == null) filters = VisitorFilters.builder().build();
        List<VisitorReadOnlyDTO> visitorsList = visitorService.getVisitorsFilteredSorted(filters);
        return ResponseEntity.ok(visitorsList);
    }

    @Operation(
            summary = "Get visitors paginated, filtered and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Visitors returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Paginated.class,
                                            example = """
                                                {
                                                    "data" = [
                                                        { "createdAt": "...", "updatedAt": "...", "id": "...", ... },
                                                        { "createdAt": "...", "updatedAt": "...", "id": "...", ... },
                                                    ],
                                                    "totalElements" = 48,
                                                    "totalPages" = 5,
                                                    "numberOfElements" = 8,
                                                    "currentPage" = 4,
                                                    "pageSize" = 10
                                                }"""
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
    @PostMapping("/search/paginated")
    public ResponseEntity<Paginated<VisitorReadOnlyDTO>> getVisitorsPaginatedFilteredSorted(
            @Nullable @RequestBody VisitorFilters filters) {
        if (filters == null) filters = VisitorFilters.builder().build();
        Paginated<VisitorReadOnlyDTO> visitorsPaginated = visitorService.getVisitorsPaginatedFilteredSorted(filters);
        return ResponseEntity.ok(visitorsPaginated);
    }

    @Operation(
            summary = "Check if the input username already exists in DB",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "If the input username already exists in DB (true/false)",
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
    @GetMapping("/check-username-exists/{username}")
    public ResponseEntity<Boolean> isUsernameExists(@PathVariable String username) {
        Boolean isUsernameExists = visitorService.isUsernameExists(username);
        return ResponseEntity.ok(isUsernameExists);
    }
}
