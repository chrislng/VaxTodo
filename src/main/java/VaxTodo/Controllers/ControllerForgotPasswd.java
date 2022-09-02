package VaxTodo.Controllers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.AES_GCM_Athentication;
import VaxTodo.Models.Benevole;
import VaxTodo.Models.Employe;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Models.EnumTypePersonne;
import VaxTodo.Views.Interface.Models.MaskedTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

/** Controller of Interface ViewMenuForgotPasswd.fxml
 * Let the user displays account informations (username and password) when they input a valid account number of either Employe or Benevole
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerForgotPasswd extends ControllerParent {
    private final Logger logger = Logger.getLogger(ControllerForgotPasswd.class.getName());
    // private static boolean blnAlertErrorDataFolder, blnAlertErrorDataFileEmploye, blnAlertErrorDataFileBenevole;

    private Stage stage;

    @FXML
    private Label lblInvalidNoCompte;

    @FXML
    private Button btnGetAccountsInfo;

    @FXML
    private MaskedTextField txtFieldNoCompte;

    @FXML
    private Button btnReturn;

    @FXML
    private RadioButton rbEmploye, rbBenevole;

    // private ArrayList<Employe> arrEmployes;
    private Employe employe;
    private Benevole benevole;

    public void setStageAndSetupListeners(Stage stage, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.stage = stage;

        // dont close without express confirmation
        // this.stage.setOnCloseRequest(event -> {
        //     quitApp();

        //     // cancel the close request
        //     event.consume();
        // });

        // txtFieldNoCompte.requestFocus();

        // Set mask for MaskedTextField
        String strMaskNoCompte = "";

        for (int i=0; i<Config.intFormatLengthNoCompte; i++)
            strMaskNoCompte += "0";
        
        txtFieldNoCompte.setMask(strMaskNoCompte);
    }

    @FXML
    private void rbChecked() {
        txtFieldNoCompte.requestFocus();
    }

    @FXML
    private void btnReturnClick() {
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.RETURN_BACK, new MenuInfos(Config.strInterfaceViewLogin));
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }

    // private void gotoMenu(EnumTypeMenuInterface enumTypeMenuInterface) {
    //     switch(enumTypeMenuInterface) {
    //         case RETURN_BACK:
    //             Alert confirm = new Alert(AlertType.CONFIRMATION, "Retourner au Menu Principal ?", ButtonType.YES, ButtonType.CANCEL);
    //             ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Retourner!");
    //             ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
    //             confirm.showAndWait();

    //             if (confirm.getResult() == ButtonType.YES) {
    //                 System.out.println("");
    //                 // System.out.println("Goto Menu Principal\n");
    //                 logger.log(Level.INFO, "Goto Menu Principal\n");
                    
    //                 try {
    //                     // System.out.println(Config.strInterfaceViewMenuEmploye);
    //                     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Config.strInterfaceViewLogin));
    //                     Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
    //                     scene.getStylesheets().add(Config.strCssFile);

    //                     ControllerLogin controllerLogin = (ControllerLogin) fxmlLoader.getController();
    //                     controllerLogin.setStageAndSetupListeners(stage);

    //                     stage.setTitle("VaxTodo");
    //                     stage.setScene(scene);
    //                     stage.setResizable(false);
    //                     stage.show();
    //                 }
    //                 catch (IOException ioe) {
    //                     // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
    //                     logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
    //                     System.out.println("");

    //                     System.exit(0);
    //                 }
    //                 catch (Exception e) {
    //                     // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
    //                     logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
    //                     System.out.println("");

    //                     System.exit(0);
    //                 }
    //             }
    //             break;

    //         default:
    //             System.out.println("");
    //             logger.log(Level.SEVERE, "No valid EnumTypeMenuInterface was given in parameter 'gotoMenu(EnumTypeMenuInterface)'!\nExiting Application Now!\n");
    //             stage.close();
    //             break;
    //     }
    // }

    @FXML
    private void btnGetAccountsInfoClick() {
        // Cannot find TextFields
        if ((txtFieldNoCompte == null || txtFieldNoCompte.getText() == null) || (rbEmploye == null || rbBenevole == null)) {
            
            logger.log(Level.SEVERE, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction); // Please create a ticket and contact your system administrator.\n");
            System.out.println("");

            Alert alertError = new Alert(AlertType.ERROR, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction, ButtonType.OK); // Please create a ticket and contact your system administrator.", ButtonType.OK);
            alertError.showAndWait();

            // System.exit(0);
        }
        else {
            // if no radio button is selected, show warning
            if (!rbEmploye.isSelected() && !rbBenevole.isSelected()) {
                logger.log(Level.WARNING, "Veuillez séléctionner le type du compte utilisateur!\n");
                System.out.println("");

                Alert alertError = new Alert(AlertType.WARNING, "Veuillez séléctionner le type du compte utilisateur!\n", ButtonType.OK);
                alertError.showAndWait();
            }
            else if (rbEmploye.isSelected())
                getCompteInfosConnexion(EnumTypePersonne.EMPLOYE);
            else if (rbBenevole.isSelected())
                getCompteInfosConnexion(EnumTypePersonne.BENEVOLE);
        }
    }

    @FXML
    private void btnActionEnterNoCompte() {
        // Cannot find TextFields
        if ((txtFieldNoCompte == null || txtFieldNoCompte.getText() == null) || (rbEmploye == null || rbBenevole == null)) {
            logger.log(Level.SEVERE, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction); // Please create a ticket and contact your system administrator.\n");
            System.out.println("");

            Alert alertError = new Alert(AlertType.ERROR, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction, ButtonType.OK); // Please create a ticket and contact your system administrator.", ButtonType.OK);
            alertError.showAndWait();

            // System.exit(0);
        }
        else {
            // if no radio button is selected, show warning
            if (!rbEmploye.isSelected() && !rbBenevole.isSelected()) {
                logger.log(Level.WARNING, "Veuillez séléctionner le type du compte utilisateur!\n");
                System.out.println("");

                Alert alertError = new Alert(AlertType.WARNING, "Veuillez séléctionner le type du compte utilisateur!\n", ButtonType.OK);
                alertError.showAndWait();
            }
            else if (rbEmploye.isSelected())
                getCompteInfosConnexion(EnumTypePersonne.EMPLOYE);
            else if (rbBenevole.isSelected())
                getCompteInfosConnexion(EnumTypePersonne.BENEVOLE);
        }
    }

    private void getCompteInfosConnexion(EnumTypePersonne enumTypePersonne) {
        long lngNoCompte;
        String strNoCompte = txtFieldNoCompte.getText().trim();

        // Set Error Style
        txtFieldNoCompte.setStyle(Config.strStyleErrorBorder);

        if ((strNoCompte == null || strNoCompte.isBlank()) || (strNoCompte == null || strNoCompte.isBlank())) {
            lblInvalidNoCompte.setText("Le no du compte ne doit pas être vide!");
            lblInvalidNoCompte.setStyle(Config.strStyleErrorMessage);
        }
        else {
            try {
                lngNoCompte = Long.parseLong(strNoCompte);
            }
            catch (NumberFormatException nfe) {
                // System.out.println("\nError: Code d'identification n'est pas un nombre entier!\n" + nfe.toString() + "\n");
                logger.log(Level.WARNING, "Erreur NumberFormatException: Le numéro du compte n'est pas un nombre entier!\n"); //, nfe);
                lngNoCompte = 0;
            }
            
            if (lngNoCompte <= 0 || strNoCompte.length() != Config.intFormatLengthNoCompte) {
                lblInvalidNoCompte.setText("Erreur no du compte!");
                lblInvalidNoCompte.setStyle(Config.strStyleErrorMessage);

                logger.log(Level.SEVERE, "Le numéro du compte " + (rbEmploye.isSelected() ? "d'un employé" : "d'un bénévole") + " doit être un chiffre entier et d'une longueur exacte de " + Config.intFormatLengthNoCompte + " charactère!\n");
                System.out.println("");

                Alert alertError = new Alert(AlertType.ERROR, "Le numéro du compte " + (rbEmploye.isSelected() ? "d'un employé" : "d'un bénévole") + " doit être un chiffre entier et d'une longueur exacte de " + Config.intFormatLengthNoCompte + " charactère!", ButtonType.OK);
                alertError.showAndWait();
            }
            else if (lngNoCompte>0 && strNoCompte.length() == Config.intFormatLengthNoCompte && checkAccountInfo((rbEmploye.isSelected() ? EnumTypePersonne.EMPLOYE : EnumTypePersonne.BENEVOLE), lngNoCompte)) {
                lblInvalidNoCompte.setStyle(Config.strStyleSuccessMessage);
                // Clear Error Style
                txtFieldNoCompte.setStyle(Config.strStyleSuccessBorder);

                if (rbEmploye.isSelected()) {
                    lblInvalidNoCompte.setText("Compte Employé trouvé!");

                    try {
                        String strPasswdDecrypted = AES_GCM_Athentication.decrypt(employe.getStrMotDePasse());

                        System.out.println();
                        logger.log(Level.INFO, "Info du compte employé '" + Long.toString(lngNoCompte) + "'\n-------------------------------------------\nCode d'identification:\t" + employe.getIntCodeIdentification() + "\nMot de passe:\t\t\"" + strPasswdDecrypted + "\"\n");
                        System.out.println("");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Info du compte employé '" + Long.toString(lngNoCompte) + "'\n---------------------------------------------------------\nCode d'identification:\t" + employe.getIntCodeIdentification() + "\nMot de passe:\t\t\t\"" + strPasswdDecrypted + "\"\n", ButtonType.OK);
                        alertError.showAndWait();
                    }
                    catch (Exception e) {
                        System.out.println();
                        logger.log(Level.SEVERE, "Le mot de passe du compte employé [No Compte: '" + employe.getLngNoCompte() + "', Code Identification: '" + employe.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction);
                        System.out.println("");

                        Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du compte employé [No Compte: '" + employe.getLngNoCompte() + "', Code Identification: '" + employe.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction, ButtonType.OK); //Contactez votre administrateur système à l'adresse courriel '" + Config.strSystemAdministratorEmail + "'\n", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                else if (rbBenevole.isSelected()) {
                    lblInvalidNoCompte.setText("Compte Bénévole trouvé!");

                    try {
                        String strPasswdDecrypted = AES_GCM_Athentication.decrypt(benevole.getStrMotDePasse());

                        System.out.println();
                        logger.log(Level.INFO, "Info du compte bénévole '" + Long.toString(lngNoCompte) + "'\n-------------------------------------------\nCode d'identification:\t" + benevole.getIntCodeIdentification() + "\nMot de passe:\t\t\"" + strPasswdDecrypted + "\"\n");
                        System.out.println("");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Info du compte bénévole '" + Long.toString(lngNoCompte) + "'\n---------------------------------------------------------\nCode d'identification:\t" + benevole.getIntCodeIdentification() + "\nMot de passe:\t\t\t\"" + strPasswdDecrypted + "\"\n", ButtonType.OK);
                        alertError.showAndWait();
                    }
                    catch (Exception e) {
                        System.out.println();
                        logger.log(Level.SEVERE, "Le mot de passe du compte bénévole [No Compte: '" + benevole.getLngNoCompte() + "', Code Identification: '" + benevole.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction);
                        System.out.println("");

                        Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du compte bénévole [No Compte: '" + benevole.getLngNoCompte() + "', Code Identification: '" + benevole.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction, ButtonType.OK); //Contactez votre administrateur système à l'adresse courriel '" + Config.strSystemAdministratorEmail + "'\n", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
            }
            else {
                if (rbEmploye.isSelected()) {
                    lblInvalidNoCompte.setText("Compte Employé introuvable!");
                    logger.log(Level.WARNING, "Informations du compte employé introuvable pour le code utilisateur fournis '" + strNoCompte + "'!\n");
                }
                else if (rbBenevole.isSelected()) {
                    lblInvalidNoCompte.setText("Compte Bénévole introuvable!");
                    logger.log(Level.WARNING, "Informations du compte bénévole introuvable pour le code utilisateur fournis '" + strNoCompte + "'!\n");
                }

                lblInvalidNoCompte.setStyle(Config.strStyleErrorMessage);
            }
        }
    }

    private boolean checkAccountInfo(EnumTypePersonne enumTypePersonne, long lngNoCompte) {
        switch (enumTypePersonne) {
            case EMPLOYE:
                ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                if (arrEmployes != null && arrEmployes.size() > 0)
                    for(Employe employe : arrEmployes) {
                        if (employe.getLngNoCompte()>0 && employe.getIntCodeIdentification()>0 &&
                            employe.getLngNoCompte() == lngNoCompte) {
                            // System.out.println("Numéro du compte de l'employé '" + Long.toString(lngNoCompte) + "' est valide");
                            logger.log(Level.INFO, "Numéro du compte de l'employé '" + Long.toString(lngNoCompte) + "' est valide\n");
                            this.employe = employe;

                            return true;
                        }
                    }
                return false;

            case BENEVOLE:
                ArrayList<Benevole> arrBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();

                if (arrBenevoles != null && arrBenevoles.size() > 0)
                    for(Benevole benevole : arrBenevoles) {
                        if (benevole.getLngNoCompte()>0 && benevole.getIntCodeIdentification()>0 &&
                            benevole.getLngNoCompte() == lngNoCompte) {
                            // System.out.println("Numéro du compte du bénévole '" + Long.toString(lngNoCompte) + "' est valide");
                            logger.log(Level.INFO, "Numéro du compte du bénévole '" + Long.toString(lngNoCompte) + "' est valide\n");
                            this.benevole = benevole;

                            return true;
                        }
                    }
                return false;

            case VISITEUR:
            default:
                System.out.println("");
                // System.out.println("No EnumTypePerson or VISITEUR was given in parameter 'checkAccountInfo(EnumTypePersonne)'!\n");
                logger.log(Level.SEVERE, "No EnumTypePerson or VISITEUR was given in parameter 'checkAccountInfo(EnumTypePersonne)'!\nExiting Application Now!\n");
                stage.close();
                // System.err.println("\n!!! Have to choose an already defined EnumType !!!\n");
                return false;
        }
    }
}
