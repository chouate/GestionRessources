package ma.sdsi.gestionressources.controllers;
import ma.sdsi.gestionressources.entities.AppelOffre;
import ma.sdsi.gestionressources.entities.Demande;
import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.services.AppelOffreService;
import ma.sdsi.gestionressources.services.DemandeService;
import ma.sdsi.gestionressources.services.RessourceService;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AppelOffreController {

	@Autowired
    private AppelOffreService appelOffreService;
	@Autowired
    private RessourceService ressourceService;
	@GetMapping("/responsable")
    public String home() {
 
        return "redirect:/propositions"; // Retournez le nom de la vue
    }
	@GetMapping("/appelOffres")
    public String appelOffres(Model model) {
        List<Ressource> ressources = (List<Ressource>) ressourceService.getAllRessources(); // Obtenez tous les besoins depuis le service
        model.addAttribute("ressources", ressources); // Ajoutez les besoins à l'attribut du modèle
        return "appelOffre"; // Retournez le nom de la vue
    }
	@Autowired
	private DemandeService demandeService;
@PostMapping("/createAppelOffre")
public String createApelOffre(@ModelAttribute("appelOffre") AppelOffre appelOffre, @RequestParam("demandeIds") String demandeIds) {
//	    // Traitement pour convertir la chaîne de départementIds en une liste d'IDs de département
         List<Long> ids = Arrays.stream(demandeIds.split(","))
	                           .map(Long::parseLong)
                           .collect(Collectors.toList());
	    System.out.println(ids.size());
//	    // Appel de la méthode pour créer l'appel d'offre dans votre service
	    appelOffreService.createAppelOffre(appelOffre);
//
//	    // Récupération des demandes liées aux IDs de département
	    List<Demande> demandes = demandeService.getDemandesByIds(ids);
	    System.out.println(demandes.size());
//	    // Ajout de l'ID de l'appel d'offre aux demandes
	    for (Demande demande : demandes) {
	        demande.setAppelOffre(appelOffre);
//	        // Enregistrer les modifications dans votre service de demande
	        demandeService.updateDemande(demande);
	    }
//
//	    // Redirection vers une page appropriée après la création de l'appel d'offre
	    return "redirect:/appelOffres";
	}



}
