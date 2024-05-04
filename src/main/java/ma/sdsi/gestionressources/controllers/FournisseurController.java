package ma.sdsi.gestionressources.controllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.sdsi.gestionressources.repositories.FournisseurRepository;
import ma.sdsi.gestionressources.repositories.UserRepository;
import ma.sdsi.gestionressources.services.*;
import org.springframework.format.annotation.DateTimeFormat;

import ma.sdsi.gestionressources.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
public class FournisseurController {
	@Autowired
	PropositionService propositionService;
	@Autowired
	FournisseurService fournisseurService;
	@Autowired
	AppelOffreService appelOffreService;
	@Autowired
	RessourceService ressourceService;
	@Autowired
	PropositionMaterielService propositionMaterielService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FournisseurRepository fournisseurRepository;

	private Fournisseur getFournisseurFromSession() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getName() != null) {
			String username = authentication.getName();
			User user = userRepository.findByEmail(username); // Trouver l'utilisateur par email
			return fournisseurRepository.findByUser(user); // Trouver le fournisseur par utilisateur
		}
		return null;
	}
	@PostMapping("/editerFournisseur")
	public String editerFournisseur(@ModelAttribute("fournisseur") Fournisseur fournisseur) {
		System.out.println(fournisseur.getSociete());
		fournisseurService.saveSociete(fournisseur.getSociete());
	    fournisseurService.saveFournisseur(fournisseur);
	    return "redirect:/responsable"; // Redirige vers la page d'accueil ou une autre vue appropriée
	}
	@GetMapping("/toutAppelOffres")
	public String getTout(Model model, HttpServletRequest request){
		Long id = 1L;
		Fournisseur fournisseur = getFournisseurFromSession();
		// Définir la variable de session avec l'ID généré
		HttpSession session = request.getSession();
		session.setAttribute("sessionId", fournisseur.getId());
		List<AppelOffre> liste = appelOffreService.findAll();
		List<AppelOffre> appelOffres = new ArrayList<>();

		for(AppelOffre a : liste) {
			if(!a.getOld()) {
				appelOffres.add(a);
			}
		}

		model.addAttribute("appelOffres", appelOffres);
		return "toutAppelOffres";
	}
	@GetMapping("/consulterRessources")
	public String appelOffre(Model model, @RequestParam("AppelOffreId") Long appelOffreId, HttpServletRequest request) {
		Optional<AppelOffre> ap = appelOffreService.findById(appelOffreId);
		List<Demande> listed = ap.get().getDemandeList();
		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("sessionId");
		Set<Ressource> ressources = new HashSet<>(); // Utilisation d'un ensemble au lieu d'une liste
		List<PropositionMateriel> ressPro = propositionMaterielService.findRessources();

		for (Demande d : listed) {
			List<Ressource> r = d.getRessourceList();
			for (Ressource rr : r) {
				boolean found = false;
				// Vérifier si la ressource est déjà incluse dans les propositions de matériel
				for (PropositionMateriel pm : ressPro) {
					if (rr.getId().equals(pm.getRessource().getId()) && pm.getProposition() != null
							&& pm.getProposition().getFournisseur() != null
							&& pm.getProposition().getFournisseur().getId().equals(userId)) {
						found = true;
						break;
					}
				}
				// Si la ressource n'est pas trouvée dans les propositions de matériel, l'ajouter à l'ensemble
				if (!found) {
					ressources.add(rr);
				}
			}
		}

		// Conversion de l'ensemble de ressources en ArrayList
		List<Ressource> ressourcesList = new ArrayList<>(ressources);

		model.addAttribute("appelOffre", ap);
		model.addAttribute("ressources", ressourcesList);

		return "toutRessourcesApplOffre";
	}


	@PostMapping("/proposer")
	public String proposer(@RequestParam(name = "ressourceIds") List<Long> ressourceIds,
						   @RequestParam(name = "prixUnitaire") List<Double> listePrix,
						   @RequestParam(name = "dureeGarantie") int dureeGarantie,
						   @RequestParam(name = "dateLivraisonFuture") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateLivraisonFuture,
						   @RequestParam(name = "appelOffre") Long appelOffre,
						   HttpServletRequest request
						   ) {
		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("sessionId");
		Fournisseur f=fournisseurService.findById(userId);
        Proposition proposition =new Proposition();
		proposition.setDureeGarantie(dureeGarantie);
		proposition.setDateLivraisonFuture(dateLivraisonFuture);
		Optional<AppelOffre> ap=appelOffreService.findById(appelOffre);
        proposition.setAppelOffre(ap.get());
		proposition.setFournisseur(f);
		Double total =0.0;
		for (Double d:listePrix){
			total=total+d;
		}
		proposition.setTotal(total);
		proposition.setStatus("0");
		Proposition pro=propositionService.save(proposition);
		for (int i=0;i<ressourceIds.size();i++){
			PropositionMateriel propositionMateriel=new PropositionMateriel();
			propositionMateriel.setProposition(pro);
			Optional<Ressource> ress=ressourceService.findById(ressourceIds.get(i));
			Double prix=listePrix.get(i);
			propositionMateriel.setRessource(ress.get());
			propositionMateriel.setPrixUnitaire(prix);
			propositionMaterielService.saveOrUpdate(propositionMateriel);

		}
		return "toutRessourcesApplOffre";
	}


}

