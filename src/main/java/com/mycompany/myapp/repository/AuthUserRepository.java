package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AuthUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByRefreshTokenAndEmail(String token, String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM AuthUser u JOIN FETCH u.role WHERE u.email = :email")
    Optional<AuthUser> findByEmailWithRole(@Param("email") String email);
}
