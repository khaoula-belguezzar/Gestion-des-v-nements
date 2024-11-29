package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
