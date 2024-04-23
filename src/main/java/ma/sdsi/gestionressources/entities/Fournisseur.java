package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String location;//adresse

    private String contactPerson;

    private String contactEmail;
    @ManyToOne
    @JoinColumn(name = "societe_societeId")
    private Societe societe;
}
