package ma.sdsi.gestionressources.services;

import java.util.List;

import ma.sdsi.gestionressources.entities.AppelOffre;
import ma.sdsi.gestionressources.repositories.AppelOffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppelOffreService {

	@Autowired
    private AppelOffreRepository renRep;

    public List<AppelOffre> getAllTenders() {
        return renRep.findAll();
    }

    public AppelOffre createAppelOffre(AppelOffre appelOffre) {
        return renRep.save(appelOffre);
    }

}
