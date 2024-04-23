package ma.sdsi.gestionressources.web;


import jakarta.validation.Valid;
import ma.sdsi.gestionressources.entities.*;
import ma.sdsi.gestionressources.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BesoinMaterielController {
    private DemandeRepository demandeRepository;
    private EnseignantRepository enseignantRepository;
    private BesoinMaterielRepository besoinMaterielRepository;
    private ImprimanteBesoinRepository imprimanteBesoinRepository;
    private RessourceRepository ressourceRepository;

    @Autowired
    public BesoinMaterielController(BesoinMaterielRepository besoinMaterielRepository, EnseignantRepository enseignantRepository,
                                    DemandeRepository demandeRepository,
                                    ImprimanteBesoinRepository imprimanteBesoinRepository,
                                    RessourceRepository ressourceRepository) {
        this.besoinMaterielRepository = besoinMaterielRepository;
        this.enseignantRepository =enseignantRepository;
        this.demandeRepository = demandeRepository;
        this.imprimanteBesoinRepository=imprimanteBesoinRepository;
        this.ressourceRepository = ressourceRepository;

    }

    @PostMapping("/saveBesoin")
    public  String ajouter(Model model, @Valid Ressource ressource, BindingResult bindingResult, @RequestParam(defaultValue = "0") int page){
        model.addAttribute("besoinMateriel",new Ressource());
        if(bindingResult.hasErrors())
            return  "formBesoinMateriel";//il faut l'ajouter cette vue mais pour le moment n'est pas utile
        besoinMaterielRepository.save(ressource);
        return "redirect:/index?page="+page;
    }
    @PostMapping("/saveOrdinateurBesoin")
    public  String ajouter1(
            Model model,
            @Valid Ordinateur ordinateur,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long id){
        // Récupérer l'enseignant depuis la base de données
        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);

        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);

        // Ajouter l'enseignant à l'ordinateur
        ordinateur.setEnseignant(enseignant);

        // Ajouter la demande à l'ordinateur
        ordinateur.setDemande(demande);

        // Valider et enregistrer l'ordinateur
        if (bindingResult.hasErrors()) {
            return "formBesoinMateriel";
        }

        besoinMaterielRepository.save(ordinateur);

        // Rediriger vers la page BesoinDepartement
        return "redirect:/consulterBesoinDepartement2?id="+id;
    }
    @PostMapping("/saveImprimanteBesoin")
    public  String ajouterImrimante(
            Model model,
            @Valid Imprimante imprimante,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long id){
        // Récupérer l'enseignant depuis la base de données
        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);

        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);

        // Ajouter l'enseignant à l'imprimante
        imprimante.setEnseignant(enseignant);

        // Ajouter la demande à l'imprimante
        imprimante.setDemande(demande);

        // Valider et enregistrer l'imprimante
        if (bindingResult.hasErrors()) {
            return "formBesoinMateriel";
        }

        besoinMaterielRepository.save(imprimante);

        // Rediriger vers la page BesoinDepartement
        return "redirect:/consulterBesoinDepartement2?id="+id;
    }

    @GetMapping("/formOrdinateurBesoinMateriel")
    public String formOrdinateurBesoinMateriel(Model model,@RequestParam Long id){
        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);

        model.addAttribute("demande",demande);
        // Créer un nouvel objet Ordinateur et l'ajouter au modèle
        Ordinateur ordinateur = new Ordinateur();
        ordinateur.setDemande(demande);
        model.addAttribute("ordinateur", ordinateur);

        // Retourner la vue formBesoinMateriel
        return "formBesoinMateriel";
    }
    @GetMapping("/formImprimanteBesoinMateriel")
    public String formImprimanteBesoinMateriel(Model model,@RequestParam Long id){
        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande",demande);
        Imprimante imprimante = new Imprimante();
        imprimante.setDemande(demande);
        model.addAttribute("imprimante", imprimante);

        // Retourner la vue formBesoinMateriel
        return "formImprimanteBesoinMateriel";
    }

    @GetMapping("/consulterBesoinDepartement2")
    public  String consulterBesoinDepartement(Model model,@RequestParam Long id){
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande",demande);
        //ici récuperer l'id de chef depuis la session
        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
        model.addAttribute("chefDepartement",enseignant);
        return "BesoinsDepartement";
    }


    @GetMapping("/consulterBesoinEnseignant")
    public String consulterBesoinEnseignant(@RequestParam Long id, Model model) {
        Demande demande = demandeRepository.findById(id).orElse(null);

        // Obtenez les enseignants qui ont exprimé des besoins pour cette demande
        //List<Enseignant> enseignants = enseignantRepository.findByDemandesContains(demande);
        List<Enseignant> enseignants = enseignantRepository.findEnseignantsByDemandeIdExcludingChef(demande.getId());

        //ici récuperer l'id de chef depuis la session
        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
        model.addAttribute("chefDepartement",enseignant);

        model.addAttribute("enseignants", enseignants);
        model.addAttribute("demande",demande);

        return "listeEnseignants"; // Nom de la vue
    }



    @GetMapping("/enseignant/{enseignantId}/demande/{demandeId}/ressources")
    public String getRessourcesByEnseignantAndDemande(@PathVariable Long enseignantId, @PathVariable Long demandeId, Model model) {
        // Récupérer les ressources associées à l'enseignant et à la demande spécifiée
        List<Ressource> ressources = ressourceRepository.findByEnseignantIdAndDemandeId(enseignantId, demandeId);
        model.addAttribute("ressources", ressources);

        // Récupérer l'enseignant chef de département depuis la session (ici, en supposant que l'ID est 1)
        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
        model.addAttribute("chefDepartement", enseignant);

        // Récupérer la demande associée à l'ID spécifié
        Demande demande = demandeRepository.findById(demandeId).orElse(null);
        model.addAttribute("demande", demande);

        return "ressourcesEnseignant";
    }



    @GetMapping("/supprimerBesoin/{id}")
    public String supprimerBesoin(@PathVariable Long id) {
        // Récupérer le besoin par son ID
        Ressource besoin = besoinMaterielRepository.findById(id).orElse(null);

        if (besoin == null) {
            throw new RuntimeException("Besoin introuvable");
        }

        // Supprimer le besoin
        besoinMaterielRepository.deleteById(id);

        // Récupérer l'ID de la demande associée au besoin
        Long demandeId = besoin.getDemande().getId();

        // Rediriger vers la page de la demande après suppression
        return "redirect:/consulterBesoinDepartement2?id=" + demandeId;
    }

    @GetMapping("/modifierBesoin/{id}")
    public String formModifierBesoin(@PathVariable Long id, Model model) {
        // Récupérer le besoin à partir de l'ID
        Ressource besoin = besoinMaterielRepository.findById(id).orElse(null);

        if (besoin == null) {
            throw new RuntimeException("Besoin introuvable");
        }

        // Ajouter le besoin au modèle pour afficher dans le formulaire
        model.addAttribute("besoin", besoin);

        // Retourner le nom du template à utiliser pour l'affichage
        return "formModifierBesoin";
    }

    @PostMapping("/modifierBesoin")
    public String modifierBesoin(@Valid Ressource besoin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("besoin", besoin);
            return "formModifierBesoin";
        }

        // Récupérer la demande associée au besoin modifié
        Demande demande = besoin.getDemande();

        // Vérifier si la demande est null ou non
        if (demande == null) {
            // Gérer le cas où la demande est null
            // Par exemple, rediriger vers une page d'erreur ou afficher un message à l'utilisateur
            return "redirect:/error";
        }

        // Sauvegarder les modifications du besoin
        besoinMaterielRepository.save(besoin);

        // Redirection après la modification
        return "redirect:/consulterBesoinDepartement2?id=" + demande.getId();
    }





    //---------------------------------------------------------------------------------
    /*
    @GetMapping("/supprimerBesoin")
    public String supprimer( Long id, int page) {
        System.out.println("Suppression de la demande avec l'ID : " + id);
        Optional<Demande> demandeOptional = demandeRepository.findById(id);
        if (demandeOptional.isPresent()) {
            demandeRepository.deleteById(id);
            System.out.println("Demande supprimée avec succès !");
        } else {
            System.out.println("Demande non trouvée avec l'ID : " + id);
        }
        return "redirect:/index?page="+page;
    }
    */



    @GetMapping("/editBesoin")
    public  String editDemande(Model model, long id, int page){
        model.addAttribute("currentPage",page);
        Demande demande = demandeRepository.findById(id).orElse(null);
        if(demande==null)
            throw new RuntimeException("Demande introuvable");
        model.addAttribute("demande",demande);
        return "editDemande";
    }

    //--------------------------------------------------------------------------------------------



}
