package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface SociteRepository extends JpaRepository<Societe, Long> {

}
