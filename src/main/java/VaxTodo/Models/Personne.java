package VaxTodo.Models;

/** Super class of main classes (Benevole, Employe, Visiteur) and contains mostly data that will be needed by those sub classes
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class Personne {
    private long lngNoCompte, lngNoTel;
    private String strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel;

    public Personne() {
        this.lngNoCompte = 0;
        this.lngNoTel = 0;
        this.strNom = "";
        this.strPrenom = "";
        this.strAdresse = "";
        this.strCodePostal = "";
        this.strVille = "";
        this.strCourriel = "";
    }
    public Personne(long lngNoCompte, long lngNoTel, String strNom, String strPrenom, String strAdresse, String strCodePostal, String strVille, String strCourriel) {
        this.lngNoCompte = lngNoCompte;
        this.lngNoTel = lngNoTel;
        this.strNom = strNom;
        this.strPrenom = strPrenom;
        this.strAdresse = strAdresse;
        this.strCodePostal = strCodePostal;
        this.strVille = strVille;
        this.strCourriel = strCourriel;
    }

    public long getLngNoCompte() {
        return this.lngNoCompte;
    }

    public void setLngNoCompte(long lngNoCompte) {
        this.lngNoCompte = lngNoCompte;
    }

    public long getLngNoTel() {
        return this.lngNoTel;
    }

    public void setLngNoTel(long lngNoTel) {
        this.lngNoTel = lngNoTel;
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

    public String getStrAdresse() {
        return this.strAdresse;
    }

    public void setStrAdresse(String strAdresse) {
        this.strAdresse = strAdresse;
    }

    public String getStrCodePostal() {
        return this.strCodePostal;
    }

    public void setStrCodePostal(String strCodePostal) {
        this.strCodePostal = strCodePostal;
    }

    public String getStrVille() {
        return this.strVille;
    }

    public void setStrVille(String strVille) {
        this.strVille = strVille;
    }

    public String getStrCourriel() {
        return this.strCourriel;
    }

    public void setStrCourriel(String strCourriel) {
        this.strCourriel = strCourriel;
    }

    @Override
    public String toString() {
        return "{" +
            "lngNoCompte='" + getLngNoCompte() + "'" +
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
