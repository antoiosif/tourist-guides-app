package gr.tourist_guides.ds.touristguidesapp.service;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Role;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectAlreadyExistsException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectInvalidArgumentException;
import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotFoundException;
import gr.tourist_guides.ds.touristguidesapp.core.filters.Paginated;
import gr.tourist_guides.ds.touristguidesapp.core.filters.VisitorFilters;
import gr.tourist_guides.ds.touristguidesapp.core.specifications.VisitorSpecification;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.mapper.Mapper;
import gr.tourist_guides.ds.touristguidesapp.model.*;
import gr.tourist_guides.ds.touristguidesapp.repository.RegionRepository;
import gr.tourist_guides.ds.touristguidesapp.repository.UserRepository;
import gr.tourist_guides.ds.touristguidesapp.repository.VisitorRepository;
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
public class VisitorService implements IVisitorService {
    private final VisitorRepository visitorRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public VisitorReadOnlyDTO insertVisitor(VisitorInsertDTO insertDTO)
            throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException {
        Visitor visitor;
        Visitor insertedVisitor;

        // Check if the unique fields already exist in the DB
        if (userRepository.findByUsername(insertDTO.userInsertDTO().username()).isPresent()) {
            throw new AppObjectAlreadyExistsException(
                    "User",
                    "User with username=" + insertDTO.userInsertDTO().username() + " already exists.");
        }

        visitor = mapper.mapToVisitor(insertDTO);
        visitor.getUser().setRole(Role.VISITOR);

        // Add the Region (region can be null)
        if (insertDTO.regionId() != null) {
            Region region = regionRepository.findById(insertDTO.regionId()).orElseThrow(() ->
                    new AppObjectInvalidArgumentException(
                            "Region",
                            "Region id=" + insertDTO.regionId() + " is not valid."));
            visitor.addRegion(region);
        }

        // Inserting the Visitor inserts also the User
        insertedVisitor = visitorRepository.save(visitor);
        log.info("Visitor with id={}, uuid={}, firstname={}, lastname={}, gender={}, region={} inserted.",
                insertedVisitor.getId(), insertedVisitor.getUuid(), insertedVisitor.getUser().getFirstname(),
                insertedVisitor.getUser().getLastname(), insertedVisitor.getUser().getGender(),
                insertedVisitor.getRegion());
        return mapper.mapToVisitorReadOnlyDTO(insertedVisitor);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public VisitorReadOnlyDTO updateVisitor(VisitorUpdateDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectAlreadyExistsException {
        Visitor visitorToUpdate;
        Visitor visitor;
        Visitor updatedVisitor;

        // Check if the entity exists in the DB
        visitorToUpdate = visitorRepository.findByUuid(updateDTO.uuid()).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Visitor",
                        "Visitor with uuid=" + updateDTO.uuid() + " not found.")
        );

        /*
         * Check if the Visitor is Active so that it can be updated.
         * User isn't checked as its state is the same with the Visitor's state,
         * that is if Visitor is Active so is User and if Visitor is not Active so is User.
         */
        if (!visitorToUpdate.getIsActive()) {
            throw new AppObjectInvalidArgumentException(
                    "Visitor",
                    "Visitor with uuid=" + updateDTO.uuid() + " is not active and can not be updated.");
        }

        /*
         * Check if the unique fields already exist in the DB and that the input value
         * is not the same with the value of the entity to update
         */
        if (userRepository.findByUsername(updateDTO.userUpdateDTO().username()).isPresent() &&
                !visitorToUpdate.getUser().getUsername().equals(updateDTO.userUpdateDTO().username())) {
            throw new AppObjectAlreadyExistsException(
                    "User",
                    "User with username=" + updateDTO.userUpdateDTO().username() + " already exists.");
        }

        visitor = mapper.mapToVisitor(updateDTO);

        // Update the Region (region can be null)
        Region oldRegion = visitorToUpdate.getRegion();
        Region newRegion = updateDTO.regionId() == null ? null : regionRepository.findById(updateDTO.regionId())
                .orElseThrow(() -> new AppObjectInvalidArgumentException(
                        "Region",
                        "Region id=" + updateDTO.regionId() + " is not valid."));
        if (oldRegion != null) visitorToUpdate.removeRegion(oldRegion);
        if (newRegion != null) visitor.addRegion(newRegion);

        // Updating the Visitor updates also the User
        updatedVisitor = visitorRepository.save(visitor);
        log.info("Visitor with uuid={} updated.", updatedVisitor.getUuid());
        return mapper.mapToVisitorReadOnlyDTO(updatedVisitor);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteVisitor(String uuid)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        // Check if the entity exists in the DB
        Visitor visitorToDelete = visitorRepository.findByUuid(uuid).orElseThrow(() ->
                new AppObjectNotFoundException(
                        "Visitor",
                        "Visitor with uuid=" + uuid + " not found."));

        /*
         * Check if the Visitor is Active (only an Active Visitor can be deleted)
         */
        if (!visitorToDelete.getIsActive()) {
            throw new AppObjectInvalidArgumentException(
                    "Visitor",
                    "Visitor with uuid=" + uuid + " is not active and can not be deleted.");
        }

        // Change "isActive" field in Visitor and its corresponding User to FALSE
        visitorToDelete.setIsActive(false);
        visitorToDelete.getUser().setIsActive(false);

        // Updating the Visitor updates also the User
        visitorRepository.save(visitorToDelete);
        log.info("Visitor with uuid={} is deleted (soft delete - is not active anymore).", uuid);
    }

    @Override
    public VisitorReadOnlyDTO getVisitorByUuid(String uuid)
            throws AppObjectNotFoundException {
        VisitorReadOnlyDTO visitorReadOnlyDTO = visitorRepository.findByUuid(uuid)
                .map(mapper::mapToVisitorReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException(
                        "Visitor",
                        "Visitor with uuid=" + uuid + " not found."));
        log.debug("Get visitor by uuid={} was returned successfully.", uuid);
        return visitorReadOnlyDTO;
    }

    @Override
    public List<VisitorReadOnlyDTO> getVisitorsFilteredSorted(VisitorFilters filters) {
        Sort sort = Sort.by(Sort.Direction.fromString(filters.getSortDirection().toString()), filters.getSortBy());
        List<VisitorReadOnlyDTO> visitors = visitorRepository.findAll(getSpecsFromFilters(filters), sort)
                .stream()
                .map(mapper::mapToVisitorReadOnlyDTO)
                .collect(Collectors.toList());
        log.debug("Filtered and sorted visitors were returned successfully");
        return visitors;
    }

    @Override
    public Paginated<VisitorReadOnlyDTO> getVisitorsPaginatedFilteredSorted(VisitorFilters filters) {
        var visitors = visitorRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        log.debug("Paginated, filtered and sorted visitors were returned successfully with page={} and size={}",
                filters.getPage(), filters.getPageSize());
        return new Paginated<>(visitors.map(mapper::mapToVisitorReadOnlyDTO));
    }

    @Override
    public Boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private Specification<Visitor> getSpecsFromFilters(VisitorFilters filters) {
        return VisitorSpecification.visitorIsActive(filters.getIsActive())
                .and(VisitorSpecification.visitorStringFieldLike("uuid", filters.getUuid()))
                .and(VisitorSpecification.visitorUserStringFieldLike("username", filters.getUserUsername()))
                .and(VisitorSpecification.visitorUserStringFieldLike("firstname", filters.getUserFirstname()))
                .and(VisitorSpecification.visitorUserStringFieldLike("lastname", filters.getUserLastname()))
                .and(VisitorSpecification.visitorRegionIdIs(filters.getRegionId()));
    }
}
