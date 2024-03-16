package com.example.carrentingapp.user;

import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.token.Token;
import com.example.carrentingapp.user.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.example.carrentingapp.user.enums.Role.USER;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_base")
public class BaseUser implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique=true)
    private String email;

    @JsonIgnore
    private String password;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Float rank;

    private Boolean isLocked;

    private Boolean isEnabled;

    @OneToMany(targetEntity = Token.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Token> loginTokens;

    @OneToMany(targetEntity = UserLock.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserLock> userLocks;

    @OneToMany(targetEntity = CarRent.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<CarRent> rentedCars;

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

    @JsonIgnore
    public void updateRank(float valueToAdd){
        this.rank += valueToAdd;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isEnabled;
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
