package com.MusicHub.api.repository;

import com.MusicHub.api.model.Beat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Beat entity.
 */
@Repository
public interface BeatRepository extends JpaRepository<Beat, Long> {

    // Custom query for Creator discovery: Finds beats matching mood OR genre.
    List<Beat> findByMoodInOrGenreIn(List<String> moods, List<String> genres);
}
