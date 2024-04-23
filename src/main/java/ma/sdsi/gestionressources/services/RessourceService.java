package ma.sdsi.gestionressources.services;

import java.util.List;
import java.util.Optional;

import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.repositories.AffectResouDeparteRepository;
import ma.sdsi.gestionressources.repositories.PropositionMaterielRepository;
import ma.sdsi.gestionressources.repositories.RessourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class RessourceService {

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    AffectResouDeparteRepository affectResouDeparteRepository;
    @Autowired
    PropositionMaterielRepository propositionMaterielRepository;
    @Transactional
    public Ressource updateRessource(Ressource ressource) {

        return ressourceRepository.save(ressource);
    }



    @Transactional
    public void deleteRessource(Long id) {
        // Supprimer les entrées de AffectResouDeparte liées à la ressource
        affectResouDeparteRepository.deleteByRessourceId(id);
        
        // Supprimer les entrées de PropositionMateriel liées à la ressource
        propositionMaterielRepository.deleteByRessourceId(id);
        
        // Ensuite, supprimer la ressource elle-même
        ressourceRepository.deleteById(id);
    }

    // Méthode pour marquer une ressource comme livrée
    public void marquerRessourceCommeLivrée(Long ressourceId) {
        Ressource ressource = ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
        ressource.setLivree(true);
        ressourceRepository.save(ressource);
    }

    // Méthode pour récupérer les ressources livrées
    public List<Ressource> recupererRessourcesLivrées() {
        return ressourceRepository.findByLivree(true);
    }

    // Méthode pour récupérer les ressources non livrées
    public List<Ressource> recupererRessourcesNonLivrées() {
        return ressourceRepository.findByLivree(false);
    }
 

    public void affecterNumInve(Ressource ressource, String inventoryNumber) {
        // Affecter le numéro d'inventaire à la ressource
        ressource.setNumeroInventaire(inventoryNumber);
        // Enregistrer la ressource mise à jour dans la base de données
        ressourceRepository.save(ressource);
    }


    public List<Ressource> getAllRessources() {
        return ressourceRepository.findAll();
    }


	public Optional<Ressource> findById(Long ressourceId) {
		return ressourceRepository.findById(ressourceId);
	}
}
