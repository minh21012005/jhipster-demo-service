package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Permission;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, UUID>, JpaSpecificationExecutor<Permission> {
    List<Permission> findByActiveTrue();
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
