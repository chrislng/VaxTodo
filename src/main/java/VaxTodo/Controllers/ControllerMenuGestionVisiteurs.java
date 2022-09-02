package VaxTodo.Controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Models.Formulaire;
import VaxTodo.Models.Visiteur;
import VaxTodo.Models.Visiteur.ReturnCrudDataListVisiteurs;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/** Controller of Interface ViewMenuGestionVisiteurs.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * Let the user add, modify or delete Visiteurs accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuGestionVisiteurs {
    private final Logger logger = Logger.getLogger(ControllerMenuGestionVisiteurs.class.getName());

    private Stage stage;

    private EnumTypeMenuInterface enumTypeMenuInterfaceReturnBack;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    private boolean blnDisableAllButtons;
    
    @FXML
    private Label lblFullName, lblAccountInfos, lblListViewInfos, lblTitleOption,
                    lblInvalidNoCompte, lblInvalidNoTel, lblInvalidCourriel, lblInvalidNom, lblInvalidPrenom, lblInvalidAdresse, lblInvalidCodePostal, lblInvalidVille, lblInvalidDateNaissance;

    @FXML
    private Button btnMenuAjouterVisiteur, btnMenuModifierVisiteur, btnMenuSupprimerVisiteur, btnRestoreAllFields, btnClearAllFields, btnClearDatePicker,
                    btnSendReport, btnGotoFormulaire;

    @FXML
    private ListView<Visiteur> lvAccountInfos;

    private int intListViewSelectedIndex;
    private boolean blnListViewSelectedTwice;
    private Visiteur currentListViewSelectedVisiteur;

    @FXML
    private MaskedTextField txtFieldNoCompte, txtFieldNoTel, txtFieldCourriel, txtFieldNom, txtFieldPrenom, txtFieldAdresse, txtFieldCodePostal, txtFieldVille;

    @FXML
    private DatePicker datePickerDateNaissance;

    public void setStageAndSetupListeners(Stage stage, EnumTypeMenuInterface enumTypeMenuInterface, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification, boolean ...blnArrOptional) {
        blnDisableAllButtons = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnDisableAllButtons = blnArrOptional[0];

        this.stage = stage;

        this.enumTypeMenuInterfaceReturnBack = enumTypeMenuInterface;
        
        this.currentUserStrFullName = currentUserStrFullName;
        this.currentUserLngNoCompte = currentUserLngNoCompte;
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;

        lblFullName.setText(currentUserStrFullName);
        lblAccountInfos.setText("Numéro du Compte:\t" + Long.toString(currentUserLngNoCompte) + "\nCode Identification:\t\t" + Integer.toString(currentUserIntCodeIdentification));

        // Set mask for MaskedTextField
        String strMaskNoCompte = "", strMaskNoTel = "", strMaskEmail = "", strMaskNom = "", strMaskPrenom = "", strMaskAdresse = "", strMaskCodePostal = "", strMaskVille = "";

        for (int i=0; i<Config.intFormatLengthNoCompte; i++)
            strMaskNoCompte += "0";
        // for (int i=0; i<Config.intFormatLengthCodeIdentification; i++)
        //     strMaskCodeIdentification += "0";
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
        // currentListViewSelectedVisiteur = null;
        lvAccountInfos.setCellFactory(cell -> new ListCell<Visiteur>() {
            private ImageView imageView;

            @Override
            public void updateItem(Visiteur visiteur, boolean blnVide) {
                super.updateItem(visiteur, blnVide);

                if (blnVide || visiteur == null || visiteur.getListViewAccountInfos(true) == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent");
                    setGraphic(null);
                }
                else {
                    imageView = new ImageView(new Image(getClass().getResourceAsStream(Config.strImageFileAccountInfos)));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);

                    setText(visiteur.getListViewAccountInfos(true));
                    setStyle("");
                }
            }
        });
        lvAccountInfos.getSelectionModel().selectedItemProperty().addListener(changeListener -> {
            try {
                if (!blnListViewSelectedTwice && blnTextFieldsDiscardChanges()) {
                    currentListViewSelectedVisiteur = lvAccountInfos.getSelectionModel().getSelectedItem();
                    intListViewSelectedIndex = lvAccountInfos.getSelectionModel().getSelectedIndex();

                    if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur) {
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
                        resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), false);

                        // if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur) {
                        //     logger.log(Level.INFO, "ListView Hard Reset\n");
                        //     resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                        // }
                        // else {
                        //     logger.log(Level.INFO, "ListView Soft Reset\n");
                        //     resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), false);
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
        resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);

        if (blnDisableAllButtons) {
            lblTitleOption.setText("");

            btnMenuAjouterVisiteur.setDisable(true);
            btnMenuModifierVisiteur.setDisable(true);
            btnMenuSupprimerVisiteur.setDisable(true);
            btnRestoreAllFields.setDisable(true);
            btnClearAllFields.setDisable(true);
            btnClearDatePicker.setDisable(true);

            btnSendReport.setDisable(true);
            btnGotoFormulaire.setDisable(true);
        }
    }

    private void resetListView(ArrayList<Visiteur> arrVisiteurs, boolean blnResetHard, boolean ...blnArrOptional) {
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // reset hard by also clearing textfields & reseting currentListViewSelectedVisiteur
        if (blnResetHard) {
            // System.out.println("\n--->Textfields cleared\n");
            clearAllFields();

            intListViewSelectedIndex = -1;
            blnListViewSelectedTwice = false;

            // System.out.println("\n--->New Visiteur\n");
            currentListViewSelectedVisiteur = new Visiteur();
        }

        // System.out.println("\nInside method resetListView\n");
        lvAccountInfos.getItems().clear();
        // System.out.println("\nCleared ListView items\n");

        // lvAccountInfos.getSelectionModel().clearSelection();
        // ArrayList<Visiteur> arrVisiteurs = Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, Config.strNewDataFileVisiteurs, null).getArrVisiteurs();

        // System.out.println("\n\nArrVisiteurs Size: " + arrVisiteurs.size() + "\n\n");

        if (arrVisiteurs != null && arrVisiteurs.size() > 0) {
            if (!blnDisableAllButtons) {
                btnMenuModifierVisiteur.setDisable(false);
                btnMenuSupprimerVisiteur.setDisable(false);
            }
            else {
                lblTitleOption.setText("");

                btnMenuAjouterVisiteur.setDisable(true);
                btnMenuModifierVisiteur.setDisable(true);
                btnMenuSupprimerVisiteur.setDisable(true);
                btnRestoreAllFields.setDisable(true);
                btnClearAllFields.setDisable(true);
                btnClearDatePicker.setDisable(true);

                btnSendReport.setDisable(true);
                btnGotoFormulaire.setDisable(true);
            }

            lblListViewInfos.setText("Liste des Visiteurs");
            // for (int i=0; i<15;i++)
            lvAccountInfos.getItems().addAll(arrVisiteurs);
        }
        else {
            if (!blnDisableAllButtons) {
                btnMenuModifierVisiteur.setDisable(true);
                btnMenuSupprimerVisiteur.setDisable(true);
            }
            else {
                lblTitleOption.setText("");

                btnMenuAjouterVisiteur.setDisable(true);
                btnMenuModifierVisiteur.setDisable(true);
                btnMenuSupprimerVisiteur.setDisable(true);
                btnRestoreAllFields.setDisable(true);
                btnClearAllFields.setDisable(true);
                btnClearDatePicker.setDisable(true);
                
                btnSendReport.setDisable(true);
                btnGotoFormulaire.setDisable(true);
            }

            lblListViewInfos.setText("Liste des Visiteurs VIDES!");

            //  ou se déconnecter pour que l'application créé un compte visiteur automatiquement
            logger.log(Level.SEVERE, "Aucun compte visiteur VALIDE présent dans le fichier Visiteurs.csv!\nVeuillez ajouter un nouveau compte visiteur!\n");

            // ou se déconnecter pour que l'application créé un compte visiteur automatiquement
            Alert alertError = new Alert(AlertType.ERROR, "Aucun compte visiteur VALIDE présent dans le fichier Visiteurs.csv\n\nVeuillez ajouter un nouveau compte visiteur", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    private void clearAllFields(boolean ...blnArrOptional) {
        // disable textfield No Compte if it is current selected visiteur
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // System.out.println("\n\nblnClearListBeforetextFields: " + blnClearListBeforetextFields + "\n\n");

        if (blnClearListBeforetextFields) {
            currentListViewSelectedVisiteur = null;
            // lvAccountInfos.getItems().clear(); // Seriously WTF, fuck ListViews & fuck Java for calling 'selectedItemProperty()' when deleting list view

            System.out.println("\n\nCleared List View Before TextFields!\n\n");
        }

        // if an account from list view is selected, disable Textfield No Compte
        // if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && currentListViewSelectedVisiteur.getLngNoCompte() == currentUserLngNoCompte && !(lvAccountInfos.getSelectionModel().isEmpty()))
        // /*&& lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur*/
        //     // if current user visiteur is selected in listview, disable Textfield No Compte
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
        txtFieldNoTel.clear();
        txtFieldCourriel.clear();
        // txtFieldDateNaissance.clear();
        txtFieldNom.clear();
        txtFieldPrenom.clear();
        txtFieldAdresse.clear();
        txtFieldCodePostal.clear();
        txtFieldVille.clear();

        // set Textfields text to empty
        txtFieldNoTel.setText("");
        txtFieldCourriel.setText("");
        // txtFieldDateNaissance.setText("");
        txtFieldNom.setText("");
        txtFieldPrenom.setText("");
        txtFieldAdresse.setText("");
        txtFieldCodePostal.setText("");
        txtFieldVille.setText("");

        // set Textfields plaintext to empty
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
        lblInvalidNoTel.setText("");
        lblInvalidCourriel.setText("");
        lblInvalidDateNaissance.setText("");
        lblInvalidNom.setText("");
        lblInvalidPrenom.setText("");
        lblInvalidAdresse.setText("");
        lblInvalidCodePostal.setText("");
        lblInvalidVille.setText("");

        // clear Textfields Style
        txtFieldNoCompte.setStyle(Config.strStyleSuccessBorder);
        txtFieldNoTel.setStyle(Config.strStyleSuccessBorder);
        txtFieldCourriel.setStyle(Config.strStyleSuccessBorder);
        // txtFieldDateNaissance.setStyle(Config.strStyleSuccessBorder);
        txtFieldNom.setStyle(Config.strStyleSuccessBorder);
        txtFieldPrenom.setStyle(Config.strStyleSuccessBorder);
        txtFieldAdresse.setStyle(Config.strStyleSuccessBorder);
        txtFieldCodePostal.setStyle(Config.strStyleSuccessBorder);
        txtFieldVille.setStyle(Config.strStyleSuccessBorder);

        // clear Labels Style
        lblInvalidNoCompte.setStyle(Config.strStyleSuccessMessage);
        lblInvalidNoTel.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCourriel.setStyle(Config.strStyleSuccessMessage);
        lblInvalidDateNaissance.setStyle(Config.strStyleSuccessMessage);
        lblInvalidNom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidPrenom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidAdresse.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCodePostal.setStyle(Config.strStyleSuccessMessage);
        lblInvalidVille.setStyle(Config.strStyleSuccessMessage);
    }
    private void restoreAllFields(boolean ...blnArrOptional) {
        clearAllFields();

        // init Textfields with data from selected Visiteur
        txtFieldNoCompte.setPlainText(Long.toString(currentListViewSelectedVisiteur.getLngNoCompte()));
        txtFieldNoTel.setPlainText(Long.toString(currentListViewSelectedVisiteur.getLngNoTel()));
        txtFieldCourriel.setPlainText(currentListViewSelectedVisiteur.getStrCourriel());
        // txtFieldDateNaissance.setPlainText(currentListViewSelectedVisiteur.getStrDateNaissance());
        txtFieldNom.setPlainText(currentListViewSelectedVisiteur.getStrNom());
        txtFieldPrenom.setPlainText(currentListViewSelectedVisiteur.getStrPrenom());
        txtFieldAdresse.setPlainText(currentListViewSelectedVisiteur.getStrAdresse());
        txtFieldCodePostal.setPlainText(currentListViewSelectedVisiteur.getStrCodePostal());
        txtFieldVille.setPlainText(currentListViewSelectedVisiteur.getStrVille());

        try {
            LocalDate localDate = LocalDate.parse(currentListViewSelectedVisiteur.getStrDateNaissance(), Config.DATE_TIME_FORMATTER);
            // System.out.println("\n\n--->Date Naissance Visiteur: '" + localDate.toString() + "'\n\n");
            datePickerDateNaissance.setValue(localDate);
        }
        catch (DateTimeParseException dtpe) {
            System.out.println();
            logger.log(Level.SEVERE, "Impossible d'analyser et de PARSE la date de naissance du compte visiteur [No Compte: '" + currentListViewSelectedVisiteur.getLngNoCompte() + "']\nDate de Naissance: '" + currentListViewSelectedVisiteur.getStrDateNaissance() + "'!\n");
            System.out.println("");

            // Alert alertError = new Alert(AlertType.ERROR, "Impossible d'analyser la date de naissance du compte visiteur [No Compte: '" + currentListViewSelectedVisiteur.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedVisiteur.getIntCodeIdentification() + "']\nDate de Naissance: '" + currentListViewSelectedVisiteur.getStrDateNaissance() + "'\n", ButtonType.OK);
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
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.RETURN_BACK, new MenuInfos(Config.strInterfaceViewMenuVisiteur, currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification, "Menu Employé"));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
        ControllerMenusNaviguation.gotoMenu(stage, logger, enumTypeMenuInterfaceReturnBack, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
    }

    @FXML
    private void btnMenuAjouterVisiteurClick() {
        crudAccountInfos(EnumTypeMenuInterface.ADD);
    }
    @FXML
    private void btnMenuModifierVisiteurClick() {
        crudAccountInfos(EnumTypeMenuInterface.MODIFY);
    }
    @FXML
    private void btnMenuSupprimerVisiteurClick() {
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
        if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment RESTAURER tous les champs de texte ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Restaurer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();
    
            if (confirm.getResult() == ButtonType.YES)
                restoreAllFields();
        
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    @FXML
    private void btnClearDatePickerClick() {
        datePickerDateNaissance.getEditor().clear();
        datePickerDateNaissance.setValue(null);
    }

    @FXML
    private void btnSendReportClick() {
        if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment ENVOYER son rapport de vaccination ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Envoyer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                Alert alertSendNotification = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de rapport de vaccination au visiteur ci-dessous qui a comme information\n\n" + currentListViewSelectedVisiteur.getEmailConfirmation(false), ButtonType.OK);
                alertSendNotification.showAndWait();
            }
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    @FXML
    private void btnGotoFormulaireClick() {
        if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment GÉRER les formulaires d'identification ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Gérer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) 
                ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_FORMULAIRES, EnumTypeMenuInterface.GESTION_VISITEURS, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification, currentListViewSelectedVisiteur));
            
            // ArrayList<Formulaire> arrFormulaires = Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.READ, currentListViewSelectedVisiteur).getArrFormulaires();
            // if (arrFormulaires != null && arrFormulaires.size() > 0) {
            //     ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_FORMULAIRES, EnumTypeMenuInterface.GESTION_VISITEURS, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification));
            // }
            // else {
            //     Alert alertError = new Alert(AlertType.INFORMATION, "Le compte visiteur sélectionné n'a aucun formulaire!\n\nVeuillez créer un formulaire pour ", ButtonType.OK);
            //     alertError.showAndWait();
            // }
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!", ButtonType.OK);
            alertError.showAndWait();
        }
    }

    private class ReturnGetVisiteurFromTextFields{
        private Visiteur visiteur;
        private boolean blnErrorNoCompte, blnErrorNoTel, blnErrorCourriel, blnErrorDateNaissance, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille;

        public ReturnGetVisiteurFromTextFields() {
            this.visiteur = new Visiteur();

            this.blnErrorNoCompte = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorDateNaissance = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;
        }
        public ReturnGetVisiteurFromTextFields(Visiteur visiteur) {
            this.visiteur = visiteur;

            this.blnErrorNoCompte = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorDateNaissance = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;
        }
        public ReturnGetVisiteurFromTextFields(boolean blnErrorNoCompte, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorDateNaissance, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille) {
            this.visiteur = new Visiteur();

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorDateNaissance = blnErrorDateNaissance;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;
        }
        public ReturnGetVisiteurFromTextFields(Visiteur visiteur, boolean blnErrorNoCompte, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorDateNaissance, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille) {
            this.visiteur = visiteur;

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorDateNaissance = blnErrorDateNaissance;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;
        }

        public Visiteur getVisiteur() {
            return this.visiteur;
        }
        public boolean getBlnErrorNoCompte() {
            return this.blnErrorNoCompte;
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

        public void setVisiteur(Visiteur visiteur) {
            this.visiteur = visiteur;
        }
        public void setBlnErrorNoCompte(boolean blnErrorNoCompte) {
            this.blnErrorNoCompte = blnErrorNoCompte;
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
    private ReturnGetVisiteurFromTextFields getVisiteurFromTextFields() {
        // Set All TextFields to error style, and only change when input is valid
        txtFieldNoCompte.setStyle(Config.strStyleErrorBorder);
        txtFieldNoTel.setStyle(Config.strStyleErrorBorder);
        txtFieldCourriel.setStyle(Config.strStyleErrorBorder);
        // txtFieldDateNaissance.setStyle(Config.strStyleErrorBorder);
        txtFieldNom.setStyle(Config.strStyleErrorBorder);
        txtFieldPrenom.setStyle(Config.strStyleErrorBorder);
        txtFieldAdresse.setStyle(Config.strStyleErrorBorder);
        txtFieldCodePostal.setStyle(Config.strStyleErrorBorder);
        txtFieldVille.setStyle(Config.strStyleErrorBorder);

        // Set All Labels to error style, and only change when input is valid
        lblInvalidNoCompte.setStyle(Config.strStyleErrorMessage);
        lblInvalidNoTel.setStyle(Config.strStyleErrorMessage);
        lblInvalidCourriel.setStyle(Config.strStyleErrorMessage);
        lblInvalidDateNaissance.setStyle(Config.strStyleErrorMessage);
        lblInvalidNom.setStyle(Config.strStyleErrorMessage);
        lblInvalidPrenom.setStyle(Config.strStyleErrorMessage);
        lblInvalidAdresse.setStyle(Config.strStyleErrorMessage);
        lblInvalidCodePostal.setStyle(Config.strStyleErrorMessage);
        lblInvalidVille.setStyle(Config.strStyleErrorMessage);

        // init all variables that will be returned
        Visiteur visiteur = new Visiteur();

        boolean blnErrorNoCompte = false, blnErrorNoTel = false, blnErrorCourriel = false, blnErrorDateNaissance = false, blnErrorNom = false, blnErrorPrenom = false, blnErrorAdresse = false, blnErrorCodePostal = false, blnErrorVille = false;

        // init all variables that will be used inside this function
        long lngNoCompte = Long.parseLong("0"), lngNoTel = Long.parseLong("0");
        int intCodeIdentification = 0;

        String strNoCompte = "", strNoTel = "", strDateNaissance = "";
        String strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";
        
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
        
        visiteur.setLngNoCompte(lngNoCompte);
        visiteur.setLngNoTel(lngNoTel);
        visiteur.setStrCourriel(txtFieldCourriel.getPlainText());
        // visiteur.setStrDateNaissance((txtFieldDateNaissance.getText().replaceAll("-", "") == null || txtFieldDateNaissance.getText().replaceAll("-", "").isEmpty()) ? "" : txtFieldDateNaissance.getText());
        visiteur.setStrDateNaissance(strDateNaissance); //datePickerDateNaissance.getValue().format(Config.DATE_TIME_FORMATTER).toString());
        visiteur.setStrNom(txtFieldNom.getPlainText());
        visiteur.setStrPrenom(txtFieldPrenom.getPlainText());
        visiteur.setStrAdresse(txtFieldAdresse.getPlainText());
        visiteur.setStrCodePostal(txtFieldCodePostal.getPlainText());
        visiteur.setStrVille(txtFieldVille.getPlainText());

        ReturnGetVisiteurFromTextFields returnGetVisiteurFromTextFields = new ReturnGetVisiteurFromTextFields(visiteur, blnErrorNoCompte, blnErrorNoTel, blnErrorCourriel, blnErrorDateNaissance, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille);
        
        return returnGetVisiteurFromTextFields;
    }
    private boolean blnTextFieldsDiscardChanges(boolean ...blnArrOptionalShowAlertConfirm) {
        boolean blnShowAlertConfirm = true;
        if (blnArrOptionalShowAlertConfirm != null && blnArrOptionalShowAlertConfirm.length > 0)
            blnShowAlertConfirm = blnArrOptionalShowAlertConfirm[0];

        Visiteur visiteurFromTextFields = getVisiteurFromTextFields().getVisiteur();
        logger.log(Level.INFO, "currentListViewSelectedVisiteur: " + currentListViewSelectedVisiteur + "\n");
        logger.log(Level.INFO, "visiteurFromTextFields: " + visiteurFromTextFields + "\n");

        if (currentListViewSelectedVisiteur == null || !(currentListViewSelectedVisiteur instanceof Visiteur)) {
            currentListViewSelectedVisiteur = new Visiteur();
        }

        // check if textfields have changed & if user input new text value
        boolean isTextFieldsChanged;
        try {
            isTextFieldsChanged = !(currentListViewSelectedVisiteur.equalsExactly(visiteurFromTextFields)); //getVisiteurFromTextFields());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "currentListViewSelectedVisiteur ne correspond pas exactement à visiteurFromTextFields!\n");
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
                    if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        if (blnTextFieldsDiscardChanges()) {
                            resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                            clearAllFields();

                            Alert alertInfo = new Alert(AlertType.INFORMATION, "Les champs de texte ont été vidés!\n\nVeuillez remplir les champs de texte et ensuite ajouter le nouveau visiteur\n", ButtonType.OK);
                            alertInfo.showAndWait();
                        }
                    }
                    else {
                        ReturnGetVisiteurFromTextFields returnGetVisiteurFromTextFields = getVisiteurFromTextFields();

                        Visiteur visiteurFromTxtFields = returnGetVisiteurFromTextFields.getVisiteur();
                        boolean blnErrorNoCompte = returnGetVisiteurFromTextFields.getBlnErrorNoCompte(), blnErrorNoTel = returnGetVisiteurFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetVisiteurFromTextFields.getBlnErrorCourriel(), blnErrorDateNaissance = returnGetVisiteurFromTextFields.getBlnErrorDateNaissance(), blnErrorNom = returnGetVisiteurFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetVisiteurFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetVisiteurFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetVisiteurFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetVisiteurFromTextFields.getBlnErrorVille();

                        // if textfields contains error, dont add new visiteur to textfield
                        if (blnErrorNoCompte || blnErrorNoTel || blnErrorCourriel || blnErrorDateNaissance || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille) {
                            String strAlertError = "Les champs de texte suivants sont erronés ou invalides:\n\n";

                            if (blnErrorNoCompte)
                                strAlertError += "Champs de texte Numéro du Compte\n";

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
                        // if textfields are valid, add new visiteur
                        else {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment AJOUTER le compte visiteur ci-dessous qui a comme information ?\n\n" + visiteurFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Ajouter!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Visiteur> arrVisiteurs = Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs();

                                if (arrVisiteurs != null && arrVisiteurs.size() > 0) {
                                    boolean blnVisiteurMatch = false;
                                    loopVisiteurMatch:for (Visiteur b : arrVisiteurs)
                                        if (b.equalsNoCompte(visiteurFromTxtFields)) {
                                            blnVisiteurMatch = true;
                                            break loopVisiteurMatch;
                                        }
            
                                    if (blnVisiteurMatch) {
                                        logger.log(Level.WARNING, "Le visiteur EXISTE DÉJÀ dans le fichier Visiteurs.csv!\nVeuillez ajouter un compte visiteur qui a un numéro de compte différents des numéros de comptes qui se trouvent parmi la liste des visiteurs!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "Le visiteur EXISTE DÉJÀ dans le fichier Visiteurs.csv!\n\nVeuillez ajouter un compte visiteur qui a un numéro de compte différents des autres visiteurs!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrVisiteurs, false);
                                    }
                                    else {
                                        String strCourriel = visiteurFromTxtFields.getEmailConfirmation(false);

                                        Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, visiteurFromTxtFields);
                                        resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                                        clearAllFields();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                }
                                else {
                                    String strCourriel = visiteurFromTxtFields.getEmailConfirmation(false);

                                    // create data file and add new account if file doesnt exist
                                    ReturnCrudDataListVisiteurs returnCrudDataListVisiteurs = Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                    boolean blnAlertErrorDataFolder = returnCrudDataListVisiteurs.getBlnAlertErrorDataFolder(), 
                                            blnAlertErrorDataFileVisiteur = returnCrudDataListVisiteurs.getBlnAlertErrorDataFileVisiteur();

                                    if (blnAlertErrorDataFolder || blnAlertErrorDataFileVisiteur) {
                                        String strAlertError = "";

                                        if (blnAlertErrorDataFolder)
                                            strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                        else
                                            strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                        if (blnAlertErrorDataFileVisiteur)
                                            strAlertError += "Création d'un nouveau fichier de donnés 'Visiteurs.csv' pour les comptes visiteurs!\n\n";

                                        Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, visiteurFromTxtFields);
                                        resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                                        clearAllFields();

                                        Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                        alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                        alertError.showAndWait();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                    else {
                                        resetListView(arrVisiteurs, true);
                                        Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Visiteurs.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                        alertError.showAndWait();
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case MODIFY: {
                    if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // only act & modify if changes to textfields have been made
                        if (!blnTextFieldsDiscardChanges(false)) {
                            ReturnGetVisiteurFromTextFields returnGetVisiteurFromTextFields = getVisiteurFromTextFields();

                            Visiteur visiteurFromTxtFields = returnGetVisiteurFromTextFields.getVisiteur();
                            boolean blnErrorNoCompte = returnGetVisiteurFromTextFields.getBlnErrorNoCompte(), blnErrorNoTel = returnGetVisiteurFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetVisiteurFromTextFields.getBlnErrorCourriel(), blnErrorDateNaissance = returnGetVisiteurFromTextFields.getBlnErrorDateNaissance(), blnErrorNom = returnGetVisiteurFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetVisiteurFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetVisiteurFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetVisiteurFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetVisiteurFromTextFields.getBlnErrorVille();

                            // if textfields contains error, dont add new visiteur to textfield
                            if (blnErrorNoCompte || blnErrorNoTel || blnErrorCourriel || blnErrorDateNaissance || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille) {
                                String strAlertError = "Les champs de texte suivants sont erronés ou invalides:\n\n";

                                if (blnErrorNoCompte)
                                    strAlertError += "Champs de texte Numéro du Compte\n";

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
                            // if textfields are valid, add new visiteur
                            else {
                                Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment MODIFIER le compte visiteur ci-dessous qui a comme information ?\n\n" + visiteurFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Modifier!");
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                confirm.showAndWait();

                                if (confirm.getResult() == ButtonType.YES) {
                                    ArrayList<Visiteur> arrVisiteurs = Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs();

                                    if (arrVisiteurs != null && arrVisiteurs.size() > 0) {
                                        boolean blnVisiteurMatch = false;
                                        loopVisiteurMatch:for (Visiteur b : arrVisiteurs)
                                            if (b.equalsNoCompte(visiteurFromTxtFields)) {
                                                blnVisiteurMatch = true;
                                                break loopVisiteurMatch;
                                            }
                
                                        if (blnVisiteurMatch) {
                                            String strCourriel = visiteurFromTxtFields.getEmailConfirmation(false);

                                            Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, visiteurFromTxtFields);
                                            resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                                            clearAllFields();

                                            Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                            confirmEmail.showAndWait();
                                        }
                                        else {
                                            String strCourriel = visiteurFromTxtFields.getEmailConfirmation(false);

                                            logger.log(Level.WARNING, "Le visiteur choisi comme numéro de compte '" + Long.toString(visiteurFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Visiteurs.csv!\nVoulez-vous CRÉER ce compte visiteur ci-dessous qui a comme information ?\n" + visiteurFromTxtFields.getListViewAccountInfos(false) + "\n");

                                            Alert confirmCreateVisiteur = new Alert(AlertType.CONFIRMATION, "Le visiteur choisi comme numéro de compte '" + Long.toString(visiteurFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Visiteurs.csv!\nVoulez-vous CRÉER ce compte visiteur ci-dessous qui a comme information ?\n\n" + visiteurFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                            ((Button) confirmCreateVisiteur.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Créer!");
                                            ((Button) confirmCreateVisiteur.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                            confirmCreateVisiteur.showAndWait();

                                            if (confirmCreateVisiteur.getResult() == ButtonType.YES) {                                                
                                                System.out.println("\n\n" + visiteurFromTxtFields + "\n\n");
                                                Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, visiteurFromTxtFields);
                                                resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                                                clearAllFields();

                                                Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                                confirmEmail.showAndWait();
                                            }
                                        }
                                    }
                                    else {
                                        String strCourriel = visiteurFromTxtFields.getEmailConfirmation(false);

                                        // create data file and add new account if file doesnt exist
                                        ReturnCrudDataListVisiteurs returnCrudDataListVisiteurs = Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                        boolean blnAlertErrorDataFolder = returnCrudDataListVisiteurs.getBlnAlertErrorDataFolder(), 
                                                blnAlertErrorDataFileVisiteur = returnCrudDataListVisiteurs.getBlnAlertErrorDataFileVisiteur();

                                        if (blnAlertErrorDataFolder || blnAlertErrorDataFileVisiteur) {
                                            String strAlertError = "";

                                            if (blnAlertErrorDataFolder)
                                                strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                            else
                                                strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                            if (blnAlertErrorDataFileVisiteur)
                                                strAlertError += "Création d'un nouveau fichier de donnés 'Visiteurs.csv' pour les comptes visiteurs!\n\n";

                                            Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, visiteurFromTxtFields);
                                            resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                                            clearAllFields();

                                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                            alertError.showAndWait();

                                            Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                            confirmEmail.showAndWait();
                                        }
                                        else {
                                            resetListView(arrVisiteurs, true);
                                            Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Visiteurs.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                            alertError.showAndWait();
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            logger.log(Level.INFO, "Aucun changement n'a été effectué sur les champs de texte!\nVeuillez modifier les champs de texte avant de vouloir modifier ce compte visiteur!\n");

                            Alert alertError = new Alert(AlertType.WARNING, "Aucun changement n'a été effectué sur les champs de texte!\n\nVeuillez modifier les champs de texte avant de vouloir modifier ce compte visiteur!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                break;

            case DELETE: {
                    if (currentListViewSelectedVisiteur != null && currentListViewSelectedVisiteur instanceof Visiteur && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visiteur && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // if (currentListViewSelectedVisiteur.getLngNoCompte() == currentUserLngNoCompte) {
                        //     logger.log(Level.WARNING, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\nVeuillez sélectioner un autre compte visiteur parmi la liste des visiteurs ou créer un nouveau compte visiteur, se connecter à ce compte et ensuite supprimer celui-ci!\n");

                        //     Alert alertError = new Alert(AlertType.ERROR, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\n\nVeuillez sélectioner un autre compte visiteur parmi la liste des visiteurs ou créer un nouveau compte visiteur, se connecter à ce compte et ensuite supprimer celui-ci!\n", ButtonType.OK);
                        //     alertError.showAndWait();
                        // }
                        // else
                        if (blnTextFieldsDiscardChanges()) {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment SUPPRIMER le compte visiteur ci-dessous qui a comme information ?\n\n" + currentListViewSelectedVisiteur.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Supprimer!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Visiteur> arrVisiteurs = Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs();

                                if (arrVisiteurs != null && arrVisiteurs.size() > 0) {
                                    boolean blnVisiteurMatch = false;
                                    loopVisiteurMatch:for (Visiteur b : arrVisiteurs)
                                        if (b.equalsNoCompte(currentListViewSelectedVisiteur)) {
                                            blnVisiteurMatch = true;
                                            break loopVisiteurMatch;
                                        }
            
                                    if (blnVisiteurMatch) {
                                        String strCourriel = currentListViewSelectedVisiteur.getEmailConfirmation(false);

                                        Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.DELETE, currentListViewSelectedVisiteur);
                                        resetListView(Visiteur.crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs(), true);
                                        clearAllFields();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                    else {
                                        logger.log(Level.WARNING, "Le visiteur choisi ne se trouve plus dans le fichier Visiteurs.csv!\nVeuillez sélectionné à nouveau un compte visiteur parmi la liste des visiteurs!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "Le visiteur choisi ne se trouve plus dans le fichier Visiteurs.csv!\n\nVeuillez sélectionné à nouveau un compte visiteur parmi la liste des visiteurs!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrVisiteurs, true);
                                    }
                                }
                                else 
                                    resetListView(arrVisiteurs, true);
                            }
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un compte visiteur parmi la liste des visiteurs!", ButtonType.OK);
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
