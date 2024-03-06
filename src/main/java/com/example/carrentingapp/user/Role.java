package com.example.carrentingapp.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.carrentingapp.user.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    USER_GET_CAR
            )
    ),

    ADMIN(
            Set.of(
                    ADMIN_CREATE_CAR,
                    ADMIN_UPDATE_CAR
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
