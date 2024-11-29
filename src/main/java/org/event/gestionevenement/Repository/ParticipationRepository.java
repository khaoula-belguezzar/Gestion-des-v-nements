package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    boolean existsByUserAndEvenement(Utilisateur user, Evenement evenement);
    List<Participation> findByUser(Utilisateur user);
    List<Participation> findByEvenementId(int evenementId);
    List<Participation> findByEvenementDate(LocalDateTime date);
    List<Participation> findByUserUsername(String username);
    @Query("SELECT p FROM Participation p WHERE p.evenement.date BETWEEN :start AND :end")
    List<Participation> findByEvenementDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}

