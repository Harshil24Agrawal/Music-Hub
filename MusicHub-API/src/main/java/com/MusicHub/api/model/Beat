package com.MusicHub.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a Song in the MusicHub system.
 * Data is typically cached from external APIs (iTunes, Genius) here.
 */
@Entity
@Data
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for local caching

    private String songId; // Unique ID from external API (e.g., iTunes trackId)
    private String title;
    private String artist;
    private String genre;
    private String mood;
    private String lyrics;
    private String link; // Preview or video link
    private int popularity; // Used by the Recommendation Engine

}
