package ma.sdsi.gestionressources.web;


import jakarta.validation.Valid;
import ma.sdsi.gestionressources.entities.Demande;
import ma.sdsi.gestionressources.entities.Enseignant;
import ma.sdsi.gestionressources.repositories.DemandeRepository;
import ma.sdsi.gestionressources.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class DemandeController {
    private DemandeRepository demandeRepository;
    private EnseignantRepository enseignantRepository;

    @Autowired
    public DemandeController(DemandeRepository demandeRepository, EnseignantRepository enseignantRepository) {
        this.demandeRepository = demandeRepository;
        this.enseignantRepository =enseignantRepository;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String afficherFormulaireDemande(Model model,
                                            @RequestParam(name = "page",defaultValue = "0") int page,
                                            @RequestParam(name = "size",defaultValue = "5")int size,
                                            @RequestParam(name = "id", required = false) Long id )
    {


        if (id != null) {
            Demande demande = demandeRepository.findById(id).orElse(null);
            model.addAttribute("demande", demande);
            //ici récuperer l'id de chef depuis la session
            Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
            Page<Demande> pagesDemandes = demandeRepository.findByEnseignantId(1L, PageRequest.of(page, size, Sort.by("id").descending()));
            model.addAttribute("totalPages",pagesDemandes.getTotalPages());
            model.addAttribute("listDemandes", pagesDemandes.getContent() );
            model.addAttribute("chefDepartement",enseignant);
            model.addAttribute("pages", new int[pagesDemandes.getTotalPages()]);
            model.addAttribute("currentPage", page);
            return "demandes"; // Ajoutez le modèle pour afficher les détails de la demande dans votre page d'index
        } else {
            Page<Demande> pagesDemandes = demandeRepository.findByEnseignantId(1L, PageRequest.of(page, size, Sort.by("id").descending()));
            //ici récuperer l'id de chef depuis la session
            Enseignant enseignant = enseignantRepository.findById(1L).orElse(null);
            model.addAttribute("chefDepartement",enseignant);
            model.addAttribute("totalPages",pagesDemandes.getTotalPages());
            model.addAttribute("listDemandes", pagesDemandes.getContent());
            model.addAttribute("pages", new int[pagesDemandes.getTotalPages()]);
            model.addAttribute("currentPage", page);
            return "ViewschefDepartement/demandes"; // Retourne la même page d'index avec ou sans les détails de la demande
        }
    }


    @PostMapping("/save")
    public  String ajouter(Model model, @Valid Demande demande, BindingResult bindingResult, @RequestParam(defaultValue = "0") int page){
        model.addAttribute("demande",new Demande());
        if(bindingResult.hasErrors())
            return  "formDemandes";
        demandeRepository.save(demande);
        return "redirect:/index?page="+page;
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

    @GetMapping("/formDemandes")
    public String formDemandes(Model model){
        model.addAttribute("demande",new Demande());
        return  "ViewschefDepartement/formDemandes";
    }

    @GetMapping("/editDemande")
    public  String editDemande(Model model, long id, int page){
        model.addAttribute("currentPage",page);
        Demande demande = demandeRepository.findById(id).orElse(null);
        if(demande==null)
            throw new RuntimeException("Demande introuvable");
        model.addAttribute("demande",demande);
        return "ViewschefDepartement/editDemande";
    }

    @GetMapping("/envoyerResponsable")
    public String envoyerResponsable(@RequestParam Long id) {
        Demande demande = demandeRepository.findById(id).orElse(null);

        if (demande != null && !demande.getEnvoyerResponsable()) {
            demande.setEnvoyerResponsable(true);
            demandeRepository.save(demande);
        }

        return "redirect:/consulterBesoinDepartement?id=" + id;
    }




}
