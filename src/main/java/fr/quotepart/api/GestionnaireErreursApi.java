package fr.quotepart.api;

import fr.quotepart.application.MedicamentIntrouvableException;
import fr.quotepart.domaine.remboursement.DonneesMedicamentIncompletesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduit les erreurs métier en réponses HTTP explicites (RFC 7807 ProblemDetail).
 */
@RestControllerAdvice
public class GestionnaireErreursApi {

    @ExceptionHandler(MedicamentIntrouvableException.class)
    public ProblemDetail medicamentIntrouvable(MedicamentIntrouvableException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DonneesMedicamentIncompletesException.class)
    public ProblemDetail donneesIncompletes(DonneesMedicamentIncompletesException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
    }
}
