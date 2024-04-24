package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@IdClass(AffectationRessouDeparId.class)
public class AffectationRessouDepar implements Serializable {

    @Id
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ressource_id", referencedColumnName = "id")
    private Ressource ressource;

    @Id
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "departement_id", referencedColumnName = "id")
    private Departement departement;

    private String personne; // Nom de la personne affectée




}
