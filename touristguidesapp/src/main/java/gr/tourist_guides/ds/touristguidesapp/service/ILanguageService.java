package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageUpdateDTO;
import java.util.List;

public interface ILanguageService {
    LanguageReadOnlyDTO insertLanguage(LanguageInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException;
    LanguageReadOnlyDTO updateLanguage(LanguageUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    void deleteLanguage(Long id)
            throws AppObjectNotFoundException;
    LanguageReadOnlyDTO getLanguageById(Long id)
            throws AppObjectNotFoundException;
    LanguageReadOnlyDTO getLanguageByName(String name)
            throws AppObjectNotFoundException;
    List<LanguageReadOnlyDTO> getAllLanguages();
    List<LanguageReadOnlyDTO> getAllLanguagesSortedByName();
    Boolean isCodeExists(String code);
    Boolean isNameExists(String name);
}
