package ma.sdsi.gestionressources.web;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ma.sdsi.gestionressources.entities.Demande;
import ma.sdsi.gestionressources.entities.Enseignant;
import ma.sdsi.gestionressources.entities.Panne;
import ma.sdsi.gestionressources.entities.User;
import ma.sdsi.gestionressources.repositories.DemandeRepository;
import ma.sdsi.gestionressources.repositories.EnseignantRepository;
import ma.sdsi.gestionressources.repositories.PanneRepository;
import ma.sdsi.gestionressources.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class DemandeController {
    private DemandeRepository demandeRepository;
    private EnseignantRepository enseignantRepository;
    private UserRepository userRepository;
    private PanneRepository panneRepository;

    @Autowired
    public DemandeController(DemandeRepository demandeRepository,
                             EnseignantRepository enseignantRepository,
                             UserRepository userRepository,
                             PanneRepository panneRepository)
    {
        this.demandeRepository = demandeRepository;
        this.enseignantRepository =enseignantRepository;
        this.userRepository = userRepository;
        this.panneRepository = panneRepository;
    }
    private Enseignant getEnseignantFromSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            User user = userRepository.findByEmail(username); // Trouver l'utilisateur par email
            return enseignantRepository.findByUser(user); // Trouver l'enseignant par utilisateur
        }
        return null;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String afficherFormulaireDemande(Model model,
                                            @RequestParam(name = "page",defaultValue = "0") int page,
                                            @RequestParam(name = "size",defaultValue = "5")int size,
                                            @RequestParam(name = "id", required = false) Long id)

    {
        Enseignant enseignantFromSession = getEnseignantFromSession(); // Récupérer l'enseignant depuis la session

        if (enseignantFromSession == null) {
            return "redirect:/login"; // Si l'enseignant n'est pas trouvé, rediriger vers la page de connexion
        }

        if (id != null) {
            Demande demande = demandeRepository.findById(id).orElse(null);
            model.addAttribute("demande", demande);
            //ici récuperer l'id de chef depuis la session
            //Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
            Page<Demande> pagesDemandes = demandeRepository.findByEnseignantId(enseignantFromSession.getId(), PageRequest.of(page, size, Sort.by("id").descending()));
            model.addAttribute("totalPages",pagesDemandes.getTotalPages());
            model.addAttribute("listDemandes", pagesDemandes.getContent() );
            model.addAttribute("chefDepartement",enseignantFromSession);
            model.addAttribute("pages", new int[pagesDemandes.getTotalPages()]);
            model.addAttribute("currentPage", page);
            return "ViewschefDepartement/demandes"; // Ajoutez le modèle pour afficher les détails de la demande dans votre page d'index
        } else {
            Page<Demande> pagesDemandes = demandeRepository.findByEnseignantId(enseignantFromSession.getId(), PageRequest.of(page, size, Sort.by("id").descending()));
            //ici récuperer l'id de chef depuis la session
            //Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
            model.addAttribute("chefDepartement",enseignantFromSession);
            model.addAttribute("totalPages",pagesDemandes.getTotalPages());
            model.addAttribute("listDemandes", pagesDemandes.getContent());
            model.addAttribute("pages", new int[pagesDemandes.getTotalPages()]);
            model.addAttribute("currentPage", page);
            return "ViewschefDepartement/demandes"; // Retourne la même page d'index avec ou sans les détails de la demande
        }
    }


    @PostMapping("/chefdepartement/save")
    public  String ajouter(Model model, @Valid Demande demande, BindingResult bindingResult, @RequestParam(defaultValue = "0") int page){
        model.addAttribute("demande",demande);
        if(bindingResult.hasErrors())
            return  "ViewschefDepartement/formDemandes";
        demande.setEnvoyerResponsable(false);
        demande.setOld(false);
        Enseignant enseignantFromSession = getEnseignantFromSession(); // Récupérer l'enseignant depuis la session
        demande.setEnseignant(enseignantFromSession);
        demandeRepository.save(demande);
        return "redirect:/index?page="+page;
    }
    @GetMapping("/chefdepartement/formDemandes")
    public String formDemandes(Model model){
        model.addAttribute("demande",new Demande());
        return  "ViewschefDepartement/formDemandes";
    }
    @GetMapping("/chefdepartement/editDemande")
    public  String editDemande(Model model, long id, int page){
        model.addAttribute("currentPage",page);
        Demande demande = demandeRepository.findById(id).orElse(null);
        if(demande==null)
            throw new RuntimeException("Demande introuvable");
        model.addAttribute("demande",demande);
        return "ViewschefDepartement/editDemande";
    }
    @GetMapping("/supprimer")
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

//    @GetMapping("/consulterDemande")
//    public String consulterDemande(Model model, Long id) {
//        Demande demande = demandeRepository.findById(id).orElse(null);
//        model.addAttribute("demande",demande);
//        //ici récuperer l'id de chef depuis la session
//        Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
//        model.addAttribute("chefDepartement",enseignant);
//        return "demandeDetail";
//    }





    @GetMapping("/envoyerResponsable")
    public String envoyerResponsable(@RequestParam Long id) {
        Demande demande = demandeRepository.findById(id).orElse(null);

        if (demande != null && !demande.getEnvoyerResponsable()) {
            demande.setEnvoyerResponsable(true);
            demandeRepository.save(demande);
        }

        return "redirect:/consulterBesoinDepartement?id=" + id;
    }



    @GetMapping("/signalerPanneChef")
    public String signalerPanne(Model model) {
        model.addAttribute("panne", new Panne()); // Ajoute un objet Panne au modèle

        return "ViewschefDepartement/signalerPanne"; // Vue pour signaler une panne
    }
    @PostMapping("/signalerPanneChef")
    public String enregistrerPanne(@Valid @ModelAttribute("panne") Panne panne, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "ViewschefDepartement/signalerPanne"; // Retourner la même vue en cas d'erreur
        }

        Enseignant enseignantFromSession = getEnseignantFromSession();
        //ici recuperer l'enseignant depuis la session
        panne.setEnseignant(enseignantFromSession);
        panneRepository.save(panne); // Enregistrer la panne
        System.out.println("Panne signalée: " + panne.getDescription());

        return "redirect:/index"; // Redirection après le signalement
    }



}
