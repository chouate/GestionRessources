package ma.sdsi.gestionressources.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ma.sdsi.gestionressources.entities.Fournisseur;
import ma.sdsi.gestionressources.entities.ListeNoir;
import ma.sdsi.gestionressources.entities.Proposition;
import ma.sdsi.gestionressources.repositories.FournisseurRepository;
import ma.sdsi.gestionressources.repositories.ListeNoirRepository;
import ma.sdsi.gestionressources.repositories.PropositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class PropositionService {
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private PropositionRepository propositionRepository;
    @Autowired
    private ListeNoirRepository listeNoirRepository;

    // Méthode pour vérifier si le fournisseur existe déjà dans la liste noire
    public Optional<Proposition> getById(Long id){
        return propositionRepository.findById(id);
    }
    public boolean verifierFournisseurDansListeNoire(Long fournisseurId) {
        return listeNoirRepository.existsByFournisseur_Id(fournisseurId);
    }

    public void ajouterFournisseurListeNoire(Long fournisseurId, String motif) {

            ListeNoir listeNoir = new ListeNoir();
			Fournisseur fournisseur=new Fournisseur();
			fournisseur.setId(fournisseurId);
			listeNoir.setFournisseur(fournisseur);
            listeNoir.setRaison(motif); // Utiliser le motif fourni
            
            // Enregistrer l'entrée dans la liste noire
            listeNoirRepository.save(listeNoir);
       
    }

    public List<Proposition> getAllPropositions(Long id) {
        List<ListeNoir> listeNoir = this.getAllListeNoir();
        List<Proposition> liste = new ArrayList<>();
        List<Proposition> all = propositionRepository.findByAppelOffreId(id);
        
        for (Proposition p : all) {
            boolean isInListeNoir = false;
            for (ListeNoir ln : listeNoir) {
                if (ln.getFournisseur().getId().equals(p.getFournisseur().getId())) {
                    isInListeNoir = true;
                    break;
                }
            }
            if (!isInListeNoir) {
                liste.add(p);
            }
        }
        return liste;
    }

    public List<ListeNoir> getAllListeNoir() {
        return listeNoirRepository.findAll();
    }
    @Transactional
    public void eliminerFournisseurListeNoire(Long fournisseurId) {
        // Recherchez l'entrée correspondante dans la liste noire en fonction de l'ID du fournisseur
        listeNoirRepository.deleteByFournisseurId(fournisseurId);
    }
    public void modifierStatutProposition(Long propositionId,String s ) {
    	if(s.equals("1")) {
        // Récupérer la proposition par son ID
        Proposition proposition = propositionRepository.findById(propositionId)
                .orElseThrow(() -> new RuntimeException("Proposition non trouvée"));

        // Modifier le statut de la proposition sélectionnée à "1"
        proposition.setStatus("1"); // Mettre à jour le statut à 1

        // Enregistrer la modification dans la base de données
        propositionRepository.save(proposition);

        // Mettre à jour le statut des autres propositions à "0"
        List<Proposition> autresPropositions = propositionRepository.findAll();
        for (Proposition autreProposition : autresPropositions) {
            if (!autreProposition.getId().equals(propositionId)) {
                autreProposition.setStatus("0");
                propositionRepository.save(autreProposition);
            }
        }}
    	if(s.equals("0")){
    		 Proposition proposition = propositionRepository.findById(propositionId)
    	                .orElseThrow(() -> new RuntimeException("Proposition non trouvée"));

    	        // Modifier le statut de la proposition sélectionnée à "1"
    	        proposition.setStatus("0");
    	        // Enregistrer la modification dans la base de données
    	        propositionRepository.save(proposition);
    	}
    }
    public Proposition save(Proposition p){
        propositionRepository.save(p);
        return p;
    }




}
