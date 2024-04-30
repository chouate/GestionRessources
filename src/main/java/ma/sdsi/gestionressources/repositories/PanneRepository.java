package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Imprimante;
import ma.sdsi.gestionressources.entities.Panne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanneRepository extends JpaRepository<Panne,Long> {

}
