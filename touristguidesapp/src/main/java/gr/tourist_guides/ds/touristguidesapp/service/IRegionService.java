package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionUpdateDTO;
import java.util.List;

public interface IRegionService {
    RegionReadOnlyDTO insertRegion(RegionInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException;
    RegionReadOnlyDTO updateRegion(RegionUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    void deleteRegion(Long id)
            throws AppObjectNotFoundException;
    RegionReadOnlyDTO getRegionById(Long id)
            throws AppObjectNotFoundException;
    RegionReadOnlyDTO getRegionByName(String name)
            throws AppObjectNotFoundException;
    List<RegionReadOnlyDTO> getAllRegions();
    List<RegionReadOnlyDTO> getAllRegionsSortedByName();
    Boolean isNameExists(String name);
}
