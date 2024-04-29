package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Ordinateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdinateurBesoinRepository extends JpaRepository<Ordinateur,Long> {

}
