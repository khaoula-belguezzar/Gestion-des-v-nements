package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    @Override
    List<Paiement> findAll();
    List<Paiement> findByEvenementIn(List<Evenement> evenements);
}
