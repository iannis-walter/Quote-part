package fr.quotepart.api;

import fr.quotepart.application.CalculerResteACharge;
import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.remboursement.Decompte;
import fr.quotepart.domaine.remboursement.ProfilPatient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculs")
public class CalculController {

    private final CalculerResteACharge calculerResteACharge;

    public CalculController(CalculerResteACharge calculerResteACharge) {
        this.calculerResteACharge = calculerResteACharge;
    }

    @PostMapping
    public DecompteResponse calculer(@Valid @RequestBody CalculRequest request) {
        ProfilPatient profil = new ProfilPatient(
                request.profil().parcoursSoinsRespecte(),
                request.profil().ald(),
                request.profil().c2s(),
                request.profil().regimeLocal());
        Decompte decompte = calculerResteACharge.executer(new CodeCip13(request.cip13()), profil);
        return DecompteResponse.depuis(decompte);
    }
}
