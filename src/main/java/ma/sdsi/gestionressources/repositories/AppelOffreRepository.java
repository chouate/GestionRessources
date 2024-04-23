package ma.sdsi.gestionressources.repositories;

import java.util.List;

import ma.sdsi.gestionressources.entities.AppelOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppelOffreRepository extends JpaRepository<AppelOffre, Long> {

	public List<AppelOffre> findAll() ;

	public AppelOffre save(AppelOffre tender);

	
}
