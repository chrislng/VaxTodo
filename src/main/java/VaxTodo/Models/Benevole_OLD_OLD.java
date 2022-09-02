package VaxTodo.Models;

import VaxTodo.Configs.Config;

/** Used for manipulating Benevoles objects and compare between objects
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Not currently used
 */
public class Benevole_OLD_OLD extends Personne {
    //! Un bénévole a aussi un horaire listant les jours où il est disponible pour venir au local.
    // private int intCodeIdentification;
    // private String strMotDePasse;
    private String strDateNaissance;

    public Benevole_OLD_OLD() {
        // this.intCodeIdentification = 0;
        // this.strMotDePasse = "";
        this.strDateNaissance = "";
    }
    public Benevole_OLD_OLD(/*int intCodeIdentification, String strMotDePasse, */String strDateNaissance) {
        // this.intCodeIdentification = intCodeIdentification;
        // this.strMotDePasse = strMotDePasse;
        this.strDateNaissance = strDateNaissance;
    }
    public Benevole_OLD_OLD(/*int intCodeIdentification, String strMotDePasse,*/ String strDateNaissance, long lngNoCompte, long lngNoTel, String strNom, String strPrenom, String strAdresse, String strCodePostal, String strVille, String strCourriel) {
        // this.intCodeIdentification = intCodeIdentification;
        // this.strMotDePasse = strMotDePasse;
        this.strDateNaissance = strDateNaissance;

        this.setLngNoCompte(lngNoCompte);
        this.setLngNoTel(lngNoTel);
        this.setStrNom(strNom);
        this.setStrPrenom(strPrenom);
        this.setStrAdresse(strAdresse);
        this.setStrCodePostal(strCodePostal);
        this.setStrVille(strVille);
        this.setStrCourriel(strCourriel);
    }

    // public int getIntCodeIdentification() {
    //     return this.intCodeIdentification;
    // }

    // public void setIntCodeIdentification(int intCodeIdentification) {
    //     this.intCodeIdentification = intCodeIdentification;
    // }

    // public String getStrMotDePasse() {
    //     return this.strMotDePasse;
    // }

    // public void setStrMotDePasse(String strMotDePasse) {
    //     this.strMotDePasse = strMotDePasse;
    // }

    public String getStrDateNaissance() {
        return this.strDateNaissance;
    }

    public void setStrDateNaissance(String strDateNaissance) {
        this.strDateNaissance = strDateNaissance;
    }

    public String getInfosPersonnelles() {
        return /*getIntCodeIdentification() + Config.strCSVMainSeparator + getStrMotDePasse() + Config.strCSVMainSeparator +*/ getStrDateNaissance();
    }
    public String getInfosPersonne() {
        return getLngNoCompte() + Config.strCSVMainSeparator + getLngNoTel() + Config.strCSVMainSeparator + getStrNom() + Config.strCSVMainSeparator + getStrPrenom() + Config.strCSVMainSeparator + getStrAdresse() + Config.strCSVMainSeparator + getStrCodePostal() + Config.strCSVMainSeparator + getStrVille() + Config.strCSVMainSeparator + getStrCourriel();
    }
    public String printInfosBenevole() {
        return getInfosPersonnelles() + Config.strCSVMainSeparator + getInfosPersonne();
    }

    @Override
    public String toString() {
        return "{[" +
            // "intCodeIdentification='" + getIntCodeIdentification() + "'" +
            // ", strMotDePasse='" + getStrMotDePasse() + "'" +
            "strDateNaissance='" + getStrDateNaissance() + "'" +
            "], lngNoCompte='" + getLngNoCompte() + "'" +
            ", lngNoTel='" + getLngNoTel() + "'" +
            ", strNom='" + getStrNom() + "'" +
            ", strPrenom='" + getStrPrenom() + "'" +
            ", strAdresse='" + getStrAdresse() + "'" +
            ", strCodePostal='" + getStrCodePostal() + "'" +
            ", strVille='" + getStrVille() + "'" +
            ", strCourriel='" + getStrCourriel() + "'" +
            "}";
    }

}
