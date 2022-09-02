package VaxTodo.Controllers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.AES_GCM_Athentication;
import VaxTodo.Models.Employe;
import VaxTodo.Models.Employe.ReturnCrudDataListEmployes;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Views.Interface.Models.MaskedTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/** Controller of Interface ViewMenuGestionEmployes.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * Let the user add, modify or delete Employe accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuGestionEmployes extends ControllerParent {
    private final Logger logger = Logger.getLogger(ControllerMenuGestionEmployes.class.getName());

    private Stage stage;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;
    
    @FXML
    private Label lblFullName, lblAccountInfos, lblListViewInfos,
                    lblInvalidNoCompte, lblInvalidCodeIdentification, lblInvalidPasswd, lblInvalidNoTel, lblInvalidCourriel, lblInvalidNom, lblInvalidPrenom, lblInvalidAdresse, lblInvalidCodePostal, lblInvalidVille;

    @FXML
    private Button btnMenuAjouterEmploye, btnMenuModifierEmploye, btnMenuSupprimerEmploye;

    @FXML
    private ListView<Employe> lvAccountInfos;

    private int intListViewSelectedIndex;
    private boolean blnListViewSelectedTwice;
    private Employe currentListViewSelectedEmploye;

    @FXML
    private TextField passwdField, txtFieldPasswd;
    
    @FXML
    private MaskedTextField txtFieldNoCompte, txtFieldCodeIdentification, txtFieldNoTel, txtFieldCourriel, txtFieldNom, txtFieldPrenom, txtFieldAdresse, txtFieldCodePostal, txtFieldVille;

    @FXML
    private ImageView viewTogglePasswd;
    
    public void setStageAndSetupListeners(Stage stage, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.stage = stage;
        
        this.currentUserStrFullName = currentUserStrFullName;
        this.currentUserLngNoCompte = currentUserLngNoCompte;
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;

        lblFullName.setText(currentUserStrFullName);
        lblAccountInfos.setText("Numéro du Compte:\t" + Long.toString(currentUserLngNoCompte) + "\nCode Identification:\t\t" + Integer.toString(currentUserIntCodeIdentification));

        // hide password textfields
        txtFieldPasswd.setVisible(false);

        // Set mask for MaskedTextField
        String strMaskNoCompte = "", strMaskCodeIdentification = "", strMaskNoTel = "", strMaskEmail = "", strMaskNom = "", strMaskPrenom = "", strMaskAdresse = "", strMaskCodePostal = "", strMaskVille = "";

        for (int i=0; i<Config.intFormatLengthNoCompte; i++)
            strMaskNoCompte += "0";
        for (int i=0; i<Config.intFormatLengthCodeIdentification; i++)
            strMaskCodeIdentification += "0";
        // for (int i=0; i<Config.intFormatLengthTelephone; i++)
        //     strMaskNoTel += "0";
        strMaskNoTel = "(000) 000-0000";
        for (int i = 0; i<Config.intFormatLengthEmail; i++)
            strMaskEmail += "E";
        for (int i=0; i<Config.intFormatLengthLastName; i++)
            strMaskNom += "S";
        for (int i=0; i<Config.intFormatLengthFirstName; i++)
            strMaskPrenom += "S";
        for (int i=0; i<Config.intFormatLengthAdresse; i++)
            strMaskAdresse += "A";
        // for (int i=0; i<Config.intFormatLengthCodePostal; i++)
        //     strMaskCodePostal += "A";
        strMaskCodePostal += "P0P 0P0";
        for (int i=0; i<Config.intFormatLengthVille; i++)
            strMaskVille += "S";

        txtFieldNoCompte.setMask(strMaskNoCompte);
        txtFieldCodeIdentification.setMask(strMaskCodeIdentification);
        txtFieldNoTel.setMask(strMaskNoTel);
        txtFieldCourriel.setMask(strMaskEmail);
        txtFieldNom.setMask(strMaskNom);
        txtFieldPrenom.setMask(strMaskPrenom);
        txtFieldAdresse.setMask(strMaskAdresse);
        txtFieldCodePostal.setMask(strMaskCodePostal);
        txtFieldVille.setMask(strMaskVille);
        
        // init ListView and its data
        // currentListViewSelectedEmploye = null;
        lvAccountInfos.setCellFactory(cell -> new ListCell<Employe>() {
            private ImageView imageView;

            @Override
            public void updateItem(Employe employe, boolean blnVide) {
                super.updateItem(employe, blnVide);

                if (blnVide || employe == null || employe.getListViewAccountInfos(true) == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent");
                    setGraphic(null);
                }
                else {
                    imageView = new ImageView(new Image(getClass().getResourceAsStream(Config.strImageFileAccountInfos)));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);

                    setText(employe.getListViewAccountInfos(true));
                    setStyle("");
                }
            }
        });
        lvAccountInfos.getSelectionModel().selectedItemProperty().addListener(changeListener -> {
            // lvAccountInfos.getSelectionModel().clearSelection(lvAccountInfos.getSelectionModel().getSelectedIndex());
            // System.out.println("\nListView getSelectedIndex:\t"+lvAccountInfos.getSelectionModel().getSelectedItem()+"\n"); //lvAccountInfos.getSelectionModel().getSelectedIndex()+"\n");
            // System.out.println("\n" + lvAccountInfos.getSelectionModel(). + "\n");

            try {
                if (!blnListViewSelectedTwice && blnTextFieldsDiscardChanges()) {
                    currentListViewSelectedEmploye = lvAccountInfos.getSelectionModel().getSelectedItem();
                    intListViewSelectedIndex = lvAccountInfos.getSelectionModel().getSelectedIndex();

                    if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe) {
                        restoreAllFields();
                    }
                }
                // clear listview selection if user wants to stay modifying previously changed textfields
                else {
                    logger.log(Level.INFO, "intListViewSelectedIndex: " + intListViewSelectedIndex + ", ListView Size: " + lvAccountInfos.getItems().size() + "\n");
                    // list view select previously selected item
                    if (intListViewSelectedIndex > -1 && intListViewSelectedIndex < lvAccountInfos.getItems().size()) {
                        if (blnListViewSelectedTwice)
                            blnListViewSelectedTwice = false;
                        else
                            blnListViewSelectedTwice = true;

                        logger.log(Level.INFO, "ListView reset to previously selected item\n");
                        // lvAccountInfos.getSelectionModel().clearAndSelect(intListViewSelectedIndex);
                        lvAccountInfos.scrollTo(intListViewSelectedIndex);
                        lvAccountInfos.getSelectionModel().select(intListViewSelectedIndex);
                        lvAccountInfos.getFocusModel().focus(intListViewSelectedIndex);
                    }
                    // delete list view & reconstruct it if no item was selected
                    else {
                        logger.log(Level.INFO, "ListView Soft Reset\n");
                        resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), false);

                        // if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe) {
                        //     logger.log(Level.INFO, "ListView Hard Reset\n");
                        //     resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                        // }
                        // else {
                        //     logger.log(Level.INFO, "ListView Soft Reset\n");
                        //     resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), false);
                        // }
                    }
                }
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "Exception occured while changing list view selection of 'lvAccountInfos.getSelectionModel().selectedItemProperty()'!\n", e);
            }
        });

        resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
    }

    private void resetListView(ArrayList<Employe> arrEmployes, boolean blnResetHard, boolean ...blnArrOptional) {
        boolean blnClearListBeforeTextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforeTextFields = blnArrOptional[0];

        // reset hard by also clearing textfields & reseting currentListViewSelectedEmploye
        if (blnResetHard) {
            // System.out.println("\n--->Textfields cleared\n");
            // clearAllFields(blnClearListBeforeTextFields); //! MAY GENERATE A BUG!!! 

            intListViewSelectedIndex = -1;
            blnListViewSelectedTwice = false;

            // System.out.println("\n--->New Employe\n");
            currentListViewSelectedEmploye = new Employe();
            try {
                currentListViewSelectedEmploye.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                currentListViewSelectedEmploye.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }

            clearAllFields(); //! MAY GENERATE A BUG!!! 
        }

        // System.out.println("\nInside method resetListView\n");
        lvAccountInfos.getItems().clear();
        // System.out.println("\nCleared ListView items\n");

        // lvAccountInfos.getSelectionModel().clearSelection();
        // ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, Config.strNewDataFileEmployes, null).getArrEmployes();

        // System.out.println("\n\nArrEmployes Size: " + arrEmployes.size() + "\n\n");

        if (arrEmployes != null && arrEmployes.size() > 0) {
            btnMenuModifierEmploye.setDisable(false);
            btnMenuSupprimerEmploye.setDisable(false);

            lblListViewInfos.setText("Liste des Employés");
            // for (int i=0; i<15;i++)
            lvAccountInfos.getItems().addAll(arrEmployes);
        }
        else {
            btnMenuModifierEmploye.setDisable(true);
            btnMenuSupprimerEmploye.setDisable(true);

            lblListViewInfos.setText("Liste des Employés VIDES!");

            logger.log(Level.SEVERE, "Aucun compte employé VALIDE présent dans le fichier Employes.csv!\nVeuillez ajouter un nouveau compte employé ou se déconnecter pour que l'application créé un compte employé automatiquement!\n");

            Alert alertError = new Alert(AlertType.ERROR, "Aucun compte employé VALIDE présent dans le fichier Employes.csv\n\nVeuillez ajouter un nouveau compte employé ou se déconnecter pour que l'application créé un compte employé automatiquement", ButtonType.OK);
            alertError.showAndWait();
        }

        // System.out.println("\nSet ListView items\n");

        // System.out.println("\nEnd of method resetListView\n");
    }
    private void clearAllFields(boolean ...blnArrOptional) {
        // disable textfield No Compte if it is current selected employe
        // if (!(lvAccountInfos.getSelectionModel().isEmpty()) && currentListViewSelectedEmploye instanceof Employe && currentListViewSelectedEmploye.getLngNoCompte() == currentUserLngNoCompte)

        boolean blnClearListBeforeTextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforeTextFields = blnArrOptional[0];

        // System.out.println("\n\nblnClearListBeforeTextFields: " + blnClearListBeforeTextFields + "\n\n");

        if (blnClearListBeforeTextFields) {
            currentListViewSelectedEmploye = null;
            // lvAccountInfos.getItems().clear(); // Seriously WTF, fuck ListViews & fuck Java for calling 'selectedItemProperty()' when deleting list view

            System.out.println("\n\nCleared List View Before TextFields!\n\n");
        }

        // if an account from list view is selected, disable Textfield No Compte
        if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe && currentListViewSelectedEmploye.getLngNoCompte() == currentUserLngNoCompte && !(lvAccountInfos.getSelectionModel().isEmpty()))
        /*&& lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Employe*/
            // if current user employe is selected in listview, disable Textfield No Compte
            txtFieldNoCompte.setDisable(true);
        else {
            txtFieldNoCompte.setDisable(false);

            txtFieldNoCompte.clear();
            txtFieldNoCompte.setText("");
            txtFieldNoCompte.setPlainText("");
        }

        // clear Textfields Text
        txtFieldCodeIdentification.clear();
        txtFieldNoTel.clear();
        txtFieldCourriel.clear();
        txtFieldNom.clear();
        txtFieldPrenom.clear();
        txtFieldAdresse.clear();
        txtFieldCodePostal.clear();
        txtFieldVille.clear();

        passwdField.clear();
        txtFieldPasswd.clear();

        // set Textfields text to empty        
        txtFieldCodeIdentification.setText("");
        txtFieldNoTel.setText("");
        txtFieldCourriel.setText("");
        txtFieldNom.setText("");
        txtFieldPrenom.setText("");
        txtFieldAdresse.setText("");
        txtFieldCodePostal.setText("");
        txtFieldVille.setText("");

        passwdField.setText("");
        txtFieldPasswd.setText("");

        // set Textfields plaintext to empty
        txtFieldCodeIdentification.setPlainText("");
        txtFieldNoTel.setPlainText("");
        txtFieldCourriel.setPlainText("");
        txtFieldNom.setPlainText("");
        txtFieldPrenom.setPlainText("");
        txtFieldAdresse.setPlainText("");
        txtFieldCodePostal.setPlainText("");
        txtFieldVille.setPlainText("");
        // passwdField.clear();
        // txtFieldPasswd.clear();

        // clear Labels Text
        lblInvalidNoCompte.setText("");
        lblInvalidCodeIdentification.setText("");
        lblInvalidNoTel.setText("");
        lblInvalidCourriel.setText("");
        lblInvalidNom.setText("");
        lblInvalidPrenom.setText("");
        lblInvalidAdresse.setText("");
        lblInvalidCodePostal.setText("");
        lblInvalidVille.setText("");
        lblInvalidPasswd.setText("");

        // clear Textfields Style
        txtFieldNoCompte.setStyle(Config.strStyleSuccessBorder);
        txtFieldCodeIdentification.setStyle(Config.strStyleSuccessBorder);
        txtFieldNoTel.setStyle(Config.strStyleSuccessBorder);
        txtFieldCourriel.setStyle(Config.strStyleSuccessBorder);
        txtFieldNom.setStyle(Config.strStyleSuccessBorder);
        txtFieldPrenom.setStyle(Config.strStyleSuccessBorder);
        txtFieldAdresse.setStyle(Config.strStyleSuccessBorder);
        txtFieldCodePostal.setStyle(Config.strStyleSuccessBorder);
        txtFieldVille.setStyle(Config.strStyleSuccessBorder);

        passwdField.setStyle(Config.strStyleSuccessBorder);
        txtFieldPasswd.setStyle(Config.strStyleSuccessBorder);

        // clear Labels Style
        lblInvalidNoCompte.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCodeIdentification.setStyle(Config.strStyleSuccessMessage);
        lblInvalidNoTel.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCourriel.setStyle(Config.strStyleSuccessMessage);
        lblInvalidNom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidPrenom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidAdresse.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCodePostal.setStyle(Config.strStyleSuccessMessage);
        lblInvalidVille.setStyle(Config.strStyleSuccessMessage);
        lblInvalidPasswd.setStyle(Config.strStyleSuccessMessage);
    }
    private void restoreAllFields(boolean ...blnArrOptional) {
        clearAllFields();
                        
        // if (currentListViewSelectedEmploye.getLngNoCompte() == currentUserLngNoCompte)
        //     txtFieldNoCompte.setDisable(true);
        // else
        //     txtFieldNoCompte.setDisable(false);

        // init Textfields with data from selected Employe
        txtFieldNoCompte.setPlainText(Long.toString(currentListViewSelectedEmploye.getLngNoCompte()));
        txtFieldCodeIdentification.setPlainText(Integer.toString(currentListViewSelectedEmploye.getIntCodeIdentification()));
        txtFieldNoTel.setPlainText(Long.toString(currentListViewSelectedEmploye.getLngNoTel()));
        txtFieldCourriel.setPlainText(currentListViewSelectedEmploye.getStrCourriel());
        txtFieldNom.setPlainText(currentListViewSelectedEmploye.getStrNom());
        txtFieldPrenom.setPlainText(currentListViewSelectedEmploye.getStrPrenom());
        txtFieldAdresse.setPlainText(currentListViewSelectedEmploye.getStrAdresse());
        txtFieldCodePostal.setPlainText(currentListViewSelectedEmploye.getStrCodePostal());
        txtFieldVille.setPlainText(currentListViewSelectedEmploye.getStrVille());

        try {
            // txtFieldPasswd.setPlainText(currentListViewSelectedEmploye.getStrMotDePasse());
            String strPasswdDecrypted = AES_GCM_Athentication.decrypt(currentListViewSelectedEmploye.getStrMotDePasse());
            setStrPasswdTextField(strPasswdDecrypted);
        }
        catch (Exception e) {
            setStrPasswdTextField("");

            System.out.println();
            logger.log(Level.SEVERE, "Le mot de passe du compte employé [No Compte: '" + currentListViewSelectedEmploye.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedEmploye.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction);
            System.out.println("");

            Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du compte employé [No Compte: '" + currentListViewSelectedEmploye.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedEmploye.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction, ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    private void btnDisconnectClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }

    @FXML
    private void btnReturnClick() {
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.RETURN_BACK, new MenuInfos(Config.strInterfaceViewMenuEmploye, currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification, "Menu Employé"));
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
    }

    @FXML
    private void btnMenuAjouterEmployeClick() {
        crudAccountInfos(EnumTypeMenuInterface.ADD);
    }
    @FXML
    private void btnMenuModifierEmployeClick() {
        crudAccountInfos(EnumTypeMenuInterface.MODIFY);
    }
    @FXML
    private void btnMenuSupprimerEmployeClick() {
        crudAccountInfos(EnumTypeMenuInterface.DELETE);
    }
    @FXML
    private void btnClearAllFieldsClick() {
        Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment EFFACER tous les champs de texte ?\n\n", ButtonType.YES, ButtonType.CANCEL);
        ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Effacer!");
        ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES)
            clearAllFields();
    }
    @FXML
    private void btnRestoreAllFieldsClick() {
        if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Employe && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment RESTAURER tous les champs de texte ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Restaurer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();
    
            if (confirm.getResult() == ButtonType.YES)
                restoreAllFields();
        
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner un compte employé parmi la liste des employés!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte employé parmi la liste des employés!", ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    private void btnTogglePasswdClick() {
        logger.log(Level.INFO, "Toggle View Password Field Gestion Employe!\n");

        if (txtFieldPasswd.isVisible()) {
            passwdField.setText(txtFieldPasswd.getText());
            passwdField.setStyle(txtFieldPasswd.getStyle());

            txtFieldPasswd.setVisible(false);
            passwdField.setVisible(true);

            viewTogglePasswd.setImage(new Image(getClass().getResourceAsStream(Config.strImageFileEyeClosed)));

            passwdField.requestFocus();
        }
        else if (passwdField.isVisible()) {
            txtFieldPasswd.setText(passwdField.getText());
            txtFieldPasswd.setStyle(passwdField.getStyle());

            txtFieldPasswd.setVisible(true);
            passwdField.setVisible(false);

            viewTogglePasswd.setImage(new Image(getClass().getResourceAsStream(Config.strImageFileEyeOpened)));

            txtFieldPasswd.requestFocus();
        }
    }
    private String getStrPasswdTextField() {
        if (txtFieldPasswd.isVisible())
            return txtFieldPasswd.getText().trim();
        else if (passwdField.isVisible())
            return passwdField.getText().trim();
        else 
            return ""; //null;
    }
    private void setStrPasswdTextField(String strText) {
        if (txtFieldPasswd.isVisible()) {
            txtFieldPasswd.setText(strText.trim());
            passwdField.setText("");
        }
        else if (passwdField.isVisible()) {
            txtFieldPasswd.setText("");
            passwdField.setText(strText.trim());
        }
    }
    private void setStylePasswdTextField(String strStyle) {
        if (txtFieldPasswd.isVisible()) {
            txtFieldPasswd.setStyle(strStyle);
            passwdField.setStyle(Config.strStyleVoidBorder);
        }
        else if (passwdField.isVisible()) {
            txtFieldPasswd.setStyle(Config.strStyleVoidBorder);
            passwdField.setStyle(strStyle);
        }
    }

    private class ReturnGetEmployeFromTextFields{
        private Employe employe;
        private boolean blnErrorNoCompte, blnErrorCodeIdentification, blnErrorMotDePasse, blnErrorNoTel, blnErrorCourriel, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille;

        public ReturnGetEmployeFromTextFields() {
            this.employe = new Employe();
            try {
                this.employe.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                this.employe.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }

            this.blnErrorNoCompte = false;
            this.blnErrorCodeIdentification = false;
            this.blnErrorMotDePasse = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;
        }
        public ReturnGetEmployeFromTextFields(Employe employe) {
            this.employe = employe;

            this.blnErrorNoCompte = false;
            this.blnErrorCodeIdentification = false;
            this.blnErrorMotDePasse = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;
        }
        public ReturnGetEmployeFromTextFields(boolean blnErrorNoCompte, boolean blnErrorCodeIdentification, boolean blnErrorMotDePasse, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille) {
            this.employe = new Employe();
            try {
                this.employe.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                this.employe.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorCodeIdentification = blnErrorCodeIdentification;
            this.blnErrorMotDePasse = blnErrorMotDePasse;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;
        }
        public ReturnGetEmployeFromTextFields(Employe employe, boolean blnErrorNoCompte, boolean blnErrorCodeIdentification, boolean blnErrorMotDePasse, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille) {
            this.employe = employe;

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorCodeIdentification = blnErrorCodeIdentification;
            this.blnErrorMotDePasse = blnErrorMotDePasse;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;
        }

        public Employe getEmploye() {
            return this.employe;
        }
        public boolean getBlnErrorNoCompte() {
            return this.blnErrorNoCompte;
        }
        public boolean getBlnErrorCodeIdentification() {
            return this.blnErrorCodeIdentification;
        }
        public boolean getBlnErrorMotDePasse() {
            return this.blnErrorMotDePasse;
        }
        public boolean getBlnErrorNoTel() {
            return this.blnErrorNoTel;
        }
        public boolean getBlnErrorCourriel() {
            return this.blnErrorCourriel;
        }
        public boolean getBlnErrorNom() {
            return this.blnErrorNom;
        }
        public boolean getBlnErrorPrenom() {
            return this.blnErrorPrenom;
        }
        public boolean getBlnErrorAdresse() {
            return this.blnErrorAdresse;
        }
        public boolean getBlnErrorCodePostal() {
            return this.blnErrorCodePostal;
        }
        public boolean getBlnErrorVille() {
            return this.blnErrorVille;
        }

        public void setEmploye(Employe employe) {
            this.employe = employe;
        }
        public void setBlnErrorNoCompte(boolean blnErrorNoCompte) {
            this.blnErrorNoCompte = blnErrorNoCompte;
        }
        public void setBlnErrorCodeIdentification(boolean blnErrorCodeIdentification) {
            this.blnErrorCodeIdentification = blnErrorCodeIdentification;
        }
        public void setBlnErrorMotDePasse(boolean blnErrorMotDePasse) {
            this.blnErrorMotDePasse = blnErrorMotDePasse;
        }
        public void setBlnErrorNoTel(boolean blnErrorNoTel) {
            this.blnErrorNoTel = blnErrorNoTel;
        }
        public void setBlnErrorCourriel(boolean blnErrorCourriel) {
            this.blnErrorCourriel = blnErrorCourriel;
        }
        public void setBlnErrorNom(boolean blnErrorNom) {
            this.blnErrorNom = blnErrorNom;
        }
        public void setBlnErrorPrenom(boolean blnErrorPrenom) {
            this.blnErrorPrenom = blnErrorPrenom;
        }
        public void setBlnErrorAdresse(boolean blnErrorAdresse) {
            this.blnErrorAdresse = blnErrorAdresse;
        }
        public void setBlnErrorCodePostal(boolean blnErrorCodePostal) {
            this.blnErrorCodePostal = blnErrorCodePostal;
        }
        public void setBlnErrorVille(boolean blnErrorVille) {
            this.blnErrorVille = blnErrorVille;
        }
    }
    private ReturnGetEmployeFromTextFields getEmployeFromTextFields() {
        // Set All TextFields to error style, and only change when input is valid
        txtFieldNoCompte.setStyle(Config.strStyleErrorBorder);
        txtFieldCodeIdentification.setStyle(Config.strStyleErrorBorder);
        txtFieldNoTel.setStyle(Config.strStyleErrorBorder);
        txtFieldCourriel.setStyle(Config.strStyleErrorBorder);
        txtFieldNom.setStyle(Config.strStyleErrorBorder);
        txtFieldPrenom.setStyle(Config.strStyleErrorBorder);
        txtFieldAdresse.setStyle(Config.strStyleErrorBorder);
        txtFieldCodePostal.setStyle(Config.strStyleErrorBorder);
        txtFieldVille.setStyle(Config.strStyleErrorBorder);
        setStylePasswdTextField(Config.strStyleErrorBorder);

        // Set All Labels to error style, and only change when input is valid
        lblInvalidNoCompte.setStyle(Config.strStyleErrorMessage);
        lblInvalidCodeIdentification.setStyle(Config.strStyleErrorMessage);
        lblInvalidNoTel.setStyle(Config.strStyleErrorMessage);
        lblInvalidCourriel.setStyle(Config.strStyleErrorMessage);
        lblInvalidNom.setStyle(Config.strStyleErrorMessage);
        lblInvalidPrenom.setStyle(Config.strStyleErrorMessage);
        lblInvalidAdresse.setStyle(Config.strStyleErrorMessage);
        lblInvalidCodePostal.setStyle(Config.strStyleErrorMessage);
        lblInvalidVille.setStyle(Config.strStyleErrorMessage);
        lblInvalidPasswd.setStyle(Config.strStyleErrorMessage);

        // init all variables that will be returned
        Employe employe = new Employe();

        boolean blnErrorNoCompte = false, blnErrorCodeIdentification = false, blnErrorMotDePasse = false, blnErrorNoTel = false, blnErrorCourriel = false, blnErrorNom = false, blnErrorPrenom = false, blnErrorAdresse = false, blnErrorCodePostal = false, blnErrorVille = false;

        // init all variables that will be used inside this function
        long lngNoCompte = Long.parseLong("0"), lngNoTel = Long.parseLong("0");
        int intCodeIdentification = 0;

        String strNoCompte = "", strCodeIdentification = "", strNoTel = "";
        String strMotDePasse = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        // "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[!?-@#$%^&+=])\\S{8,}\\z"
        String regexPasswordValidator = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$%^&+=]).{8,}";
        
        try {
            strNoCompte = txtFieldNoCompte.getPlainText();
            lngNoCompte = Long.parseLong(strNoCompte);
        } 
        catch (NumberFormatException nfe) {
            blnErrorNoCompte = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'lngNoCompte' for TextField 'txtFieldNoCompte'\n"); //, nfe);
            lngNoCompte = Long.parseLong("0");
        }
        if (lngNoCompte > 0 && strNoCompte.length() == Config.intFormatLengthNoCompte) {
            txtFieldNoCompte.setStyle(Config.strStyleSuccessBorder);
            lblInvalidNoCompte.setText("Numéro du Compte Valide");
            lblInvalidNoCompte.setStyle(Config.strStyleSuccessMessage);
        }
        else if (lngNoCompte <= 0) {
            blnErrorNoCompte = true;
            lblInvalidNoCompte.setText("Doit être un chiffre entier et supérieur à 0!");
        }
        else if (strNoCompte.length() != Config.intFormatLengthNoCompte) {
            blnErrorNoCompte = true;
            lblInvalidNoCompte.setText("Longueur exacte à " + Config.intFormatLengthNoCompte + " charactères!");
        }

        try {
            strCodeIdentification = txtFieldCodeIdentification.getPlainText();
            intCodeIdentification = Integer.parseInt(strCodeIdentification);
        } 
        catch (NumberFormatException nfe) {
            blnErrorCodeIdentification = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'intCodeIdentification' for TextField 'txtFieldCodeIdentification'\n"); //, nfe);
            intCodeIdentification = 0;
        }
        if (intCodeIdentification > 0 && strCodeIdentification.length() == Config.intFormatLengthCodeIdentification) {
            txtFieldCodeIdentification.setStyle(Config.strStyleSuccessBorder);
            lblInvalidCodeIdentification.setText("Code Identification Valide");
            lblInvalidCodeIdentification.setStyle(Config.strStyleSuccessMessage);
        }
        else if (intCodeIdentification <= 0) {
            blnErrorCodeIdentification = true;
            lblInvalidCodeIdentification.setText("Doit être un chiffre entier et supérieur à 0!");
        }
        else if (strCodeIdentification.length() != Config.intFormatLengthCodeIdentification) {
            blnErrorCodeIdentification = true;
            lblInvalidCodeIdentification.setText("Longueur exacte à " + Config.intFormatLengthCodeIdentification + " charactères!");
        }

        strMotDePasse = getStrPasswdTextField();
        if (strMotDePasse.length() >= Config.intFormatLengthPassword && Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) {
            setStylePasswdTextField(Config.strStyleSuccessBorder);
            lblInvalidPasswd.setText("Mot de Passe Valide");
            lblInvalidPasswd.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strMotDePasse.length() < Config.intFormatLengthPassword) {
            blnErrorMotDePasse = true;
            lblInvalidPasswd.setText("Longueur exacte ou supérieur à " + Config.intFormatLengthPassword + " charactères!");
        }
        else if (!(Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find())) {
            blnErrorMotDePasse = true;
            lblInvalidPasswd.setText("1 chiffre, 1 MAJ., 1 MIN. et 1 car. spécial"); // parmis [! ? - @ # $ % ^ & + =]
        }
        
        try {
            strNoTel = txtFieldNoTel.getPlainText();
            lngNoTel = Long.parseLong(strNoTel);
        } 
        catch (NumberFormatException nfe) {
            blnErrorNoTel = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'lngNoTel' for TextField 'txtFieldNoTel'\n"); //, nfe);
            lngNoTel = Long.parseLong("0");
        }
        if (lngNoTel > 0 && strNoTel.length() == Config.intFormatLengthTelephone) {
            blnErrorNoTel = false;
            txtFieldNoTel.setStyle(Config.strStyleSuccessBorder);
            lblInvalidNoTel.setText("Numéro de Téléphone Valide");
            lblInvalidNoTel.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strNoTel == null || strNoTel.isEmpty() || lngNoTel == 0) {
            blnErrorNoTel = false;
            txtFieldNoTel.setStyle(Config.strStyleSuccessBorder);
            lblInvalidNoTel.setText("");
            lblInvalidNoTel.setStyle(Config.strStyleSuccessMessage);
        }
        else if (lngNoTel < 0) {
            blnErrorNoTel = true;
            lblInvalidNoTel.setText("Doit être un chiffre entier et supérieur à 0!");
        }
        else if (!(strNoTel == null || strNoTel.isEmpty()) && strNoTel.length() != Config.intFormatLengthTelephone) {
            blnErrorNoTel = true;
            lblInvalidNoTel.setText("Longueur exacte à " + Config.intFormatLengthTelephone + " charactères!");
        }

        strCourriel = txtFieldCourriel.getPlainText();
        if (strCourriel.length() <= Config.intFormatLengthEmail) {
            txtFieldCourriel.setStyle(Config.strStyleSuccessBorder);
            lblInvalidCourriel.setText("Courriel Valide");
            lblInvalidCourriel.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strCourriel.length() > Config.intFormatLengthEmail) {
            blnErrorCourriel = true;
            lblInvalidCourriel.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthEmail + " charactères!");
        }

        strNom = txtFieldNom.getPlainText();
        if (strNom.length() <= Config.intFormatLengthLastName) {
            txtFieldNom.setStyle(Config.strStyleSuccessBorder);
            lblInvalidNom.setText("Nom Valide");
            lblInvalidNom.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strNom.length() > Config.intFormatLengthLastName) {
            blnErrorNom = true;
            lblInvalidNom.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthLastName + " charactères!");
        }
        
        strPrenom = txtFieldPrenom.getPlainText();
        if (strPrenom.length() <= Config.intFormatLengthFirstName) {
            txtFieldPrenom.setStyle(Config.strStyleSuccessBorder);
            lblInvalidPrenom.setText("Prénom Valide");
            lblInvalidPrenom.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strPrenom.length() > Config.intFormatLengthFirstName) {
            blnErrorPrenom = true;
            lblInvalidPrenom.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthFirstName + " charactères!");
        }

        strAdresse = txtFieldAdresse.getPlainText();
        if (strAdresse.length() <= Config.intFormatLengthAdresse) {
            txtFieldAdresse.setStyle(Config.strStyleSuccessBorder);
            lblInvalidAdresse.setText("Adresse Valide");
            lblInvalidAdresse.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strAdresse.length() > Config.intFormatLengthAdresse) {
            blnErrorAdresse = true;
            lblInvalidAdresse.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthAdresse + " charactères!");
        }

        strCodePostal = txtFieldCodePostal.getPlainText();
        if (strCodePostal.length() == Config.intFormatLengthCodePostal) {
            txtFieldCodePostal.setStyle(Config.strStyleSuccessBorder);
            lblInvalidCodePostal.setText("Code Postal Valide");
            lblInvalidCodePostal.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strCodePostal == null || strCodePostal.isEmpty()) {
            txtFieldCodePostal.setStyle(Config.strStyleSuccessBorder);
            lblInvalidCodePostal.setText("");
            lblInvalidCodePostal.setStyle(Config.strStyleSuccessMessage);
        }
        else if (!(strCodePostal == null || strCodePostal.isEmpty()) && strCodePostal.length() != Config.intFormatLengthCodePostal) {
            blnErrorCodePostal = true;
            lblInvalidCodePostal.setText("Longueur exacte à " + Config.intFormatLengthCodePostal + " charactères! (A0A0A0)");
        }

        strVille = txtFieldVille.getPlainText();
        if (strVille.length() <= Config.intFormatLengthVille) {
            txtFieldVille.setStyle(Config.strStyleSuccessBorder);
            lblInvalidVille.setText("Ville Valide");
            lblInvalidVille.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strVille.length() > Config.intFormatLengthVille) {
            blnErrorVille = true;
            lblInvalidVille.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthVille + " charactères!");
        }
        
        employe.setLngNoCompte(lngNoCompte);
        employe.setIntCodeIdentification(intCodeIdentification);
        employe.setLngNoTel(lngNoTel);
        employe.setStrCourriel(txtFieldCourriel.getPlainText());
        employe.setStrNom(txtFieldNom.getPlainText());
        employe.setStrPrenom(txtFieldPrenom.getPlainText());
        employe.setStrAdresse(txtFieldAdresse.getPlainText());
        employe.setStrCodePostal(txtFieldCodePostal.getPlainText());
        employe.setStrVille(txtFieldVille.getPlainText());
                
        try {
            employe.setStrMotDePasse(AES_GCM_Athentication.encrypt(getStrPasswdTextField()));
            // setStylePasswdTextField(Config.strStyleSuccessBorder);
        } 
        catch (Exception e) {
            blnErrorMotDePasse = true;
            lblInvalidPasswd.setText("Erreur de chiffrement du mot de passe!");

            String strError = getStrPasswdTextField();
            logger.log(Level.SEVERE, "Erreur de chiffrement du TextField mot de passe '" + strError + "' !\n");

            Alert alertError = new Alert(AlertType.ERROR, "Erreur de chiffrement du TextField mot de passe '" + strError + "' !", ButtonType.OK);
            alertError.showAndWait();
        }

        ReturnGetEmployeFromTextFields returnGetEmployeFromTextFields = new ReturnGetEmployeFromTextFields(employe, blnErrorNoCompte, blnErrorCodeIdentification, blnErrorMotDePasse, blnErrorNoTel, blnErrorCourriel, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille);
        // ReturnGetEmployeFromTextFields returnGetEmployeFromTextFields = new ReturnGetEmployeFromTextFields(employe, true, true, true, true, true, true, true, true, true, true);
        
        return returnGetEmployeFromTextFields;
    }
    private boolean blnTextFieldsDiscardChanges(boolean ...blnArrOptionalShowAlertConfirm) {
        boolean blnShowAlertConfirm = true;
        if (blnArrOptionalShowAlertConfirm != null && blnArrOptionalShowAlertConfirm.length > 0)
            blnShowAlertConfirm = blnArrOptionalShowAlertConfirm[0];

        Employe employeFromTextFields = getEmployeFromTextFields().getEmploye();
        logger.log(Level.INFO, "currentListViewSelectedEmploye: " + currentListViewSelectedEmploye + "\n");
        logger.log(Level.INFO, "employeFromTextFields: " + employeFromTextFields + "\n");

        if (currentListViewSelectedEmploye == null || !(currentListViewSelectedEmploye instanceof Employe)) {
            currentListViewSelectedEmploye = new Employe();
            try {
                currentListViewSelectedEmploye.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                currentListViewSelectedEmploye.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }
        }

        // check if textfields have changed & if user input new text value
        boolean isTextFieldsChanged;
        try {
            isTextFieldsChanged = !(currentListViewSelectedEmploye.equalsExactly(employeFromTextFields)); //getEmployeFromTextFields());
        } catch (Exception e) {
            // logger.log(Level.SEVERE, "Le mot de passe du TextField 'txtFieldPasswd' ne correspond pas au mot de passe qui lui a été assigné!\n");
            logger.log(Level.SEVERE, "currentListViewSelectedEmploye ne correspond pas exactement à employeFromTextFields!\n");
            System.out.println("");

            // Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du TextField 'txtFieldPasswd' ne correspond pas au mot de passe qui lui a été assigné!\n" + Config.strRestartAppInstruction, ButtonType.OK);
            // alertError.showAndWait();

            isTextFieldsChanged = true;
        }

        logger.log(Level.INFO, "----->Have TextFields Changed ?: " + (isTextFieldsChanged) + "\n");

        // show confirmation to continue if textfields have changed & user did in fact input new text value
        if (isTextFieldsChanged) {
            if (blnShowAlertConfirm) {
                Alert confirm = new Alert(AlertType.CONFIRMATION, "Des changements se sont produits aux champs de texte.\n\nVoulez-vous IGNORER les modifications apportées aux champs de texte ou les CONSERVER et continuer à les modifier ?\n", ButtonType.YES, ButtonType.CANCEL);
                ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Ignorer les changements!");
                ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Non, Conserver les changements");
                confirm.showAndWait();

                // discard changes made to textfields
                if (confirm.getResult() == ButtonType.YES) {
                    logger.log(Level.INFO, "Changes made to the TextFields were discarded!\n");
                    return true;
                }
                // stay with changed textfields & let user continue modifying them
                else {
                    logger.log(Level.INFO, "Changes made to the TextFields are conserved to modify!\n");
                    return false;
                }
            }
            else 
                return false;
        }
        // no need to confirm if nothing has changed
        else {
            logger.log(Level.INFO, "No changes to the TextFields have been found!\n");
            return true;
        }
    }

    private void crudAccountInfos(EnumTypeMenuInterface enumTypeMenuInterface) {
        switch(enumTypeMenuInterface) {
            case ADD: {
                    // System.out.println("\nInside ADD of 'crudAccountInfos(EnumTypeMenuInterface)'");
                    // System.out.println("Is ListView Selected ?: " + (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Employe && !(lvAccountInfos.getSelectionModel().isEmpty())) + "\n\n");

                    if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Employe && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        if (blnTextFieldsDiscardChanges()) {
                            // clearAllFields();
                            resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                            clearAllFields();

                            Alert alertInfo = new Alert(AlertType.INFORMATION, "Les champs de texte ont été vidés!\n\nVeuillez remplir les champs de texte et ensuite ajouter le nouveau employé\n", ButtonType.OK);
                            alertInfo.showAndWait();
                        }
                    }
                    else {
                        ReturnGetEmployeFromTextFields returnGetEmployeFromTextFields = getEmployeFromTextFields();

                        Employe employeFromTxtFields = returnGetEmployeFromTextFields.getEmploye();
                        boolean blnErrorNoCompte = returnGetEmployeFromTextFields.getBlnErrorNoCompte(), blnErrorCodeIdentification = returnGetEmployeFromTextFields.getBlnErrorCodeIdentification(), blnErrorMotDePasse = returnGetEmployeFromTextFields.getBlnErrorMotDePasse(), blnErrorNoTel = returnGetEmployeFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetEmployeFromTextFields.getBlnErrorCourriel(), blnErrorNom = returnGetEmployeFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetEmployeFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetEmployeFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetEmployeFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetEmployeFromTextFields.getBlnErrorVille();

                        // if textfields contains error, dont add new employe to textfield
                        if (blnErrorNoCompte || blnErrorCodeIdentification || blnErrorMotDePasse || blnErrorNoTel || blnErrorCourriel || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille) {
                            String strAlertError = "Les champs de texte suivants sont erronés ou invalides:\n\n";

                            if (blnErrorNoCompte)
                                strAlertError += "Champs de texte Numéro du Compte\n";

                            if (blnErrorCodeIdentification)
                                strAlertError += "Champs de texte Code d'Identification\n";

                            if (blnErrorMotDePasse)
                                strAlertError += "\nChamps de texte Mot de passe:  \"" + Config.strFormatMotDePasse + "\"\n\n";

                            if (blnErrorNoTel)
                                strAlertError += "Champs de texte Numéro de Téléphone\n";

                            if (blnErrorCourriel)
                                strAlertError += "Champs de texte Courriel\n";

                            if (blnErrorNom)
                                strAlertError += "Champs de texte Nom\n";

                            if (blnErrorPrenom)
                                strAlertError += "Champs de texte Prénom\n";

                            if (blnErrorAdresse)
                                strAlertError += "Champs de texte Adresse\n";

                            if (blnErrorCodePostal)
                                strAlertError += "Champs de texte Code Postal\n";

                            if (blnErrorVille)
                                strAlertError += "Champs de texte Ville\n";

                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alertError.showAndWait();
                        }
                        // if textfields are valid, add new employe
                        else {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment AJOUTER le compte employé ci-dessous qui a comme information ?\n\n" + employeFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Ajouter!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                                if (arrEmployes != null && arrEmployes.size() > 0) {
                                    boolean blnEmployeMatch = false;
                                    loopEmployeMatch:for (Employe e : arrEmployes)
                                        if (e.equalsNoCompte(employeFromTxtFields)) {
                                            blnEmployeMatch = true;
                                            break loopEmployeMatch;
                                        }
            
                                    if (blnEmployeMatch) {
                                        logger.log(Level.WARNING, "L'employé EXISTE DÉJÀ dans le fichier Employes.csv!\nVeuillez ajouter un compte employé qui a un numéro de compte différents des numéros de comptes qui se trouvent parmi la liste des employés!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "L'employé EXISTE DÉJÀ dans le fichier Employes.csv!\n\nVeuillez ajouter un compte employé qui a un numéro de compte différents des autres employés!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrEmployes, false);
                                    }
                                    else {
                                        // if (employeFromTxtFields.getLngNoCompte() == currentUserLngNoCompte) {
                                        //     currentUserIntCodeIdentification = employeFromTxtFields.getIntCodeIdentification();
                                        //     currentUserStrFullName = employeFromTxtFields.getFullName();
                                        // }

                                        Employe.crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, employeFromTxtFields);
                                        resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                                        clearAllFields();
                                    }
                                }
                                else {
                                    // create data file and add new account if file doesnt exist
                                    ReturnCrudDataListEmployes returnCrudDataListEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                    boolean blnAlertErrorDataFolder = returnCrudDataListEmployes.getBlnAlertErrorDataFolder(), 
                                            blnAlertErrorDataFileEmploye = returnCrudDataListEmployes.getBlnAlertErrorDataFileEmploye();

                                    if (blnAlertErrorDataFolder || blnAlertErrorDataFileEmploye) {
                                        String strAlertError = "";

                                        if (blnAlertErrorDataFolder)
                                            strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                        else
                                            strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                        if (blnAlertErrorDataFileEmploye)
                                            strAlertError += "Création d'un nouveau fichier de donnés 'Employes.csv' pour les comptes employés!\n\n";

                                        Employe.crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, employeFromTxtFields);
                                        resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                                        clearAllFields();

                                        Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                        alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                        alertError.showAndWait();
                                    }
                                    else {
                                        resetListView(arrEmployes, true);
                                        Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Employes.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                        alertError.showAndWait();
                                    }
                                }
                            }
                        }
                    }
                }

                break;

            case MODIFY: {
                    if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Employe && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // only act & modify if changes to textfields have been made
                        if (!blnTextFieldsDiscardChanges(false)) {
                            ReturnGetEmployeFromTextFields returnGetEmployeFromTextFields = getEmployeFromTextFields();

                            Employe employeFromTxtFields = returnGetEmployeFromTextFields.getEmploye();
                            boolean blnErrorNoCompte = returnGetEmployeFromTextFields.getBlnErrorNoCompte(), blnErrorCodeIdentification = returnGetEmployeFromTextFields.getBlnErrorCodeIdentification(), blnErrorMotDePasse = returnGetEmployeFromTextFields.getBlnErrorMotDePasse(), blnErrorNoTel = returnGetEmployeFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetEmployeFromTextFields.getBlnErrorCourriel(), blnErrorNom = returnGetEmployeFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetEmployeFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetEmployeFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetEmployeFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetEmployeFromTextFields.getBlnErrorVille();

                            // if textfields contains error, dont add new employe to textfield
                            if (blnErrorNoCompte || blnErrorCodeIdentification || blnErrorMotDePasse || blnErrorNoTel || blnErrorCourriel || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille) {
                                String strAlertError = "Les champs de texte suivants sont erronés ou invalides:\n\n";

                                if (blnErrorNoCompte)
                                    strAlertError += "Champs de texte Numéro du Compte\n";

                                if (blnErrorCodeIdentification)
                                    strAlertError += "Champs de texte Code d'Identification\n";

                                if (blnErrorMotDePasse)
                                    strAlertError += "\nChamps de texte Mot de passe:  \"" + Config.strFormatMotDePasse + "\"\n\n";

                                if (blnErrorNoTel)
                                    strAlertError += "Champs de texte Numéro de Téléphone\n";

                                if (blnErrorCourriel)
                                    strAlertError += "Champs de texte Courriel\n";

                                if (blnErrorNom)
                                    strAlertError += "Champs de texte Nom\n";

                                if (blnErrorPrenom)
                                    strAlertError += "Champs de texte Prénom\n";

                                if (blnErrorAdresse)
                                    strAlertError += "Champs de texte Adresse\n";

                                if (blnErrorCodePostal)
                                    strAlertError += "Champs de texte Code Postal\n";

                                if (blnErrorVille)
                                    strAlertError += "Champs de texte Ville\n";

                                Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alertError.showAndWait();
                            }
                            // if textfields are valid, add new employe
                            else {
                                Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment MODIFIER le compte employé ci-dessous qui a comme information ?\n\n" + employeFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Modifier!");
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                confirm.showAndWait();

                                if (confirm.getResult() == ButtonType.YES) {
                                    // ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                                    // if (arrEmployes != null && arrEmployes.size() > 0) {
                                    //     boolean blnEmployeMatch = false;
                                    //     loopEmployeMatch:for (Employe e : arrEmployes)
                                    //         if (e.equalsNoCompte(currentListViewSelectedEmploye)) {
                                    //             blnEmployeMatch = true;
                                    //             break loopEmployeMatch;
                                    //         }

                                    //     if (blnEmployeMatch) {

                                    //     }
                                    //     else {
                                    //         logger.log(Level.WARNING, "L'employé choisi ne se trouve plus dans le fichier Employes.csv!\nVeuillez sélectionné à nouveau un compte employé parmi la liste des employés!\n");

                                    //         Alert alertError = new Alert(AlertType.ERROR, "L'employé choisi ne se trouve plus dans le fichier Employes.csv!\n\nVeuillez sélectionné à nouveau un compte employé parmi la liste des employés!", ButtonType.OK);
                                    //         alertError.showAndWait();

                                    //         resetListView(arrEmployes, true);
                                    //     }
                                    // }
                                    // else 
                                    //     resetListView(arrEmployes, true);

                                    ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                                    if (arrEmployes != null && arrEmployes.size() > 0) {
                                        boolean blnEmployeMatch = false;
                                        loopEmployeMatch:for (Employe e : arrEmployes)
                                            if (e.equalsNoCompte(employeFromTxtFields)) {
                                                blnEmployeMatch = true;
                                                break loopEmployeMatch;
                                            }
                
                                        if (blnEmployeMatch) {
                                            // System.out.println("\n\nemployeFromTxtFields.getLngNoCompte(): " + employeFromTxtFields.getLngNoCompte());
                                            // System.out.println("currentUserLngNoCompte: " + currentUserLngNoCompte);
                                            // System.out.println((employeFromTxtFields.getLngNoCompte() == currentUserLngNoCompte) + "\n\n");

                                            if (employeFromTxtFields.getLngNoCompte() == currentUserLngNoCompte) {
                                                currentUserStrFullName = employeFromTxtFields.getFullName();
                                                currentUserIntCodeIdentification = employeFromTxtFields.getIntCodeIdentification();

                                                lblFullName.setText(currentUserStrFullName);
                                                lblAccountInfos.setText("Numéro du Compte:\t" + Long.toString(currentUserLngNoCompte) + "\nCode Identification:\t\t" + Integer.toString(currentUserIntCodeIdentification));
                                            }

                                            Employe.crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, employeFromTxtFields);
                                            resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                                            clearAllFields();
                                        }
                                        else {
                                            logger.log(Level.WARNING, "L'employé choisi comme numéro de compte '" + Long.toString(employeFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Employes.csv!\nVoulez-vous CRÉER ce compte employé ci-dessous qui a comme information ?\n" + employeFromTxtFields.getListViewAccountInfos(false) + "\n");

                                            Alert confirmCreateEmploye = new Alert(AlertType.CONFIRMATION, "L'employé choisi comme numéro de compte '" + Long.toString(employeFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Employes.csv!\nVoulez-vous CRÉER ce compte employé ci-dessous qui a comme information ?\n\n" + employeFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                            ((Button) confirmCreateEmploye.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Créer!");
                                            ((Button) confirmCreateEmploye.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                            confirmCreateEmploye.showAndWait();

                                            if (confirmCreateEmploye.getResult() == ButtonType.YES) {
                                                // if (employeFromTxtFields.getLngNoCompte() == currentUserLngNoCompte) {
                                                //     currentUserIntCodeIdentification = employeFromTxtFields.getIntCodeIdentification();
                                                //     currentUserStrFullName = employeFromTxtFields.getFullName();
                                                // }

                                                Employe.crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, employeFromTxtFields);
                                                resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                                                clearAllFields();
                                            }
                                            // else 
                                            //     resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);

                                            // Alert alertError = new Alert(AlertType.ERROR, "L'employé choisi ne se trouve plus dans le fichier Employes.csv!\n\nVeuillez sélectionné à nouveau un compte employé parmi la liste des employés!", ButtonType.OK);
                                            // alertError.showAndWait();
                                            // resetListView(arrEmployes, true);
                                        }
                                    }
                                    else {
                                        // create data file and add new account if file doesnt exist
                                        ReturnCrudDataListEmployes returnCrudDataListEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                        boolean blnAlertErrorDataFolder = returnCrudDataListEmployes.getBlnAlertErrorDataFolder(), 
                                                blnAlertErrorDataFileEmploye = returnCrudDataListEmployes.getBlnAlertErrorDataFileEmploye();

                                        if (blnAlertErrorDataFolder || blnAlertErrorDataFileEmploye) {
                                            String strAlertError = "";

                                            if (blnAlertErrorDataFolder)
                                                strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                            else
                                                strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                            if (blnAlertErrorDataFileEmploye)
                                                strAlertError += "Création d'un nouveau fichier de donnés 'Employes.csv' pour les comptes employés!\n\n";

                                            Employe.crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, employeFromTxtFields);
                                            resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                                            clearAllFields();

                                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                            alertError.showAndWait();
                                        }
                                        else {
                                            resetListView(arrEmployes, true);
                                            Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Employes.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                            alertError.showAndWait();
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            logger.log(Level.INFO, "Aucun changement n'a été effectué sur les champs de texte!\nVeuillez modifier les champs de texte avant de vouloir modifier ce compte employé!\n");

                            Alert alertError = new Alert(AlertType.WARNING, "Aucun changement n'a été effectué sur les champs de texte!\n\nVeuillez modifier les champs de texte avant de vouloir modifier ce compte employé!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un compte employé parmi la liste des employés!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte employé parmi la liste des employés!", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                break;

            case DELETE: {
                    if (currentListViewSelectedEmploye != null && currentListViewSelectedEmploye instanceof Employe && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Employe && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // if (currentListViewSelectedEmploye.getFullName().trim().equals(currentUserStrFullName.trim()) && 
                        //     currentListViewSelectedEmploye.getLngNoCompte() == currentUserLngNoCompte &&
                        //     currentListViewSelectedEmploye.getIntCodeIdentification() == currentUserIntCodeIdentification) {
                        if (currentListViewSelectedEmploye.getLngNoCompte() == currentUserLngNoCompte) {
                            logger.log(Level.WARNING, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\nVeuillez sélectioner un autre compte employé parmi la liste des employés ou créer un nouveau compte employé, se connecter à ce compte et ensuite supprimer celui-ci!\n");

                            Alert alertError = new Alert(AlertType.ERROR, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\n\nVeuillez sélectioner un autre compte employé parmi la liste des employés ou créer un nouveau compte employé, se connecter à ce compte et ensuite supprimer celui-ci!\n", ButtonType.OK);
                            alertError.showAndWait();
                        }
                        else if (blnTextFieldsDiscardChanges()) {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment SUPPRIMER le compte employé ci-dessous qui a comme information ?\n\n" + currentListViewSelectedEmploye.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Supprimer!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Employe> arrEmployes = Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                                if (arrEmployes != null && arrEmployes.size() > 0) {
                                    boolean blnEmployeMatch = false;
                                    loopEmployeMatch:for (Employe e : arrEmployes)
                                        if (e.equalsNoCompte(currentListViewSelectedEmploye)) {
                                            blnEmployeMatch = true;
                                            break loopEmployeMatch;
                                        }
            
                                    if (blnEmployeMatch) {
                                        Employe.crudDataListEmployes(logger, EnumTypeCRUD.DELETE, currentListViewSelectedEmploye);
                                        resetListView(Employe.crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes(), true);
                                        clearAllFields();
                                    }
                                    else {
                                        logger.log(Level.WARNING, "L'employé choisi ne se trouve plus dans le fichier Employes.csv!\nVeuillez sélectionné à nouveau un compte employé parmi la liste des employés!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "L'employé choisi ne se trouve plus dans le fichier Employes.csv!\n\nVeuillez sélectionné à nouveau un compte employé parmi la liste des employés!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrEmployes, true);
                                    }
                                }
                                else 
                                    resetListView(arrEmployes, true);
                            }
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un compte employé parmi la liste des employés!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte employé parmi la liste des employés!", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                break;

            default:
                System.out.println("");
                logger.log(Level.SEVERE, "No valid EnumTypeMenuInterface was given in parameter 'crudAccountInfos(EnumTypeMenuInterface)'!\nExiting Application Now!\n");
                stage.close();
                break;
        }
    }
}
