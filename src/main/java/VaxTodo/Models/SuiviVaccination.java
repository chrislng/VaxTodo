package VaxTodo.Models;

import java.util.Date;

/** Used for manipulating SuiviVaccination objects and compare between objects
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Not currently used
 */
public class SuiviVaccination {
    private long lngNoCompteVisiteur;
    private int intSuiviVaccination;
    private String strDate;

    public SuiviVaccination() {
        this.intSuiviVaccination = 0;
        this.lngNoCompteVisiteur = 0;
        this.strDate = null;
    }
    public SuiviVaccination(int intSuiviVaccination, long lngNoCompteVisiteur, String strDate) {
        this.intSuiviVaccination = intSuiviVaccination;
        this.lngNoCompteVisiteur = lngNoCompteVisiteur;
        this.strDate = strDate;
    }

    public int getIntSuiviVaccination() {
        return this.intSuiviVaccination;
    }

    public void setIntSuiviVaccination(int intSuiviVaccination) {
        this.intSuiviVaccination = intSuiviVaccination;
    }

    public long getLngNoCompteVisiteur() {
        return this.lngNoCompteVisiteur;
    }

    public void setLngNoCompteVisiteur(long lngNoCompteVisiteur) {
        this.lngNoCompteVisiteur = lngNoCompteVisiteur;
    }

    public String getStrDate() {
        return this.strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    @Override
    public String toString() {
        return "{" +
            "intSuiviVaccination='" + getIntSuiviVaccination() + "'" +
            ", lngNoCompteVisiteur='" + getLngNoCompteVisiteur() + "'" +
            ", strDate='" + getStrDate() + "'" +
            "}";
    }

    // ToDo implemente envoiRappelVaccination()
    public void envoiRappelVaccination(Visiteur visiteur) {
        System.out.println("\nEnvoi d'un courriel de rappel de vaccination au visiteur '" + Long.toString(visiteur.getLngNoCompte()) + "'\n");
    }
}
