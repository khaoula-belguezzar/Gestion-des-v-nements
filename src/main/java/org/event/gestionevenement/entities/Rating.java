package org.event.gestionevenement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@AllArgsConstructor @NoArgsConstructor @Data @Builder
@ToString(exclude = "event")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference   //éviter des cycles dans les relations JSON
    private Utilisateur user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private Evenement event;

    @Min(1)
    @Max(5)
    @NotNull
    private int rating;

}
