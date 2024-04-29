package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AppelOffre {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private  Boolean old;

    @OneToMany(mappedBy = "appelOffre")
    private List<Demande> demandeList;
    @OneToMany(mappedBy = "appelOffre")
    private List<Proposition> propositionList;

    @ManyToOne
    private Responsable responsable;
    @Override
    public String toString() {
        return "AppelOffre{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", old=" + old +
                '}';
    }

}
