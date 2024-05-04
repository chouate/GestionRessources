package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.*;


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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id") // Indique que la clé étrangère se trouve dans `Enseignant`
    private User user; // Relation OneToOne avec `User`
}
