package ma.sdsi.gestionressources.repositories;


import java.util.List;

import ma.sdsi.gestionressources.entities.Enseignant;
import ma.sdsi.gestionressources.entities.Fournisseur;
import ma.sdsi.gestionressources.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long>{
	
	public List<Fournisseur> findAll() ;

	Fournisseur findByUser(User user);
}
