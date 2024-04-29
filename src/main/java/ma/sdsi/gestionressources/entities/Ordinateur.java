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
public class Ordinateur extends Ressource {

    @NotBlank(message = "Le CPU ne peut pas être vide")
    private String cpu;

    @Min(value = 1, message = "La RAM doit être au moins 1 Go")
    @Max(value = 128, message = "La RAM ne peut pas dépasser 128 Go")
    private int ram; // En Go

    @Min(value = 120, message = "Le disque dur doit être au moins 120 Go")
    @Max(value = 2048, message = "Le disque dur ne peut pas dépasser 2048 Go")
    private int disqueDur; // En Go

    @NotBlank(message = "L'écran ne peut pas être vide")
    @Size(min = 2, max = 100, message = "L'écran doit avoir entre 2 et 50 caractères")
    private String ecran;

}
