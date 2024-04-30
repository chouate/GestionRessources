package ma.sdsi.gestionressources.repositories;

import java.util.List;

import ma.sdsi.gestionressources.entities.PropositionMateriel;
import ma.sdsi.gestionressources.entities.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropositionMaterielRepository extends JpaRepository<PropositionMateriel, Long> {

	List<PropositionMateriel> findAll();
    // Méthodes personnalisées si nécessaire

	List<PropositionMateriel> findByPropositionId(Long propositionId);

	void deleteByRessourceId(Long id);

}

