package fr.quotepart.infrastructure.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Projection de persistance d'une présentation BDPM (table {@code presentation}).
 * Distincte du modèle de domaine : l'infrastructure ne contamine pas le domaine.
 */
@Entity
@Table(name = "presentation")
public class PresentationEntity {

    @Id
    @Column(name = "cip13")
    private String cip13;

    @Column(name = "cis", nullable = false)
    private String cis;

    @Column(name = "prix", nullable = false)
    private BigDecimal prix;

    @Column(name = "taux")
    private Integer taux;

    @Column(name = "remboursable", nullable = false)
    private boolean remboursable;

    protected PresentationEntity() {
        // requis par JPA
    }

    public PresentationEntity(String cip13, String cis, BigDecimal prix, Integer taux, boolean remboursable) {
        this.cip13 = cip13;
        this.cis = cis;
        this.prix = prix;
        this.taux = taux;
        this.remboursable = remboursable;
    }

    public String getCip13() {
        return cip13;
    }

    public String getCis() {
        return cis;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public Integer getTaux() {
        return taux;
    }

    public boolean isRemboursable() {
        return remboursable;
    }
}
