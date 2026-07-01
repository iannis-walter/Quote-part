package fr.quotepart.api;

import fr.quotepart.application.Catalogue;
import fr.quotepart.application.MedicamentIntrouvableException;
import fr.quotepart.application.MedicamentResume;
import fr.quotepart.domaine.medicament.CodeCip13;
import java.util.List;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final Catalogue catalogue;

    public MedicamentController(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    @GetMapping
    public List<MedicamentResponse> lister(@RequestParam(name = "q", required = false) String q) {
        List<MedicamentResume> resumes =
                StringUtils.hasText(q) ? catalogue.rechercher(q) : catalogue.lister();
        return resumes.stream().map(MedicamentResponse::depuis).toList();
    }

    @GetMapping("/{cip13}")
    public MedicamentResponse detail(@PathVariable String cip13) {
        CodeCip13 code = new CodeCip13(cip13);
        return catalogue.resume(code)
                .map(MedicamentResponse::depuis)
                .orElseThrow(() -> new MedicamentIntrouvableException(code));
    }
}
