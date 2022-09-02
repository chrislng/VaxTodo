package VaxTodo.Controllers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.AES_GCM_Athentication;
import VaxTodo.Models.Benevole;
import VaxTodo.Models.Benevole.ReturnCrudDataListBenevoles;
import VaxTodo.Models.Employe;
import VaxTodo.Models.Employe.ReturnCrudDataListEmployes;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Models.EnumTypePersonne;
import VaxTodo.Views.Interface.Models.MaskedTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/** Controller of Interface ViewMenuLogin.fxml
 * Reads input and then connects to either Employe account or Benevole account depending on the fields used
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerLogin extends ControllerParent {
    private final Logger logger = Logger.getLogger(ControllerLogin.class.getName());
    // private boolean blnAlertErrorDataFolder, blnAlertErrorDataFileEmploye, blnAlertErrorDataFileBenevole;

    private Stage stage;

    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    // Import the application's controls
    @FXML
    private Label lblInvalidEmploye, lblInvalidBenevole, lblCopyright;

    @FXML
    private Button btnQuit;

    @FXML
    private TextField passwdFieldEmploye, txtFieldPasswdEmploye, passwdFieldBenevole, txtFieldPasswdBenevole;

    @FXML
    private MaskedTextField txtFieldEmploye, txtFieldBenevole;

    @FXML
    private ImageView viewTogglePasswdEmploye, viewTogglePasswdBenevole;

    @FXML
    private WebView webView;

    // Needs to be public for being called by VaxTodo outside package Controllers
    public void setStageAndSetupListeners(Stage stage, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.stage = stage;

        // dont close without express confirmation
        this.stage.setOnCloseRequest(event -> {
            ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.QUIT_APP, null, new MenuInfos());

            // cancel the close request
            event.consume();
        });

        // Set mask for MaskedTextField
        String strMaskCodeIdentificationEmploye = "", strMaskCodeIdentificationBenevole = "";

        for (int i=0; i<Config.intFormatLengthCodeIdentification; i++)
            strMaskCodeIdentificationEmploye += "0";
        for (int i=0; i<Config.intFormatLengthCodeIdentification; i++)
            strMaskCodeIdentificationBenevole += "0";
        
        txtFieldEmploye.setMask(strMaskCodeIdentificationEmploye);
        txtFieldBenevole.setMask(strMaskCodeIdentificationBenevole);

        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource(Config.strInterfaceViewLoginHtml).toString());

        ReturnCrudDataListEmployes returnCrudDataListEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.CREATE, null, true, false, true);
        ReturnCrudDataListBenevoles returnCrudDataListBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.CREATE, null, true, false, true);

        // create Data folder & Employes.csv file & Benevoles.csv file
        boolean blnAlertErrorDataFolder = returnCrudDataListEmployes.getBlnAlertErrorDataFolder() || returnCrudDataListBenevoles.getBlnAlertErrorDataFolder(),
                blnAlertErrorDataFileEmploye = returnCrudDataListEmployes.getBlnAlertErrorDataFileEmploye(),
                blnAlertErrorDataFileBenevole = returnCrudDataListBenevoles.getBlnAlertErrorDataFileBenevole();

        // System.out.println("\nblnAlertErrorDataFileBenevole:\t" + blnAlertErrorDataFileBenevole + "\n");

        if (blnAlertErrorDataFolder || blnAlertErrorDataFileEmploye || blnAlertErrorDataFileBenevole) {
            String strAlertError = "";

            if (blnAlertErrorDataFolder)
                strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
            else
                strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

            if (blnAlertErrorDataFileEmploye)
                strAlertError += "Création d'un nouveau fichier de donnés 'Employes.csv' pour les comptes employés!\n\n";

            if (blnAlertErrorDataFileBenevole)
                strAlertError += "Création d'un nouveau fichier de donnés 'Benevoles.csv' pour les comptes bénévoles!\n\n";

            // System.out.println("\nstrAlertError:\t" + strAlertError + "\n");

            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alertError.showAndWait();
        }

        // hide password textfields
        txtFieldPasswdEmploye.setVisible(false);
        txtFieldPasswdBenevole.setVisible(false);

        // set focus to employe code identification textfield
        txtFieldEmploye.requestFocus();
    }
    
    @FXML
    private void initialize() {
        // System.out.println("\n\nINSIDE\n\n");
        // lblCopyright.setAlignment(Pos.CENTER); // equivalent to CSS alignment="CENTER"
        
        // stage = (Stage) lblCopyright.getScene().getWindow(); // Doesnt work

        // crudDataListEmployes(EnumTypeCRUD.CREATE, Config.strDataFolderPath, false);
    }

    // Creation of methods which are activated on events in the forms
    @FXML
    private void btnQuitClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.QUIT_APP, null, new MenuInfos());
    }
    @FXML
    private void btnForgotPasswdClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.ACCOUNT_INFOS, null, new MenuInfos());
    }

    @FXML
    private void btnTogglePasswdEmployeClick() {
        logger.log(Level.INFO, "Toggle View Password Field Employe!\n");

        if (txtFieldPasswdEmploye.isVisible()) {
            passwdFieldEmploye.setText(txtFieldPasswdEmploye.getText());
            passwdFieldEmploye.setStyle(txtFieldPasswdEmploye.getStyle());

            txtFieldPasswdEmploye.setVisible(false);
            passwdFieldEmploye.setVisible(true);

            viewTogglePasswdEmploye.setImage(new Image(getClass().getResourceAsStream(Config.strImageFileEyeClosed)));

            passwdFieldEmploye.requestFocus();
        }
        else if (passwdFieldEmploye.isVisible()) {
            txtFieldPasswdEmploye.setText(passwdFieldEmploye.getText());
            txtFieldPasswdEmploye.setStyle(passwdFieldEmploye.getStyle());

            txtFieldPasswdEmploye.setVisible(true);
            passwdFieldEmploye.setVisible(false);

            viewTogglePasswdEmploye.setImage(new Image(getClass().getResourceAsStream(Config.strImageFileEyeOpened)));

            txtFieldPasswdEmploye.requestFocus();
        }
    }
    @FXML
    private void btnTogglePasswdBenevoleClick() {
        logger.log(Level.INFO, "Toggle View Password Field Benevole!\n");

        if (txtFieldPasswdBenevole.isVisible()) {
            passwdFieldBenevole.setText(txtFieldPasswdBenevole.getText());
            passwdFieldBenevole.setStyle(txtFieldPasswdBenevole.getStyle());

            txtFieldPasswdBenevole.setVisible(false);
            passwdFieldBenevole.setVisible(true);

            viewTogglePasswdBenevole.setImage(new Image(getClass().getResourceAsStream(Config.strImageFileEyeClosed)));

            passwdFieldBenevole.requestFocus();
        }
        else if (passwdFieldBenevole.isVisible()) {
            txtFieldPasswdBenevole.setText(passwdFieldBenevole.getText());
            txtFieldPasswdBenevole.setStyle(passwdFieldBenevole.getStyle());

            txtFieldPasswdBenevole.setVisible(true);
            passwdFieldBenevole.setVisible(false);

            viewTogglePasswdBenevole.setImage(new Image(getClass().getResourceAsStream(Config.strImageFileEyeOpened)));

            txtFieldPasswdBenevole.requestFocus();
        }
    }
    private String getStrPasswdTextField(EnumTypePersonne enumTypePersonne) {
        switch (enumTypePersonne) {
            case EMPLOYE:
                if (txtFieldPasswdEmploye.isVisible())
                    return txtFieldPasswdEmploye.getText().trim();
                else if (passwdFieldEmploye.isVisible())
                    return passwdFieldEmploye.getText().trim();
                else 
                    return null;

            case BENEVOLE:
                if (txtFieldPasswdBenevole.isVisible())
                    return txtFieldPasswdBenevole.getText().trim();
                else if (passwdFieldBenevole.isVisible())
                    return passwdFieldBenevole.getText().trim();
                else 
                    return null;

            default:
                return null;
        }
    }
    private void setStylePasswdTextField(EnumTypePersonne enumTypePersonne, String strStyle) {
        switch (enumTypePersonne) {
            case EMPLOYE:
                if (txtFieldPasswdEmploye.isVisible()) {
                    txtFieldPasswdEmploye.setStyle(strStyle);
                    passwdFieldEmploye.setStyle(Config.strStyleVoidBorder);
                }
                else if (passwdFieldEmploye.isVisible()) {
                    txtFieldPasswdEmploye.setStyle(Config.strStyleVoidBorder);
                    passwdFieldEmploye.setStyle(strStyle);
                }
                break;
            
            case BENEVOLE:
                if (txtFieldPasswdBenevole.isVisible()) {
                    txtFieldPasswdBenevole.setStyle(strStyle);
                    passwdFieldBenevole.setStyle(Config.strStyleVoidBorder);
                }
                else if (passwdFieldBenevole.isVisible()) {
                    txtFieldPasswdBenevole.setStyle(Config.strStyleVoidBorder);
                    passwdFieldBenevole.setStyle(strStyle);
                }
                break;

            default:
                break;
        }
    }

    @FXML
    private void btnLoginEmployeClick() {
        login(EnumTypePersonne.EMPLOYE);
    }
    @FXML
    private void btnLoginBenevoleClick() {
        login(EnumTypePersonne.BENEVOLE);
    }
    @FXML
    private void btnActionEnterEmploye(ActionEvent event) {
        // System.out.println("\nENTER ACTION\n");

        // if(((KeyEvent) event).getCode().equals(KeyCode.ENTER))
        login(EnumTypePersonne.EMPLOYE);
    }
    @FXML
    private void btnActionEnterBenevole(ActionEvent event) {
        // if(keyEvent.getCode().equals(KeyCode.ENTER))
        login(EnumTypePersonne.BENEVOLE);
    }

    private void login(EnumTypePersonne enumTypePersonne) {
        int intCodeIdentification;
        String strIntCodeIdentification, strPasswd;

        switch (enumTypePersonne) {
            case EMPLOYE:
                // Cannot find TextFields
                if ((txtFieldEmploye == null || txtFieldEmploye.getText() == null) || getStrPasswdTextField(EnumTypePersonne.EMPLOYE) == null) {
                    logger.log(Level.SEVERE, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction); // Please create a ticket and contact your system administrator.\n");
                    System.out.println("");

                    Alert alertError = new Alert(AlertType.ERROR, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction, ButtonType.OK); // Please create a ticket and contact your system administrator.", ButtonType.OK);
                    alertError.showAndWait();
        
                    // System.exit(0);
                }
                else {
                    strIntCodeIdentification = txtFieldEmploye.getText().trim();
                    strPasswd = getStrPasswdTextField(EnumTypePersonne.EMPLOYE); //passwdFieldEmploye.getText().trim();

                    // Set Error Style
                    txtFieldEmploye.setStyle(Config.strStyleErrorBorder);
                    setStylePasswdTextField(EnumTypePersonne.EMPLOYE, Config.strStyleErrorBorder);

                    if ((strIntCodeIdentification == null || strIntCodeIdentification.isBlank()) || (strPasswd == null || strPasswd.isBlank())) {
                        lblInvalidEmploye.setText("Aucun champs ne doit être vide!");
                        lblInvalidEmploye.setStyle(Config.strStyleErrorMessage);
                        // lblInvalidEmploye.setStyle();
            
                        // Clear Error Style
                        if (strIntCodeIdentification != null && !strIntCodeIdentification.isBlank())
                            txtFieldEmploye.setStyle(Config.strStyleSuccessBorder);

                        if (strPasswd != null && !strPasswd.isBlank())
                            setStylePasswdTextField(EnumTypePersonne.EMPLOYE, Config.strStyleSuccessBorder);
                            
                        // txtFieldEmploye.setStyle(Config.strStyleSuccessBorder);
                        // setStylePasswdTextField(EnumTypePersonne.EMPLOYE, Config.strStyleSuccessBorder);
            
                        // if (txtFieldEmploye.getText().trim().isBlank()) {
                        //     txtFieldEmploye.setStyle(Config.strStyleErrorBorder);
                        // }
            
                        // if (passwdFieldEmploye.getText().trim().isBlank()) {
                        //     setStylePasswdTextField(EnumTypePersonne.EMPLOYE, Config.strStyleErrorBorder);
                        // }
                    }
                    else {
                        try {
                            intCodeIdentification = Integer.parseInt(strIntCodeIdentification);
                        }
                        catch (NumberFormatException nfe) {
                            logger.log(Level.WARNING, "Error: Code d'identification n'est pas un nombre entier!\n" + nfe.toString() + "\n");
                            intCodeIdentification = 0;
                        }
                        
                        if (intCodeIdentification <= 0 || strIntCodeIdentification.length() != Config.intFormatLengthCodeIdentification) {
                            lblInvalidEmploye.setText("Erreur code d'identification!");
                            lblInvalidEmploye.setStyle(Config.strStyleErrorMessage);

                            // Clear Error Style
                            setStylePasswdTextField(EnumTypePersonne.EMPLOYE, Config.strStyleSuccessBorder);

                            logger.log(Level.SEVERE, "Le code d'identification d'un employé doit être un chiffre entier et d'une longueur exacte de " + Config.intFormatLengthCodeIdentification + " charactère!\n");
                            System.out.println("");

                            Alert alertError = new Alert(AlertType.ERROR, "Le code d'identification d'un employé doit être un chiffre entier et d'une longueur exacte de " + Config.intFormatLengthCodeIdentification + " charactère!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                        else if (intCodeIdentification>0 && strIntCodeIdentification.length() == Config.intFormatLengthCodeIdentification && checkCredentials(EnumTypePersonne.EMPLOYE, intCodeIdentification, strPasswd)) {
                            logger.log(Level.INFO, "Connexion Employé Réussi!\n");

                            lblInvalidEmploye.setText("Connexion Employé Réussi!");
                            lblInvalidEmploye.setStyle(Config.strStyleSuccessMessage);

                            // Clear Error Style
                            txtFieldEmploye.setStyle(Config.strStyleSuccessBorder);
                            setStylePasswdTextField(EnumTypePersonne.EMPLOYE, Config.strStyleSuccessBorder);
                        
                            ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
                        }
                        else {
                            lblInvalidEmploye.setText("Informations d'authentification incorrectes!");
                            lblInvalidEmploye.setStyle(Config.strStyleErrorMessage);

                            logger.log(Level.WARNING, "Informations d'authentification incorrectes pour le code utilisateur '" + strIntCodeIdentification + "' et le mot de passe '" + strPasswd + "' d'un compte employé!\n");
                            // System.out.println("");
                        }
                    }
                }
                break;

            case BENEVOLE:
                // Cannot find TextFields
                if ((txtFieldBenevole == null || txtFieldBenevole.getText() == null) || getStrPasswdTextField(EnumTypePersonne.BENEVOLE) == null) {
                    logger.log(Level.SEVERE, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction); // Please create a ticket and contact your system administrator.\n");
                    System.out.println("");

                    Alert alertError = new Alert(AlertType.ERROR, "TextFields are NULL or cannot be identified!\n" + Config.strRestartAppInstruction, ButtonType.OK); // Please create a ticket and contact your system administrator.", ButtonType.OK);
                    alertError.showAndWait();

                    // System.exit(0);
                }
                else {
                    strIntCodeIdentification = txtFieldBenevole.getText().trim();
                    strPasswd = getStrPasswdTextField(EnumTypePersonne.BENEVOLE); //passwdFieldBenevole.getText().trim();

                    // Set Error Style
                    txtFieldBenevole.setStyle(Config.strStyleErrorBorder);
                    setStylePasswdTextField(EnumTypePersonne.BENEVOLE, Config.strStyleErrorBorder);

                    if ((strIntCodeIdentification == null || strIntCodeIdentification.isBlank()) || (strPasswd == null || strPasswd.isBlank())) {
                        lblInvalidBenevole.setText("Aucun champs ne doit être vide!");
                        lblInvalidBenevole.setStyle(Config.strStyleErrorMessage);
                        // lblInvalidBenevole.setStyle();

                        // Clear Error Style
                        if (strIntCodeIdentification != null && !strIntCodeIdentification.isBlank())
                            txtFieldBenevole.setStyle(Config.strStyleSuccessBorder);

                        if (strPasswd != null && !strPasswd.isBlank())
                            setStylePasswdTextField(EnumTypePersonne.BENEVOLE, Config.strStyleSuccessBorder);
                            
                        // txtFieldBenevole.setStyle(Config.strStyleSuccessBorder);
                        // setStylePasswdTextField(EnumTypePersonne.BENEVOLE, Config.strStyleSuccessBorder);

                        // if (txtFieldBenevole.getText().trim().isBlank()) {
                        //     txtFieldBenevole.setStyle(Config.strStyleErrorBorder);
                        // }

                        // if (passwdFieldBenevole.getText().trim().isBlank()) {
                        //     setStylePasswdTextField(EnumTypePersonne.BENEVOLE, Config.strStyleErrorBorder);
                        // }
                    }
                    else {
                        try {
                            intCodeIdentification = Integer.parseInt(strIntCodeIdentification);
                        }
                        catch (NumberFormatException nfe) {
                            logger.log(Level.WARNING, "Error: Code d'identification n'est pas un nombre entier!\n" + nfe.toString() + "\n");
                            intCodeIdentification = 0;
                        }
                        
                        if (intCodeIdentification <= 0 || strIntCodeIdentification.length() != Config.intFormatLengthCodeIdentification) {
                            lblInvalidBenevole.setText("Erreur code d'identification!");
                            lblInvalidBenevole.setStyle(Config.strStyleErrorMessage);

                            // Clear Error Style
                            setStylePasswdTextField(EnumTypePersonne.BENEVOLE, Config.strStyleSuccessBorder);

                            logger.log(Level.SEVERE, "Le code d'identification d'un bénévole doit être un chiffre entier et d'une longueur exacte de " + Config.intFormatLengthCodeIdentification + " charactère!\n");
                            System.out.println("");

                            Alert alertError = new Alert(AlertType.ERROR, "Le code d'identification d'un bénévole doit être un chiffre entier et d'une longueur exacte de " + Config.intFormatLengthCodeIdentification + " charactère!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                        else if (intCodeIdentification>0 && strIntCodeIdentification.length() == Config.intFormatLengthCodeIdentification && checkCredentials(EnumTypePersonne.BENEVOLE, intCodeIdentification, strPasswd)) {
                            logger.log(Level.INFO, "Connexion Bénévole Réussi!\n");

                            lblInvalidBenevole.setText("Connexion Bénévole Réussi!");
                            lblInvalidBenevole.setStyle(Config.strStyleSuccessMessage);

                            // Clear Error Style
                            txtFieldBenevole.setStyle(Config.strStyleSuccessBorder);
                            setStylePasswdTextField(EnumTypePersonne.BENEVOLE, Config.strStyleSuccessBorder);
                        
                            ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.BENEVOLE, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
                        }
                        else {
                            lblInvalidBenevole.setText("Informations d'authentification incorrectes!");
                            lblInvalidBenevole.setStyle(Config.strStyleErrorMessage);

                            logger.log(Level.WARNING, "Informations d'authentification incorrectes pour le code utilisateur '" + strIntCodeIdentification + "' et le mot de passe '" + strPasswd + "' d'un compte bénévole!\n");
                        }
                    }
                }
                // if (txtFieldBenevole.getText().trim().isBlank() || passwdFieldBenevole.getText().trim().isBlank() ) {
                //     lblInvalidBenevole.setText("Aucun champs ne doit être vide!");
                //     lblInvalidBenevole.setStyle(Config.strStyleErrorMessage);
        
                //     // Clear error style
                //     txtFieldBenevole.setStyle(Config.strStyleSuccessBorder);
                //     passwdFieldBenevole.setStyle(Config.strStyleSuccessBorder);
        
                //     if (txtFieldBenevole.getText().trim().isBlank()) {
                //         txtFieldBenevole.setStyle(Config.strStyleErrorBorder);
                //     }
        
                //     if (passwdFieldBenevole.getText().trim().isBlank()) {
                //         passwdFieldBenevole.setStyle(Config.strStyleErrorBorder);
                //     }
                // }
                // else {
                //     lblInvalidBenevole.setText("Connexion Bénévole Réussi!");
                //     lblInvalidBenevole.setStyle(Config.strStyleSuccessMessage);
        
                //     txtFieldBenevole.setStyle(Config.strStyleSuccessBorder);
                //     passwdFieldBenevole.setStyle(Config.strStyleSuccessBorder);
                // }
                break;

            case VISITEUR:
            default:
                System.out.println("");
                logger.log(Level.SEVERE, "No valid EnumTypePerson or VISITEUR was given in parameter 'login(EnumTypePersonne)'!\nExiting Application Now!\n");
                stage.close();
                break;
        }
    }

    // validate user login credentials with csv Data file
    private boolean checkCredentials(EnumTypePersonne enumTypePersonne, int intCodeIdentification, String strPasswd) {
        // System.out.println("");
        // for(String[] s : readDataList(Miscellaneous.strDataFileEmployes, EnumPersonneType.EMPLOYE))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println("");
        // for(String[] s : readDataList(Miscellaneous.strDataFileBenevoles))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println("");
        // for(String[] s : readDataList(Miscellaneous.strDataFileVisiteurs))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println();

        switch(enumTypePersonne) {
            case EMPLOYE:
                ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();
                // System.out.println();
                // for (Employe e : arrEmployes)
                //     System.out.println(e + "\n");
                // System.out.println();

                
                // try {
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("Test1234@"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("VzS#z9?R"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("testT1!3@"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("Password1!"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("bUs_54eq"));
                    // System.out.println("\n" + AES_GCM_Athentication.encrypt("Test1234@").equals("Y3loQOsvZkY+xE0asrc41WooINKP0lUrEfVuHvuUDFsdmuO3ndfhTXJ8P+g8hIo2WgPaOrA=")); //.decrypt("Y3loQOsvZkY+xE0asrc41WooINKP0lUrEfVuHvuUDFsdmuO3ndfhTXJ8P+g8hIo2WgPaOrA=").equals("Test12"));
                // } catch (Exception e) {
                //     System.out.println();
                //     logger = 
                //     logger.log(Level.SEVERE, "Erreur de déchiffrement du mot de passe!\n");
                //     System.out.println("");

                //     Alert alertError = new Alert(AlertType.ERROR, "Erreur de déchiffrement du mot de passe!", ButtonType.OK);
                //     alertError.showAndWait();
                // }

                if (arrEmployes != null && arrEmployes.size() > 0)
                    for(Employe employe : arrEmployes) {
                        try {
                            if (employe.getLngNoCompte()>0 && employe.getIntCodeIdentification()>0 &&
                                employe.getIntCodeIdentification() == intCodeIdentification && /*employe.getStrMotDePasse().equals(strPasswd)*/
                                AES_GCM_Athentication.decrypt(employe.getStrMotDePasse()).equals(strPasswd)) {
                                this.currentUserStrFullName = employe.getFullName();
                                this.currentUserLngNoCompte = employe.getLngNoCompte();
                                this.currentUserIntCodeIdentification = employe.getIntCodeIdentification();

                                logger.log(Level.INFO, "Informations de l'employé '" + Long.toString(this.currentUserLngNoCompte) + "' sont valides\n");
                                return true;
                            }
                        }
                        catch (Exception e) {
                            System.out.println();
                            logger.log(Level.SEVERE, "Le mot de passe du compte [No Compte: '" + employe.getLngNoCompte() + "', Code Identification: '" + employe.getIntCodeIdentification() + "'] ne correspond pas au mot de passe fournis '" + strPasswd + "'!\n");
                            System.out.println("");

                            Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du compte [No Compte: '" + employe.getLngNoCompte() + "', Code Identification: '" + employe.getIntCodeIdentification() + "'] ne correspond pas au mot de passe fournis '" + strPasswd + "'!\n" + Config.strRestartAppInstruction, ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                return false;

            case BENEVOLE:
                ArrayList<Benevole> arrBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();
                // System.out.println();
                // for (Employe e : arrEmployes)
                //     System.out.println(e + "\n");
                // System.out.println();

                
                // try {
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("Test1234@"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("VzS#z9?R"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("testT1!3@"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("Password1!"));
                    // System.out.println("\nAES Encrypted Password: " + AES_GCM_Athentication.encrypt("bUs_54eq"));
                    // System.out.println("\n" + AES_GCM_Athentication.encrypt("Test1234@").equals("Y3loQOsvZkY+xE0asrc41WooINKP0lUrEfVuHvuUDFsdmuO3ndfhTXJ8P+g8hIo2WgPaOrA=")); //.decrypt("Y3loQOsvZkY+xE0asrc41WooINKP0lUrEfVuHvuUDFsdmuO3ndfhTXJ8P+g8hIo2WgPaOrA=").equals("Test12"));
                // } catch (Exception e) {
                //     System.out.println();
                //     logger = 
                //     logger.log(Level.SEVERE, "Erreur de déchiffrement du mot de passe!\n");
                //     System.out.println("");

                //     Alert alertError = new Alert(AlertType.ERROR, "Erreur de déchiffrement du mot de passe!", ButtonType.OK);
                //     alertError.showAndWait();
                // }

                if (arrBenevoles != null && arrBenevoles.size() > 0)
                    for(Benevole benevole : arrBenevoles) {
                        try {
                            if (benevole.getLngNoCompte()>0 && benevole.getIntCodeIdentification()>0 &&
                                benevole.getIntCodeIdentification() == intCodeIdentification && /*employe.getStrMotDePasse().equals(strPasswd)*/
                                AES_GCM_Athentication.decrypt(benevole.getStrMotDePasse()).equals(strPasswd)) {
                                this.currentUserStrFullName = benevole.getFullName();
                                this.currentUserLngNoCompte = benevole.getLngNoCompte();
                                this.currentUserIntCodeIdentification = benevole.getIntCodeIdentification();

                                logger.log(Level.INFO, "Informations du bénévole '" + Long.toString(this.currentUserLngNoCompte) + "' sont valides\n");
                                return true;
                            }
                        }
                        catch (Exception e) {
                            System.out.println();
                            logger.log(Level.SEVERE, "Le mot de passe du compte [No Compte: '" + benevole.getLngNoCompte() + "', Code Identification: '" + benevole.getIntCodeIdentification() + "'] ne correspond pas au mot de passe fournis '" + strPasswd + "'!\n");
                            System.out.println("");

                            Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du compte [No Compte: '" + benevole.getLngNoCompte() + "', Code Identification: '" + benevole.getIntCodeIdentification() + "'] ne correspond pas au mot de passe fournis '" + strPasswd + "'!\n" + Config.strRestartAppInstruction, ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                // for(String[] s : readDataList(Config.strDataFileBenevoles))
                //     if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
                //         System.out.println("Informations du bénévole '" + strUsername + "' sont valides");
                //         return true;
                //     }
                return false;

            // case VISITEUR:
            //     for(String[] s : readDataList(Config.strDataFileVisiteurs))
            //         if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
            //             System.out.println("Informations du visiteur '" + strUsername + "' sont valides");
            //             return true;
            //         }
            //     return false;

            case VISITEUR:
            default:
                // System.err.println("\n!!! Have to choose an already defined EnumType !!!\n");
                System.out.println("");
                logger.log(Level.SEVERE, "No valid EnumTypePerson or VISITEUR was given in parameter 'checkCredentials(EnumTypePersonne)'!\nExiting Application Now!\n");
                stage.close();
                return false;
        }
    }
}