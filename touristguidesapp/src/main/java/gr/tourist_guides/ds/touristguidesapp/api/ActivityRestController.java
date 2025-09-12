package gr.tourist_guides.ds.touristguidesapp.api;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.ValidationException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.ActivityFilters;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.dto.ResponseMessageDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.service.ActivityService;
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
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityRestController {
    private final ActivityService activityService;

    @Operation(
            summary = "Insert an activity",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Activity created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ActivityReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error or invalid category",
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
    public ResponseEntity<ActivityReadOnlyDTO> insertActivity(@Valid @RequestBody ActivityInsertDTO activityInsertDTO,
                                                              BindingResult bindingResult)
            throws ValidationException, AppObjectInvalidArgumentException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        ActivityReadOnlyDTO activityReadOnlyDTO = activityService.insertActivity(activityInsertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(activityReadOnlyDTO.uuid())
                .toUri();
        return ResponseEntity.created(location).body(activityReadOnlyDTO);
    }

    @Operation(
            summary = "Update an activity",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activity updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ActivityReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error or invalid category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Activity not found",
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
    public ResponseEntity<ActivityReadOnlyDTO> updateActivity(@PathVariable String uuid,
                                                              @Valid @RequestBody ActivityUpdateDTO activityUpdateDTO,
                                                              BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException, AppObjectInvalidArgumentException  {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        ActivityReadOnlyDTO activityReadOnlyDTO = activityService.updateActivity(activityUpdateDTO);
        return ResponseEntity.ok(activityReadOnlyDTO);
    }

    @Operation(
            summary = "Delete an activity",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activity deleted",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404", description = "Activity not found",
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
    public ResponseEntity<Void> deleteActivity(@PathVariable String uuid)
            throws AppObjectNotFoundException {
        activityService.deleteActivity(uuid);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get one activity by uuid",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activity returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ActivityReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Activity not found",
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
    public ResponseEntity<ActivityReadOnlyDTO> getActivityByUuid(@PathVariable String uuid)
            throws AppObjectNotFoundException {
        ActivityReadOnlyDTO activityReadOnlyDTO = activityService.getActivityByUuid(uuid);
        return ResponseEntity.ok(activityReadOnlyDTO);
    }

    @Operation(
            summary = "Get activities filtered and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activities returned",
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
    public ResponseEntity<List<ActivityReadOnlyDTO>> getActivitiesFilteredSorted(
            @Nullable @RequestBody ActivityFilters filters) {
        if (filters == null) filters = ActivityFilters.builder().build();
        List<ActivityReadOnlyDTO> activitiesList = activityService.getActivitiesFilteredSorted(filters);
        return ResponseEntity.ok(activitiesList);
    }

    @Operation(
            summary = "Get activities paginated, filtered and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activities returned",
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
    public ResponseEntity<Paginated<ActivityReadOnlyDTO>> getActivitiesPaginatedFilteredSorted(
            @Nullable @RequestBody ActivityFilters filters) {
        if (filters == null) filters = ActivityFilters.builder().build();
        Paginated<ActivityReadOnlyDTO> activitiesPaginated = activityService.getActivitiesPaginatedFilteredSorted(filters);
        return ResponseEntity.ok(activitiesPaginated);
    }
}
