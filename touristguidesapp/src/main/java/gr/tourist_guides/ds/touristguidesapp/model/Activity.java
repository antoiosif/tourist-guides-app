package gr.tourist_guides.ds.touristguidesapp.model;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "activities")
public class Activity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Getter(AccessLevel.PROTECTED)
    @ManyToMany (fetch = FetchType.EAGER)   // set to 'EAGER' to avoid LazyInitializationException
    @JoinTable(name = "activities_guides")
    private Set<Guide> guides = new HashSet<>();

    public Set<Guide> getAllGuides() {
        if (guides == null) guides = new HashSet<>();
        return Collections.unmodifiableSet(guides);
    }

    public void addCategory(Category category) {
        setCategory(category);
        if (category.getActivities() == null) category.setActivities(new HashSet<>());
        category.getActivities().add(this);
    }

    public void removeCategory(Category category) {
        setCategory(null);
        if (category.getActivities() == null) return;
        category.getActivities().remove(this);
    }

    public void addGuide(Guide guide) {
        if (guides == null) guides = new HashSet<>();
        guides.add(guide);
        if (guide.getActivities() == null) guide.setActivities(new HashSet<>());
        guide.getActivities().add(this);
    }

    public void removeGuide(Guide guide) {
        if (guides == null) guides = new HashSet<>();
        guides.remove(guide);
        if (guide.getActivities() == null) return;
        guide.getActivities().remove(this);
    }

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", title=" + title +
                ", category=" + category +
                ", description=" + description +
                ", dateTime=" + dateTime +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
