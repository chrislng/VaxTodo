package VaxTodo.Models;

import VaxTodo.Configs.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FormulaireTest {

    @Test
    public void checkHeritage() {
        Visiteur visiteurToTest = new Visiteur(Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com", LocalDate.now().minusDays(1).format(Config.DATE_TIME_FORMATTER).toString());

        // Formulaire is sub-class of Visiteur
        Formulaire formulaireToTest = new Formulaire();
        formulaireToTest.setVisiteur(visiteurToTest);

        // check if values have been passed to sub-class properly and super-class values equals sub-class values

        // both classes have the exact same super-class values
        Assertions.assertEquals(formulaireToTest.getInfosPersonne(), visiteurToTest.getInfosPersonne());

        // both classes can print the exact same super-class values
        Assertions.assertEquals(formulaireToTest.printInfosVisiteur(), visiteurToTest.printInfosVisiteur());
    }
}