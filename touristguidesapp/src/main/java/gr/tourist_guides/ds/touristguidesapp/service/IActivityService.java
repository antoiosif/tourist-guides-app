package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.ActivityFilters;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityUpdateDTO;
import java.util.List;

public interface IActivityService {
    ActivityReadOnlyDTO insertActivity(ActivityInsertDTO insertDTO)
            throws AppObjectInvalidArgumentException;
    ActivityReadOnlyDTO updateActivity(ActivityUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException;
    void deleteActivity(String uuid)
            throws AppObjectNotFoundException;
    ActivityReadOnlyDTO getActivityByUuid(String uuid)
            throws AppObjectNotFoundException;
    List<ActivityReadOnlyDTO> getActivitiesFilteredSorted(ActivityFilters filters);
    Paginated<ActivityReadOnlyDTO> getActivitiesPaginatedFilteredSorted(ActivityFilters filters);
}
