package ma.sdsi.gestionressources.services;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import ma.sdsi.gestionressources.entities.Panne;
import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.repositories.PanneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PanneService {

    @Autowired
    private PanneRepository panneRepository;

    public List<Panne> getAllPannes() {
        return panneRepository.findAll();
    }

    public Panne getPanneById(Long id) {
        Optional<Panne> panneOptional = panneRepository.findById(id);
        return panneOptional.orElse(null);
    }

    public Panne saveOrUpdatePanne(Panne panne) {
        return panneRepository.save(panne);
    }

    public void deletePanne(Long id) {
        panneRepository.deleteById(id);
    }
    @Transactional
    public void remplirConstat(Long idPanne, Ressource idMachine, String explicationPanne, @NotNull Date dateApparition, String frequence, String ordre) {
        // Récupérer l'objet Panne correspondant à l'ID spécifié
        Optional<Panne> panneOptional = panneRepository.findById(idPanne);

        // Vérifier si l'objet Panne existe
        if (panneOptional.isPresent()) {
            Panne panne = panneOptional.get();
            panne.setRessource(idMachine);
            // Mettre à jour les champs avec les nouvelles valeurs

            panne.setExplicationPanne(explicationPanne);
            panne.setDateApparition(dateApparition);
            panne.setFrequence(frequence);
            panne.setOrdre(ordre);
            // Ajouter l'ID de la machine


            // Enregistrer la mise à jour dans la base de données
            panneRepository.save(panne);
        }
    }

}







