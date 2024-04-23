package ma.sdsi.gestionressources.services;


import java.util.List;

import ma.sdsi.gestionressources.entities.Fournisseur;
import ma.sdsi.gestionressources.entities.Societe;
import ma.sdsi.gestionressources.repositories.FournisseurRepository;
import ma.sdsi.gestionressources.repositories.SociteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private SociteRepository societeRepository;
    public List<Fournisseur> all(){
    	return fournisseurRepository.findAll();
    }
    public Fournisseur findById(Long fournisseurId) {
        return fournisseurRepository.findById(fournisseurId).orElse(null);
    }
    public Societe saveSociete(Societe s) {
        return societeRepository.save(s);
    }
    public Fournisseur saveFournisseur(Fournisseur fournisseur) {
        Societe societe = fournisseur.getSociete();
        System.out.println("voila socite "+societe.getGerant());
        if (societe != null && societe.getSocieteId() != null) {
            societe = societeRepository.save(societe);
            fournisseur.setSociete(societe); 
            System.out.println("voila socite "+societe.getGerant());
        }
        fournisseurRepository.save(fournisseur);
        return fournisseurRepository.save(fournisseur);
    }
}

