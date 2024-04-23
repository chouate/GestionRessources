package ma.sdsi.gestionressources.services;

import java.util.List;

import ma.sdsi.gestionressources.entities.Demande;
import ma.sdsi.gestionressources.repositories.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DemandeService {

   @Autowired
    private DemandeRepository demandeRepository;
//
    public Demande createRequirement(Demande requirement) {
        return demandeRepository.save(requirement);
    }
//
    public List<Demande> getAllBesoins() {
        return demandeRepository.findAll();
    }
//
    public List<Demande> getDemandesByIds(List<Long> demandeIds) {
        return demandeRepository.findAllById(demandeIds);
    }
//
//
    public Demande updateDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

 
}
