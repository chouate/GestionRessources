package ma.sdsi.gestionressources.repositories;


import java.util.List;

import ma.sdsi.gestionressources.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long>{
	
	public List<Fournisseur> findAll() ;
}
