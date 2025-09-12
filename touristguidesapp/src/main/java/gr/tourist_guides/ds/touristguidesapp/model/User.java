package gr.tourist_guides.ds.touristguidesapp.model;

import gr.tourist_guides.ds.touristguidesapp.core.enums.GenderType;
import gr.tourist_guides.ds.touristguidesapp.core.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Guide guide;

    @OneToOne(mappedBy = "user")
    private Visitor visitor;

    public boolean isGuide() {
        return getGuide() != null;
    }

    public boolean isVisitor() {
        return getVisitor() != null;
    }

    @PrePersist
    public void initializeIsActive() {
        // The default value of the field "isActive" when a new User is created is TRUE.
        // It changes to FALSE when the entity related to the User (Guide or Visitor) is deleted (soft delete).
        if (isActive == null) isActive = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getIsActive();
    }
}
