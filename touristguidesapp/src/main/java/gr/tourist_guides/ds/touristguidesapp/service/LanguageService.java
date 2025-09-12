package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.mapper.Mapper;
import gr.tourist_guides.ds.touristguidesapp.model.Language;
import gr.tourist_guides.ds.touristguidesapp.repository.LanguageRepository;
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
public class LanguageService implements ILanguageService {
    private final LanguageRepository languageRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public LanguageReadOnlyDTO insertLanguage(LanguageInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException {
        Language language;
        Language insertedLanguage;

        // Check if the unique fields already exist in the DB
        if (languageRepository.findByCode(insertDTO.code()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "Language",
                    "Language with code=" + insertDTO.code() + " already exists.");
        }
        if (languageRepository.findByName(insertDTO.name()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "Language",
                    "Language with name=" + insertDTO.name() + " already exists.");
        }

        language = mapper.mapToLanguage(insertDTO);
        insertedLanguage = languageRepository.save(language);
        log.info("Language with id={}, code={}, name={} inserted.",
                insertedLanguage.getId(), insertedLanguage.getCode(), insertedLanguage.getName());
        return mapper.mapToLanguageReadOnlyDTO(insertedLanguage);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public LanguageReadOnlyDTO updateLanguage(LanguageUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException {
        Language languageToUpdate;
        Language language;
        Language updatedLanguage;

        // Check if the entity exists in the DB
        languageToUpdate = languageRepository.findById(updateDTO.id()).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Language",
                        "Language with id=" + updateDTO.id() + " not found.")
        );

        /*
         * Check if the unique fields already exist in the DB and that the input value
         * is not the same with the value of the entity to update
         */
        if (languageRepository.findByCode(updateDTO.code()).isPresent() &&
                !languageToUpdate.getCode().equals(updateDTO.code())) {
            throw new AppObjectAlreadyExistsException(
                    "Language",
                    "Language with code=" + updateDTO.code() + " already exists.");
        }
        if (languageRepository.findByName(updateDTO.name()).isPresent() &&
                !languageToUpdate.getName().equals(updateDTO.name())) {
            throw new AppObjectAlreadyExistsException(
                    "Language",
                    "Language with name=" + updateDTO.name() + " already exists.");
        }

        language = mapper.mapToLanguage(updateDTO);
        updatedLanguage = languageRepository.save(language);
        log.info("Language with id={} updated.", updatedLanguage.getId());
        return mapper.mapToLanguageReadOnlyDTO(updatedLanguage);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteLanguage(Long id)
            throws AppObjectNotFoundException {

        // Check if the entity exists in the DB
        if (!languageRepository.existsById(id)) {
            throw new AppObjectNotFoundException(
                    "Language",
                    "Language with id=" + id + " not found.");
        }

        languageRepository.deleteById(id);
        log.info("Language with id={} deleted.", id);
    }

    @Override
    public LanguageReadOnlyDTO getLanguageById(Long id)
            throws AppObjectNotFoundException {
        LanguageReadOnlyDTO languageReadOnlyDTO = languageRepository.findById(id)
                .map(mapper::mapToLanguageReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Language",
                        "Language with id=" + id + " not found."));
        log.debug("Get language by id={} was returned successfully.", id);
        return languageReadOnlyDTO;
    }

    @Override
    public LanguageReadOnlyDTO getLanguageByName(String name)
            throws AppObjectNotFoundException {
        LanguageReadOnlyDTO languageReadOnlyDTO = languageRepository.findByName(name.trim())
                .map(mapper::mapToLanguageReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Language",
                        "Language with name=" + name + " not found."));
        log.debug("Get language by name={} was returned successfully.", name);
        return languageReadOnlyDTO;
    }

    @Override
    public List<LanguageReadOnlyDTO> getAllLanguages() {
        List<LanguageReadOnlyDTO> languages = languageRepository.findAll()
                .stream()
                .map(mapper::mapToLanguageReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Get all languages were returned successfully.");
        return languages;
    }

    @Override
    public List<LanguageReadOnlyDTO> getAllLanguagesSortedByName() {
        List<LanguageReadOnlyDTO> sortedLanguages = languageRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Language::getName))
                .map(mapper::mapToLanguageReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Get all languages sorted by Name were returned successfully.");
        return sortedLanguages;
    }

    @Override
    public Boolean isCodeExists(String code) {
        return languageRepository.findByCode(code).isPresent();
    }

    @Override
    public Boolean isNameExists(String name) {
        return languageRepository.findByName(name.trim()).isPresent();
    }
}
