package fr.quotepart.api;

import fr.quotepart.application.CalculerOrdonnance;
import fr.quotepart.application.CalculerResteACharge;
import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Complementaire;
import fr.quotepart.domaine.remboursement.Decompte;
import fr.quotepart.domaine.remboursement.ProfilPatient;
import fr.quotepart.domaine.remboursement.Taux;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculs")
public class CalculController {

    private final CalculerResteACharge calculerResteACharge;
    private final CalculerOrdonnance calculerOrdonnance;

    public CalculController(CalculerResteACharge calculerResteACharge, CalculerOrdonnance calculerOrdonnance) {
        this.calculerResteACharge = calculerResteACharge;
        this.calculerOrdonnance = calculerOrdonnance;
    }

    @PostMapping
    public DecompteResponse calculer(@Valid @RequestBody CalculRequest request) {
        Decompte decompte = calculerResteACharge.executer(new CodeCip13(request.cip13()), versProfil(request.profil()));

        if (request.complementaire() != null) {
            Complementaire complementaire = new Complementaire(Taux.pourcent(request.complementaire()));
            return DecompteResponse.depuis(decompte, complementaire.resteApres(decompte));
        }
        return DecompteResponse.depuis(decompte);
    }

    @PostMapping("/ordonnance")
    public OrdonnanceResponse ordonnance(@Valid @RequestBody OrdonnanceRequest request) {
        List<CodeCip13> codes = request.cip13s().stream().map(CodeCip13::new).toList();
        Montant dejaConsommee = request.franchiseDejaConsommee() != null
                ? new Montant(request.franchiseDejaConsommee())
                : Montant.ZERO;
        return OrdonnanceResponse.depuis(
                calculerOrdonnance.executer(codes, versProfil(request.profil()), dejaConsommee));
    }

    private ProfilPatient versProfil(CalculRequest.ProfilRequest profil) {
        return new ProfilPatient(profil.parcoursSoinsRespecte(), profil.ald(), profil.c2s(), profil.regimeLocal());
    }
}
