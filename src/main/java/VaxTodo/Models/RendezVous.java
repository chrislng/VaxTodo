package VaxTodo.Models;

import VaxTodo.Configs.Config;

/** Used for manipulating RendezVous objects and compare between objects
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Not currently used
 */
public class RendezVous {
    private int intRendezVous, intTypeDose;
    private String strDate, strNom, strPrenom, strCourriel;

    public RendezVous() {
        this.intRendezVous = 0;
        this.intTypeDose = 0;
        this.strDate = "";
        this.strNom = "";
        this.strPrenom = "";
        this.strCourriel = "";
    }
    public RendezVous(int intRendezVous, int intTypeDose, String strDate, String strNom, String strPrenom, String strCourriel) {
        this.intRendezVous = intRendezVous;
        this.intTypeDose = intTypeDose;
        this.strDate = strDate;
        this.strNom = strNom;
        this.strPrenom = strPrenom;
        this.strCourriel = strCourriel;
    }

    public int getIntRendezVous() {
        return this.intRendezVous;
    }

    public void setIntRendezVous(int intRendezVous) {
        this.intRendezVous = intRendezVous;
    }

    public int getIntTypeDose() {
        return this.intTypeDose;
    }

    public void setIntTypeDose(int intTypeDose) {
        this.intTypeDose = intTypeDose;
    }

    public String getStrDate() {
        return this.strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrNom() {
        return this.strNom;
    }

    public void setStrNom(String strNom) {
        this.strNom = strNom;
    }

    public String getStrPrenom() {
        return this.strPrenom;
    }

    public void setStrPrenom(String strPrenom) {
        this.strPrenom = strPrenom;
    }

    public String getStrCourriel() {
        return this.strCourriel;
    }

    public void setStrCourriel(String strCourriel) {
        this.strCourriel = strCourriel;
    }

    public String printInfosRendezVous() {
        return getIntRendezVous() + Config.strCSVMainSeparator + getIntTypeDose() + Config.strCSVMainSeparator + getStrDate() + Config.strCSVMainSeparator + getStrNom() + Config.strCSVMainSeparator + getStrPrenom() + Config.strCSVMainSeparator + getStrCourriel();
    }

    @Override
    public String toString() {
        return "{" +
            "intRendezVous='" + getIntRendezVous() + "'" +
            ", intTypeDose='" + getIntTypeDose() + "'" +
            ", strDate='" + getStrDate() + "'" +
            ", strNom='" + getStrNom() + "'" +
            ", strPrenom='" + getStrPrenom() + "'" +
            ", strCourriel='" + getStrCourriel() + "'" +
            "}";
    }

    // ToDo implemente EnvoiConfirmationRendezVous()
    public void EnvoiConfirmationRendezVous() {
        System.out.println("\nEnvoi d'un courriel de confirmation au visiteur '" + getStrCourriel() + "'\n");
    }
}
