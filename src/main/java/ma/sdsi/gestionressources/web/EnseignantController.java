package ma.sdsi.gestionressources.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.sdsi.gestionressources.entities.*;
import ma.sdsi.gestionressources.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EnseignantController {

    private EnseignantRepository enseignantRepository;

    private final DemandeRepository demandeRepository;
    private RessourceRepository ressourceRepository;

    private BesoinMaterielRepository besoinMaterielRepository;

    private ImprimanteBesoinRepository imprimanteBesoinRepository;
    private  OrdinateurBesoinRepository ordinateurBesoinRepository;
    private  PanneRepository panneRepository;

    public EnseignantController(DemandeRepository demandeRepository,
                                EnseignantRepository enseignantRepository,
                                RessourceRepository ressourceRepository,
                                BesoinMaterielRepository besoinMaterielRepository,
                                ImprimanteBesoinRepository imprimanteBesoinRepository,
                                OrdinateurBesoinRepository ordinateurBesoinRepository,
                                PanneRepository panneRepository
    ) {
        this.demandeRepository = demandeRepository;
        this.enseignantRepository = enseignantRepository;
        this.ressourceRepository = ressourceRepository;
        this.besoinMaterielRepository = besoinMaterielRepository;
        this.imprimanteBesoinRepository = imprimanteBesoinRepository;
        this.ordinateurBesoinRepository = ordinateurBesoinRepository;
        this.panneRepository = panneRepository;
    }

    @GetMapping("/enseignant")
    public String home(Model model,
                       @RequestParam(name = "page",defaultValue = "0") int page,
                       @RequestParam(name = "size",defaultValue = "5")int size){
        //ici récuperer l'enseignant depuis la session
        Enseignant enseignant = enseignantRepository.findById(3L).orElse(null);
        Page<Demande> pagesDemandes = demandeRepository.findByEnseignantId(enseignant.getChefDepartement().getId(), PageRequest.of(page, size, Sort.by("id").descending()));
        model.addAttribute("enseignant",enseignant);
        model.addAttribute("listDemandes",pagesDemandes.getContent());
        model.addAttribute("pages", new int[pagesDemandes.getTotalPages()]);
        model.addAttribute("totalPages",pagesDemandes.getTotalPages());
        model.addAttribute("currentPage", page);
        return "ViewsEnseignant/demandesEnseignant";
    }

    @GetMapping("/consulterBesoinsEnseignant")
    public  String consulterBesoinsEnseignant(Model model,@RequestParam Long id){
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande",demande);
        //ici récuperer l'id de l'enseignant depuis la session
        Enseignant enseignant = enseignantRepository.findById(3L).orElse(null);


        List<Ressource> ressources = ressourceRepository.findByEnseignantIdAndDemandeId(3L,demande.getId());
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
        model.addAttribute("enseignant",enseignant);
        return "ViewsEnseignant/besoinsEnseignant";
    }

    @GetMapping("/enseignant/formOrdinateurBesoinMateriel")
    public String formOrdinateurBesoinMateriel(Model model,@RequestParam Long id){
        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);

        model.addAttribute("demande",demande);
        // Créer un nouvel objet Ordinateur et l'ajouter au modèle
        Ordinateur ordinateur = new Ordinateur();
        ordinateur.setDemande(demande);
        model.addAttribute("ordinateur", ordinateur);

        // Retourner la vue formBesoinMateriel
        return "ViewsEnseignant/formBesoinOrdinateurMateriel";
    }

    @GetMapping("/enseignant/formImprimanteBesoinMateriel")
    public String formImprimanteBesoinMateriel(Model model,@RequestParam Long id){
        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande",demande);
        Imprimante imprimante = new Imprimante();
        imprimante.setDemande(demande);
        model.addAttribute("imprimante", imprimante);

        // Retourner la vue formBesoinMateriel
        return "ViewsEnseignant/formImprimanteBesoinMateriel";
    }

    @PostMapping("/saveOrdinateurBesoinForEns")
    public  String ajouterOrdinateur(
            Model model,
            @Valid Ordinateur ordinateur,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long id){
        // Récupérer l'enseignant depuis la session
        Enseignant enseignant = enseignantRepository.findById(3L).orElse(null);

        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande", demande);
        // Ajouter l'enseignant à l'ordinateur
        ordinateur.setEnseignant(enseignant);

        // Ajouter la demande à l'ordinateur
        ordinateur.setDemande(demande);

        // Valider et enregistrer l'ordinateur
        if (bindingResult.hasErrors()) {
            return "ViewsEnseignant/formBesoinOrdinateurMateriel";
        }

        besoinMaterielRepository.save(ordinateur);

        // Rediriger vers la page BesoinDepartement
        return "redirect:/consulterBesoinsEnseignant?id="+id;
    }
    @PostMapping("/saveImprimanteBesoinForEns")
    public  String ajouterImrimante(
            Model model,
            @Valid Imprimante imprimante,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long id){
        // Récupérer l'enseignant depuis la session
        Enseignant enseignant = enseignantRepository.findById(3L).orElse(null);

        // Récupérer la demande depuis la base de données en utilisant l'ID passé en paramètre
        Demande demande = demandeRepository.findById(id).orElse(null);
        model.addAttribute("demande", demande);
        // Ajouter l'enseignant à l'imprimante
        imprimante.setEnseignant(enseignant);

        // Ajouter la demande à l'imprimante
        imprimante.setDemande(demande);

        // Valider et enregistrer l'imprimante
        if (bindingResult.hasErrors()) {
            return "ViewsEnseignant/formImprimanteBesoinMateriel";
        }

        besoinMaterielRepository.save(imprimante);

        // Rediriger vers la page BesoinDepartement
        return "redirect:/consulterBesoinsEnseignant?id="+id;
    }

    @GetMapping("/enseignant/modifierBesoin/{id}")
    public String formModifierBesoin(@PathVariable Long id, Model model) {
        // Récupérer le besoin à partir de l'ID
        Ressource besoin = besoinMaterielRepository.findById(id).orElse(null);

        if (besoin == null) {
            throw new RuntimeException("Besoin introuvable");
        }

        // Ajouter le besoin au modèle pour afficher dans le formulaire
        model.addAttribute("besoin", besoin);

        // Retourner le nom du template à utiliser pour l'affichage
        return "ViewsEnseignant/formModifierBesoin2";
    }

    @PostMapping("/modifierBesoinOrdinateurForEns")
    public String modifierBesoinOrdinateur(@Valid Ordinateur besoin, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("besoin", besoin);
            return "ViewsEnseignant/formModifierBesoin";
        }

        ordinateurBesoinRepository.save(besoin);

        // Redirection après la modification
        return "redirect:/consulterBesoinsEnseignant?id="+besoin.getDemande().getId() ;
    }
    @PostMapping("/modifierBesoinImprimanteForEns")
    public String modifierBesoinImprimante(@Valid Imprimante besoin, BindingResult bindingResult, Model model) {
        model.addAttribute("besoin",new Imprimante());
        if (bindingResult.hasErrors()) {
            model.addAttribute("besoin", besoin);
            return "ViewsEnseignant/formModifierBesoin";
        }

        imprimanteBesoinRepository.save(besoin);

        // Redirection après la modification
        return "redirect:/consulterBesoinsEnseignant?id="+besoin.getDemande().getId() ;
    }

    @GetMapping("/supprimerBesoinForEns/{id}")
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
        return "redirect:/consulterBesoinsEnseignant?id=" + demandeId;
    }

    //###################################################################################################
    @GetMapping("/consulterDemandes")
    public String consulterDemandes(Model model, @RequestParam(name = "id", required = false) Long id) {
        // Afficher toutes les demandes ou une demande spécifique selon le paramètre "id"
        if (id != null) {
            Demande demande = demandeRepository.findById(id).orElse(null);
            model.addAttribute("demande", demande);
        } else {
            model.addAttribute("demandes", demandeRepository.findAll());
        }
        return "consulterDemandes"; // Vue où les demandes sont affichées
    }

    @GetMapping("/signalerPanne")
    public String signalerPanne(Model model) {
        model.addAttribute("panne", new Panne()); // Ajoute un objet Panne au modèle

        return "ViewsEnseignant/signalerPanne"; // Vue pour signaler une panne
    }

    @PostMapping("/signalerPanne")
    public String enregistrerPanne(@Valid @ModelAttribute("panne") Panne panne, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "ViewsEnseignant/signalerPanne"; // Retourner la même vue en cas d'erreur
        }

        //ici recuperer l'enseignant depuis la session
        panne.setEnseignant(enseignantRepository.findById(3L).orElse(null));
        panneRepository.save(panne); // Enregistrer la panne
        System.out.println("Panne signalée: " + panne.getDescription());

        return "redirect:/enseignant"; // Redirection après le signalement
    }


}
