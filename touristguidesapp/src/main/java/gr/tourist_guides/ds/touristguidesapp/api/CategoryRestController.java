package gr.tourist_guides.ds.touristguidesapp.api;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.ValidationException;
import gr.tourist_guides.ds.touristguidesapp.dto.ResponseMessageDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Insert a category",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Category created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Category already exists",
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
    public ResponseEntity<CategoryReadOnlyDTO> insertCategory(@Valid @RequestBody CategoryInsertDTO categoryInsertDTO,
                                                              BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CategoryReadOnlyDTO categoryReadOnlyDTO = categoryService.insertCategory(categoryInsertDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryReadOnlyDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(categoryReadOnlyDTO);
    }

    @Operation(
            summary = "Update a category",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Category updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Category not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDTO.class))),
                    @ApiResponse(
                            responseCode = "409", description = "Category already exists",
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
    public ResponseEntity<CategoryReadOnlyDTO> updateCategory(@PathVariable Long id,
                                                              @Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO,
                                                              BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException, AppObjectAlreadyExistsException {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CategoryReadOnlyDTO categoryReadOnlyDTO = categoryService.updateCategory(categoryUpdateDTO);
        return ResponseEntity.ok(categoryReadOnlyDTO);
    }

    @Operation(
            summary = "Delete a category",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Category deleted",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404", description = "Category not found",
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
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id)
            throws AppObjectNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get one category by id",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Category returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Category not found",
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
    public ResponseEntity<CategoryReadOnlyDTO> getCategoryById(@PathVariable Long id)
            throws AppObjectNotFoundException {
        CategoryReadOnlyDTO categoryReadOnlyDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryReadOnlyDTO);
    }

    @Operation(
            summary = "Get one category by name",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Category returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDTO.class))),
                    @ApiResponse(
                            responseCode = "404", description = "Category not found",
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
    public ResponseEntity<CategoryReadOnlyDTO> getCategoryByName(@PathVariable String name)
            throws AppObjectNotFoundException {
        CategoryReadOnlyDTO categoryReadOnlyDTO = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(categoryReadOnlyDTO);
    }

    @Operation(
            summary = "Get all categories",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Categories returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "id": "1", "name": "Σεμινάριο" },
                                                    { "id": "2", "name": "Εκδρομή" }
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
    public ResponseEntity<List<CategoryReadOnlyDTO>> getAllCategories() {
        List<CategoryReadOnlyDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Get all categories sorted by name",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Categories returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class,
                                            example = """
                                                [
                                                    { "id": "2", "name": "Εκδρομή" },
                                                    { "id": "1", "name": "Σεμινάριο" }
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
    public ResponseEntity<List<CategoryReadOnlyDTO>> getAllCategoriesSortedByName() {
        List<CategoryReadOnlyDTO> sortedCategories = categoryService.getAllCategoriesSortedByName();
        return ResponseEntity.ok(sortedCategories);
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
        Boolean isNameExists = categoryService.isNameExists(name);
        return ResponseEntity.ok(isNameExists);
    }
}
