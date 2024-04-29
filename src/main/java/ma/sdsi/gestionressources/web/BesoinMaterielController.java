package ma.sdsi.gestionressources.web;


import jakarta.validation.Valid;
import ma.sdsi.gestionressources.entities.*;
import ma.sdsi.gestionressources.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BesoinMaterielController {
    private DemandeRepository demandeRepository;
    private EnseignantRepository enseignantRepository;
    private BesoinMaterielRepository besoinMaterielRepository;
    private ImprimanteBesoinRepository imprimanteBesoinRepository;
    private RessourceRepository ressourceRepository;
    private OrdinateurBesoinRepository ordinateurBesoinRepository;

    @Autowired
    public BesoinMaterielController(BesoinMaterielRepository besoinMaterielRepository, EnseignantRepository enseignantRepository,
                                    DemandeRepository demandeRepository,
                                    ImprimanteBesoinRepository imprimanteBesoinRepository,
                                    RessourceRepository ressourceRepository,
                                    OrdinateurBesoinRepository ordinateurBesoinRepository) {
        this.besoinMaterielRepository = besoinMaterielRepository;
        this.enseignantRepository =enseignantRepository;
        this.demandeRepository = demandeRepository;
        this.imprimanteBesoinRepository=imprimanteBesoinRepository;
        this.ressourceRepository = ressourceRepository;
        this.ordinateurBesoinRepository = ordinateurBesoinRepository;

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
    public  String ajouterOrdinateur(
            Model model,
            @Valid Ordinateur ordinateur,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long id){
        // Récupérer l'enseignant depuis la base de données
        Enseignant chef = enseignantRepository.findById(1L).orElse(null);

        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande", demande);
        // Ajouter l'enseignant à l'ordinateur
        ordinateur.setEnseignant(chef);

        // Ajouter la demande à l'ordinateur
        ordinateur.setDemande(demande);

        // Valider et enregistrer l'ordinateur
        if (bindingResult.hasErrors()) {
            return "ViewschefDepartement/formBesoinMateriel";
        }

        besoinMaterielRepository.save(ordinateur);

        // Rediriger vers la page BesoinDepartement
        return "redirect:/consulterBesoinDepartement?id="+id;
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
        model.addAttribute("demande", demande);
        // Ajouter l'enseignant à l'imprimante
        imprimante.setEnseignant(enseignant);

        // Ajouter la demande à l'imprimante
        imprimante.setDemande(demande);

        // Valider et enregistrer l'imprimante
        if (bindingResult.hasErrors()) {
            return "ViewschefDepartement/formImprimanteBesoinMateriel";
        }

        besoinMaterielRepository.save(imprimante);

        // Rediriger vers la page BesoinDepartement
        return "redirect:/consulterBesoinDepartement?id="+id;
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
        return "ViewschefDepartement/formBesoinMateriel";
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
        return "ViewschefDepartement/formImprimanteBesoinMateriel";
    }

    @GetMapping("/consulterBesoinDepartement")
    public  String consulterBesoinDepartement(Model model,@RequestParam Long id){
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande",demande);
        //ici récuperer l'id de chef depuis la session
        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);


        List<Ressource> ressources = ressourceRepository.findByEnseignantIdAndDemandeId(1L,demande.getId());
        // Compteurs pour ordinateurs et imprimantes
        int nbrOrdinateurs = 0;
        int nbrImprimantes = 0;

        // Compter le nombre d'ordinateurs et d'imprimantes
        for (Ressource ressource : ressources) {
            if (ressource instanceof Ordinateur) {
                nbrOrdinateurs++;
            } else if (ressource instanceof Imprimante) {
                nbrImprimantes++;
            }
        }
        model.addAttribute("nbrOrdinateurs", nbrOrdinateurs);
        model.addAttribute("nbrImprimantes", nbrImprimantes);
        model.addAttribute("ressources",ressources);
        model.addAttribute("chefDepartement",enseignant);
        return "ViewschefDepartement/BesoinsDepartement";
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
        // Pour chaque enseignant, calculez le total des besoins pour la demande
        //Map<Long, Integer> totalRessources = new HashMap<>();

        // Créez une liste pour les totaux
        List<Integer> totalRessources = new ArrayList<>();
        for (Enseignant ens : enseignants) {
            //int total = besoinMaterielRepository.countByEnseignantIdAndDemandeId(ens.getId(),demande.getId());
            int total = ressourceRepository.findByEnseignantIdAndDemandeId(ens.getId(),demande.getId()).size();
            System.out.println(" ens ID : " +ens.getId());
            System.out.println("total ens : " +total);
            //totalRessources.put(enseignant.getId(), total);
            totalRessources.add(total);
        }
        System.out.println("Total Ressources: " + totalRessources);

        model.addAttribute("totalRessources", totalRessources);

        model.addAttribute("enseignants", enseignants);
        model.addAttribute("demande",demande);

        return "ViewschefDepartement/listeEnseignants"; // Nom de la vue
    }



    @GetMapping("/enseignant/{enseignantId}/demande/{demandeId}/ressources")
    public String getRessourcesByEnseignantAndDemande(@PathVariable Long enseignantId, @PathVariable Long demandeId, Model model) {
        // Récupérer les ressources associées à l'enseignant et à la demande spécifiée
       List<Ressource> ressources = ressourceRepository.findByEnseignantIdAndDemandeId(enseignantId, demandeId);
        Enseignant enseignant=enseignantRepository.findById(enseignantId).orElse(null);
        model.addAttribute("enseignant", enseignant);
        //List<Ressource> ressources = enseignant.getRessourceList();
        model.addAttribute("ressources", ressources);

        // Récupérer l'enseignant chef de département depuis la session (ici, en supposant que l'ID est 1)
        Enseignant chefDepartement = enseignantRepository.findById(1L).orElse(null);
        model.addAttribute("chefDepartement", chefDepartement);

        // Récupérer la demande associée à l'ID spécifié
        Demande demande = demandeRepository.findById(demandeId).orElse(null);
        model.addAttribute("demande", demande);

        return "ViewschefDepartement/enseignantRessources";
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
        return "redirect:/consulterBesoinDepartement?id=" + demandeId;
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
        return "ViewschefDepartement/formModifierBesoin";
    }

    @PostMapping("/modifierBesoinOrdinateur")
    public String modifierBesoinOrdinateur(@Valid Ordinateur besoin, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("besoin", besoin);
            return "ViewschefDepartement/formModifierBesoin";
        }

        ordinateurBesoinRepository.save(besoin);

        // Redirection après la modification
        return "redirect:/consulterBesoinDepartement?id="+besoin.getDemande().getId() ;
    }
    @PostMapping("/modifierBesoinImprimante")
    public String modifierBesoinImprimante(@Valid Imprimante besoin, BindingResult bindingResult, Model model) {
        model.addAttribute("besoin",new Imprimante());
        if (bindingResult.hasErrors()) {
            model.addAttribute("besoin", besoin);
            return "ViewschefDepartement/formModifierBesoin";
        }

        imprimanteBesoinRepository.save(besoin);

        // Redirection après la modification
        return "redirect:/consulterBesoinDepartement?id="+besoin.getDemande().getId() ;
    }


    // Méthode de suppression
    @GetMapping("/ressource/delete/{id}")
    public String deleteRessource(@PathVariable Long id) {
        Ressource ressource = ressourceRepository.findById(id).orElse(null);
        Long enseignantId = ressource.getEnseignant().getId();
        Long demandeId = ressource.getDemande().getId();
        ressourceRepository.deleteById(id);
        return "redirect:/enseignant/"+enseignantId+"/demande/"+demandeId+"/ressources"; // Rediriger vers la liste des ressources
    }

    // Méthode pour afficher la page de modification
    @GetMapping("/ressource/edit/{id}")
    public String editRessource(@PathVariable Long id, Model model) {
        Ressource ressource = ressourceRepository.findById(id).orElse(null);
        model.addAttribute("ressource", ressource);
        return "ViewschefDepartement/editBesoinEnsParChef"; // Nom de la page de modification
    }


    @PostMapping("/ressourceOrdinateur/edit")
    public String updateOrdinateurBesoin(@ModelAttribute Ordinateur besoinOrdinateur) {

        Long enseignantId = besoinOrdinateur.getEnseignant().getId();
        Long demandeId = besoinOrdinateur.getDemande().getId();
        ordinateurBesoinRepository.save(besoinOrdinateur);
        return "redirect:/enseignant/"+enseignantId+"/demande/"+demandeId+"/ressources"; // Rediriger vers la liste des ressources
    }

    @PostMapping("/ressourceImprimante/edit")
    public String updateImprimanteBesoin(@ModelAttribute Imprimante besoinImprimante) {
        Long enseignantId = besoinImprimante.getEnseignant().getId();
        Long demandeId = besoinImprimante.getDemande().getId();
        imprimanteBesoinRepository.save(besoinImprimante);
        return "redirect:/enseignant/"+enseignantId+"/demande/"+demandeId+"/ressources"; // Rediriger vers la liste des ressources
    }


}
