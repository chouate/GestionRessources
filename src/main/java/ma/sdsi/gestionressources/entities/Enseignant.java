package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Enseignant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prenom;
    private String nom;

    @ManyToOne
    @JoinColumn(name = "idChefDepartement")
    private Enseignant chefDepartement;

    @OneToMany(mappedBy = "chefDepartement")
    private List<Enseignant> enseignantsSousResponsabilite;

    @OneToMany(mappedBy = "enseignant")
    private List<Demande> demandes;

    @OneToMany(mappedBy = "enseignant")
    private List<Ressource> ressourceList;
}
