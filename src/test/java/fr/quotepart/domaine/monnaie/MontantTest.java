package fr.quotepart.domaine.monnaie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MontantTest {

    @Test
    void arrondit_au_centime_le_plus_proche() {
        Montant montant = Montant.euros("6.505");

        assertThat(montant.valeur()).isEqualTo(new BigDecimal("6.51"));
    }

    @Test
    void refuse_un_montant_negatif() {
        assertThatThrownBy(() -> Montant.euros("-0.01"))
                .isInstanceOf(MontantNegatifException.class);
    }
}
