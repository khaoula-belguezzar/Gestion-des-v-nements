package org.event.gestionevenement.controller;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Utilisateur;
import org.event.gestionevenement.service.EvenementServiceImpl;
import org.event.gestionevenement.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SecuriteController {
    @Autowired
    EvenementServiceImpl evenementService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @GetMapping("/notAuthenticated")
    public String  notAuthenticated() {
        return "notAuthenticated";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @GetMapping("test")
    public String test(){
        return "notAuthenticated";
    }

    @GetMapping("/")
    public String home(Model model){
        // Get the authenticated user’s roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // récupère le nom d'utilisateur (user connecté)
        // Trouver l'utilisateur connecté
        Utilisateur user = userDetailService.findByUsername(username); // Trouver l'utilisateur par son nom
        model.addAttribute("username", username);
        // Récupérer les événements créés par cet utilisateur
        List<Evenement> evenements = evenementService.findByUser(user);
        model.addAttribute("evenements", evenements);
        // Check if the user has the role "ADMIN" and redirect to homeAdmin
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "homeAdmin";
        }

        // Check if the user has the role "USER" and redirect to homeUser
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            List<Evenement> listevent = evenementService.getAllEvenement();
            model.addAttribute("listevent", listevent);
            return "homeUser";
        }

        // Default redirection if no role is matched
        return "redirect:/login?error"; // or any other page
    }
}
