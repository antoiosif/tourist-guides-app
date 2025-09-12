package gr.tourist_guides.ds.touristguidesapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "guides")
public class Guide extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @Column(name = "record_number", nullable = false, unique = true)
    private String recordNumber;

    @Column(name = "date_of_issue", nullable = false)
    private Date dateOfIssue;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    private String email;

    @Column(length = 5000)
    private String bio;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(mappedBy = "guides", fetch = FetchType.EAGER)   // set to 'EAGER' to avoid LazyInitializationException
    private Set<Activity> activities = new HashSet<>();

    public Set<Activity> getAllActivities() {
        if (activities == null) activities = new HashSet<>();
        return Collections.unmodifiableSet(activities);
    }

    public void addRegion(Region region) {
        setRegion(region);
        if (region.getGuides() == null) region.setGuides(new HashSet<>());
        region.getGuides().add(this);
    }

    public void removeRegion(Region region) {
        setRegion(null);
        if (region.getGuides() == null) return;
        region.getGuides().remove(this);
    }

    public void addLanguage(Language language) {
        setLanguage(language);
        if (language.getGuides() == null) language.setGuides(new HashSet<>());
        language.getGuides().add(this);
    }

    public void removeLanguage(Language language) {
        setLanguage(null);
        if (language.getGuides() == null) return;
        language.getGuides().remove(this);
    }

    public void addActivity(Activity activity) {
        if (activities == null) activities = new HashSet<>();
        activities.add(activity);
        if (activity.getGuides() == null) activity.setGuides(new HashSet<>());
        activity.getGuides().add(this);
    }

    public void removeActivity(Activity activity) {
        if (activities == null) activities = new HashSet<>();
        activities.remove(activity);
        if (activity.getGuides() == null) return;
        activity.getGuides().remove(this);
    }

    @PrePersist
    public void initialize() {
        // The default value of the field "isActive" when a new Guide is created is TRUE.
        // It changes to FALSE when the entity is deleted (soft delete).
        if (isActive == null) isActive = true;
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Guide{" +
                "id=" + id +
                ", is Active=" + isActive +
                ", uuid=" + uuid +
                ", username=" + user.getUsername() +
                ", firstname=" + user.getFirstname() +
                ", lastname=" + user.getLastname() +
                ", gender=" + user.getGender() +
                ", record number=" + recordNumber +
                ", date of issue=" + dateOfIssue +
                ", phone number=" + phoneNumber +
                ", email=" + email +
                ", region=" + region +
                ", language=" + language +
                ", bio=" + bio +
                '}';
    }
}
