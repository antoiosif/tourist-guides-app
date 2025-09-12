package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryUpdateDTO;
import java.util.List;

public interface ICategoryService {
    CategoryReadOnlyDTO insertCategory(CategoryInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException;
    CategoryReadOnlyDTO updateCategory(CategoryUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    void deleteCategory(Long id)
            throws AppObjectNotFoundException;
    CategoryReadOnlyDTO getCategoryById(Long id)
            throws AppObjectNotFoundException;
    CategoryReadOnlyDTO getCategoryByName(String name)
            throws AppObjectNotFoundException;
    List<CategoryReadOnlyDTO> getAllCategories();
    List<CategoryReadOnlyDTO> getAllCategoriesSortedByName();
    Boolean isNameExists(String name);
}
