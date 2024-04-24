package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinMaterielRepository extends JpaRepository<Ressource,Long> {

    //Page<Demande> findByChefDepartementId(Long chefDepartementId, Pageable pageable);
    //Page<Demande> findByEnseignantId(Long besoinMaterielId, Pageable pageable);
    int countByEnseignantIdAndDemandeId(Long enseignantId, Long demandeId);
}
