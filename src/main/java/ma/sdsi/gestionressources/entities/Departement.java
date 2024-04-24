package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    // Relation OneToOne pour le chef de département
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "chef_depart_id") // Nom de la colonne de jointure
    private Enseignant chefDepartement;  // Chef de département

}
