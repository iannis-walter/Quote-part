package fr.quotepart.domaine.monnaie;

import java.math.BigDecimal;

public class MontantNegatifException extends RuntimeException {

    public MontantNegatifException(BigDecimal valeur) {
        super("Un montant ne peut pas être négatif : " + valeur);
    }
}
