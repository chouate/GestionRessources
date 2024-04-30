package ma.sdsi.gestionressources;

import ma.sdsi.gestionressources.entities.Demande;
import ma.sdsi.gestionressources.entities.Enseignant;
import ma.sdsi.gestionressources.repositories.DemandeRepository;
import ma.sdsi.gestionressources.repositories.EnseignantRepository;
import ma.sdsi.gestionressources.web.DemandeController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Optional;

@SpringBootApplication
public class GestionRessourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionRessourcesApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(DemandeRepository demandeRepository, EnseignantRepository enseignantRepository){
        return  args -> {


            Enseignant enseignant1 = new Enseignant() ;
            enseignant1.setPrenom("mehdi");
            enseignant1.setNom("echeouti");
            enseignantRepository.save(enseignant1);
            Enseignant enseignant2 = new Enseignant() ;
            enseignant2.setPrenom("hanan");
            enseignant2.setNom("hanan");
            enseignantRepository.save(enseignant2);
            Enseignant enseignant3 = new Enseignant() ;
            enseignant3.setPrenom("hajar");
            enseignant3.setNom("OZTIT");
            enseignant3.setChefDepartement(enseignant1);
            enseignantRepository.save(enseignant3);
            Enseignant enseignant4 = new Enseignant() ;
            enseignant4.setPrenom("hatim");
            enseignant4.setNom("el amarti");
            enseignant4.setChefDepartement(enseignant1);
            enseignantRepository.save(enseignant4);

            demandeRepository.save(
                     new Demande(null,new Date(),new Date(),new Date(),false,false,enseignant1,null,null)
            );
            demandeRepository.save(
                    new Demande(null,new Date(),new Date(),new Date(),false,false,enseignant1,null,null)
            );
            demandeRepository.save(
                    new Demande(null,new Date(),new Date(),new Date(),false,false,enseignant2,null,null)
            );
            demandeRepository.save(
                    new Demande(null,new Date(),new Date(),new Date(),false,false,enseignant2,null,null)
            );

            demandeRepository.findAll().forEach(demande -> {
                System.out.println(demande.getId());
                System.out.println(demande.getDateDebut());
                System.out.println(demande.getDateFin());
                System.out.println(demande.getDateReunion());
            });

            // Création de nouveaux enseignants





        };
    }

}
