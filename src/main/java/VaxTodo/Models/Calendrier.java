package VaxTodo.Models;

/** Used for manipulating Calendrier objects and compare between objects
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Not currently used
 */
public class Calendrier {
    private int intRendezVous;
    private String strDate, strHeure;

    public Calendrier() {
        this.intRendezVous = 0;
        this.strDate = "";
        this.strHeure = "";
    }
    public Calendrier(int intRendezVous, String strDate, String strHeure) {
        this.intRendezVous = intRendezVous;
        this.strDate = strDate;
        this.strHeure = strHeure;
    }

    public int getIntRendezVous() {
        return this.intRendezVous;
    }

    public void setIntRendezVous(int intRendezVous) {
        this.intRendezVous = intRendezVous;
    }

    public String getStrDate() {
        return this.strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrHeure() {
        return this.strHeure;
    }

    public void setStrHeure(String strHeure) {
        this.strHeure = strHeure;
    }

    @Override
    public String toString() {
        return "{" +
            "intRendezVous='" + getIntRendezVous() + "'" +
            ", strDate='" + getStrDate() + "'" +
            ", strHeure='" + getStrHeure() + "'" +
            "}";
    }    

    // ToDo implemente imprimerCalendrier()
    public void imprimerCalendrier() {

    }

    // ToDo implemente consulterCalendrier()
    public Calendrier consulterCalendrier() {
        return this;
    }
}
