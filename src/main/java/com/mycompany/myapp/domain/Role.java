package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends AbstractAuditingEntity<String> {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToMany
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = { "role_id", "permission_id" })
    )
    private List<Permission> permissions;

    @Override
    public String getId() {
        return id != null ? id.toString() : null;
    }

    @Override
    public void setId(String id) {
        this.id = id != null ? UUID.fromString(id) : null;
    }
}
