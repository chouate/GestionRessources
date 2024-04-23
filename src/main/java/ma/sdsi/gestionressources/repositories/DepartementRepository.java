package ma.sdsi.gestionressources.repositories;
import java.util.List;

import ma.sdsi.gestionressources.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
	public List<Departement> findAll();
}
