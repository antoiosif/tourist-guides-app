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
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "region")
    private Set<Guide> guides = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "region")
    private Set<Visitor> visitors = new HashSet<>();

    public Region(String name) {
        this.name = name;
    }

    public Region(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<Guide> getAllGuides() {
        if (guides == null) guides = new HashSet<>();
        return Collections.unmodifiableSet(guides);
    }

    public Set<Visitor> getAllVisitors() {
        if (visitors == null) visitors = new HashSet<>();
        return Collections.unmodifiableSet(visitors);
    }

    public void addGuide(Guide guide) {
        if (guides == null) guides = new HashSet<>();
        guides.add(guide);
        guide.setRegion(this);
    }

    public void removeGuide(Guide guide) {
        if (guides == null) guides = new HashSet<>();
        guides.remove(guide);
        guide.setRegion(null);
    }

    public void addVisitor(Visitor visitor) {
        if (visitors == null) visitors = new HashSet<>();
        visitors.add(visitor);
        visitor.setRegion(this);
    }

    public void removeVisitor(Visitor visitor) {
        if (visitors == null) visitors = new HashSet<>();
        visitors.remove(visitor);
        visitor.setRegion(null);
    }

    @Override
    public String toString() {
        return name;
    }
}
