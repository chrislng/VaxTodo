package VaxTodo.Controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.AES_GCM_Athentication;
import VaxTodo.Models.Benevole;
import VaxTodo.Models.Benevole.ReturnCrudDataListBenevoles;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Views.Interface.Models.MaskedTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/** Controller of Interface ViewMenuGestionBenevoles.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * Let the user add, modify or delete Benevole accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuGestionBenevoles extends ControllerParent {
    private final Logger logger = Logger.getLogger(ControllerMenuGestionBenevoles.class.getName());

    private Stage stage;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;
    
    @FXML
    private Label lblFullName, lblAccountInfos, lblListViewInfos,
                    lblInvalidNoCompte, lblInvalidCodeIdentification, lblInvalidPasswd, lblInvalidNoTel, lblInvalidCourriel, lblInvalidNom, lblInvalidPrenom, lblInvalidAdresse, lblInvalidCodePostal, lblInvalidVille, lblInvalidDateNaissance;

    @FXML
    private Button btnMenuAjouterBenevole, btnMenuModifierBenevole, btnMenuSupprimerBenevole;

    @FXML
    private ListView<Benevole> lvAccountInfos;

    private int intListViewSelectedIndex;
    private boolean blnListViewSelectedTwice;
    private Benevole currentListViewSelectedBenevole;

    @FXML
    private TextField passwdField, txtFieldPasswd;

    @FXML
    private MaskedTextField txtFieldNoCompte, txtFieldCodeIdentification, txtFieldNoTel, txtFieldCourriel, txtFieldNom, txtFieldPrenom, txtFieldAdresse, txtFieldCodePostal, txtFieldVille;

    @FXML
    private DatePicker datePickerDateNaissance;

    @FXML
    private ImageView viewTogglePasswd;

    @Override
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
        strMaskNoTel = "(000) 000-0000";
        for (int i = 0; i<Config.intFormatLengthEmail; i++)
            strMaskEmail += "E";
        for (int i=0; i<Config.intFormatLengthLastName; i++)
            strMaskNom += "S";
        for (int i=0; i<Config.intFormatLengthFirstName; i++)
            strMaskPrenom += "S";
        for (int i=0; i<Config.intFormatLengthAdresse; i++)
            strMaskAdresse += "A";
        strMaskCodePostal += "P0P 0P0";
        for (int i=0; i<Config.intFormatLengthVille; i++)
            strMaskVille += "S";

        txtFieldNoCompte.setMask(strMaskNoCompte);
        txtFieldCodeIdentification.setMask(strMaskCodeIdentification);
        txtFieldNoTel.setMask(strMaskNoTel);
        txtFieldCourriel.setMask(strMaskEmail);
        // txtFieldDateNaissance.setMask("0000-00-00");
        txtFieldNom.setMask(strMaskNom);
        txtFieldPrenom.setMask(strMaskPrenom);
        txtFieldAdresse.setMask(strMaskAdresse);
        txtFieldCodePostal.setMask(strMaskCodePostal);
        txtFieldVille.setMask(strMaskVille);

        // init valid date of birth
        datePickerDateNaissance.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean blnVide) {
                super.updateItem(localDate, blnVide);
                
                setDisable(blnVide || localDate.compareTo(LocalDate.now())>=0); //localDate.compareTo(LocalDate.now())<0);
            }
        });
        datePickerDateNaissance.setConverter(new StringConverter<LocalDate>() {    
            @Override
            public String toString(LocalDate localDate) {
                if(localDate==null) {
                    // System.out.println("\n--->SetConverter ToString LocalDate NULL or EMPTY\n");
                    return "";
                }

                // System.out.println("\n--->SetConverter ToString LocalDate\n");
                return localDate.toString(); //Config.DATE_TIME_FORMATTER.format(localDate);
            }
    
            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    // System.out.println("\n--->SetConverter FromString LocalDate NULL or EMPTY\n");
                    return null;
                }

                try {
                    // System.out.println("\n--->SetConverter FromString Parsing LocalDate\n");
                    return LocalDate.parse(dateString, Config.DATE_TIME_FORMATTER);
                }
                catch (Exception e) {
                    // Bad values as input
                    // System.out.println("\n--->SetConverter FromString Parse Excepton\n");
                    return null;
                }
            }
        });

        //! DOES NOT WORK !!! WILL CRASH APP
        // datePickerDateNaissance.focusedProperty().addListener(new ChangeListener<Boolean>() {
        //     @Override
        //     public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        //         if (!newValue){
        //             datePickerDateNaissance.setValue(datePickerDateNaissance.getConverter().fromString(datePickerDateNaissance.getEditor().getText()));
        //         }
        //     }
        // });
        // datePickerDateNaissance.getEditor().focusedProperty().addListener((obj, wasFocused, isFocused)->{
        //     if (!isFocused) {
        //         try {
        //             datePickerDateNaissance.setValue(datePickerDateNaissance.getConverter().fromString(datePickerDateNaissance.getEditor().getText()));
        //         } 
        //         catch (DateTimeParseException e) {
        //             datePickerDateNaissance.getEditor().setText(datePickerDateNaissance.getConverter().toString(datePickerDateNaissance.getValue()));
        //         }
        //     }
        // });
        
        // init ListView and its data
        // currentListViewSelectedBenevole = null;
        lvAccountInfos.setCellFactory(cell -> new ListCell<Benevole>() {
            private ImageView imageView;

            @Override
            public void updateItem(Benevole benevole, boolean blnVide) {
                super.updateItem(benevole, blnVide);

                if (blnVide || benevole == null || benevole.getListViewAccountInfos(true) == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent");
                    setGraphic(null);
                }
                else {
                    imageView = new ImageView(new Image(getClass().getResourceAsStream(Config.strImageFileAccountInfos)));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);

                    setText(benevole.getListViewAccountInfos(true));
                    setStyle("");
                }
            }
        });
        lvAccountInfos.getSelectionModel().selectedItemProperty().addListener(changeListener -> {
            try {
                if (!blnListViewSelectedTwice && blnTextFieldsDiscardChanges()) {
                    currentListViewSelectedBenevole = lvAccountInfos.getSelectionModel().getSelectedItem();
                    intListViewSelectedIndex = lvAccountInfos.getSelectionModel().getSelectedIndex();

                    if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole) {
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
                        resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), false);

                        // if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole) {
                        //     logger.log(Level.INFO, "ListView Hard Reset\n");
                        //     resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                        // }
                        // else {
                        //     logger.log(Level.INFO, "ListView Soft Reset\n");
                        //     resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), false);
                        // }
                    }
                }
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "Exception occured while changing list view selection of 'lvAccountInfos.getSelectionModel().selectedItemProperty()'!\n", e);
            }
        });

        datePickerDateNaissance.getEditor().setDisable(true);
        datePickerDateNaissance.getEditor().setOpacity(1);
        resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
    }

    private void resetListView(ArrayList<Benevole> arrBenevoles, boolean blnResetHard, boolean ...blnArrOptional) {
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // reset hard by also clearing textfields & reseting currentListViewSelectedBenevole
        if (blnResetHard) {
            // System.out.println("\n--->Textfields cleared\n");
            clearAllFields();

            intListViewSelectedIndex = -1;
            blnListViewSelectedTwice = false;

            // System.out.println("\n--->New Benevole\n");
            currentListViewSelectedBenevole = new Benevole();
            try {
                currentListViewSelectedBenevole.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                currentListViewSelectedBenevole.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }
        }

        // System.out.println("\nInside method resetListView\n");
        lvAccountInfos.getItems().clear();
        // System.out.println("\nCleared ListView items\n");

        // lvAccountInfos.getSelectionModel().clearSelection();
        // ArrayList<Benevole> arrBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, Config.strNewDataFileBenevoles, null).getArrBenevoles();

        // System.out.println("\n\nArrBenevoles Size: " + arrBenevoles.size() + "\n\n");

        if (arrBenevoles != null && arrBenevoles.size() > 0) {
            btnMenuModifierBenevole.setDisable(false);
            btnMenuSupprimerBenevole.setDisable(false);

            lblListViewInfos.setText("Liste des Bénévoles");
            // for (int i=0; i<15;i++)
            lvAccountInfos.getItems().addAll(arrBenevoles);
        }
        else {
            btnMenuModifierBenevole.setDisable(true);
            btnMenuSupprimerBenevole.setDisable(true);

            lblListViewInfos.setText("Liste des Bénévoles VIDES!");

            logger.log(Level.SEVERE, "Aucun compte bénévole VALIDE présent dans le fichier Benevoles.csv!\nVeuillez ajouter un nouveau compte bénévole ou se déconnecter pour que l'application créé un compte bénévole automatiquement!\n");

            Alert alertError = new Alert(AlertType.ERROR, "Aucun compte bénévole VALIDE présent dans le fichier Benevoles.csv\n\nVeuillez ajouter un nouveau compte bénévole ou se déconnecter pour que l'application créé un compte bénévole automatiquement", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    private void clearAllFields(boolean ...blnArrOptional) {
        // disable textfield No Compte if it is current selected benevole
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // System.out.println("\n\nblnClearListBeforetextFields: " + blnClearListBeforetextFields + "\n\n");

        if (blnClearListBeforetextFields) {
            currentListViewSelectedBenevole = null;
            // lvAccountInfos.getItems().clear(); // Seriously WTF, fuck ListViews & fuck Java for calling 'selectedItemProperty()' when deleting list view

            System.out.println("\n\nCleared List View Before TextFields!\n\n");
        }

        // if an account from list view is selected, disable Textfield No Compte
        // if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole && currentListViewSelectedBenevole.getLngNoCompte() == currentUserLngNoCompte && !(lvAccountInfos.getSelectionModel().isEmpty()))
        // /*&& lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Benevole*/
        //     // if current user benevole is selected in listview, disable Textfield No Compte
        //     txtFieldNoCompte.setDisable(true);
        // else {
        //     txtFieldNoCompte.setDisable(false);

        //     txtFieldNoCompte.clear();
        //     txtFieldNoCompte.setText("");
        //     txtFieldNoCompte.setPlainText("");
        // }
        txtFieldNoCompte.setDisable(false);
        txtFieldNoCompte.clear();
        txtFieldNoCompte.setText("");
        txtFieldNoCompte.setPlainText("");

        // clear Textfields Text
        txtFieldCodeIdentification.clear();
        txtFieldNoTel.clear();
        txtFieldCourriel.clear();
        // txtFieldDateNaissance.clear();
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
        // txtFieldDateNaissance.setText("");
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
        // txtFieldDateNaissance.setPlainText("");
        txtFieldNom.setPlainText("");
        txtFieldPrenom.setPlainText("");
        txtFieldAdresse.setPlainText("");
        txtFieldCodePostal.setPlainText("");
        txtFieldVille.setPlainText("");
        // passwdField.clear();
        // txtFieldPasswd.clear();

        datePickerDateNaissance.getEditor().clear();
        datePickerDateNaissance.setValue(null);

        // clear Labels Text
        lblInvalidNoCompte.setText("");
        lblInvalidCodeIdentification.setText("");
        lblInvalidNoTel.setText("");
        lblInvalidCourriel.setText("");
        lblInvalidDateNaissance.setText("");
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
        // txtFieldDateNaissance.setStyle(Config.strStyleSuccessBorder);
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
        lblInvalidDateNaissance.setStyle(Config.strStyleSuccessMessage);
        lblInvalidNom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidPrenom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidAdresse.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCodePostal.setStyle(Config.strStyleSuccessMessage);
        lblInvalidVille.setStyle(Config.strStyleSuccessMessage);
        lblInvalidPasswd.setStyle(Config.strStyleSuccessMessage);
    }
    private void restoreAllFields(boolean ...blnArrOptional) {
        clearAllFields();
                        
        // if (currentListViewSelectedBenevole.getLngNoCompte() == currentUserLngNoCompte)
        //     txtFieldNoCompte.setDisable(true);
        // else
        //     txtFieldNoCompte.setDisable(false);

        // init Textfields with data from selected Benevole
        txtFieldNoCompte.setPlainText(Long.toString(currentListViewSelectedBenevole.getLngNoCompte()));
        txtFieldCodeIdentification.setPlainText(Integer.toString(currentListViewSelectedBenevole.getIntCodeIdentification()));
        txtFieldNoTel.setPlainText(Long.toString(currentListViewSelectedBenevole.getLngNoTel()));
        txtFieldCourriel.setPlainText(currentListViewSelectedBenevole.getStrCourriel());
        // txtFieldDateNaissance.setPlainText(currentListViewSelectedBenevole.getStrDateNaissance());
        txtFieldNom.setPlainText(currentListViewSelectedBenevole.getStrNom());
        txtFieldPrenom.setPlainText(currentListViewSelectedBenevole.getStrPrenom());
        txtFieldAdresse.setPlainText(currentListViewSelectedBenevole.getStrAdresse());
        txtFieldCodePostal.setPlainText(currentListViewSelectedBenevole.getStrCodePostal());
        txtFieldVille.setPlainText(currentListViewSelectedBenevole.getStrVille());

        try {
            // txtFieldPasswd.setPlainText(currentListViewSelectedBenevole.getStrMotDePasse());
            String strPasswdDecrypted = AES_GCM_Athentication.decrypt(currentListViewSelectedBenevole.getStrMotDePasse());
            setStrPasswdTextField(strPasswdDecrypted);
        }
        catch (Exception e) {
            setStrPasswdTextField("");

            System.out.println();
            logger.log(Level.SEVERE, "Le mot de passe du compte bénévole [No Compte: '" + currentListViewSelectedBenevole.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedBenevole.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction);
            System.out.println("");

            Alert alertError = new Alert(AlertType.ERROR, "Le mot de passe du compte bénévole [No Compte: '" + currentListViewSelectedBenevole.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedBenevole.getIntCodeIdentification() + "'] ne peut pas être déchiffré!\n" + Config.strRestartAppInstruction, ButtonType.OK);
            alertError.showAndWait();
        }

        try {
            LocalDate localDate = LocalDate.parse(currentListViewSelectedBenevole.getStrDateNaissance(), Config.DATE_TIME_FORMATTER);
            // System.out.println("\n\n--->Date Naissance Benevole: '" + localDate.toString() + "'\n\n");
            datePickerDateNaissance.setValue(localDate);
        }
        catch (DateTimeParseException dtpe) {
            System.out.println();
            logger.log(Level.SEVERE, "Impossible d'analyser et de PARSE la date de naissance du compte bénévole [No Compte: '" + currentListViewSelectedBenevole.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedBenevole.getIntCodeIdentification() + "']\nDate de Naissance: '" + currentListViewSelectedBenevole.getStrDateNaissance() + "'!\n");
            System.out.println("");

            // Alert alertError = new Alert(AlertType.ERROR, "Impossible d'analyser la date de naissance du compte bénévole [No Compte: '" + currentListViewSelectedBenevole.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedBenevole.getIntCodeIdentification() + "']\nDate de Naissance: '" + currentListViewSelectedBenevole.getStrDateNaissance() + "'\n", ButtonType.OK);
            // alertError.showAndWait();

            datePickerDateNaissance.getEditor().clear();
            datePickerDateNaissance.setValue(null);
        }
    }

    @FXML
    private void btnDisconnectClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }

    @FXML
    private void btnReturnClick() {
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.RETURN_BACK, new MenuInfos(Config.strInterfaceViewMenuBenevole, currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification, "Menu Employé"));
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
    }

    @FXML
    private void btnMenuAjouterBenevoleClick() {
        crudAccountInfos(EnumTypeMenuInterface.ADD);
    }
    @FXML
    private void btnMenuModifierBenevoleClick() {
        crudAccountInfos(EnumTypeMenuInterface.MODIFY);
    }
    @FXML
    private void btnMenuSupprimerBenevoleClick() {
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
        if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Benevole && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment RESTAURER tous les champs de texte ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Restaurer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();
    
            if (confirm.getResult() == ButtonType.YES)
                restoreAllFields();
        
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner un compte bénévole parmi la liste des bénévoles!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte bénévole parmi la liste des bénévoles!", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    @FXML
    private void btnClearDatePickerClick() {
        datePickerDateNaissance.getEditor().clear();
        datePickerDateNaissance.setValue(null);
    }

    @FXML
    private void btnTogglePasswdClick() {
        logger.log(Level.INFO, "Toggle View Password Field Gestion Benevole!\n");

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
    
    private class ReturnGetBenevoleFromTextFields{
        private Benevole benevole;
        private boolean blnErrorNoCompte, blnErrorCodeIdentification, blnErrorMotDePasse, blnErrorNoTel, blnErrorCourriel, blnErrorDateNaissance, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille;

        public ReturnGetBenevoleFromTextFields() {
            this.benevole = new Benevole();
            try {
                this.benevole.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                this.benevole.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }

            this.blnErrorNoCompte = false;
            this.blnErrorCodeIdentification = false;
            this.blnErrorMotDePasse = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorDateNaissance = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;
        }
        public ReturnGetBenevoleFromTextFields(Benevole benevole) {
            this.benevole = benevole;

            this.blnErrorNoCompte = false;
            this.blnErrorCodeIdentification = false;
            this.blnErrorMotDePasse = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorDateNaissance = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;
        }
        public ReturnGetBenevoleFromTextFields(boolean blnErrorNoCompte, boolean blnErrorCodeIdentification, boolean blnErrorMotDePasse, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorDateNaissance, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille) {
            this.benevole = new Benevole();
            try {
                this.benevole.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                this.benevole.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorCodeIdentification = blnErrorCodeIdentification;
            this.blnErrorMotDePasse = blnErrorMotDePasse;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorDateNaissance = blnErrorDateNaissance;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;
        }
        public ReturnGetBenevoleFromTextFields(Benevole benevole, boolean blnErrorNoCompte, boolean blnErrorCodeIdentification, boolean blnErrorMotDePasse, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorDateNaissance, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille) {
            this.benevole = benevole;

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorCodeIdentification = blnErrorCodeIdentification;
            this.blnErrorMotDePasse = blnErrorMotDePasse;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorDateNaissance = blnErrorDateNaissance;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;
        }

        public Benevole getBenevole() {
            return this.benevole;
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
        public boolean getBlnErrorDateNaissance() {
            return this.blnErrorDateNaissance;
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

        public void setBenevole(Benevole benevole) {
            this.benevole = benevole;
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
        public void setBlnErrorDateNaissance(boolean blnErrorDateNaissance) {
            this.blnErrorDateNaissance = blnErrorDateNaissance;
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
    private ReturnGetBenevoleFromTextFields getBenevoleFromTextFields() {
        // Set All TextFields to error style, and only change when input is valid
        txtFieldNoCompte.setStyle(Config.strStyleErrorBorder);
        txtFieldCodeIdentification.setStyle(Config.strStyleErrorBorder);
        txtFieldNoTel.setStyle(Config.strStyleErrorBorder);
        txtFieldCourriel.setStyle(Config.strStyleErrorBorder);
        // txtFieldDateNaissance.setStyle(Config.strStyleErrorBorder);
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
        lblInvalidDateNaissance.setStyle(Config.strStyleErrorMessage);
        lblInvalidNom.setStyle(Config.strStyleErrorMessage);
        lblInvalidPrenom.setStyle(Config.strStyleErrorMessage);
        lblInvalidAdresse.setStyle(Config.strStyleErrorMessage);
        lblInvalidCodePostal.setStyle(Config.strStyleErrorMessage);
        lblInvalidVille.setStyle(Config.strStyleErrorMessage);
        lblInvalidPasswd.setStyle(Config.strStyleErrorMessage);

        // init all variables that will be returned
        Benevole benevole = new Benevole();

        boolean blnErrorNoCompte = false, blnErrorCodeIdentification = false, blnErrorMotDePasse = false, blnErrorNoTel = false, blnErrorCourriel = false, blnErrorDateNaissance = false, blnErrorNom = false, blnErrorPrenom = false, blnErrorAdresse = false, blnErrorCodePostal = false, blnErrorVille = false;

        // init all variables that will be used inside this function
        long lngNoCompte = Long.parseLong("0"), lngNoTel = Long.parseLong("0");
        int intCodeIdentification = 0;

        String strNoCompte = "", strCodeIdentification = "", strNoTel = "", strDateNaissance = "";
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
        
        //!!! LocalDate localDate = datePickerDateNaissance.getValue();
        // String strTempDateNaissance = datePickerDateNaissance.getEditor().getText().trim();
        
        // boolean blnConvertDateNaissanceSuccess = false;
        // Date dateDateNaissance;

        // try {
        //     dateDateNaissance = DateConverter.toDate(datePickerDateNaissance.getEditor().getText());
        // }

        // System.out.println("\n--->DATE: " + (datePickerDateNaissance.getValue()==null ? "NULL" : datePickerDateNaissance.getValue().toString()) /*(datePickerDateNaissance.getEditor().getText())*/ + "\n");

        // // if (localDate == null)
        // //     strDateNaissance = "";
        // // else
        // //     strDateNaissance = localDate.toString(); //txtFieldDateNaissance.getText();
        // // if (strDateNaissance == null || strDateNaissance.replaceAll("-", "") == null || strDateNaissance.replaceAll("-", "").isEmpty())
        // //     strDateNaissance = "";

        // if (localDate == null || strTempDateNaissance.isEmpty()) {
        //     // txtFieldDateNaissance.setStyle(Config.strStyleSuccessBorder);
        //     lblInvalidDateNaissance.setText("");
        //     lblInvalidDateNaissance.setStyle(Config.strStyleSuccessMessage);
        // }
        // else if ()

        // if (strDateNaissance.length() == Config.intFormatLengthDate) {
        //     // txtFieldDateNaissance.setStyle(Config.strStyleSuccessBorder);
        //     lblInvalidDateNaissance.setText("Date de Naissance Valide");
        //     lblInvalidDateNaissance.setStyle(Config.strStyleSuccessMessage);
        // }
        // else if (strDateNaissance == null || strDateNaissance.isEmpty()) {
            
        // }
        // else if (!(strDateNaissance == null || strDateNaissance.isEmpty()) && strDateNaissance.length() != Config.intFormatLengthDate) {
        //     blnErrorDateNaissance = true;
        //     lblInvalidDateNaissance.setText("Longueur exacte à " + Config.intFormatLengthDate + " charactères!");
        // }

        LocalDate localDate = datePickerDateNaissance.getValue();
        if (localDate == null)
            strDateNaissance = "";
        else
            strDateNaissance = localDate.toString();
        if (!(strDateNaissance.trim().isEmpty()) && strDateNaissance.length() == Config.intFormatLengthDate) {
            lblInvalidDateNaissance.setText("Date de Naissance Valide");
            lblInvalidDateNaissance.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strDateNaissance.trim().isEmpty()) {
            lblInvalidDateNaissance.setText("");
            lblInvalidDateNaissance.setStyle(Config.strStyleSuccessMessage);
        }
        else if (!(strDateNaissance.trim().isEmpty()) && strDateNaissance.length() != Config.intFormatLengthDate) {
            blnErrorDateNaissance = true;
            lblInvalidDateNaissance.setText("Format Date: yyyy-mm-dd");
            lblInvalidDateNaissance.setStyle(Config.strStyleErrorMessage);
        }

        // System.out.println("\n--->Date Naissance: '" + datePickerDateNaissance.getValue() + "'\n");

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
        
        benevole.setLngNoCompte(lngNoCompte);
        benevole.setIntCodeIdentification(intCodeIdentification);
        benevole.setLngNoTel(lngNoTel);
        benevole.setStrCourriel(txtFieldCourriel.getPlainText());
        // benevole.setStrDateNaissance((txtFieldDateNaissance.getText().replaceAll("-", "") == null || txtFieldDateNaissance.getText().replaceAll("-", "").isEmpty()) ? "" : txtFieldDateNaissance.getText());
        benevole.setStrDateNaissance(strDateNaissance); //datePickerDateNaissance.getValue().format(Config.DATE_TIME_FORMATTER).toString());
        benevole.setStrNom(txtFieldNom.getPlainText());
        benevole.setStrPrenom(txtFieldPrenom.getPlainText());
        benevole.setStrAdresse(txtFieldAdresse.getPlainText());
        benevole.setStrCodePostal(txtFieldCodePostal.getPlainText());
        benevole.setStrVille(txtFieldVille.getPlainText());
                
        try {
            benevole.setStrMotDePasse(AES_GCM_Athentication.encrypt(getStrPasswdTextField()));
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

        ReturnGetBenevoleFromTextFields returnGetBenevoleFromTextFields = new ReturnGetBenevoleFromTextFields(benevole, blnErrorNoCompte, blnErrorCodeIdentification, blnErrorMotDePasse, blnErrorNoTel, blnErrorCourriel, blnErrorDateNaissance, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille);
        
        return returnGetBenevoleFromTextFields;
    }
    private boolean blnTextFieldsDiscardChanges(boolean ...blnArrOptionalShowAlertConfirm) {
        boolean blnShowAlertConfirm = true;
        if (blnArrOptionalShowAlertConfirm != null && blnArrOptionalShowAlertConfirm.length > 0)
            blnShowAlertConfirm = blnArrOptionalShowAlertConfirm[0];

        Benevole benevoleFromTextFields = getBenevoleFromTextFields().getBenevole();
        logger.log(Level.INFO, "currentListViewSelectedBenevole: " + currentListViewSelectedBenevole + "\n");
        logger.log(Level.INFO, "benevoleFromTextFields: " + benevoleFromTextFields + "\n");

        if (currentListViewSelectedBenevole == null || !(currentListViewSelectedBenevole instanceof Benevole)) {
            currentListViewSelectedBenevole = new Benevole();
            try {
                currentListViewSelectedBenevole.initStrMotDePasseEncryption("");
            } catch (Exception e) {
                currentListViewSelectedBenevole.setStrMotDePasse("");
                logger.log(Level.SEVERE, "Erreur de chiffrement du mot de passe 'VIDE' !\n");
            }
        }

        // check if textfields have changed & if user input new text value
        boolean isTextFieldsChanged;
        try {
            isTextFieldsChanged = !(currentListViewSelectedBenevole.equalsExactly(benevoleFromTextFields)); //getBenevoleFromTextFields());
        } catch (Exception e) {
            // logger.log(Level.SEVERE, "Le mot de passe du TextField 'txtFieldPasswd' ne correspond pas au mot de passe qui lui a été assigné!\n");
            logger.log(Level.SEVERE, "currentListViewSelectedBenevole ne correspond pas exactement à benevoleFromTextFields!\n");
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
                    if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Benevole && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        if (blnTextFieldsDiscardChanges()) {
                            resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                            clearAllFields();

                            Alert alertInfo = new Alert(AlertType.INFORMATION, "Les champs de texte ont été vidés!\n\nVeuillez remplir les champs de texte et ensuite ajouter le nouveau bénévole\n", ButtonType.OK);
                            alertInfo.showAndWait();
                        }
                    }
                    else {
                        ReturnGetBenevoleFromTextFields returnGetBenevoleFromTextFields = getBenevoleFromTextFields();

                        Benevole benevoleFromTxtFields = returnGetBenevoleFromTextFields.getBenevole();
                        boolean blnErrorNoCompte = returnGetBenevoleFromTextFields.getBlnErrorNoCompte(), blnErrorCodeIdentification = returnGetBenevoleFromTextFields.getBlnErrorCodeIdentification(), blnErrorMotDePasse = returnGetBenevoleFromTextFields.getBlnErrorMotDePasse(), blnErrorNoTel = returnGetBenevoleFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetBenevoleFromTextFields.getBlnErrorCourriel(), blnErrorDateNaissance = returnGetBenevoleFromTextFields.getBlnErrorDateNaissance(), blnErrorNom = returnGetBenevoleFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetBenevoleFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetBenevoleFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetBenevoleFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetBenevoleFromTextFields.getBlnErrorVille();

                        // if textfields contains error, dont add new benevole to textfield
                        if (blnErrorNoCompte || blnErrorCodeIdentification || blnErrorMotDePasse || blnErrorNoTel || blnErrorCourriel || blnErrorDateNaissance || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille) {
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

                            if (blnErrorDateNaissance)
                                strAlertError += "Champs de texte Date de Naissance\n";

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
                        // if textfields are valid, add new benevole
                        else {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment AJOUTER le compte bénévole ci-dessous qui a comme information ?\n\n" + benevoleFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Ajouter!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Benevole> arrBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();

                                if (arrBenevoles != null && arrBenevoles.size() > 0) {
                                    boolean blnBenevoleMatch = false;
                                    loopBenevoleMatch:for (Benevole b : arrBenevoles)
                                        if (b.equalsNoCompte(benevoleFromTxtFields)) {
                                            blnBenevoleMatch = true;
                                            break loopBenevoleMatch;
                                        }
            
                                    if (blnBenevoleMatch) {
                                        logger.log(Level.WARNING, "Le bénévole EXISTE DÉJÀ dans le fichier Benevoles.csv!\nVeuillez ajouter un compte bénévole qui a un numéro de compte différents des numéros de comptes qui se trouvent parmi la liste des bénévoles!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "Le bénévole EXISTE DÉJÀ dans le fichier Benevoles.csv!\n\nVeuillez ajouter un compte bénévole qui a un numéro de compte différents des autres bénévoles!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrBenevoles, false);
                                    }
                                    else {
                                        Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, benevoleFromTxtFields);
                                        resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                                        clearAllFields();
                                    }
                                }
                                else {
                                    // create data file and add new account if file doesnt exist
                                    ReturnCrudDataListBenevoles returnCrudDataListBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                    boolean blnAlertErrorDataFolder = returnCrudDataListBenevoles.getBlnAlertErrorDataFolder(), 
                                            blnAlertErrorDataFileBenevole = returnCrudDataListBenevoles.getBlnAlertErrorDataFileBenevole();

                                    if (blnAlertErrorDataFolder || blnAlertErrorDataFileBenevole) {
                                        String strAlertError = "";

                                        if (blnAlertErrorDataFolder)
                                            strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                        else
                                            strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                        if (blnAlertErrorDataFileBenevole)
                                            strAlertError += "Création d'un nouveau fichier de donnés 'Benevoles.csv' pour les comptes bénévoles!\n\n";

                                        Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, benevoleFromTxtFields);
                                        resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                                        clearAllFields();

                                        Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                        alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                        alertError.showAndWait();
                                    }
                                    else {
                                        resetListView(arrBenevoles, true);
                                        Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Benevoles.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                        alertError.showAndWait();
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case MODIFY: {
                    if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Benevole && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // only act & modify if changes to textfields have been made
                        if (!blnTextFieldsDiscardChanges(false)) {
                            ReturnGetBenevoleFromTextFields returnGetBenevoleFromTextFields = getBenevoleFromTextFields();

                            Benevole benevoleFromTxtFields = returnGetBenevoleFromTextFields.getBenevole();
                            boolean blnErrorNoCompte = returnGetBenevoleFromTextFields.getBlnErrorNoCompte(), blnErrorCodeIdentification = returnGetBenevoleFromTextFields.getBlnErrorCodeIdentification(), blnErrorMotDePasse = returnGetBenevoleFromTextFields.getBlnErrorMotDePasse(), blnErrorNoTel = returnGetBenevoleFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetBenevoleFromTextFields.getBlnErrorCourriel(), blnErrorDateNaissance = returnGetBenevoleFromTextFields.getBlnErrorDateNaissance(), blnErrorNom = returnGetBenevoleFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetBenevoleFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetBenevoleFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetBenevoleFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetBenevoleFromTextFields.getBlnErrorVille();

                            // if textfields contains error, dont add new benevole to textfield
                            if (blnErrorNoCompte || blnErrorCodeIdentification || blnErrorMotDePasse || blnErrorNoTel || blnErrorCourriel || blnErrorDateNaissance || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille) {
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

                                if (blnErrorDateNaissance)
                                    strAlertError += "Champs de texte Date de Naissance\n";

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
                            // if textfields are valid, add new benevole
                            else {
                                Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment MODIFIER le compte bénévole ci-dessous qui a comme information ?\n\n" + benevoleFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Modifier!");
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                confirm.showAndWait();

                                if (confirm.getResult() == ButtonType.YES) {
                                    ArrayList<Benevole> arrBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();

                                    if (arrBenevoles != null && arrBenevoles.size() > 0) {
                                        boolean blnBenevoleMatch = false;
                                        loopBenevoleMatch:for (Benevole b : arrBenevoles)
                                            if (b.equalsNoCompte(benevoleFromTxtFields)) {
                                                blnBenevoleMatch = true;
                                                break loopBenevoleMatch;
                                            }
                
                                        if (blnBenevoleMatch) {
                                            Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, benevoleFromTxtFields);
                                            resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                                            clearAllFields();
                                        }
                                        else {
                                            logger.log(Level.WARNING, "Le bénévole choisi comme numéro de compte '" + Long.toString(benevoleFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Benevoles.csv!\nVoulez-vous CRÉER ce compte bénévole ci-dessous qui a comme information ?\n" + benevoleFromTxtFields.getListViewAccountInfos(false) + "\n");

                                            Alert confirmCreateBenevole = new Alert(AlertType.CONFIRMATION, "Le bénévole choisi comme numéro de compte '" + Long.toString(benevoleFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Benevoles.csv!\nVoulez-vous CRÉER ce compte bénévole ci-dessous qui a comme information ?\n\n" + benevoleFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                            ((Button) confirmCreateBenevole.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Créer!");
                                            ((Button) confirmCreateBenevole.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                            confirmCreateBenevole.showAndWait();

                                            if (confirmCreateBenevole.getResult() == ButtonType.YES) {
                                                System.out.println("\n\n" + benevoleFromTxtFields + "\n\n");
                                                Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, benevoleFromTxtFields);
                                                resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                                                clearAllFields();
                                            }
                                        }
                                    }
                                    else {
                                        // create data file and add new account if file doesnt exist
                                        ReturnCrudDataListBenevoles returnCrudDataListBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                        boolean blnAlertErrorDataFolder = returnCrudDataListBenevoles.getBlnAlertErrorDataFolder(), 
                                                blnAlertErrorDataFileBenevole = returnCrudDataListBenevoles.getBlnAlertErrorDataFileBenevole();

                                        if (blnAlertErrorDataFolder || blnAlertErrorDataFileBenevole) {
                                            String strAlertError = "";

                                            if (blnAlertErrorDataFolder)
                                                strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                            else
                                                strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                            if (blnAlertErrorDataFileBenevole)
                                                strAlertError += "Création d'un nouveau fichier de donnés 'Benevoles.csv' pour les comptes bénévoles!\n\n";

                                            Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, benevoleFromTxtFields);
                                            resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                                            clearAllFields();

                                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                            alertError.showAndWait();
                                        }
                                        else {
                                            resetListView(arrBenevoles, true);
                                            Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Benevoles.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                            alertError.showAndWait();
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            logger.log(Level.INFO, "Aucun changement n'a été effectué sur les champs de texte!\nVeuillez modifier les champs de texte avant de vouloir modifier ce compte bénévole!\n");

                            Alert alertError = new Alert(AlertType.WARNING, "Aucun changement n'a été effectué sur les champs de texte!\n\nVeuillez modifier les champs de texte avant de vouloir modifier ce compte bénévole!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un compte bénévole parmi la liste des bénévoles!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte bénévole parmi la liste des bénévoles!", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                break;

            case DELETE: {
                    if (currentListViewSelectedBenevole != null && currentListViewSelectedBenevole instanceof Benevole && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Benevole && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // if (currentListViewSelectedBenevole.getLngNoCompte() == currentUserLngNoCompte) {
                        //     logger.log(Level.WARNING, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\nVeuillez sélectioner un autre compte bénévole parmi la liste des bénévoles ou créer un nouveau compte bénévole, se connecter à ce compte et ensuite supprimer celui-ci!\n");

                        //     Alert alertError = new Alert(AlertType.ERROR, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\n\nVeuillez sélectioner un autre compte bénévole parmi la liste des bénévoles ou créer un nouveau compte bénévole, se connecter à ce compte et ensuite supprimer celui-ci!\n", ButtonType.OK);
                        //     alertError.showAndWait();
                        // }
                        // else
                        if (blnTextFieldsDiscardChanges()) {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment SUPPRIMER le compte bénévole ci-dessous qui a comme information ?\n\n" + currentListViewSelectedBenevole.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Supprimer!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Benevole> arrBenevoles = Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();

                                if (arrBenevoles != null && arrBenevoles.size() > 0) {
                                    boolean blnBenevoleMatch = false;
                                    loopBenevoleMatch:for (Benevole b : arrBenevoles)
                                        if (b.equalsNoCompte(currentListViewSelectedBenevole)) {
                                            blnBenevoleMatch = true;
                                            break loopBenevoleMatch;
                                        }
            
                                    if (blnBenevoleMatch) {
                                        Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.DELETE, currentListViewSelectedBenevole);
                                        resetListView(Benevole.crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles(), true);
                                        clearAllFields();
                                    }
                                    else {
                                        logger.log(Level.WARNING, "Le bénévole choisi ne se trouve plus dans le fichier Benevoles.csv!\nVeuillez sélectionné à nouveau un compte bénévole parmi la liste des bénévoles!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "Le bénévole choisi ne se trouve plus dans le fichier Benevoles.csv!\n\nVeuillez sélectionné à nouveau un compte bénévole parmi la liste des bénévoles!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrBenevoles, true);
                                    }
                                }
                                else 
                                    resetListView(arrBenevoles, true);
                            }
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un compte bénévole parmi la liste des bénévoles!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte bénévole parmi la liste des bénévoles!", ButtonType.OK);
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

    Date parseDate(String maybeDate, boolean lenient) {
        Date date = null;
    
        // test date string matches format structure using regex
        // - weed out illegal characters and enforce 4-digit year
        // - create the regex based on the local format string
        String reFormat = Pattern.compile("d+|M+").matcher(Matcher.quoteReplacement(Config.strFormatDate)).replaceAll("\\\\d{1,2}");
        reFormat = Pattern.compile("y+").matcher(reFormat).replaceAll("\\\\d{4}");
        if ( Pattern.compile(reFormat).matcher(maybeDate).matches() ) {
            // date string matches format structure, 
            // - now test it can be converted to a valid date
            SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateInstance();
            sdf.applyPattern(Config.strFormatDate);
            sdf.setLenient(lenient);
            
            try { 
                date = sdf.parse(maybeDate); 
            } 
            catch (ParseException e) {

            }
        } 
        return date;
    }
}
