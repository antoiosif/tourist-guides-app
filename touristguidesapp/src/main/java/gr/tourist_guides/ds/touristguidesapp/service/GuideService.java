package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Role;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.GuideFilters;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.core.specifications.GuideSpecification;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.mapper.Mapper;
import gr.tourist_guides.ds.touristguidesapp.model.*;
import gr.tourist_guides.ds.touristguidesapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuideService implements IGuideService {
    private final GuideRepository guideRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final LanguageRepository languageRepository;
    private final ActivityRepository activityRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public GuideReadOnlyDTO insertGuide(GuideInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {
        Guide guide;
        Guide insertedGuide;

        // Check if the unique fields already exist in the DB
        if (userRepository.findByUsername(insertDTO.userInsertDTO().username()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "User",
                    "User with username=" + insertDTO.userInsertDTO().username() + " already exists.");
        }
        if (guideRepository.findByRecordNumber(insertDTO.recordNumber()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "Guide",
                    "Guide with record number=" + insertDTO.recordNumber() + " already exists.");
        }

        guide = mapper.mapToGuide(insertDTO);
        guide.getUser().setRole(Role.GUIDE);

        // Add the Region (region can not be null)
        Region region = regionRepository.findById(insertDTO.regionId()).orElseThrow(() ->
                new AppObjectInvalidArgumentException(
                        "Region",
                        "Region id=" + insertDTO.regionId() + " is not valid."));
        guide.addRegion(region);

        // Add the Language (language can not be null)
        Language language = languageRepository.findById(insertDTO.languageId()).orElseThrow(() ->
                new AppObjectInvalidArgumentException(
                        "Language",
                        "Language id=" + insertDTO.languageId() + " is not valid."));
        guide.addLanguage(language);

        // Inserting the Guide inserts also the User
        insertedGuide = guideRepository.save(guide);
        log.info("Guide with id={}, uuid={}, firstname={}, lastname ={}, record number={}, date of issue={}, gender={}, " +
                        "phone number={}, email={}, region={} inserted.",
                insertedGuide.getId(), insertedGuide.getUuid(), insertedGuide.getUser().getFirstname(),
                insertedGuide.getUser().getLastname(), insertedGuide.getRecordNumber(), insertedGuide.getDateOfIssue(),
                insertedGuide.getUser().getGender(), insertedGuide.getPhoneNumber(), insertedGuide.getEmail(),
                insertedGuide.getRegion());
        return mapper.mapToGuideReadOnlyDTO(insertedGuide);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public GuideReadOnlyDTO updateGuide(GuideUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectAlreadyExistsException {
        Guide guideToUpdate;
        Guide guide;
        Guide updatedGuide;

        // Check if the entity exists in the DB
        guideToUpdate = guideRepository.findByUuid(updateDTO.uuid()).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Guide",
                        "Guide with uuid=" + updateDTO.uuid() + " not found.")
        );

        /*
         * Check if the Guide is Active so that it can be updated.
         * User isn't checked as its state is the same with the Guide's state,
         * that is if Guide is Active so is User and if Guide is not Active so is User.
         */
        if (!guideToUpdate.getIsActive()) {
            throw new AppObjectInvalidArgumentException(
                    "Guide",
                    "Guide with uuid=" + updateDTO.uuid() + " is not active and can not be updated.");
        }

        /*
         * Check if the unique fields already exist in the DB and that the input value
         * is not the same with the value of the entity to update
         */
        if (userRepository.findByUsername(updateDTO.userUpdateDTO().username()).isPresent() &&
                !guideToUpdate.getUser().getUsername().equals(updateDTO.userUpdateDTO().username())) {
            throw new AppObjectAlreadyExistsException(
                    "User",
                    "User with username=" + updateDTO.userUpdateDTO().username() + " already exists.");
        }
        if (guideRepository.findByRecordNumber(updateDTO.recordNumber()).isPresent() &&
                !guideToUpdate.getRecordNumber().equals(updateDTO.recordNumber())) {
            throw new AppObjectAlreadyExistsException(
                    "Guide",
                    "Guide with record number=" + updateDTO.recordNumber() + " already exists.");
        }

        guide = mapper.mapToGuide(updateDTO);

        // Update the Region (region can not be null)
        Region oldRegion = guideToUpdate.getRegion();
        Region newRegion = regionRepository.findById(updateDTO.regionId()).orElseThrow(() ->
                new AppObjectInvalidArgumentException(
                        "Region",
                        "Region id=" + updateDTO.regionId() + " is not valid."));
        guideToUpdate.removeRegion(oldRegion);
        guide.addRegion(newRegion);

        // Update the Language (language can not be null)
        Language oldLanguage = guideToUpdate.getLanguage();
        Language newLanguage = languageRepository.findById(updateDTO.languageId()).orElseThrow(() ->
                new AppObjectInvalidArgumentException(
                        "Language",
                        "Language id=" + updateDTO.languageId() + " is not valid."));
        guideToUpdate.removeLanguage(oldLanguage);
        guide.addLanguage(newLanguage);

        // Updating the Guide updates also the User
        updatedGuide = guideRepository.save(guide);
        log.info("Guide with uuid={} updated.", updatedGuide.getUuid());
        return mapper.mapToGuideReadOnlyDTO(updatedGuide);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteGuide(String uuid)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        // Check if the entity exists in the DB
        Guide guideToDelete = guideRepository.findByUuid(uuid).orElseThrow(() ->
            new AppObjectNotFoundException(
                    "Guide",
                    "Guide with uuid=" + uuid + " not found."));

        /*
         * Check if the Guide is Active (only an Active Guide can be deleted)
         */
        if (!guideToDelete.getIsActive()) {
            throw new AppObjectInvalidArgumentException(
                    "Guide",
                    "Guide with uuid=" + uuid + " is not active and can not be deleted.");
        }

        // Change "isActive" field in Guide and its corresponding User to FALSE
        guideToDelete.setIsActive(false);
        guideToDelete.getUser().setIsActive(false);

        // Updating the Guide updates also the User
        guideRepository.save(guideToDelete);
        log.info("Guide with uuid={} is deleted (soft delete - is not active anymore).", uuid);
    }

    @Override
    public GuideReadOnlyDTO getGuideByUuid(String uuid)
            throws AppObjectNotFoundException {
        GuideReadOnlyDTO guideReadOnlyDTO = guideRepository.findByUuid(uuid)
                .map(mapper::mapToGuideReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Guide",
                        "Guide with uuid=" + uuid + " not found."));
        log.debug("Get guide by uuid={} was returned successfully.", uuid);
        return guideReadOnlyDTO;
    }

    @Override
    public List<GuideReadOnlyDTO> getGuidesFilteredSorted(GuideFilters filters) {
        Sort sort = Sort.by(Sort.Direction.fromString(filters.getSortDirection().toString()), filters.getSortBy());
        List<GuideReadOnlyDTO> guides = guideRepository.findAll(getSpecsFromFilters(filters), sort)
                .stream()
                .map(mapper::mapToGuideReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Filtered and sorted guides were returned successfully");
        return guides;
    }

    @Override
    public Paginated<GuideReadOnlyDTO> getGuidesPaginatedFilteredSorted(GuideFilters filters) {
        var guides = guideRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        log.debug("Paginated, filtered and sorted guides were returned successfully with page={} and size={}",
                filters.getPage(), filters.getPageSize());
        return new Paginated<>(guides.map(mapper::mapToGuideReadOnlyDTO));
    }

    @Override
    public Boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public Boolean isRecordNumberExists(String recordNumber) {
        return guideRepository.findByRecordNumber(recordNumber.trim()).isPresent();
    }

    // API for Guide's favorite Activities
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addActivityToGuide(String guideUuid, String activityUuid)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException {

        // Check if the entities exist in the DB
        Guide guide = guideRepository.findByUuid(guideUuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Guide",
                        "Guide with uuid=" + guideUuid + " not found."));
        Activity activity = activityRepository.findByUuid(activityUuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Activity",
                        "Activity with uuid=" + activityUuid + " not found."));

        // Check if the Activity is already in Guide's favorites
        if (guide.getAllActivities().stream().anyMatch(a -> a.getUuid().equals(activityUuid))) {
            throw new AppObjectAlreadyExistsException(
                    "Activity",
                    "Activity with uuid=" + activityUuid +
                            " is already in favorites of Guide with uuid=" + guideUuid);
        }

        guide.addActivity(activity);
        guideRepository.save(guide);
        log.info("Activity with uuid={} added to Guide with uuid={}.", activityUuid, guideUuid);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removeActivityFromGuide(String guideUuid, String activityUuid)
            throws AppObjectNotFoundException {

        // Check if the entities exist in the DB
        Guide guide = guideRepository.findByUuid(guideUuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Guide",
                        "Guide with uuid=" + guideUuid + " not found."));
        Activity activity = activityRepository.findByUuid(activityUuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Activity",
                        "Activity with uuid=" + activityUuid + " not found."));

        // Check if the Activity is not in Guide's favorites
        if (guide.getAllActivities().stream().noneMatch(a -> a.getUuid().equals(activityUuid))) {
            throw new AppObjectNotFoundException(
                    "Activity",
                    "Activity with uuid=" + activityUuid +
                            " is not in favorites of Guide with uuid=" + guideUuid);
        }

        guide.removeActivity(activity);
        guideRepository.save(guide);
        log.info("Activity with uuid={} removed from Guide with uuid={}.", activityUuid, guideUuid);
    }

    @Override
    public List<ActivityReadOnlyDTO> getGuideActivities(String uuid)
            throws AppObjectNotFoundException {

        // Check if the entity exists in the DB
        Guide guide = guideRepository.findByUuid(uuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Guide",
                        "Guide with uuid=" + uuid + " not found."));

        List<ActivityReadOnlyDTO> guideActivities = guide.getAllActivities()
                .stream()
                .map(mapper::mapToActivityReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Activities of Guide with uuid={} were returned successfully", uuid);
        return guideActivities;
    }

    @Override
    public boolean isActivityExistsInGuide(String guideUuid, String activityUuid)
            throws AppObjectNotFoundException {

        // Check if the Guide exists in the DB
        Guide guide = guideRepository.findByUuid(guideUuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Guide",
                        "Guide with uuid=" + guideUuid + " not found."));

        return guide.getAllActivities().stream().anyMatch(a -> a.getUuid().equals(activityUuid));
    }

    private Specification<Guide> getSpecsFromFilters(GuideFilters filters) {
        return GuideSpecification.guideIsActive(filters.getIsActive())
                .and(GuideSpecification.guideStringFieldLike("uuid", filters.getUuid()))
                .and(GuideSpecification.guideStringFieldLike("recordNumber", filters.getRecordNumber()))
                .and(GuideSpecification.guideUserStringFieldLike("username", filters.getUserUsername()))
                .and(GuideSpecification.guideUserStringFieldLike("firstname", filters.getUserFirstname()))
                .and(GuideSpecification.guideUserStringFieldLike("lastname", filters.getUserLastname()))
                .and(GuideSpecification.guideRegionIdIs(filters.getRegionId()))
                .and(GuideSpecification.guideLanguageIdIs(filters.getLanguageId()));
    }
}
