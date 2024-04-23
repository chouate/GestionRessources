package ma.sdsi.gestionressources.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Ordinateur extends Ressource {

    private String cpu;
    private int ram; // En Go
    private int disqueDur; // En Go
    private String ecran;


}
