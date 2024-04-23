package ma.sdsi.gestionressources.repositories;

import ma.sdsi.gestionressources.entities.ListeNoir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ListeNoirRepository extends JpaRepository<ListeNoir, Long> {
    // Vérifier si un fournisseur existe déjà dans la liste noire en utilisant son ID
    boolean existsByFournisseur_Id(Long fournisseurId);
    ListeNoir findByFournisseurId(Long fournisseurId);
	void deleteByFournisseurId(Long fournisseurId);
}
