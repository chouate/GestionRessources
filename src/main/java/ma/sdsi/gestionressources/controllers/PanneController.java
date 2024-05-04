package ma.sdsi.gestionressources.controllers;

import jakarta.validation.constraints.NotNull;
import ma.sdsi.gestionressources.entities.Panne;
import ma.sdsi.gestionressources.entities.Ressource;
import ma.sdsi.gestionressources.repositories.RessourceRepository;
import ma.sdsi.gestionressources.services.PanneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class PanneController {

    @Autowired
    private PanneService panneService;


    @Autowired
    private RessourceRepository ressourceRepository; // Injectez le repository de la ressource

    @GetMapping("/remplir-constat")
    public String afficherPageRemplirConstat(Model model) {
        List<Panne> pannes = panneService.getAllPannes();
        model.addAttribute("pannes", pannes);
        return "panne2";
    }

    @PostMapping("/enregistrer-constats")
    public String enregistrerConstatPanne(@RequestParam Long idPanne,
                                          @RequestParam Long ressource,
                                          @RequestParam String explicationPanne,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull Date dateApparition,
                                          @RequestParam String frequence,
                                          @RequestParam String ordre) {
        // Votre code de traitement ici
        Ressource ressource1 = ressourceRepository.findById(ressource).orElse(null);
        panneService.remplirConstat(idPanne, ressource1, explicationPanne, dateApparition, frequence, ordre);
        // Rediriger vers la page de liste des pannes
        return "redirect:/remplir-constat";
    }



    @GetMapping("/formPannes/{id}")
    public String formPannes(@PathVariable Long id, Model model) {
        // Utilisez l'ID pour récupérer la panne correspondante
        Panne panne = panneService.getPanneById(id);
        // Ajoutez la panne au modèle
        model.addAttribute("panne", panne);
        return "formPannes";
    }

}
