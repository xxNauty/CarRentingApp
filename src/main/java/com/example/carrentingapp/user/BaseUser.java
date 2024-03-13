package com.example.carrentingapp.user;

import com.example.carrentingapp.token.Token;
import com.example.carrentingapp.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.example.carrentingapp.user.enums.Role.USER;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BaseUser implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique=true)
    private String email;

    private String password;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Float rank;

    private Boolean isLocked;

    private Boolean isEnabled;

    @OneToMany(targetEntity = Token.class, mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(targetEntity = UserLock.class, mappedBy = "user")
    private List<UserLock> locks;

//    private List<CarRental> rentedCars;
//
//    private List<CarRental> reservedCars;
// todo: odkomentować po dodaniu brakującej klasy

    public BaseUser(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.rank = 5.0F;
        this.isLocked = false;
        this.isEnabled = false;
        this.role = USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Boolean isAccountLocked(){ return isLocked; }

    public Boolean isAccountEnabled(){ return isEnabled; }

    @Override
    @Deprecated
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Deprecated
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Deprecated
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Deprecated
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
