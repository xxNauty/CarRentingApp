package com.example.carrentingapp.user;

import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_base")
public class UserBase implements UserDetails {

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

    @Column(precision = 1)
    private Float rank;

    @OneToMany(targetEntity = Token.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Token> loginTokens;

    @OneToMany(targetEntity = UserLock.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserLock> userLocks;

    @OneToMany(targetEntity = CarRent.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<CarRent> rentedCars;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Integer carReturnDelays;

    public void carReturnDelaysIncrement(){
        this.carReturnDelays++;
    }
    public UserBase(
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
        this.role = Role.USER;
        this.status = UserStatus.USER_CREATED;
        this.carReturnDelays = 0;
    }

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
        return !status.equals(UserStatus.USER_LOCKED_FOREVER) &&
                !status.equals(UserStatus.USER_LOCKED_TEMPORARY);
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !status.equals(UserStatus.USER_CREATED);
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role=" + role +
                ", rank=" + rank +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBase baseUser = (UserBase) o;
        return Objects.equals(id, baseUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum Role {
        USER,
        ADMIN;
        public List<SimpleGrantedAuthority> getAuthorities() {
            return List.of((new SimpleGrantedAuthority("ROLE_" + this.name())));
        }
    }

    public enum UserStatus{
        USER_CREATED,
        USER_READY,
        USER_HAS_CAR,
        USER_LOCKED_TEMPORARY,
        USER_LOCKED_FOREVER
    }

}
