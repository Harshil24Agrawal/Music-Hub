package com.MusicHub.api.repository;

import com.musichub.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 * JpaRepository provides all standard CRUD methods (save, findById, findAll, etc.).
 * Parameters: <Entity Type, Primary Key Type>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query example: Spring automatically implements this method to find a User by email
    User findByEmail(String email);
}
