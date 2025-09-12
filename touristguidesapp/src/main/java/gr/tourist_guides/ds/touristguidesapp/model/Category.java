package gr.tourist_guides.ds.touristguidesapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "category")
    private Set<Activity> activities = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<Activity> getAllActivities() {
        if (activities == null) activities = new HashSet<>();
        return Collections.unmodifiableSet(activities);
    }

    public void addActivity(Activity activity) {
        if (activities == null) activities = new HashSet<>();
        activities.add(activity);
        activity.setCategory(this);
    }

    public void removeActivity(Activity activity) {
        if (activities == null) activities = new HashSet<>();
        activities.remove(activity);
        activity.setCategory(null);
    }

    @Override
    public String toString() {
        return name;
    }
}
