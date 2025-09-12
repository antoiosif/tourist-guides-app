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
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "language")
    private Set<Guide> guides = new HashSet<>();

    public Language(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Set<Guide> getAllGuides() {
        if (guides == null) guides = new HashSet<>();
        return Collections.unmodifiableSet(guides);
    }

    public void addGuide(Guide guide) {
        if (guides == null) guides = new HashSet<>();
        guides.add(guide);
        guide.setLanguage(this);
    }

    public void removeGuide(Guide guide) {
        if (guides == null) guides = new HashSet<>();
        guides.remove(guide);
        guide.setLanguage(null);
    }

    @Override
    public String toString() {
        return name;
    }
}
