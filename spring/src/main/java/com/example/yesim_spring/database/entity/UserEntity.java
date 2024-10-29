package com.example.yesim_spring.database.entity;

import com.example.yesim_spring.database.entity.define.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_seq")
    private long seq;


    @Column(name="user_id", updatable = false, nullable = false, unique = true)
    private String userId;

    @Column(name="user_pw", nullable = false)
    private String password;

    @Column(name="user_name", nullable = false)
    private String userNick;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="approved_yn", nullable = false)
    private String approvedYn = "N";

    @Enumerated(EnumType.STRING)
    @Column(name ="role")
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }



    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<UsageEntity> usageList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<PurchaseEntity> purchaseRequests = new ArrayList<>();

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RefreshTokenEntity> refreshTokenEntityList = new ArrayList<>();


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
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
        return true;
    }
}
