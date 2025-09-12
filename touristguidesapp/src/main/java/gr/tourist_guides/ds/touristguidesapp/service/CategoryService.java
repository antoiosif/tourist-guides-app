package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.mapper.Mapper;
import gr.tourist_guides.ds.touristguidesapp.model.Category;
import gr.tourist_guides.ds.touristguidesapp.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CategoryReadOnlyDTO insertCategory(CategoryInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException {
        Category category;
        Category insertedCategory;

        // Check if the unique fields already exist in the DB
        if (categoryRepository.findByName(insertDTO.name()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "Category",
                    "Category with name=" + insertDTO.name() + " already exists.");
        }

        category = mapper.mapToCategory(insertDTO);
        insertedCategory = categoryRepository.save(category);
        log.info("Category with id={}, name={} inserted.", insertedCategory.getId(), insertedCategory.getName());
        return mapper.mapToCategoryReadOnlyDTO(insertedCategory);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CategoryReadOnlyDTO updateCategory(CategoryUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException {
        Category categoryToUpdate;
        Category category;
        Category updatedCategory;

        // Check if the entity exists in the DB
        categoryToUpdate = categoryRepository.findById(updateDTO.id()).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Category",
                        "Category with id=" + updateDTO.id() + " not found.")
        );

        /*
         * Check if the unique fields already exist in the DB and that the input value
         * is not the same with the value of the entity to update
         */
        if (categoryRepository.findByName(updateDTO.name()).isPresent() &&
                !categoryToUpdate.getName().equals(updateDTO.name())) {
            throw new AppObjectAlreadyExistsException(
                    "Category",
                    "Category with name=" + updateDTO.name() + " already exists.");
        }

        category = mapper.mapToCategory(updateDTO);
        updatedCategory = categoryRepository.save(category);
        log.info("Category with id={} updated.", updatedCategory.getId());
        return mapper.mapToCategoryReadOnlyDTO(updatedCategory);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteCategory(Long id)
            throws AppObjectNotFoundException {

        // Check if the entity exists in the DB
        if (!categoryRepository.existsById(id)) {
            throw new AppObjectNotFoundException(
                    "Category",
                    "Category with id=" + id + " not found.");
        }

        categoryRepository.deleteById(id);
        log.info("Category with id={} deleted.", id);
    }

    @Override
    public CategoryReadOnlyDTO getCategoryById(Long id)
            throws AppObjectNotFoundException {
        CategoryReadOnlyDTO categoryReadOnlyDTO = categoryRepository.findById(id)
                .map(mapper::mapToCategoryReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Category",
                        "Category with id=" + id + " not found."));
        log.debug("Get category by id={} was returned successfully.", id);
        return categoryReadOnlyDTO;
    }

    @Override
    public CategoryReadOnlyDTO getCategoryByName(String name)
            throws AppObjectNotFoundException {
        CategoryReadOnlyDTO categoryReadOnlyDTO = categoryRepository.findByName(name.trim())
                .map(mapper::mapToCategoryReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Category",
                        "Category with name=" + name + " not found."));
        log.debug("Get category by name={} was returned successfully.", name);
        return categoryReadOnlyDTO;
    }

    @Override
    public List<CategoryReadOnlyDTO> getAllCategories() {
        List<CategoryReadOnlyDTO> categories = categoryRepository.findAll()
                .stream()
                .map(mapper::mapToCategoryReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Get all categories were returned successfully.");
        return categories;
    }

    @Override
    public List<CategoryReadOnlyDTO> getAllCategoriesSortedByName() {
        List<CategoryReadOnlyDTO> sortedCategories = categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Category::getName))
                .map(mapper::mapToCategoryReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Get all categories sorted by Name were returned successfully.");
        return sortedCategories;
    }

    @Override
    public Boolean isNameExists(String name) {
        return categoryRepository.findByName(name.trim()).isPresent();
    }
}
