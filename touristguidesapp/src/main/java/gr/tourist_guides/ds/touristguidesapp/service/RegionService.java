package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.mapper.Mapper;
import gr.tourist_guides.ds.touristguidesapp.model.Region;
import gr.tourist_guides.ds.touristguidesapp.repository.RegionRepository;
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
public class RegionService implements IRegionService {
    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RegionReadOnlyDTO insertRegion(RegionInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException {
        Region region;
        Region insertedRegion;

        // Check if the unique fields already exist in the DB
        if (regionRepository.findByName(insertDTO.name()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "Region",
                    "Region with name=" + insertDTO.name() + " already exists.");
        }

        region = mapper.mapToRegion(insertDTO);
        insertedRegion = regionRepository.save(region);
        log.info("Region with id={}, name={} inserted.", insertedRegion.getId(), insertedRegion.getName());
        return mapper.mapToRegionReadOnlyDTO(insertedRegion);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RegionReadOnlyDTO updateRegion(RegionUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExistsException {
        Region regionToUpdate;
        Region region;
        Region updatedRegion;

        // Check if the entity exists in the DB
        regionToUpdate = regionRepository.findById(updateDTO.id()).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Region",
                        "Region with id=" + updateDTO.id() + " not found.")
        );

        /*
         * Check if the unique fields already exist in the DB and that the input value
         * is not the same with the value of the entity to update
         */
        if (regionRepository.findByName(updateDTO.name()).isPresent()
                && !regionToUpdate.getName().equals(updateDTO.name())) {
            throw new AppObjectAlreadyExistsException(
                    "Region",
                    "Region with name=" + updateDTO.name() + " already exists.");
        }

        region = mapper.mapToRegion(updateDTO);
        updatedRegion = regionRepository.save(region);
        log.info("Region with id={} updated.", updatedRegion.getId());
        return mapper.mapToRegionReadOnlyDTO(updatedRegion);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteRegion(Long id)
            throws AppObjectNotFoundException {

        // Check if the entity exists in the DB
        if (!regionRepository.existsById(id)) {
            throw new AppObjectNotFoundException(
                    "Region",
                    "Region with id=" + id + " not found.");
        }

        regionRepository.deleteById(id);
        log.info("Region with id={} deleted.", id);
    }

    @Override
    public RegionReadOnlyDTO getRegionById(Long id)
            throws AppObjectNotFoundException {
        RegionReadOnlyDTO regionReadOnlyDTO = regionRepository.findById(id)
                .map(mapper::mapToRegionReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Region",
                        "Region with id=" + id + " not found."));
        log.debug("Get region by id={} was returned successfully.", id);
        return regionReadOnlyDTO;
    }

    @Override
    public RegionReadOnlyDTO getRegionByName(String name)
            throws AppObjectNotFoundException {
        RegionReadOnlyDTO regionReadOnlyDTO = regionRepository.findByName(name.trim())
                .map(mapper::mapToRegionReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Region",
                        "Region with name=" + name + " not found."));
        log.debug("Get region by name={} was returned successfully.", name);
        return regionReadOnlyDTO;
    }

    @Override
    public List<RegionReadOnlyDTO> getAllRegions() {
        List<RegionReadOnlyDTO> regions = regionRepository.findAll()
                .stream()
                .map(mapper::mapToRegionReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Get all regions were returned successfully.");
        return regions;
    }

    @Override
    public List<RegionReadOnlyDTO> getAllRegionsSortedByName() {
        List<RegionReadOnlyDTO> sortedRegions = regionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Region::getName))
                .map(mapper::mapToRegionReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Get all regions sorted by Name were returned successfully.");
        return sortedRegions;
    }

    @Override
    public Boolean isNameExists(String name) {
        return regionRepository.findByName(name.trim()).isPresent();
    }
}
