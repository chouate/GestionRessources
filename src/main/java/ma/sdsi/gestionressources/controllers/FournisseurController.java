package ma.sdsi.gestionressources.controllers;

import ma.sdsi.gestionressources.entities.Fournisseur;
import ma.sdsi.gestionressources.services.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class FournisseurController {
	@Autowired
	FournisseurService fournisseurService;
	
	@PostMapping("/editerFournisseur")
	public String editerFournisseur(@ModelAttribute("fournisseur") Fournisseur fournisseur) {
		System.out.println(fournisseur.getSociete());
		fournisseurService.saveSociete(fournisseur.getSociete());
	    fournisseurService.saveFournisseur(fournisseur);
	    return "redirect:/responsable"; // Redirige vers la page d'accueil ou une autre vue appropriée
	}

}

