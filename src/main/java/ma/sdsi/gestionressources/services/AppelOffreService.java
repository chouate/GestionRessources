package ma.sdsi.gestionressources.services;

import java.util.List;
import java.util.Optional;

import ma.sdsi.gestionressources.entities.AppelOffre;
import ma.sdsi.gestionressources.entities.Proposition;
import ma.sdsi.gestionressources.repositories.AppelOffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppelOffreService {

	@Autowired
    private AppelOffreRepository renRep;
    public void modifierStatutsApplOffre(Proposition pro, Boolean old) {
        AppelOffre appelOffre = pro.getAppelOffre();
        if (appelOffre != null) {
            appelOffre.setOld(old);
            System.out.println("apple offre "+appelOffre.getId()+" le old"+old+"     "+appelOffre.getOld());
            renRep.save(appelOffre);

        }
    }
    public List<AppelOffre> findAll(){

        return renRep.findAll();
    }
    public Optional<AppelOffre> findById(Long id){

        return renRep.findById(id);
    }


    public List<AppelOffre> getAllTenders() {
        return renRep.findAll();
    }

    public AppelOffre createAppelOffre(AppelOffre appelOffre) {
        appelOffre.setOld(false);
        return renRep.save(appelOffre);
    }

}
