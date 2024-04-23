package ma.sdsi.gestionressources.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Imprimante extends Ressource {

    private int vitesseImpression; // En pages par minute
    private String resolution; // Résolution de l'imprimante en DPI (points par pouce)


}
