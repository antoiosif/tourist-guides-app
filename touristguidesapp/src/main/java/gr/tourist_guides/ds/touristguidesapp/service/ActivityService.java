package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.ActivityFilters;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.core.specifications.ActivitySpecification;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.mapper.Mapper;
import gr.tourist_guides.ds.touristguidesapp.model.*;
import gr.tourist_guides.ds.touristguidesapp.repository.ActivityRepository;
import gr.tourist_guides.ds.touristguidesapp.repository.CategoryRepository;
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
public class ActivityService implements IActivityService {
    private final ActivityRepository activityRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ActivityReadOnlyDTO insertActivity(ActivityInsertDTO insertDTO)
            throws AppObjectInvalidArgumentException {
        Activity activity;
        Activity insertedActivity;

        activity = mapper.mapToActivity(insertDTO);

        // Add the Category (category can not be null)
        Category category = categoryRepository.findById(insertDTO.categoryId()).orElseThrow(() ->
                new AppObjectInvalidArgumentException(
                        "Category",
                        "Category id=" + insertDTO.categoryId() + " is not valid."));
        activity.addCategory(category);

        insertedActivity = activityRepository.save(activity);
        log.info("Activity with id={}, uuid={}, title={} saved.",
                insertedActivity.getId(), insertedActivity.getUuid(), insertedActivity.getTitle());
        return mapper.mapToActivityReadOnlyDTO(insertedActivity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ActivityReadOnlyDTO updateActivity(ActivityUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {
        Activity activityToUpdate;
        Activity activity;
        Activity updatedActivity;

        // Check if the entity exists in the DB
        activityToUpdate = activityRepository.findByUuid(updateDTO.uuid()).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Activity",
                        "Activity with uuid=" + updateDTO.uuid() + " not found."));

        activity = mapper.mapToActivity(updateDTO);

        // Update the Category (category can not be null)
        Category oldCategory = activityToUpdate.getCategory();
        Category newCategory = categoryRepository.findById(updateDTO.categoryId())
                .orElseThrow(() -> new AppObjectInvalidArgumentException(
                        "Category",
                        "Category id=" + updateDTO.categoryId() + " is not valid."));
        activityToUpdate.removeCategory(oldCategory);
        activity.addCategory(newCategory);

        updatedActivity = activityRepository.save(activity);
        log.info("Activity with uuid={} updated.", updatedActivity.getUuid());
        return mapper.mapToActivityReadOnlyDTO(updatedActivity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteActivity(String uuid)
            throws AppObjectNotFoundException {

        // Check if the entity exists in the DB
        Activity activityToDelete = activityRepository.findByUuid(uuid).orElseThrow(() ->
            new AppObjectNotFoundException(
                    "Activity",
                    "Activity with uuid=" + uuid + " not found."));
        activityRepository.delete(activityToDelete);
        log.info("Activity with uuid={} deleted.", uuid);
    }

    @Override
    public ActivityReadOnlyDTO getActivityByUuid(String uuid)
            throws AppObjectNotFoundException {
        ActivityReadOnlyDTO activityReadOnlyDTO = activityRepository.findByUuid(uuid)
                .map(mapper::mapToActivityReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Activity",
                        "Activity with uuid=" + uuid + " not found."));
        log.debug("Get activity by uuid={} was returned successfully.", uuid);
        return activityReadOnlyDTO;
    }

    @Override
    public List<ActivityReadOnlyDTO> getActivitiesFilteredSorted(ActivityFilters filters) {
        Sort sort = Sort.by(Sort.Direction.fromString(filters.getSortDirection().toString()), filters.getSortBy());
        List<ActivityReadOnlyDTO> activities = activityRepository.findAll(getSpecsFromFilters(filters), sort)
                .stream()
                .map(mapper::mapToActivityReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Filtered and sorted activities were returned successfully");
        return activities;
    }

    @Override
    public Paginated<ActivityReadOnlyDTO> getActivitiesPaginatedFilteredSorted(ActivityFilters filters) {
        var activities = activityRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        log.debug("Paginated, filtered and sorted activities were returned successfully with page={} and size={}",
                filters.getPage(), filters.getPageSize());
        return new Paginated<>(activities.map(mapper::mapToActivityReadOnlyDTO));
    }

    private Specification<Activity> getSpecsFromFilters(ActivityFilters filters) {
        return ActivitySpecification.activityStringFieldLike("uuid", filters.getUuid())
                .and(ActivitySpecification.activityStringFieldLike("title", filters.getTitle()))
                .and(ActivitySpecification.activityStatusIs(filters.getStatus()))
                .and(ActivitySpecification.activityCategoryIdIs(filters.getCategoryId()));
    }
}
