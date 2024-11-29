package org.event.gestionevenement.controller;

import org.event.gestionevenement.Repository.EvenementRepository;
import org.event.gestionevenement.Repository.ParticipationRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Paiement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.event.gestionevenement.service.EmailService;
import org.event.gestionevenement.service.ParticipationService;
import org.event.gestionevenement.service.RatingService;
import org.event.gestionevenement.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class ParticipationController {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private UserDetailServiceImpl userService;
    @Autowired
    EmailService emailService;
    @Autowired
    RatingService rateServ;
    @Autowired
    UserDetailServiceImpl userDetailService;

//    @PostMapping("/participer/{id}")
//    public String participer(@PathVariable int id) {
//        try {
//            // Récupérer l'utilisateur connecté
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication.getName();
//            Utilisateur user = userService.findByUsername(username);
//
//            // Vérifier si l'événement existe
//            Optional<Evenement> evenementOpt = evenementRepository.findById(id);
//            if (evenementOpt.isEmpty()) {
//                return "redirect:/?error=eventNotFound";
//            }
//            Evenement evenement = evenementOpt.get();
//
//            // Vérifier si l'utilisateur a déjà participé
//            if (participationRepository.existsByUserAndEvenement(user, evenement)) {
//                return "redirect:/?error=alreadyParticipated";
//            }
//
//            // Vérifier la capacité
//            if (evenement.getCapacite() <= 0) {
//                return "redirect:/?error=capacityReached";
//            }
//
//            // Ajouter la participation
//            Participation participation = new Participation();
//            participation.setUser(user);
//            participation.setEvenement(evenement);
//            participationRepository.save(participation);
//
//            // Réduire la capacité
//            evenement.setCapacite(evenement.getCapacite() - 1);
//            evenementRepository.save(evenement);
//
//            // Envoi d'email de confirmation
//            // Envoi d'email de confirmation
//            String subject = "Confirmation de votre participation à l'événement";
//            String body = "Bonjour " + user.getUsername() + ",\n\n" +
//                    "Nous sommes ravis de vous informer que votre participation à l'événement suivant a été confirmée :\n\n" +
//                    "Titre de l'événement : " + evenement.getTitre() + "\n" +
//                    "Date : " + evenement.getDate() + "\n" +
//                    "Organisé par : " + evenement.getUser().getCompanyName() + "\n\n" +
//                    "Nous espérons que cet événement sera une expérience enrichissante et mémorable pour vous.\n\n" +
//                    "Merci de nous faire confiance.\n" +
//                    "L'équipe de ORGANIZIT'IT\n" +
//                    "Simplifier vos événements, une participation à la fois.";
//            emailService.sendEmail(user.getEmail(), subject, body);
//
//
//            return "redirect:/?success=participationAdded";
//        } catch (Exception e) {
//            e.printStackTrace(); // Log de l'erreur pour le débogage
//            return "redirect:/?error=internalError";
//        }
//    }
    @PostMapping("/waitingList/{id}")
    public String addToWaitingList(@PathVariable int id) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utilisateur user = userService.findByUsername(username);
        user.setRegistrationDate(LocalDateTime.now());

        // Vérifier si l'événement existe
        Optional<Evenement> evenementOpt = evenementRepository.findById(id);
        if (evenementOpt.isEmpty()) {
            return "redirect:/?error=evenementNotFound";
        }
        Evenement evenement = evenementOpt.get();

        // Vérifier si la capacité est pleine
        if (evenement.getCapacite() > 0) {
            return "redirect:/?error=eventNotFull";
        }

        // Vérifier si l'utilisateur a déjà participé à l'événement
        if (participationRepository.existsByUserAndEvenement(user, evenement)) {
            return "redirect:/?error=alreadyParticipated";
        }

        // Vérifier si l'utilisateur est déjà en liste d'attente
        if (evenement.getWaitingList().contains(user)) {
            return "redirect:/?error=alreadyInWaitingList";
        }

        // Ajouter l'utilisateur à la liste d'attente
        evenement.getWaitingList().add(user);
        evenementRepository.save(evenement);

        return "redirect:/?success=waitingListAdded";
    }

    @PostMapping("/rate")
    public String rateEvent(@RequestParam(name = "id") int id,
                            @RequestParam(name = "rating") int rating,
                            RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: rateEvent triggered with id=" + id + ", rating=" + rating);

        try {
            // Récupérer l'utilisateur actuellement authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            System.out.println("DEBUG: Authenticated username: " + username);

            // Récupérer l'utilisateur
            Utilisateur utilisateur = userDetailService.findByUsername(username);
            if (utilisateur == null) {
                System.out.println("DEBUG: Utilisateur non trouvé.");
                redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur non trouvé.");
                return "redirect:/"; // Évitez de rediriger vers /rate
            }

            // Ajouter la notation
            boolean response = rateServ.AddRating(rating, id, utilisateur.getId());
            if (response) {
                redirectAttributes.addFlashAttribute("successMessage", "Rating added successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to add rating.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Exception occurred - " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue.");
        }

        return "redirect:/"; // Redirection sûre
    }


    @Autowired
    private ParticipationService participationService;

    @GetMapping("/myevents")
    public String getMyEvents(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Evenement> events = participationService.findEventsByUser(username);
        model.addAttribute("events", events);
        return "myEvents";
    }


}

