package gr.tourist_guides.ds.touristguidesapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "visitors")
public class Visitor extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    public void addRegion(Region region) {
        setRegion(region);
        if (region.getVisitors() == null) region.setVisitors(new HashSet<>());
        region.getVisitors().add(this);
    }

    public void removeRegion(Region region) {
        setRegion(null);
        if (region.getVisitors() == null) return;
        region.getVisitors().remove(this);
    }

    @PrePersist
    public void initialize() {
        // The default value of the field "isActive" when a new Visitor is created is TRUE.
        // It changes to FALSE when the entity is deleted (soft delete).
        if (isActive == null) isActive = true;
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "id=" + id +
                ", is Active=" + isActive +
                ", uuid=" + uuid +
                ", username=" + user.getUsername() +
                ", firstname=" + user.getFirstname() +
                ", lastname=" + user.getLastname() +
                ", gender=" + user.getGender() +
                ", region=" + region +
                '}';
    }
}
