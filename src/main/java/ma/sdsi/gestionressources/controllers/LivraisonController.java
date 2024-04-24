package ma.sdsi.gestionressources.controllers;

import java.util.List;
import java.util.Optional;

import ma.sdsi.gestionressources.entities.Departement;
import ma.sdsi.gestionressources.entities.Fournisseur;
import ma.sdsi.gestionressources.entities.PropositionMateriel;
import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



import org.springframework.ui.Model;

@Controller
public class LivraisonController {

		 @Autowired
		 private PropositionMaterielService propositionMaterielService;
		 @Autowired
		 FournisseurService fournisseurService;
		 @Autowired
		 RessourceService ressourceService;
		 @Autowired
		 DepartementService departementService;
		 @Autowired
		 AffectResouDeparteService affectResouDeparteService;
		    @GetMapping("/livraison")
		    public String afficherRessourcesStatut1(Model model) {
		        // Récupérer les ressources avec le statut 1 dans la proposition correspondante depuis le service
		    	List<PropositionMateriel> ressourcesStatut1 = propositionMaterielService.getPropositionMaterielWithStatusOne();
		    	Long id=ressourcesStatut1.get(0).getProposition().getFournisseur().getId();
		    	Fournisseur f=fournisseurService.findById(id);
		        
		        model.addAttribute("fournisseur", f);
		        // Ajouter les ressources au modèle pour les transmettre à la vue
		        model.addAttribute("propositionMateriels", ressourcesStatut1);
		      
		      for(int i=0; i<ressourcesStatut1.size();i++) {
		    	  String o= ressourcesStatut1.get(i).getProposition().getStatus();
		      }
		        // Renvoyer le nom de la vue Thymeleaf
		        return "livraison";
		    }
		    @PostMapping("/attribuerCodeBarre")
		    public String attribuerCodeBarre(@RequestParam("ressourceId") Long ressourceId, @RequestParam("codeBarre") String codeBarre) {
		    	System.out.println(ressourceId);
		        Ressource ressource = ressourceService.findById(ressourceId).orElseThrow();
		        ressourceService.affecterNumInve(ressource, codeBarre);
		        // Redirection vers la page de livraison après avoir attribué le code barre
		        return "redirect:/livraison";
		    }
		    @GetMapping("/ressourceLivree")
		    public String afficherRessourceslivree(Model model) {
		        // Récupérer les ressources avec le statut 1 dans la proposition correspondante depuis le service
		    	List<Ressource> ressources = ressourceService.recupererRessourcesLivrées();
		    	System.out.println("ressources livrees :"+ressources.size());
		    	for(Ressource r:ressources) {
		    		System.out.println("departement: " + r.getEnseignant().getChefDepartement().getDepartement());

		    	}
		    	List<Departement> departements = departementService.getAll();
		        // Ajouter les ressources au modèle pour les transmettre à la vue
		        model.addAttribute("ressources", ressources);
		        model.addAttribute("departements", departements);
		        return "ressourceLivree";
		    }
		    @GetMapping("/toutRessource")
		    public String afficherToutRessources(Model model) {

		    	List<Ressource> ressources =  ressourceService.getAllRessources();

		        model.addAttribute("ressources", ressources);
		    
		        return "toutRessource";
		    }
		    @PostMapping("/affecterRessDepart")
		    public String affecterRessDepart(@RequestParam(name = "ressourceIds", required = false) List<Long> ressourceIds,
		                                      @RequestParam(name = "departementId", required = false) Long departementId) {
		        // Vérifiez si les listes ne sont pas vides et effectuez les opérations nécessaires
		        if (ressourceIds != null && departementId != null) {
		            // Parcourez les listes de ressources et effectuez les opérations d'affectation
		            for (Long ressourceId : ressourceIds) {
		                Optional<Ressource> ressourceOptional = ressourceService.findById(ressourceId);
		                // Assurez-vous que la ressource existe
		                if (ressourceOptional.isPresent()) {
		                    Ressource ressource = ressourceOptional.get();
		                    // Vérifiez si le département de la ressource correspond à celui spécifié dans la requête
		                    if (ressource.getEnseignant() != null &&
		                        ressource.getEnseignant().getChefDepartement() != null &&
		                        ressource.getEnseignant().getChefDepartement().getDepartement() != null &&
		                        ressource.getEnseignant().getChefDepartement().getDepartement().getId().equals(departementId)) {
		                        // Effectuez l'affectation de la ressource au département
		                        Optional<Departement> departementOptional = departementService.getById(departementId);
		                        // Assurez-vous que le département existe
		                        if (departementOptional.isPresent()) {
		                            Departement departement = departementOptional.get();
		                            // Effectuez l'affectation de la ressource au département
		                            affectResouDeparteService.assignRessourceToDepartement(ressource, departement);
		                        }
		                    }
		                }
		            }
		        }
		        // Redirection vers la page de livraison
		        return "redirect:/affectations";
		    }





		    
}
