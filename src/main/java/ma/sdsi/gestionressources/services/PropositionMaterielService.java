package ma.sdsi.gestionressources.services;

import java.util.List;

import ma.sdsi.gestionressources.entities.Proposition;
import ma.sdsi.gestionressources.entities.PropositionMateriel;
import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.repositories.PropositionMaterielRepository;
import ma.sdsi.gestionressources.repositories.PropositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;


@Service
public class PropositionMaterielService {
    
    @Autowired
    private PropositionMaterielRepository propositionMaterielRepository;
   public List<PropositionMateriel> findRessources(){
        return propositionMaterielRepository.findAll();
    }
    public List<PropositionMateriel> getPropositionMaterielWithStatusOne() {
        List<PropositionMateriel> allPropositions = propositionMaterielRepository.findAll();
        System.out.println("voila size "+allPropositions.size());
        List<PropositionMateriel> propositionsWithStatusOne = new ArrayList<>();

        // Parcourir toutes les propositions
        for (PropositionMateriel proposition : allPropositions) {
            // Vérifier si le statut de la proposition est "1"
            if (proposition.getProposition().getStatus().equals("1")) {
                // Ajouter la proposition à la liste des propositions avec statut "1"
                propositionsWithStatusOne.add(proposition);
            }
        }

        // Retourner la liste des propositions avec statut "1"
        return propositionsWithStatusOne;
    }
    @Autowired
    private PropositionRepository propositionRepository; // Supposons que vous ayez également un repository pour les propositions

    // Méthode pour enregistrer ou mettre à jour une proposition matérielle
    public PropositionMateriel saveOrUpdate(PropositionMateriel propositionMateriel) {
        PropositionMateriel savedPropositionMateriel = propositionMaterielRepository.save(propositionMateriel);
        // Mettre à jour le total de la proposition correspondante
        updatePropositionTotal(savedPropositionMateriel.getProposition().getId());
        return savedPropositionMateriel;
    }

    // Méthode pour mettre à jour le total d'une proposition
    private void updatePropositionTotal(Long propositionId) {
        Proposition proposition = propositionRepository.findById(propositionId).orElse(null);
        if (proposition != null) {
            double newTotal = calculateTotal(propositionId);
            proposition.setTotal(newTotal);
            propositionRepository.save(proposition);
        }
    }

    // Méthode pour calculer le total des propositions de matériel associées à une proposition
    private double calculateTotal(Long propositionId) {
        double total = 0.0;
        List<PropositionMateriel> propositionMateriels = propositionMaterielRepository.findByPropositionId(propositionId);
        if (propositionMateriels != null) {
            for (PropositionMateriel propositionMateriel : propositionMateriels) {
                total += propositionMateriel.getPrixUnitaire(); // Supposons que vous ayez une méthode getPrix() dans la classe PropositionMateriel
            }
        }
        return total;
    }
}
