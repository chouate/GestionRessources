package ma.sdsi.gestionressources.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.sdsi.gestionressources.entities.Fournisseur;
import ma.sdsi.gestionressources.entities.ListeNoir;
import ma.sdsi.gestionressources.entities.Proposition;
import ma.sdsi.gestionressources.services.FournisseurService;
import ma.sdsi.gestionressources.services.PropositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PropositionController {
	@Autowired
	private FournisseurService fournisseurService;
	@Autowired
    private PropositionService propositionService;
	@GetMapping("/propositions")
	public String home(Model model) {
	    List<Proposition> propositions = propositionService.getAllPropositions(); // Obtenez toutes les propositions depuis le service
	    
	    // Triez les propositions par total
	    Collections.sort(propositions, Comparator.comparingDouble(Proposition::getTotal));
	    System.out.println("size of propositions"+propositions.size());
	    
	    model.addAttribute("propositions", propositions); // Ajoutez les propositions triées à l'attribut du modèle
	    return "propositions"; // Retournez le nom de la vue
	}
	@PostMapping("/selectionnerProposition")
	public String selectionnerProposition(@RequestParam("propositionId") Long propositionId, Model model) {
		// Modifier le statut de la proposition à 1
	    propositionService.modifierStatutProposition(propositionId,"1");
	    
	    return "redirect:/propositions";
	}
	@PostMapping("/deselectionnerProposition")
	public String deselectionnerProposition(@RequestParam("propositionId") Long propositionId, Model model) {
		// Modifier le statut de la proposition à 1
	    propositionService.modifierStatutProposition(propositionId,"0");
	    
	    return "redirect:/propositions";
	}

	@PostMapping("/ajouterListNoir")
	public String ajouterListNoir(@RequestParam("fournisseurId") Long fournisseurId, @RequestParam("motif") String motif) {

	        propositionService.ajouterFournisseurListeNoire(fournisseurId, motif);

	        return "redirect:/listeNoir";
	    
	}
	@GetMapping("/listeNoir")
	public String listeNoir(Model model) {
	    List<Fournisseur> touFourni = fournisseurService.all();
	    List<ListeNoir> listeNoir = propositionService.getAllListeNoir();
	    Map<Fournisseur, Boolean> map = new HashMap<>();

	    // Remplir la Map en parcourant chaque fournisseur
	    for (Fournisseur fournisseur : touFourni) {
	        boolean surListeNoir = false; // Initialisez à false par défaut
	        // Vérifiez si le fournisseur est sur la liste noire
	        for (ListeNoir ln : listeNoir) {
	            if (ln.getFournisseur().getId().equals(fournisseur.getId())) {
	                surListeNoir = true;
	                break; // Pas besoin de continuer à parcourir la liste noire
	            }
	        }
	        // Ajoutez le fournisseur à la Map avec son statut sur la liste noire
	        map.put(fournisseur, surListeNoir);
	    }

	    // Ajoutez la Map à l'attribut du modèle
	    model.addAttribute("listeNoir", map);
	    // Retournez le nom de la vue
	    return "listeNoir";
	}

	   @PostMapping("/eliminerListNoir")
	   public String eliminerlisteNoir(@RequestParam("fournisseurId") Long fournisseurId) {
	       // Appelez le service pour supprimer le fournisseur de la liste noire
		   System.out.println(fournisseurId);
	       propositionService.eliminerFournisseurListeNoire(fournisseurId);
	       
	       // Redirigez l'utilisateur vers la page de la liste noire après la suppression
	       return "redirect:/listeNoir";
	   }


}
