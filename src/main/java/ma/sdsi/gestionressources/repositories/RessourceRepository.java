package ma.sdsi.gestionressources.repositories;

import java.util.List;


import ma.sdsi.gestionressources.entities.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import jakarta.transaction.Transactional;

@Repository
public interface RessourceRepository extends  JpaRepository<Ressource,Long> {
    List<Ressource> findByLivree(boolean livree);
    List<Ressource> findAll();
    List<Ressource> findByEnseignantId(Long enseignantId); // Renvoie les ressources
    List<Ressource> findByEnseignantIdAndDemandeId(Long enseignantId, Long demandeId);
}
