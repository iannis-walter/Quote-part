package fr.quotepart.domaine.remboursement;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import io.cucumber.java.ParameterType;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

/**
 * Step definitions reliant les scénarios Gherkin au domaine de calcul (pur, sans Spring).
 */
public class CalculResteAChargeSteps {

    private final CalculateurResteACharge calcul = new CalculateurResteACharge();
    private final Bareme bareme = new BaremeDeTest();

    private Presentation presentation;
    private ProfilPatient profil = new ProfilPatient(true, false);
    private Complementaire complementaire;
    private Decompte decompte;

    @ParameterType("(\\d+,\\d{2}) €")
    public Montant montant(String valeur) {
        return Montant.euros(valeur.replace(",", "."));
    }

    @Etantdonné("une présentation remboursable à {montant} avec un SMR {string}")
    public void presentationRemboursable(Montant prix, String smr) {
        presentation = new Presentation(new CodeCip13("3400000000000"), prix, prix, true, smrDepuis(smr));
    }

    @Etantdonné("une présentation remboursable à {montant} avec une base de remboursement de {montant} et un SMR {string}")
    public void presentationAvecBaseDistincte(Montant prix, Montant base, String smr) {
        presentation = new Presentation(new CodeCip13("3400000000000"), prix, base, true, smrDepuis(smr));
    }

    @Etantdonné("une présentation non remboursable à {montant}")
    public void presentationNonRemboursable(Montant prix) {
        presentation = new Presentation(new CodeCip13("3400000000000"), prix, prix, false, Smr.INSUFFISANT);
    }

    @Et("un patient dans le parcours de soins, sans ALD")
    public void patientDansLeParcours() {
        profil = new ProfilPatient(true, false);
    }

    @Et("un patient hors du parcours de soins, sans ALD")
    public void patientHorsParcours() {
        profil = new ProfilPatient(false, false);
    }

    @Et("un patient en ALD")
    public void patientEnAld() {
        profil = new ProfilPatient(true, true);
    }

    @Et("un patient bénéficiaire de la C2S")
    public void patientC2s() {
        profil = new ProfilPatient(true, false, true, false);
    }

    @Et("un patient relevant du régime local d'Alsace-Moselle")
    public void patientRegimeLocal() {
        profil = new ProfilPatient(true, false, false, true);
    }

    @Et("une complémentaire qui couvre {int} % du ticket modérateur")
    public void uneComplementaire(int pourcentage) {
        complementaire = new Complementaire(Taux.pourcent(pourcentage));
    }

    @Quand("je calcule le reste à charge")
    public void jeCalcule() {
        decompte = calcul.calculer(presentation, profil, bareme);
    }

    @Alors("le reste à charge après complémentaire est {montant}")
    public void leResteApresComplementaireEst(Montant attendu) {
        assertThat(complementaire.resteApres(decompte)).isEqualTo(attendu);
    }

    @Alors("le taux appliqué est {int} %")
    public void leTauxEst(int pourcentage) {
        assertThat(decompte.tauxApplique()).isEqualTo(Taux.pourcent(pourcentage));
    }

    @Alors("le remboursement de la Sécurité sociale est {montant}")
    public void leRemboursementEst(Montant attendu) {
        assertThat(decompte.remboursementSecu()).isEqualTo(attendu);
    }

    @Alors("le reste à charge est {montant}")
    public void leResteAChargeEst(Montant attendu) {
        assertThat(decompte.resteACharge()).isEqualTo(attendu);
    }

    private Smr smrDepuis(String libelle) {
        return switch (libelle.toLowerCase()) {
            case "important" -> Smr.IMPORTANT;
            case "modéré" -> Smr.MODERE;
            case "faible" -> Smr.FAIBLE;
            case "insuffisant" -> Smr.INSUFFISANT;
            default -> throw new IllegalArgumentException("SMR inconnu : " + libelle);
        };
    }
}
