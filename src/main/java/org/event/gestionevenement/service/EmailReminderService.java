package org.event.gestionevenement.service;

import org.event.gestionevenement.Repository.ParticipationRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailReminderService {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private EmailService emailService;

    // Exécution quotidienne à 13:40h
    @Scheduled(cron = "0 45 18 * * *")
    public void sendEventReminders() {
        // Récupérer la date et l'heure actuelles et ajouter un jour
        LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusNanos(1);

        // Récupérer toutes les participations pour les événements de demain
        List<Participation> participations = participationRepository.findByEvenementDateBetween(startOfTomorrow, endOfTomorrow);

        for (Participation participation : participations) {
            Utilisateur user = participation.getUser();
            Evenement event = participation.getEvenement();

            // Construire le contenu de l'email
            String subject = "Reminder: Your Event is Tomorrow!";
            String body = "Dear " + user.getUsername() + ",\n\n" +
                    "This is a reminder for the event you registered for:\n\n" +
                    "Organized By: " + event.getUser().getCompanyName() + "\n\n" +
                    "Event Title : " + event.getTitre() + "\n" +
                    "Date : " + event.getDate() + "\n" +
                    " Location : " + event.getLieu() + "\n\n" +
                    "We look forward to having you join us and hope this event will be an enriching and memorable experience for you.\n\n" +
                    "Thank you for placing your trust in us.\n\n" +
                    "Best regards,\n" +
                    "The ORGANIZIT'IT Team\n" +
                    "Simplifying Your Events, One Participation at a Time.";

            // Envoyer l'email
            emailService.sendEmail(user.getEmail(), subject, body);
        }
    }

}