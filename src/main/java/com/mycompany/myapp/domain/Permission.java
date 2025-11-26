package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends AbstractAuditingEntity<String> {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code; // MOVIE_CREATE, BOOKING_MANAGE...

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String apiPath;

    @Column(nullable = false)
    private String module;

    private String description; // optional

    @Column(nullable = false)
    private boolean active = true;

    @Override
    public String getId() {
        return id != null ? id.toString() : null;
    }

    @Override
    public void setId(String id) {
        this.id = id != null ? UUID.fromString(id) : null;
    }
}
