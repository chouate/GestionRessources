package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.Demande;
import ma.sdsi.gestionressources.entities.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant,Long> {
    List<Enseignant> findByDemandesContains(Demande demande);
    @Query("SELECT DISTINCT r.enseignant FROM Ressource r WHERE r.demande.id = :demandeId")
    List<Enseignant> findEnseignantsByDemandeId(@Param("demandeId") Long demandeId);

    @Query("SELECT DISTINCT r.enseignant FROM Ressource r "
            + "WHERE r.demande.id = :demandeId "
            + "AND r.enseignant != r.demande.enseignant")
    List<Enseignant> findEnseignantsByDemandeIdExcludingChef(@Param("demandeId") Long demandeId);
}
