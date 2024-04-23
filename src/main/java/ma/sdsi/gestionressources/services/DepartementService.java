package ma.sdsi.gestionressources.services;

import java.util.List;
import java.util.Optional;

import ma.sdsi.gestionressources.entities.Departement;
import ma.sdsi.gestionressources.repositories.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartementService {
	@Autowired
    private DepartementRepository departementRepository;
	public List<Departement> getAll(){
		return departementRepository.findAll();
	}
	public Optional<Departement> getById(Long id) {
		return departementRepository.findById(id);
	}
}
