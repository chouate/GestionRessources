package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public  class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private String type;
    private String marque;
    private String numeroInventaire;
    private boolean livree ;

    @ManyToOne
    private Demande demande;
    @ToString.Exclude
    @ManyToOne
    private Enseignant enseignant;


}
