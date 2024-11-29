package org.event.gestionevenement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titre;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private String lieu;
    private double capacite;
    private String type;
    private double prix;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Utilisateur user;
    @ManyToMany
    @JoinTable(
            name="event_waiting_List",
            joinColumns = @JoinColumn(name="evenement_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<Utilisateur> waitingList = new ArrayList<>();

    // Liste des participants
    @ManyToMany
    private List<Utilisateur> participants = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    public Double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0; // Si aucune évaluation, retourner 0
        }
        return ratings.stream()
                .mapToInt(Rating::getRating) // Récupérer toutes les évaluations
                .average() // Calculer la moyenne
                .orElse(0.0); // Retourner 0.0 si aucune moyenne n'est calculable
    }

}
