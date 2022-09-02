package VaxTodo.Controllers;

import java.util.logging.Logger;

import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.EnumTypeMenuInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/** Controller of Interface ViewMenuEmployes.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuEmploye extends ControllerParent {
    private final Logger logger = Logger.getLogger(ControllerMenuEmploye.class.getName());

    private Stage stage;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    @FXML
    private Label lblFullName, lblAccountInfos;

    // @FXML
    // private Button btnMenuGestionEmployes, btnMenuGestionBenevoles, btnMenuGestionVisiteurs;
    
    public void setStageAndSetupListeners(Stage stage, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.stage = stage;
        
        this.currentUserStrFullName = currentUserStrFullName;
        this.currentUserLngNoCompte = currentUserLngNoCompte;
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;

        lblFullName.setText(currentUserStrFullName);
        lblAccountInfos.setText("Numéro du Compte:\t" + Long.toString(currentUserLngNoCompte) + "\nCode Identification:\t\t" + Integer.toString(currentUserIntCodeIdentification));

        // if default user code & password are used for account '111111111111', prompt user to change the default password
        if (currentUserLngNoCompte == Long.parseLong("111111111111") && currentUserIntCodeIdentification == 111111111) {
            Alert alertError = new Alert(AlertType.WARNING, "Ce compte '111111111111' n'est pas sécuritaire et doit seulement être utilisé lors des tests de l'application!\n\nIl est fortement recommandé d'ajouter un nouveau compte employé ou AU MINIMUM de changer le CODE d'IDENTIFICATION et le mot de passe de ce compte!", ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    private void btnDisconnectClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }
    @FXML
    private void btnMenuGestionEmployesClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_EMPLOYES, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    }
    @FXML
    private void btnMenuGestionBenevolesClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_BENEVOLES, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    }
    @FXML
    private void btnMenuGestionVisiteursClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_VISITEURS, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    }
    @FXML
    private void btnMenuGestionRendezVousClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_FORMULAIRES, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    }
    @FXML
    private void btnMenuCalendrierClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_CALENDRIER, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
    }
}
