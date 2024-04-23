package ma.sdsi.gestionressources.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@IdClass(PropositionMaterielId.class)
public class PropositionMateriel {

	@Id
	@ManyToOne
	@JoinColumn(name = "ressource_id", referencedColumnName = "id")
	private Ressource ressource;
	@Id
	@ManyToOne
    @JoinColumn(name = "proposition_id",referencedColumnName = "id")
    private Proposition proposition;

    private int quantite;

    private double prixUnitaire;

}
