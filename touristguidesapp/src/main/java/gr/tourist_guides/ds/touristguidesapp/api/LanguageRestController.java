package gr.tourist_guides.ds.touristguidesapp.api;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.ValidationException;
import gr.tourist_guides.ds.touristguidesapp.dto.ResponseMessageDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.service.LanguageService;
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
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageRestController {
    private final LanguageService languageService;

    @Operation(
            summary = "Insert a language",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Language created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LanguageReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Language already exists",
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
    public ResponseEntity<LanguageReadOnlyDTO> insertLanguage(@Valid @RequestBody LanguageInsertDTO languageInsertDTO,
                                                              BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        LanguageReadOnlyDTO languageReadOnlyDTO = languageService.insertLanguage(languageInsertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(languageReadOnlyDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(languageReadOnlyDTO);
    }

    @Operation(
            summary = "Update a language",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Language updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LanguageReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Language not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Language already exists",
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
    public ResponseEntity<LanguageReadOnlyDTO> updateLanguage(@PathVariable Long id,
                                                              @Valid @RequestBody LanguageUpdateDTO languageUpdateDTO,
                                                              BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException, AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        LanguageReadOnlyDTO languageReadOnlyDTO = languageService.updateLanguage(languageUpdateDTO);
        return ResponseEntity.ok(languageReadOnlyDTO);
    }

    @Operation(
            summary = "Delete a language",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Language deleted",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404", description = "Language not found",
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
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id)
            throws AppObjectNotFoundException {
        languageService.deleteLanguage(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get one language by id",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Language returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LanguageReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Language not found",
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
    public ResponseEntity<LanguageReadOnlyDTO> getLanguageById(@PathVariable Long id)
            throws AppObjectNotFoundException {
        LanguageReadOnlyDTO languageReadOnlyDTO = languageService.getLanguageById(id);
        return ResponseEntity.ok(languageReadOnlyDTO);
    }

    @Operation(
            summary = "Get one language by name",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Language returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LanguageReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Language not found",
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
    public ResponseEntity<LanguageReadOnlyDTO> getLanguageByName(@PathVariable String name)
            throws AppObjectNotFoundException {
        LanguageReadOnlyDTO languageReadOnlyDTO = languageService.getLanguageByName(name);
        return ResponseEntity.ok(languageReadOnlyDTO);
    }

    @Operation(
            summary = "Get all languages",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Languages returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "id": "1", "code": "GER" "name": "Γερμανικά" },
                                                    { "id": "2", "code": "ENG" "name": "Αγγλικά" }
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
    public ResponseEntity<List<LanguageReadOnlyDTO>> getAllLanguages() {
        List<LanguageReadOnlyDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @Operation(
            summary = "Get all languages sorted by name",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Languages returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "id": "2", "code": "ENG" "name": "Αγγλικά" },
                                                    { "id": "1", "code": "GER" "name": "Γερμανικά" }
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
    public ResponseEntity<List<LanguageReadOnlyDTO>> getAllLanguagesSortedByName() {
        List<LanguageReadOnlyDTO> sortedLanguages = languageService.getAllLanguagesSortedByName();
        return ResponseEntity.ok(sortedLanguages);
    }

    @Operation(
            summary = "Check if the input code already exists in DB",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "If the input code already exists in DB (true/false)",
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
    @GetMapping("/check-code-exists/{code}")
    public ResponseEntity<Boolean> isCodeExists(@PathVariable String code) {
        Boolean isCodeExists = languageService.isCodeExists(code);
        return ResponseEntity.ok(isCodeExists);
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
        Boolean isNameExists = languageService.isNameExists(name);
        return ResponseEntity.ok(isNameExists);
    }
}
