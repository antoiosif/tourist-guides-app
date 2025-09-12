package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.GuideFilters;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideUpdateDTO;
import java.util.List;

public interface IGuideService {
    GuideReadOnlyDTO insertGuide(GuideInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException;
    GuideReadOnlyDTO updateGuide(GuideUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectAlreadyExistsException;
    void deleteGuide(String uuid)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException;
    GuideReadOnlyDTO getGuideByUuid(String uuid)
            throws AppObjectNotFoundException;
    List<GuideReadOnlyDTO> getGuidesFilteredSorted(GuideFilters filters);
    Paginated<GuideReadOnlyDTO> getGuidesPaginatedFilteredSorted(GuideFilters filters);
    Boolean isUsernameExists(String username);
    Boolean isRecordNumberExists(String recordNumber);

    // API for Guide's favorite Activities
    void addActivityToGuide(String guideUuid, String activityUuid)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    void removeActivityFromGuide(String guideUuid, String activityUuid)
            throws AppObjectNotFoundException;
    List<ActivityReadOnlyDTO> getGuideActivities(String uuid)
            throws AppObjectNotFoundException;
    boolean isActivityExistsInGuide(String guideUuid, String activityUuid)
            throws AppObjectNotFoundException;
}
