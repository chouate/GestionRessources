package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
@Table(name = "panne") // Spécification explicite du nom de la table
@Data @AllArgsConstructor @NoArgsConstructor
public class Panne {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long idMachine;

    @NotBlank(message = "La description ne peut pas être vide")
    private  String description;

    private Boolean old;

    @ManyToOne
    private Enseignant enseignant;

//    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateApparition;

//    @NotNull
    //@Enumerated(EnumType.STRING)
    private String frequence;

//    @NotNull
    //@Enumerated(EnumType.STRING)
    private String ordre;
//    @NotNull
    @ManyToOne
    private Ressource ressource;

    @ManyToOne
    private Technicien technicien;

    //@Enumerated(EnumType.STRING)
    private String decisonResponsable; // Ajout: Action prise par le responsable

    @Column(length = 1000) // Définir une longueur pour le champ explicationPanne
    private String explicationPanne; // Ajout du champ explicationPanne


//    @Override
//    public String toString() {
//        return "ConstatPanne{" +
//                "id=" + id +
//                ", dateApparition=" + dateApparition +
//                ", frequence=" + frequence +
//                ", ordre=" + ordre +
//                ", decisonResonsable=" + decisonResponsable +
//                ", explicationPanne='" + explicationPanne + '\'' +
//                '}';
//    }


}
