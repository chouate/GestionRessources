package ma.sdsi.gestionressources.repositories;
import java.util.List;

import ma.sdsi.gestionressources.entities.Proposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropositionRepository extends JpaRepository<Proposition, Long>{
	public List<Proposition> findAll() ;
}
