package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    
    @OneToMany(mappedBy = "departement") // Correction de mappedBy ici pour la relation avec Ressource
    private List<Ressource> ressources;

    // Relation OneToOne pour le chef de département
    @OneToOne
    @JoinColumn(name = "chef_depart_id") // Nom de la colonne de jointure
    private Enseignant chefDepartement;  // Chef de département

}
