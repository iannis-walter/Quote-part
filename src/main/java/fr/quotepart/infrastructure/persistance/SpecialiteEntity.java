package fr.quotepart.infrastructure.persistance;

import fr.quotepart.domaine.medicament.Smr;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Projection de persistance d'une spécialité BDPM (table {@code specialite}), porteuse du SMR.
 */
@Entity
@Table(name = "specialite")
public class SpecialiteEntity {

    @Id
    @Column(name = "cis")
    private String cis;

    @Enumerated(EnumType.STRING)
    @Column(name = "smr", nullable = false)
    private Smr smr;

    protected SpecialiteEntity() {
        // requis par JPA
    }

    public SpecialiteEntity(String cis, Smr smr) {
        this.cis = cis;
        this.smr = smr;
    }

    public String getCis() {
        return cis;
    }

    public Smr getSmr() {
        return smr;
    }
}
