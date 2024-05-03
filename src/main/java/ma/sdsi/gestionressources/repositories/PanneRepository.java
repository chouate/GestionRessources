package ma.sdsi.gestionressources.repositories;


import ma.sdsi.gestionressources.entities.Panne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanneRepository extends JpaRepository<Panne, Long> {
    List<Panne> findByDescription(String description);
}
