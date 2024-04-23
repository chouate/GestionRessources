package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande,Long> {

    //Page<Demande> findByChefDepartementId(Long chefDepartementId, Pageable pageable);
    Page<Demande> findByEnseignantId(Long enseignantId, Pageable pageable);
}
