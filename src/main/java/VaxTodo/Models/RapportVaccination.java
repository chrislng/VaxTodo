package VaxTodo.Models;

/** Used for manipulating RapportVaccination objects and compare between objects
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Not currently used
 */
public class RapportVaccination {
    private long lngNoCompteVisiteur;
    private int intRapportVaccination, intTypeDose;
    private long lngVaccinSerialNumber;
    private String strNom, strPrenom, strNomVaccin, strCodeQR;
    private String strDate;


    public RapportVaccination() {
        this.intRapportVaccination = 0;
        this.lngNoCompteVisiteur = 0;
        this.intTypeDose = 0;
        this.lngVaccinSerialNumber = 0;
        this.strNom = "";
        this.strPrenom = "";
        this.strNomVaccin = "";
        this.strCodeQR = "";
        this.strDate = null;
    }
    public RapportVaccination(int intRapportVaccination, long lngNoCompteVisiteur, int intTypeDose, long lngVaccinSerialNumber, String strNom, String strPrenom, String strNomVaccin, String strCodeQR, String strDate) {
        this.intRapportVaccination = intRapportVaccination;
        this.lngNoCompteVisiteur = lngNoCompteVisiteur;
        this.intTypeDose = intTypeDose;
        this.lngVaccinSerialNumber = lngVaccinSerialNumber;
        this.strNom = strNom;
        this.strPrenom = strPrenom;
        this.strNomVaccin = strNomVaccin;
        this.strCodeQR = strCodeQR;
        this.strDate = strDate;
    }

    public int getIntRapportVaccination() {
        return this.intRapportVaccination;
    }

    public void setIntRapportVaccination(int intRapportVaccination) {
        this.intRapportVaccination = intRapportVaccination;
    }

    public long getLngNoCompteVisiteur() {
        return this.lngNoCompteVisiteur;
    }

    public void setLngNoCompteVisiteur(long lngNoCompteVisiteur) {
        this.lngNoCompteVisiteur = lngNoCompteVisiteur;
    }

    public int getIntTypeDose() {
        return this.intTypeDose;
    }

    public void setIntTypeDose(int intTypeDose) {
        this.intTypeDose = intTypeDose;
    }

    public long getLngVaccinSerialNumber() {
        return this.lngVaccinSerialNumber;
    }

    public void setLngVaccinSerialNumber(long lngVaccinSerialNumber) {
        this.lngVaccinSerialNumber = lngVaccinSerialNumber;
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

    public String getStrNomVaccin() {
        return this.strNomVaccin;
    }

    public void setStrNomVaccin(String strNomVaccin) {
        this.strNomVaccin = strNomVaccin;
    }

    public String getStrCodeQR() {
        return this.strCodeQR;
    }

    public void setStrCodeQR(String strCodeQR) {
        this.strCodeQR = strCodeQR;
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
            "intRapportVaccination='" + getIntRapportVaccination() + "'" +
            ", lngNoCompteVisiteur='" + getLngNoCompteVisiteur() + "'" +
            ", intTypeDose='" + getIntTypeDose() + "'" +
            ", lngVaccinSerialNumber='" + getLngVaccinSerialNumber() + "'" +
            ", strNom='" + getStrNom() + "'" +
            ", strPrenom='" + getStrPrenom() + "'" +
            ", strNomVaccin='" + getStrNomVaccin() + "'" +
            ", strCodeQR='" + getStrCodeQR() + "'" +
            ", strDate='" + getStrDate() + "'" +
            "}";
    }

    // ToDo implemente ajoutProfilVaccination()
    public void ajoutProfilVaccination() {

    }
}
