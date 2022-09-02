package VaxTodo.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BenevoleTest {

    @Test
    public void checkAllValuesEqualsExactly() throws Exception {
        Benevole benevoleToTest = new Benevole(111111111,"x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK",Long.parseLong("111111111111"),1234567890,"Test-Nom","Test-Prenom","123 Test-Adresse","A0A0A0","Test-Ville","Test-Courriel@test.com","2021-12-15");

        // check if benevoleToTest equals exactly (check all values of both and returns true) to newly created Benevole
        Assertions.assertTrue(benevoleToTest.equalsExactly(new Benevole(111111111,"x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK",Long.parseLong("111111111111"),1234567890,"Test-Nom","Test-Prenom","123 Test-Adresse","A0A0A0","Test-Ville","Test-Courriel@test.com","2021-12-15")));
    }

    @Test
    public void checkNoCompteOnly() throws Exception {
        Benevole benevoleToTest = new Benevole(111111111,"x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK",Long.parseLong("111111111111"),1234567890,"Test-Nom","Test-Prenom","123 Test-Adresse","A0A0A0","Test-Ville","Test-Courriel@test.com","2021-12-15");

        // check if benevoleToTest's No Compte Only equals to newly created Benevole's No Compte
        Assertions.assertTrue(benevoleToTest.equalsNoCompte(new Benevole(111111111,"",Long.parseLong("0"),0,"","","","","","","")));
    }
}