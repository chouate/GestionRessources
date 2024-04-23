package ma.sdsi.gestionressources.repositories;

import java.util.List;

import ma.sdsi.gestionressources.entities.AffectationRessouDepar;
import ma.sdsi.gestionressources.entities.AffectationRessouDeparId;
import ma.sdsi.gestionressources.entities.Departement;
import ma.sdsi.gestionressources.entities.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AffectResouDeparteRepository extends JpaRepository<AffectationRessouDepar, AffectationRessouDeparId> {
    List<AffectationRessouDepar> findByRessource(Ressource ressource);
    List<AffectationRessouDepar> findByDepartement(Departement departement);
	void deleteByRessourceId(Long id);
	List<AffectationRessouDepar> findAll();

}


