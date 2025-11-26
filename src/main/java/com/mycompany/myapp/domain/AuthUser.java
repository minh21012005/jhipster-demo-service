package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "auth_users")
public class AuthUser extends AbstractAuditingEntity<String> {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private boolean enabled = true;

    @Override
    public String getId() {
        return id != null ? id.toString() : null;
    }

    @Override
    public void setId(String id) {
        this.id = id != null ? UUID.fromString(id) : null;
    }
}
