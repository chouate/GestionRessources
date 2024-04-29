package ma.sdsi.gestionressources.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Imprimante extends Ressource {
    /*
    @Min(1) // La vitesse minimale d'impression
    @Max(100) // La vitesse maximale d'impression
    private int vitesseImpression; // En pages par minute

     */

    @NotBlank // La résolution ne doit pas être vide
    @Size(max=1000) // La résolution ne doit pas excéder 10 caractères
    private String resolution; // Résolution de l'imprimante en DPI (points par pouce)

    @Min(1) // La vitesse minimale d'impression
    @Max(1000) // La vitesse maximale d'impression
    private String vitesse; // En pages par minute

}
