package fr.quotepart.infrastructure.persistance;

import fr.quotepart.domaine.medicament.Smr;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Projection de persistance d'une spécialité BDPM (table {@code specialite}).
 * Dénomination (fichier CIS) et SMR (fichier CIS_HAS_SMR) proviennent de sources distinctes,
 * fusionnées par CIS : les deux champs peuvent donc être renseignés séparément.
 */
@Entity
@Table(name = "specialite")
public class SpecialiteEntity {

    @Id
    @Column(name = "cis")
    private String cis;

    @Column(name = "denomination")
    private String denomination;

    @Enumerated(EnumType.STRING)
    @Column(name = "smr")
    private Smr smr;

    protected SpecialiteEntity() {
        // requis par JPA
    }

    public SpecialiteEntity(String cis) {
        this.cis = cis;
    }

    public SpecialiteEntity(String cis, Smr smr) {
        this.cis = cis;
        this.smr = smr;
    }

    public String getCis() {
        return cis;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public Smr getSmr() {
        return smr;
    }

    public void setSmr(Smr smr) {
        this.smr = smr;
    }
}
