package gr.tourist_guides.ds.touristguidesapp.mapper;

import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.activity.ActivityUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.category.CategoryUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.guide.GuideUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.language.LanguageUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.region.RegionUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.user.UserInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.user.UserReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.user.UserUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorInsertDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorReadOnlyDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.visitor.VisitorUpdateDTO;
import gr.tourist_guides.ds.touristguidesapp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PasswordEncoder passwordEncoder;

    // mappers for User
    public User mapToUser(UserInsertDTO dto) {
        return User.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .gender(dto.gender())
                .build();
    }

    public User mapToUser(UserUpdateDTO dto) {
        return User.builder()
                .id(dto.id())
                .isActive(dto.isActive())
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .gender(dto.gender())
                .role(dto.role())
                .build();
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getCreatedAt(), user.getUpdatedAt(), user.getId(), user.getIsActive(),
                user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getGender(),
                user.getRole());
    }

    // mappers for Guide
    public Guide mapToGuide(GuideInsertDTO dto) {
        return Guide.builder()
                .recordNumber(dto.recordNumber())
                .dateOfIssue(dto.dateOfIssue())
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .bio(dto.bio())
                .user(mapToUser(dto.userInsertDTO()))
                .build();
    }

    public Guide mapToGuide(GuideUpdateDTO dto) {
        return Guide.builder()
                .id(dto.id())
                .isActive(dto.isActive())
                .uuid(dto.uuid())
                .recordNumber(dto.recordNumber())
                .dateOfIssue(dto.dateOfIssue())
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .bio(dto.bio())
                .user(mapToUser(dto.userUpdateDTO()))
                .build();
    }

    public GuideReadOnlyDTO mapToGuideReadOnlyDTO(Guide guide) {
        return new GuideReadOnlyDTO(guide.getCreatedAt(), guide.getUpdatedAt(), guide.getId(),
                guide.getIsActive(), guide.getUuid(), guide.getRecordNumber(), guide.getDateOfIssue(),
                guide.getPhoneNumber(), guide.getEmail(), guide.getBio(), mapToUserReadOnlyDTO(guide.getUser()),
                guide.getRegion().getName(), guide.getLanguage().getName());
    }

    // mappers for Visitor
    public Visitor mapToVisitor(VisitorInsertDTO dto) {
        return Visitor.builder()
                .user(mapToUser(dto.userInsertDTO()))
                .build();
    }

    public Visitor mapToVisitor(VisitorUpdateDTO dto) {
        return Visitor.builder()
                .id(dto.id())
                .isActive(dto.isActive())
                .uuid(dto.uuid())
                .user(mapToUser(dto.userUpdateDTO()))
                .build();
    }

    public VisitorReadOnlyDTO mapToVisitorReadOnlyDTO(Visitor visitor) {
        String region = visitor.getRegion() == null ? null : visitor.getRegion().getName();
        return new VisitorReadOnlyDTO(visitor.getCreatedAt(), visitor.getUpdatedAt(), visitor.getId(),
                visitor.getIsActive(), visitor.getUuid(), mapToUserReadOnlyDTO(visitor.getUser()), region);
    }

    // mappers for Activity
    public Activity mapToActivity(ActivityInsertDTO dto) {
        return Activity.builder()
                .title(dto.title().trim())
                .description(dto.description().trim())
                .dateTime(dto.dateTime())
                .price(dto.price())
                .status(dto.status())
                .build();
    }

    public Activity mapToActivity(ActivityUpdateDTO dto) {
        return Activity.builder()
                .id(dto.id())
                .uuid(dto.uuid())
                .title(dto.title().trim())
                .description(dto.description().trim())
                .dateTime(dto.dateTime())
                .price(dto.price())
                .status(dto.status())
                .build();
    }

    public ActivityReadOnlyDTO mapToActivityReadOnlyDTO(Activity activity) {
        return new ActivityReadOnlyDTO(activity.getCreatedAt(), activity.getUpdatedAt(), activity.getId(),
                activity.getUuid(), activity.getTitle(), activity.getDescription(), activity.getDateTime(),
                activity.getPrice(), activity.getStatus(), activity.getCategory().getName());
    }

    // mappers for Region
    public Region mapToRegion(RegionInsertDTO dto) {
        return new Region(dto.name().trim());
    }

    public Region mapToRegion(RegionUpdateDTO dto) {
        return new Region(dto.id(), dto.name().trim());
    }

    public RegionReadOnlyDTO mapToRegionReadOnlyDTO(Region region) {
        return new RegionReadOnlyDTO(region.getId(), region.getName());
    }

    // mappers for Language
    public Language mapToLanguage(LanguageInsertDTO dto) {
        return new Language(dto.code().toUpperCase(), dto.name().trim());
    }

    public Language mapToLanguage(LanguageUpdateDTO dto) {
        return new Language(dto.id(), dto.code().toUpperCase(), dto.name().trim());
    }

    public LanguageReadOnlyDTO mapToLanguageReadOnlyDTO(Language language) {
        return new LanguageReadOnlyDTO(language.getId(), language.getCode(), language.getName());
    }

    // mappers for Category
    public Category mapToCategory(CategoryInsertDTO dto) {
        return new Category(dto.name().trim());
    }

    public Category mapToCategory(CategoryUpdateDTO dto) {
        return new Category(dto.id(), dto.name().trim());
    }

    public CategoryReadOnlyDTO mapToCategoryReadOnlyDTO(Category category) {
        return new CategoryReadOnlyDTO(category.getId(), category.getName());
    }
}
