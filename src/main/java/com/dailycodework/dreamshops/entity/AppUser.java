package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dailycodework.dreamshops.constant.PermissionConstant;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Email
    @Size(max = 100)
    @Column(length = 100)
    private String email;

    @Size(max = 200)
    @Column(name = "full_name", length = 200)
    private String fullName;

    @NotNull
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String role;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        authorities.addAll(resolvePermissions(role));
        return authorities;
    }

    private static List<GrantedAuthority> resolvePermissions(String role) {
        List<GrantedAuthority> perms = new ArrayList<>();
        if (role == null) return perms;
        switch (role.toUpperCase()) {
            case "ADMIN" -> {
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_VIEW));
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_CREATE));
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_UPDATE));
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_DELETE));
            }
            case "MANAGER" -> {
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_VIEW));
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_CREATE));
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_UPDATE));
            }
            case "USER" -> {
                perms.add(new SimpleGrantedAuthority(PermissionConstant.PRODUCT_VIEW));
            }
        }
        return perms;
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return Boolean.TRUE.equals(active); }
}
