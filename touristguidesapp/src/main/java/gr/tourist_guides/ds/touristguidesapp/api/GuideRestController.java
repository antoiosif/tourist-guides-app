package gr.tourist_guides.ds.touristguidesapp.api;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.ValidationException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.GuideFilters;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.dto.ResponseMessageDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.service.GuideService;
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
@RequestMapping("/api/guides")
@RequiredArgsConstructor
public class GuideRestController {
    private final GuideService guideService;

    @Operation(
            summary = "Insert a guide",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Guide created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error or invalid region/language",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Guide already exists",
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
    public ResponseEntity<GuideReadOnlyDTO> insertGuide(@Valid @RequestBody GuideInsertDTO guideInsertDTO,
                                                        BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException, AppObjectInvalidArgumentException  {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        GuideReadOnlyDTO guideReadOnlyDTO = guideService.insertGuide(guideInsertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(guideReadOnlyDTO.uuid())
                .toUri();
        return ResponseEntity.created(location).body(guideReadOnlyDTO);
    }

    @Operation(
            summary = "Update a guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Guide updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error, or invalid region/language, or guide is not active",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Guide not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Guide already exists",
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
    public ResponseEntity<GuideReadOnlyDTO> updateGuide(@PathVariable String uuid,
                                                        @Valid @RequestBody GuideUpdateDTO guideUpdateDTO,
                                                        BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException, AppObjectInvalidArgumentException,
            AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        GuideReadOnlyDTO guideReadOnlyDTO = guideService.updateGuide(guideUpdateDTO);
        return ResponseEntity.ok(guideReadOnlyDTO);
    }

    @Operation(
            summary = "Delete a guide (soft delete)",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Guide deleted",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400", description = "Guide is not active",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Guide not found",
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
    public ResponseEntity<Void> deleteGuide(@PathVariable String uuid)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException  {
        guideService.deleteGuide(uuid);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get one guide by uuid",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Guide returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Guide not found",
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
    public ResponseEntity<GuideReadOnlyDTO> getGuideByUuid(@PathVariable String uuid)
            throws AppObjectNotFoundException {
        GuideReadOnlyDTO guideReadOnlyDTO = guideService.getGuideByUuid(uuid);
        return ResponseEntity.ok(guideReadOnlyDTO);
    }

    @Operation(
            summary = "Get guides filtered and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Guides returned",
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
    public ResponseEntity<List<GuideReadOnlyDTO>> getGuidesFilteredSorted(
            @Nullable @RequestBody GuideFilters filters) {
        if (filters == null) filters = GuideFilters.builder().build();
        List<GuideReadOnlyDTO> guidesList = guideService.getGuidesFilteredSorted(filters);
        return ResponseEntity.ok(guidesList);
    }

    @Operation(
            summary = "Get guides paginated, filtered and sorted",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Guides returned",
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
    public ResponseEntity<Paginated<GuideReadOnlyDTO>> getGuidesPaginatedFilteredSorted(
            @Nullable @RequestBody GuideFilters filters) {
        if (filters == null) filters = GuideFilters.builder().build();
        Paginated<GuideReadOnlyDTO> guidesPaginated = guideService.getGuidesPaginatedFilteredSorted(filters);
        return ResponseEntity.ok(guidesPaginated);
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
        Boolean isUsernameExists = guideService.isUsernameExists(username);
        return ResponseEntity.ok(isUsernameExists);
    }

    @Operation(
            summary = "Check if the input record number already exists in DB",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "If the input record number already exists in DB (true/false)",
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
    @GetMapping("/check-record-number-exists/{recordNumber}")
    public ResponseEntity<Boolean> isRecordNumberExists(@PathVariable String recordNumber) {
        Boolean isRecordNumberExists = guideService.isRecordNumberExists(recordNumber);
        return ResponseEntity.ok(isRecordNumberExists);
    }

    // for Guide's favorite Activities

    @Operation(
            summary = "Add an activity to a guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activity added to the guide",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404", description = "Activity or guide not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Activity is already added to the guide",
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
    @PutMapping("/{uuid}/favorites/add")
    public ResponseEntity<Void> addActivityToGuide(@PathVariable(value = "uuid") String guideUuid,
                                                   @RequestBody String activityUuid)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException {
        guideService.addActivityToGuide(guideUuid, activityUuid);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove an activity from a guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activity removed from the guide",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Activity or guide not found, or activity is not added to the guide",
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
    @PutMapping("/{uuid}/favorites/remove")
    public ResponseEntity<Void> removeActivityFromGuide(@PathVariable(value = "uuid") String guideUuid,
                                                        @RequestBody String activityUuid)
            throws AppObjectNotFoundException {
        guideService.removeActivityFromGuide(guideUuid, activityUuid);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get all the activities added to a guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Activities returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class,
                                    example = """
                                        [
                                            { "createdAt": "...", "updatedAt": "...", "id": "...", ... },
                                            { "createdAt": "...", "updatedAt": "...", "id": "...", ... },
                                        ]"""
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Guide not found",
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
    @GetMapping("/{uuid}/favorites")
    public ResponseEntity<List<ActivityReadOnlyDTO>> getGuideActivities(@PathVariable String uuid)
            throws AppObjectNotFoundException {
        List<ActivityReadOnlyDTO> guideActivitiesList = guideService.getGuideActivities(uuid);
        return ResponseEntity.ok(guideActivitiesList);
    }

    @Operation(
            summary = "Check if the activity is already added to the guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "If the activity is already added to the guide (true/false)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Guide not found",
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
    @PostMapping("/{uuid}/favorites/check-activity-exists")
    public ResponseEntity<Boolean> isActivityExistsInGuide(@PathVariable(value = "uuid") String guideUuid,
                                                           @RequestBody String activityUuid)
            throws AppObjectNotFoundException {
        Boolean isActivityExistsInGuide = guideService.isActivityExistsInGuide(guideUuid, activityUuid);
        return ResponseEntity.ok(isActivityExistsInGuide);
    }
}
