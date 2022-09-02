package VaxTodo.Controllers;

import java.util.logging.Logger;

import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.EnumTypeMenuInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/** Controller of Interface ViewMenuBenevoles.fxml
 * Display Main Menu of Benevole and what a Benevole account can do with the app
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuBenevole {
    private final Logger logger = Logger.getLogger(ControllerMenuBenevole.class.getName());

    private Stage stage;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    @FXML
    private Label lblFullName, lblAccountInfos;
    
    public void setStageAndSetupListeners(Stage stage, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.stage = stage;
        
        this.currentUserStrFullName = currentUserStrFullName;
        this.currentUserLngNoCompte = currentUserLngNoCompte;
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;

        lblFullName.setText(currentUserStrFullName);
        lblAccountInfos.setText("Numéro du Compte:\t" + Long.toString(currentUserLngNoCompte) + "\nCode Identification:\t\t" + Integer.toString(currentUserIntCodeIdentification));
    }

    @FXML
    private void btnDisconnectClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }
    @FXML
    private void btnMenuGestionVisiteursClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_VISITEURS, EnumTypeMenuInterface.BENEVOLE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
    }
    // @FXML
    // private void btnMenuGestionRendezVousClick() {
    //     ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_FORMULAIRES, EnumTypeMenuInterface.BENEVOLE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    // }
    @FXML
    private void btnMenuCalendrierClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_CALENDRIER, EnumTypeMenuInterface.BENEVOLE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    }
}
