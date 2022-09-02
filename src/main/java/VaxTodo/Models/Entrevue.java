package VaxTodo.Models;

import java.util.Arrays;

import VaxTodo.Configs.Config;

/** Used for manipulating Entrevue objects and compare between objects
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Not currently used
 */
public class Entrevue {
    private long lngNoCompteVisiteur;
    private int intEntrevue, intNombreDeDosesRecu;
    private String strDate;
    private String strNoAssuranceMaladie;
    private String[] strSymptomes, strAllergies;
    private boolean blnDejaEuCovid;
    private EnumTypeVaccin enumTypeVaccin;


    public Entrevue() {
        this.intEntrevue = 0;
        this.intNombreDeDosesRecu = 0;
        this.lngNoCompteVisiteur = 0;
        this.strDate = null;
        this.enumTypeVaccin = null;
        this.strNoAssuranceMaladie = "";
        this.strSymptomes = null;
        this.strAllergies = null;
        this.blnDejaEuCovid = false;
    }
    public Entrevue(int intEntrevue, long lngNoCompteVisiteur, int intNombreDeDosesRecu, String strDate, EnumTypeVaccin enumTypeVaccin, String strNoAssuranceMaladie, String[] strSymptomes, String[] strAllergies, boolean blnDejaEuCovid) {
        this.intEntrevue = intEntrevue;
        this.intNombreDeDosesRecu = intNombreDeDosesRecu;
        this.lngNoCompteVisiteur = lngNoCompteVisiteur;
        this.strDate = strDate;
        this.enumTypeVaccin = enumTypeVaccin;
        this.strNoAssuranceMaladie = strNoAssuranceMaladie;
        this.strSymptomes = strSymptomes;
        this.strAllergies = strAllergies;
        this.blnDejaEuCovid = blnDejaEuCovid;
    }

    public int getIntEntrevue() {
        return this.intEntrevue;
    }

    public void setIntEntrevue(int intEntrevue) {
        this.intEntrevue = intEntrevue;
    }

    public int getIntNombreDeDosesRecu() {
        return this.intNombreDeDosesRecu;
    }

    public void setIntNombreDeDosesRecu(int intNombreDeDosesRecu) {
        this.intNombreDeDosesRecu = intNombreDeDosesRecu;
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

    public EnumTypeVaccin getEnumMarqueVaccin() {
        return this.enumTypeVaccin;
    }

    public void setEnumMarqueVaccin(EnumTypeVaccin enumTypeVaccin) {
        this.enumTypeVaccin = enumTypeVaccin;
    }

    public String getStrNoAssuranceMaladie() {
        return this.strNoAssuranceMaladie;
    }

    public void setStrNoAssuranceMaladie(String strNoAssuranceMaladie) {
        this.strNoAssuranceMaladie = strNoAssuranceMaladie;
    }

    public String getStrSymptomes() {
        if (strSymptomes != null && strSymptomes.length > 0) {
            // String strReturn;
            // strReturn = "[";
            // for (String s : this.strSymptomes) 
            //     strReturn += s + Config.strCSVSecondarySeparator + " ";
            // strReturn = strReturn.substring(0, strReturn.length()-2) + "]";
            
            // return strReturn;

            return Arrays.toString(strSymptomes).replace(",", Config.strCSVSecondarySeparator);
        }
        else
            return "[]";
    }
    public String[] getArrSymptomes() {
        return this.strSymptomes;
    }

    public void setArrSymptomes(String[] strSymptomes) {
        this.strSymptomes = strSymptomes;
    }

    public String getStrAllergies() {
        if (strAllergies != null && strAllergies.length > 0) {
            // String strReturn = "[";
            // for (String s : this.strAllergies)
            //     strReturn += s + Config.strCSVSecondarySeparator + " ";
            // strReturn = strReturn.substring(0, strReturn.length()-2) + "]";
            
            // return strReturn;

            return Arrays.toString(strAllergies).replace(",", Config.strCSVSecondarySeparator);
        }
        else 
            return "[]";
    }
    public String[] getArrAllergies() {
        return this.strAllergies;
    }

    public void setArrAllergies(String[] strAllergies) {
        this.strAllergies = strAllergies;
    }

    public boolean isBlnDejaEuCovid() {
        return this.blnDejaEuCovid;
    }

    public boolean getBlnDejaEuCovid() {
        return this.blnDejaEuCovid;
    }

    public void setBlnDejaEuCovid(boolean blnDejaEuCovid) {
        this.blnDejaEuCovid = blnDejaEuCovid;
    }

    public String printInfosEntrevue() {
        return getIntEntrevue() + Config.strCSVMainSeparator + getLngNoCompteVisiteur() + Config.strCSVMainSeparator + getIntNombreDeDosesRecu() + Config.strCSVMainSeparator + getStrDate() + Config.strCSVMainSeparator + getEnumMarqueVaccin() + Config.strCSVMainSeparator + getStrNoAssuranceMaladie() + Config.strCSVMainSeparator + getStrSymptomes() + Config.strCSVMainSeparator + getArrAllergies() + Config.strCSVMainSeparator + isBlnDejaEuCovid();
    }

    @Override
    public String toString() {
        return "{" +
            "intEntrevue='" + getIntEntrevue() + "'" +
            ", lngNoCompteVisiteur='" + getLngNoCompteVisiteur() + "'" +
            ", intNombreDeDosesRecu='" + getIntNombreDeDosesRecu() + "'" +
            ", strDate='" + getStrDate() + "'" +
            ", strMarqueVaccin='" + getEnumMarqueVaccin() + "'" +
            ", strNoAssuranceMaladie='" + getStrNoAssuranceMaladie() + "'" +
            ", strSymptomes='" + getStrSymptomes() + "'" +
            ", strAllergies='" + getStrAllergies() + "'" +
            ", blnDejaEuCovid='" + isBlnDejaEuCovid() + "'" +
            "}";
    }

    // ToDo implemente ajoutQuestionnaire()
    public void ajoutQuestionnaire() {

    }
}
