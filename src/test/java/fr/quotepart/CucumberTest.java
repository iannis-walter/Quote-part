package fr.quotepart;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Exécute les spécifications Gherkin (dossier {@code features}) via le moteur Cucumber.
 * Ces scénarios, écrits en français, sont la documentation vivante des règles de calcul.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.quotepart.domaine.remboursement")
public class CucumberTest {
}
