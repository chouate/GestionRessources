package ma.sdsi.gestionressources.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Proposition {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateLivraisonFuture;
	private int dureeGarantie;
	private String status;
	private double total;
	@ManyToOne
    @JoinColumn(name = "fournisseur_id") // Nom de la colonne de la clé étrangère
    private Fournisseur fournisseur;
	@ManyToOne
	private AppelOffre appelOffre;
	

}
