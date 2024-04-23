package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Imprimante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImprimanteBesoinRepository extends JpaRepository<Imprimante,Long> {

}
