package com.mycompany.myapp.domain.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private String id;
    private String name; // ADMIN, MANAGER, STAFF, CUSTOMER
}
