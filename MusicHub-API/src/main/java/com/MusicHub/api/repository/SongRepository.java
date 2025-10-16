package com.MusicHub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Song entity.
 * Includes custom methods for the Recommendation Engine (filtering by mood/genre).
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // Custom query for recommendations: Finds songs matching a mood OR a genre, ordered by popularity.
    List<Song> findByMoodInOrGenreInOrderByPopularityDesc(List<String> moods, List<String> genres);

    // Custom query for searching: Search by title or artist
    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist);

}
