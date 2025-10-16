package com.MusicHub.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a user (Listener or Creator) in the MusicHub system.
 * This class maps directly to the 'users' table in MySQL.
 */
@Entity
@Data
@Table(name = "users") // Maps this entity to the 'users' table in the database
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-incremented by MySQL

    private String name;
    private String email;

    // Role determines if the user is a 'LISTENER' or 'CREATOR'
    private String role;

    // User preferences (e.g., favorite genres, moods).
    private String preferences;

    // Note: A real application would include a secure password hash here.
}
