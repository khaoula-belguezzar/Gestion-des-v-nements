package org.event.gestionevenement.service;

import org.event.gestionevenement.Repository.EvenementRepository;
import org.event.gestionevenement.Repository.RatingRepository;
import org.event.gestionevenement.Repository.UtilisateurRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Rating;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    EvenementRepository evenementRepository;
    @Autowired
    RatingRepository ratingRepository;
    public boolean AddRating(int rating, int event_id, String user_id) {
        // Validation de la note
        if (rating < 1 || rating > 5) {
            System.out.println("DEBUG: Invalid rating value: " + rating);
            return false;
        }

        // Vérification de l'existence de l'événement
        Evenement event = evenementRepository.findById(event_id).orElse(null);
        if (event == null) {
            System.out.println("DEBUG: Event not found for ID: " + event_id);
            return false;
        }

        // Vérification de l'existence de l'utilisateur
        Utilisateur user = utilisateurRepository.findById(user_id).orElse(null);
        if (user == null) {
            System.out.println("DEBUG: User not found for ID: " + user_id);
            return false;
        }
        // Création de l'objet Rating
        try {
            Rating newRating = Rating.builder()
                    .user(user)
                    .event(event)
                    .rating(rating)
                    .build();

            // Enregistrement de la note
            ratingRepository.save(newRating);
            System.out.println("DEBUG: Rating saved successfully for user: " + user_id + ", event: " + event_id);
            return true;

        } catch (Exception e) {
            // Gestion des erreurs imprévues
            System.out.println("ERROR: Exception while saving rating - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}

