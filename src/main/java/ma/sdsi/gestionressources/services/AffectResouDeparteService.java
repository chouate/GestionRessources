package ma.sdsi.gestionressources.services;

import java.util.List;

import ma.sdsi.gestionressources.entities.AffectationRessouDepar;
import ma.sdsi.gestionressources.entities.Departement;
import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.repositories.AffectResouDeparteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class AffectResouDeparteService {

	@Autowired
    private AffectResouDeparteRepository affectResouDeparteRepository;
	@Transactional
    public void assignRessourceToDepartement(Ressource ressource, Departement departement) {
    	AffectationRessouDepar affectationRessource = new AffectationRessouDepar();
    	affectationRessource.setPersonne(ressource.getEnseignant().getChefDepartement().getNom()+" "+ressource.getEnseignant().getChefDepartement().getPrenom());
        affectationRessource.setRessource(ressource);
        affectationRessource.setDepartement(departement);

        affectResouDeparteRepository.save(affectationRessource);
    }
    public List<AffectationRessouDepar> getAll(){
    	return affectResouDeparteRepository.findAll();
    }
    public void deleteAll() {
    	affectResouDeparteRepository.deleteAll();
    }

}
