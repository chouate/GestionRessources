package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Demande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDebut;
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateReunion;

    private Boolean EnvoyerResponsable ;
    private Boolean old;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "chefDepartement_id", columnDefinition = "bigint default 1")
    private Enseignant enseignant;

    @OneToMany(mappedBy = "demande")
    private List<Ressource> ressourceList;

    @ManyToOne
    private AppelOffre appelOffre;





}
