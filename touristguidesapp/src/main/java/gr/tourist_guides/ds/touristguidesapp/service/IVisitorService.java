package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.core.filters.VisitorFilters;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorUpdateDTO;
import java.util.List;

public interface IVisitorService {
    VisitorReadOnlyDTO insertVisitor(VisitorInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException;
    VisitorReadOnlyDTO updateVisitor(VisitorUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectAlreadyExistsException;
    void deleteVisitor(String uuid)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException;
    VisitorReadOnlyDTO getVisitorByUuid(String uuid)
            throws AppObjectNotFoundException;
    List<VisitorReadOnlyDTO> getVisitorsFilteredSorted(VisitorFilters filters);
    Paginated<VisitorReadOnlyDTO> getVisitorsPaginatedFilteredSorted(VisitorFilters filters);
    Boolean isUsernameExists(String username);
}
