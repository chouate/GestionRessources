package ma.sdsi.gestionressources.controllers;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ma.sdsi.gestionressources.entities.*;
import ma.sdsi.gestionressources.services.AffectResouDeparteService;
import ma.sdsi.gestionressources.services.DepartementService;
import ma.sdsi.gestionressources.services.RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RessourceController {
    @Autowired
    DepartementService departementService;
    @Autowired
    private RessourceService ressourceService;
    @Autowired
    AffectResouDeparteService affectResouDeparteService;
    

    // Méthode pour afficher le formulaire d'édition d'une ressource
    @GetMapping("/detailsRessource/{id}")
    public String afficherFormulaireEdition(@PathVariable("id") Long id, Model model) {
    	System.out.println(id);
        Optional<Ressource> optionalRessource = ressourceService.findById(id);
        Ressource ressource = optionalRessource.orElse(null);
        model.addAttribute("ressource", ressource);
        return "editRessource";
    }
    @PostMapping("/editerOrdinateur")
    public String editerOrdinateur(@ModelAttribute("ressource") Ordinateur ressource) {
        if (ressource instanceof Ordinateur) {
           System.out.println("ordinateur");
        } 

        ressourceService.updateRessource(ressource);
        return "redirect:/toutRessource";
    }
    @PostMapping("/editerImprimante")
    public String editerImprimante(@ModelAttribute("ressource") Imprimante ressource) {
        if (ressource instanceof Imprimante) {
           System.out.println("ordinateur");
        } 

        ressourceService.updateRessource(ressource);
        return "redirect:/toutRessource";
    }



    // Méthode pour supprimer une ressource
    @GetMapping("/deleteRessource/{id}")
    public String supprimerRessource(@PathVariable("id") Long id) {
        ressourceService.deleteRessource(id);
        return "redirect:/toutRessource";
    }
    @GetMapping("/affectations")
    public String affectations(Model model) {
        List<AffectationRessouDepar> listeAffectations = affectResouDeparteService.getAll();
        Map<Departement, List<Ressource>> listeAffectationsMap = new HashMap<>();

        // Parcours de la liste des affectations
        for (AffectationRessouDepar affectation : listeAffectations) {
            Departement departement = affectation.getDepartement();
            Ressource ressource = affectation.getRessource();

            // Vérifier si le département existe déjà dans la map
            if (!listeAffectationsMap.containsKey(departement)) {
                // Si le département n'existe pas, ajouter une nouvelle entrée dans la map
                listeAffectationsMap.put(departement, new ArrayList<>());
            }

            // Ajouter la ressource à la liste des ressources affectées pour ce département
            listeAffectationsMap.get(departement).add(ressource);
        }
        List<Ressource> ressources = ressourceService.recupererRessourcesLivrées();
    	List<Departement> departements = departementService.getAll();
        model.addAttribute("ressources", ressources);
        model.addAttribute("departements", departements);
        model.addAttribute("listeAffectations", listeAffectationsMap);
        return "affectations";
    }

    @PostMapping("/modifierAffectation")
    public String modifierAffectation(@RequestParam(name = "ressourceIds", required = false) List<String> ids) {
    	affectResouDeparteService.deleteAll();
               for(int i=0;i<ids.size();i++) {
            	   String[] s=ids.get(i).split("_");
            	   Long idRessource=Long.parseLong(s[0]);
            	   Long idDep=Long.parseLong(s[1]);
            	   System.out.println(idRessource+"    "+idDep);
            	   Departement d=new Departement();
            	   d.setId(idDep);
            	   Ressource r=new Ressource();
            	   r.setId(idRessource);
            	   affectResouDeparteService.assignRessourceToDepartement(r, d);
               }

        // Après avoir traité les données, vous pouvez rediriger l'utilisateur vers une autre page
        return "redirect:/responsable"; // Redirige vers la page d'accueil, mais vous pouvez changer l'URL selon vos besoins
    }



}

