package VaxTodo.Controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Models.Visite;
import VaxTodo.Models.Visite.ReturnCrudDataListVisites;
import VaxTodo.Views.Interface.Models.MaskedTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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

/** Controller of Interface ViewMenuGestionCalendrier.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * Let the user add, modify or delete Visites Planifiées
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuGestionCalendrier {
    private final Logger logger = Logger.getLogger(ControllerMenuGestionCalendrier.class.getName());

    private Stage stage;

    private EnumTypeMenuInterface enumTypeMenuInterfaceReturnBack;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;
    
    @FXML
    private Label lblFullName, lblAccountInfos, lblListViewInfos,
                    lblInvalidNoVisite, lblInvalidDateVisite, lblInvalidHeureVisite, lblInvalidNom, lblInvalidPrenom, lblInvalidTypeDose, lblInvalidCourriel;

    @FXML
    private Button btnMenuAjouterVisitePlanifiee, btnMenuModifierVisitePlanifiee, btnMenuSupprimerVisitePlanifiee;

    @FXML
    private ListView<Visite> lvAccountInfos;

    private int intListViewSelectedIndex;
    private boolean blnListViewSelectedTwice;
    private Visite currentListViewSelectedVisite;

    @FXML
    private MaskedTextField txtFieldNoVisite, txtFieldHeureVisite, txtFieldNom, txtFieldPrenom, txtFieldCourriel;

    @FXML
    private DatePicker datePickerDateVisite;

    @FXML
    private ComboBox<Integer> cbTypeDose;

    public void setStageAndSetupListeners(Stage stage, EnumTypeMenuInterface enumTypeMenuInterface, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.stage = stage;

        this.enumTypeMenuInterfaceReturnBack = enumTypeMenuInterface;
        
        this.currentUserStrFullName = currentUserStrFullName;
        this.currentUserLngNoCompte = currentUserLngNoCompte;
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;

        lblFullName.setText(currentUserStrFullName);
        lblAccountInfos.setText("Numéro du Compte:\t" + Long.toString(currentUserLngNoCompte) + "\nCode Identification:\t\t" + Integer.toString(currentUserIntCodeIdentification));

        // Set mask for MaskedTextField
        String strMaskNoVisite = "", strMaskHeureVisite, strMaskNom = "", strMaskPrenom = "", strMaskEmail = "";

        for (int i=0; i<Config.intFormatLengthNoReservation; i++)
        strMaskNoVisite += "0";
        strMaskHeureVisite = "00:00";
        for (int i=0; i<Config.intFormatLengthLastName; i++)
            strMaskNom += "S";
        for (int i=0; i<Config.intFormatLengthFirstName; i++)
            strMaskPrenom += "S";
        for (int i = 0; i<Config.intFormatLengthEmail; i++)
            strMaskEmail += "E";

        txtFieldNoVisite.setMask(strMaskNoVisite);
        txtFieldHeureVisite.setMask(strMaskHeureVisite);
        txtFieldNom.setMask(strMaskNom);
        txtFieldPrenom.setMask(strMaskPrenom);
        txtFieldCourriel.setMask(strMaskEmail);

        // init valid date of birth
        datePickerDateVisite.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean blnVide) {
                super.updateItem(localDate, blnVide);
                
                if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(blnVide || true);
                }
                else {
                    int intLocalHour = LocalTime.now().getHour();
                    setDisable(blnVide || (intLocalHour>Config.intFormatLengthHourMax ? localDate.compareTo(LocalDate.now())<=0  : localDate.compareTo(LocalDate.now())<0));
                }
            }
        });
        datePickerDateVisite.setConverter(new StringConverter<LocalDate>() {    
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
        // currentListViewSelectedVisiteVisite = null;
        lvAccountInfos.setCellFactory(cell -> new ListCell<Visite>() {
            private ImageView imageView;

            @Override
            public void updateItem(Visite visite, boolean blnVide) {
                super.updateItem(visite, blnVide);

                if (blnVide || visite == null || visite.getListViewAccountInfos(true) == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent");
                    setGraphic(null);
                }
                else {
                    imageView = new ImageView(new Image(getClass().getResourceAsStream(Config.strImageFileAccountInfos)));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);

                    setText(visite.getListViewAccountInfos(true));
                    setStyle("");
                }
            }
        });
        lvAccountInfos.getSelectionModel().selectedItemProperty().addListener(changeListener -> {
            try {
                if (!blnListViewSelectedTwice && blnTextFieldsDiscardChanges()) {
                    currentListViewSelectedVisite = lvAccountInfos.getSelectionModel().getSelectedItem();
                    intListViewSelectedIndex = lvAccountInfos.getSelectionModel().getSelectedIndex();

                    if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite) {
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
                        resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), false);

                        // if (currentListViewSelectedVisiteVisite != null && currentListViewSelectedVisiteVisite instanceof Visite) {
                        //     logger.log(Level.INFO, "ListView Hard Reset\n");
                        //     resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                        // }
                        // else {
                        //     logger.log(Level.INFO, "ListView Soft Reset\n");
                        //     resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), false);
                        // }
                    }
                }
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "Exception occured while changing list view selection of 'lvAccountInfos.getSelectionModel().selectedItemProperty()'!\n", e);
            }
        });
            
        // cbTypeDose.getItems().clear();
        // IntStream.rangeClosed(1, Config.intFormatVaccinNbDoseMax).boxed().forEach(cbTypeDose.getItems()::add);
        // Arrays.asList(EnumTypeVaccin.values()).forEach(cbTypeDose.getItems()::add);

        datePickerDateVisite.getEditor().setDisable(true);
        datePickerDateVisite.getEditor().setOpacity(1);
        txtFieldHeureVisite.disableProperty().bind(datePickerDateVisite.valueProperty().isNull().or(datePickerDateVisite.editorProperty().getValue().textProperty().isEmpty()));
        resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
    }

    private void resetListView(ArrayList<Visite> arrVisites, boolean blnResetHard, boolean ...blnArrOptional) {
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // reset hard by also clearing textfields & reseting currentListViewSelectedVisite
        if (blnResetHard) {
            // System.out.println("\n--->Textfields cleared\n");
            clearAllFields();

            intListViewSelectedIndex = -1;
            blnListViewSelectedTwice = false;

            // System.out.println("\n--->New Visite\n");
            currentListViewSelectedVisite = new Visite();
        }

        // System.out.println("\nInside method resetListView\n");
        lvAccountInfos.getItems().clear();
        // System.out.println("\nCleared ListView items\n");

        // lvAccountInfos.getSelectionModel().clearSelection();
        // ArrayList<Visite> arrVisites = Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, Config.strNewDataFileVisites, null).getArrVisites();

        // System.out.println("\n\nArrVisites Size: " + arrVisites.size() + "\n\n");

        if (arrVisites != null && arrVisites.size() > 0) {
            btnMenuModifierVisitePlanifiee.setDisable(false);
            btnMenuSupprimerVisitePlanifiee.setDisable(false);

            lblListViewInfos.setText("Liste des Visites Planifiées");
            // for (int i=0; i<15;i++)
            lvAccountInfos.getItems().addAll(arrVisites);
        }
        else {
            btnMenuModifierVisitePlanifiee.setDisable(true);
            btnMenuSupprimerVisitePlanifiee.setDisable(true);

            lblListViewInfos.setText("Liste des Visites VIDES!");

            //  ou se déconnecter pour que l'application créé un compte visite automatiquement
            logger.log(Level.SEVERE, "Aucune visite planifiée VALIDE présent dans le fichier Visites.csv!\nVeuillez ajouter une nouvelle visite planifiée!\n");

            // ou se déconnecter pour que l'application créé un compte visite automatiquement
            Alert alertError = new Alert(AlertType.ERROR, "Aucune visite planifiée VALIDE présent dans le fichier Visites.csv\n\nVeuillez ajouter une nouvelle visite planifiée", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    private void clearAllFields(boolean ...blnArrOptional) {
        // disable textfield No Compte if it is current selected visite
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // System.out.println("\n\nblnClearListBeforetextFields: " + blnClearListBeforetextFields + "\n\n");

        if (blnClearListBeforetextFields) {
            currentListViewSelectedVisite = null;
            // lvAccountInfos.getItems().clear(); // Seriously WTF, fuck ListViews & fuck Java for calling 'selectedItemProperty()' when deleting list view

            System.out.println("\n\nCleared List View Before TextFields!\n\n");
        }

        // if an account from list view is selected, disable Textfield No Compte
        // if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite && currentListViewSelectedVisite.getLngNoCompte() == currentUserLngNoCompte && !(lvAccountInfos.getSelectionModel().isEmpty()))
        // /*&& lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visite*/
        //     // if current user visite is selected in listview, disable Textfield No Compte
        //     txtFieldNoCompte.setDisable(true);
        // else {
        //     txtFieldNoCompte.setDisable(false);

        //     txtFieldNoCompte.clear();
        //     txtFieldNoCompte.setText("");
        //     txtFieldNoCompte.setPlainText("");
        // }
        txtFieldNoVisite.setDisable(false);
        txtFieldNoVisite.clear();
        txtFieldNoVisite.setText("");
        txtFieldNoVisite.setPlainText("");

        // clear Textfields Text
        txtFieldHeureVisite.clear();
        txtFieldNom.clear();
        txtFieldPrenom.clear();
        txtFieldCourriel.clear();

        // set Textfields text to empty
        txtFieldHeureVisite.setText("");
        txtFieldNom.setText("");
        txtFieldPrenom.setText("");
        txtFieldCourriel.setText("");

        // set Textfields plaintext to empty
        txtFieldHeureVisite.setPlainText("");
        txtFieldNom.setPlainText("");
        txtFieldPrenom.setPlainText("");
        txtFieldCourriel.setPlainText("");

        datePickerDateVisite.getEditor().clear();
        datePickerDateVisite.setValue(null);

        cbTypeDose.getItems().clear();
        // cbTypeDose.getEditor().setPromptText("Choisir Type de Dose");
        IntStream.rangeClosed(1, Config.intFormatVaccinNbDoseMax).boxed().forEach(cbTypeDose.getItems()::add);

        // clear Labels Text
        lblInvalidNoVisite.setText("");
        lblInvalidHeureVisite.setText("");
        lblInvalidDateVisite.setText("");
        lblInvalidNom.setText("");
        lblInvalidPrenom.setText("");
        lblInvalidTypeDose.setText("");
        lblInvalidCourriel.setText("");

        // clear Textfields Style
        txtFieldNoVisite.setStyle(Config.strStyleSuccessBorder);
        txtFieldHeureVisite.setStyle(Config.strStyleSuccessBorder);
        txtFieldNom.setStyle(Config.strStyleSuccessBorder);
        txtFieldPrenom.setStyle(Config.strStyleSuccessBorder);
        txtFieldCourriel.setStyle(Config.strStyleSuccessBorder);

        // clear Labels Style
        lblInvalidNoVisite.setStyle(Config.strStyleSuccessMessage);
        lblInvalidHeureVisite.setStyle(Config.strStyleSuccessMessage);
        lblInvalidDateVisite.setStyle(Config.strStyleSuccessMessage);
        lblInvalidNom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidPrenom.setStyle(Config.strStyleSuccessMessage);
        lblInvalidTypeDose.setStyle(Config.strStyleSuccessMessage);
        lblInvalidCourriel.setStyle(Config.strStyleSuccessMessage);
    }
    private void restoreAllFields(boolean ...blnArrOptional) {
        clearAllFields();

        // init Textfields with data from selected Visite
        txtFieldNoVisite.setPlainText(Integer.toString(currentListViewSelectedVisite.getIntNoVisite()));
        txtFieldHeureVisite.setPlainText(currentListViewSelectedVisite.getStrHeureVisite());
        txtFieldNom.setPlainText(currentListViewSelectedVisite.getStrNom());
        txtFieldPrenom.setPlainText(currentListViewSelectedVisite.getStrPrenom());
        txtFieldCourriel.setPlainText(currentListViewSelectedVisite.getStrCourriel());

        try {
            LocalDate localDate = LocalDate.parse(currentListViewSelectedVisite.getStrDateVisite(), Config.DATE_TIME_FORMATTER);
            // System.out.println("\n\n--->Date Naissance Visite: '" + localDate.toString() + "'\n\n");
            datePickerDateVisite.setValue(localDate);
        }
        catch (DateTimeParseException dtpe) {
            System.out.println();
            logger.log(Level.SEVERE, "Impossible d'analyser et de PARSE la date de visite du visite [No Réservation: '" + currentListViewSelectedVisite.getIntNoVisite() + "']\nDate de Visite: '" + currentListViewSelectedVisite.getStrDateVisite() + "'!\n");
            System.out.println("");

            // Alert alertError = new Alert(AlertType.ERROR, "Impossible d'analyser la date de naissance du compte visite [No Compte: '" + currentListViewSelectedVisite.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedVisite.getIntCodeIdentification() + "']\nDate de Naissance: '" + currentListViewSelectedVisite.getStrDateNaissance() + "'\n", ButtonType.OK);
            // alertError.showAndWait();

            datePickerDateVisite.getEditor().clear();
            datePickerDateVisite.setValue(null);
        }

        cbTypeDose.setValue(currentListViewSelectedVisite.getIntTypeDose());
        // IntStream.rangeClosed(1, Config.intFormatVaccinNbDoseMax).boxed().forEach(cbTypeDose.getItems()::add);
    }

    @FXML
    private void btnDisconnectClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }

    @FXML
    private void btnReturnClick() {
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.RETURN_BACK, new MenuInfos(Config.strInterfaceViewMenuVisitePlanifiee, currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification, "Menu Employé"));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
        ControllerMenusNaviguation.gotoMenu(stage, logger, enumTypeMenuInterfaceReturnBack, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
    }

    @FXML
    private void btnMenuAjouterVisitePlanifieeClick() {
        crudAccountInfos(EnumTypeMenuInterface.ADD);
    }
    @FXML
    private void btnMenuModifierVisitePlanifieeClick() {
        crudAccountInfos(EnumTypeMenuInterface.MODIFY);
    }
    @FXML
    private void btnMenuSupprimerVisitePlanifieeClick() {
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
        if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visite && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment RESTAURER tous les champs de texte ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Restaurer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();
    
            if (confirm.getResult() == ButtonType.YES)
                restoreAllFields();
        
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner une visite parmi la liste des visites planifiées!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner une visite parmi la liste des visites planifiées!", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    @FXML
    private void btnClearDatePickerClick() {
        datePickerDateVisite.getEditor().clear();
        datePickerDateVisite.setValue(null);
    }
    @FXML
    private void btnSendNotificationClick() {
        if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visite && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment ENVOYER une notification de rappel ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Envoyer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                Alert alertSendNotification = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de notification de rappel au visiteur ci-dessous qui a comme information\n\n" + currentListViewSelectedVisite.getEmailConfirmation(false), ButtonType.OK);
                alertSendNotification.showAndWait();
            }
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner une visite parmi la liste des visites planifiées!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner une visite parmi la liste des visites planifiées!", ButtonType.OK);
            alertError.showAndWait();
        }
    }

    private class ReturnGetVisiteFromTextFields{
        private Visite visite;
        private boolean blnErrorNoVisite, blnErrorTypeDose, blnErrorHeureVisite, blnErrorDateVisite, blnErrorNom, blnErrorPrenom, blnErrorCourriel;

        public ReturnGetVisiteFromTextFields() {
            this.visite = new Visite();

            this.blnErrorNoVisite = false;
            this.blnErrorTypeDose = false;
            this.blnErrorHeureVisite = false;
            this.blnErrorDateVisite = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorCourriel = false;
        }
        public ReturnGetVisiteFromTextFields(Visite visite, boolean blnErrorNoVisite, boolean blnErrorTypeDose, boolean blnErrorHeureVisite, boolean blnErrorDateVisite, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorCourriel) {
            this.visite = visite;

            this.blnErrorNoVisite = blnErrorNoVisite;
            this.blnErrorTypeDose = blnErrorTypeDose;
            this.blnErrorHeureVisite = blnErrorHeureVisite;
            this.blnErrorDateVisite = blnErrorDateVisite;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorCourriel = blnErrorCourriel;
        }

        public Visite getVisite() {
            return this.visite;
        }
        public boolean getBlnErrorNoVisite() {
            return this.blnErrorNoVisite;
        }
        public boolean getBlnErrorTypeDose() {
            return this.blnErrorTypeDose;
        }
        public boolean getBlnErrorHeureVisite() {
            return this.blnErrorHeureVisite;
        }
        public boolean getBlnErrorDateVisite() {
            return this.blnErrorDateVisite;
        }
        public boolean getBlnErrorNom() {
            return this.blnErrorNom;
        }
        public boolean getBlnErrorPrenom() {
            return this.blnErrorPrenom;
        }
        public boolean getBlnErrorCourriel() {
            return this.blnErrorCourriel;
        }
    }
    private ReturnGetVisiteFromTextFields getVisiteFromTextFields() {
        // Set All TextFields to error style, and only change when input is valid
        txtFieldNoVisite.setStyle(Config.strStyleErrorBorder);
        txtFieldHeureVisite.setStyle(Config.strStyleErrorBorder);
        txtFieldNom.setStyle(Config.strStyleErrorBorder);
        txtFieldPrenom.setStyle(Config.strStyleErrorBorder);
        txtFieldCourriel.setStyle(Config.strStyleErrorBorder);

        // Set All Labels to error style, and only change when input is valid
        lblInvalidNoVisite.setStyle(Config.strStyleErrorMessage);
        lblInvalidHeureVisite.setStyle(Config.strStyleErrorMessage);
        lblInvalidDateVisite.setStyle(Config.strStyleErrorMessage);
        lblInvalidNom.setStyle(Config.strStyleErrorMessage);
        lblInvalidPrenom.setStyle(Config.strStyleErrorMessage);
        lblInvalidTypeDose.setStyle(Config.strStyleErrorMessage);
        lblInvalidCourriel.setStyle(Config.strStyleErrorMessage);

        // init all variables that will be returned
        Visite visite = new Visite();

        boolean blnErrorNoVisite = false, blnErrorTypeDose = false, blnErrorHeureVisite = false, blnErrorDateVisite = false, blnErrorNom = false, blnErrorPrenom = false, blnErrorCourriel = false;

        // init all variables that will be used inside this function
        int intNoVisite = 0, intTypeDose = 0, intHours = -1, intMinutes = -1;

        String strNoVisite = "", strTypeDose = "", strHeureVisite = "", strDateVisite = "";
        String strNom = "", strPrenom = "", strCourriel = "";
        
        try {
            strNoVisite = txtFieldNoVisite.getPlainText();
            intNoVisite = Integer.parseInt(strNoVisite);
        } 
        catch (NumberFormatException nfe) {
            blnErrorNoVisite = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'intNoVisite' for TextField 'txtFieldNoVisite'\n"); //, nfe);
            intNoVisite = 0;
        }
        if (intNoVisite > 0 && strNoVisite.length() == Config.intFormatLengthNoReservation) {
            txtFieldNoVisite.setStyle(Config.strStyleSuccessBorder);
            lblInvalidNoVisite.setText("Numéro de Réservation Valide");
            lblInvalidNoVisite.setStyle(Config.strStyleSuccessMessage);
        }
        else if (intNoVisite <= 0) {
            blnErrorNoVisite = true;
            lblInvalidNoVisite.setText("Doit être un chiffre entier et supérieur à 0!");
        }
        else if (strNoVisite.length() != Config.intFormatLengthNoReservation) {
            blnErrorNoVisite = true;
            lblInvalidNoVisite.setText("Longueur exacte à " + Config.intFormatLengthNoReservation + " charactères!");
        }
        
        if (cbTypeDose.getValue() == null)
            strTypeDose = "";
        else 
            strTypeDose = cbTypeDose.getValue().toString();
        try {
            intTypeDose = Integer.parseInt(strTypeDose);
        } 
        catch (NumberFormatException nfe) {
            blnErrorTypeDose = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'intTypeDose' for ComboBox 'cbTypeDose'\n"); //, nfe);
            intTypeDose = 0;
        }
        if (intTypeDose > 0) {
            lblInvalidTypeDose.setText("Type de Dose Valide");
            lblInvalidTypeDose.setStyle(Config.strStyleSuccessMessage);
        }
        else if (intTypeDose <= 0) {
            blnErrorTypeDose = true;
            lblInvalidTypeDose.setText("Doit être un chiffre entier et supérieur à 0!");
        }
        
        LocalDate localDateValue = datePickerDateVisite.getValue();
        if (localDateValue == null)
            strDateVisite = "";
        else
            strDateVisite = localDateValue.toString();
        if (!(strDateVisite.trim().isEmpty()) && strDateVisite.length() == Config.intFormatLengthDate) {
            lblInvalidDateVisite.setText("Date de Visite Valide");
            lblInvalidDateVisite.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strDateVisite.trim().isEmpty()) {
            blnErrorDateVisite = true;
            lblInvalidDateVisite.setText("Date de Visite VIDE");
            lblInvalidDateVisite.setStyle(Config.strStyleErrorMessage);
        }
        else if (!(strDateVisite.trim().isEmpty()) && strDateVisite.length() != Config.intFormatLengthDate) {
            blnErrorDateVisite = true;
            lblInvalidDateVisite.setText("Format Date: yyyy-mm-dd");
            lblInvalidDateVisite.setStyle(Config.strStyleErrorMessage);
        }

        strHeureVisite = txtFieldHeureVisite.getText();
        // System.out.println("\n\nHeure de Visite: " + strHeureVisite + "\n\n");
        try {
            // System.out.println("\n\n" + strHeureVisite.split(":")[0] + "\n\n");
            intHours = Integer.parseInt(strHeureVisite.split(":")[0]);
        } 
        catch (NumberFormatException nfe) {
            blnErrorTypeDose = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'intHours' for TextField 'txtFieldHeureVisite'\n"); //, nfe);
            intHours = -1;
        }
        catch (Exception e) {
            blnErrorTypeDose = true;

            logger.log(Level.WARNING, "Error General Exception 'intHours' for TextField 'txtFieldHeureVisite'\n"); //, e);
            intHours = -1;
        }
        try {
            intMinutes = Integer.parseInt(strHeureVisite.split(":")[1]);
        } 
        catch (NumberFormatException nfe) {
            blnErrorTypeDose = true;

            logger.log(Level.WARNING, "Error NumberFormatException 'intMinutes' for TextField 'txtFieldHeureVisite'\n"); //, nfe);
            intMinutes = -1;
        }
        catch (Exception e) {
            blnErrorTypeDose = true;

            logger.log(Level.WARNING, "Error General Exception 'intMinutes' for TextField 'txtFieldHeureVisite'\n"); //, e);
            intMinutes = -1;
        }
        if (intHours == -1 && intMinutes == -1)
            strHeureVisite = "";

        int intLocalHour = LocalTime.now().getHour();
        // Date Visite is not null or empty and Date Visite equals to Today's Date
        if (!(strDateVisite.trim().isEmpty()) && strDateVisite.length() == Config.intFormatLengthDate && localDateValue.compareTo(LocalDate.now()) == 0) {
            if (intLocalHour >= Config.intFormatLengthHourMin && intLocalHour < Config.intFormatLengthHourMax) {
                if (intHours >= intLocalHour && intHours < Config.intFormatLengthHourMax && intMinutes >= 0 && intMinutes <= 59) {
                    txtFieldHeureVisite.setStyle(Config.strStyleSuccessBorder);
                    lblInvalidHeureVisite.setText("Heures et Minutes de Visite Valide");
                    lblInvalidHeureVisite.setStyle(Config.strStyleSuccessMessage);
                }
                else if (intHours < intLocalHour || intHours >= Config.intFormatLengthHourMax) {
                    blnErrorHeureVisite = true;
                    lblInvalidHeureVisite.setText("Heures AJOURD'HUI entre " + intLocalHour + "h et " + (Config.intFormatLengthHourMax-1) + "h59 !");
                }
                else if (intMinutes < 0 || intMinutes > 59) {
                    blnErrorHeureVisite = true;
                    lblInvalidHeureVisite.setText("Minutes doivent être entre 0min et 59min !");
                }
            }
            else if (intLocalHour < Config.intFormatLengthHourMin) {
                if (intHours >= Config.intFormatLengthHourMin && intHours < Config.intFormatLengthHourMax && intMinutes >= 0 && intMinutes <= 59) {
                    txtFieldHeureVisite.setStyle(Config.strStyleSuccessBorder);
                    lblInvalidHeureVisite.setText("Heures et Minutes de Visite Valide");
                    lblInvalidHeureVisite.setStyle(Config.strStyleSuccessMessage);
                }
                else if (intHours < Config.intFormatLengthHourMin || intHours >= Config.intFormatLengthHourMax) {
                    blnErrorHeureVisite = true;
                    lblInvalidHeureVisite.setText("Heures doivent être entre " + Config.intFormatLengthHourMin + "h et " + (Config.intFormatLengthHourMax-1) + "h59 !");
                }
                else if (intMinutes < 0 || intMinutes > 59) {
                    blnErrorHeureVisite = true;
                    lblInvalidHeureVisite.setText("Minutes doivent être entre 0min et 59min !");
                }
            }
            else if (intLocalHour >= Config.intFormatLengthHourMax) {
                blnErrorHeureVisite = true;
                lblInvalidHeureVisite.setText("Fermé pour AJOURD'HUI (" + (Config.intFormatLengthHourMax-1) + "h59 < " + intLocalHour + "h) !");
            }
            else {
                blnErrorHeureVisite = true;
                lblInvalidHeureVisite.setText("Fermé pour AJOURD'HUI !");
            }
        }
        else {
            if (intHours >= Config.intFormatLengthHourMin && intHours < Config.intFormatLengthHourMax && intMinutes >= 0 && intMinutes <= 59) {
                txtFieldHeureVisite.setStyle(Config.strStyleSuccessBorder);
                lblInvalidHeureVisite.setText("Heures et Minutes de Visite Valide");
                lblInvalidHeureVisite.setStyle(Config.strStyleSuccessMessage);
            }
            else if (intHours < Config.intFormatLengthHourMin || intHours >= Config.intFormatLengthHourMax) {
                blnErrorHeureVisite = true;
                lblInvalidHeureVisite.setText("Heures doivent être entre " + Config.intFormatLengthHourMin + "h et " + (Config.intFormatLengthHourMax-1) + "h59 !");
            }
            else if (intMinutes < 0 || intMinutes > 59) {
                blnErrorHeureVisite = true;
                lblInvalidHeureVisite.setText("Minutes doivent être entre 0min et 59min !");
            }
        }

        strNom = txtFieldNom.getPlainText();
        if (!strNom.trim().isEmpty() && strNom.length() <= Config.intFormatLengthLastName) {
            txtFieldNom.setStyle(Config.strStyleSuccessBorder);
            lblInvalidNom.setText("Nom Valide");
            lblInvalidNom.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strNom.trim().isEmpty()) {
            blnErrorNom = true;
            lblInvalidNom.setText("Nom de Famille VIDE!");
        }
        else if (strNom.length() > Config.intFormatLengthLastName) {
            blnErrorNom = true;
            lblInvalidNom.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthLastName + " charactères!");
        }
        
        strPrenom = txtFieldPrenom.getPlainText();
        if (!strPrenom.trim().isEmpty() && strPrenom.length() <= Config.intFormatLengthFirstName) {
            txtFieldPrenom.setStyle(Config.strStyleSuccessBorder);
            lblInvalidPrenom.setText("Prénom Valide");
            lblInvalidPrenom.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strPrenom.trim().isEmpty()) {
            blnErrorPrenom = true;
            lblInvalidPrenom.setText("Prénom VIDE!");
        }
        else if (strPrenom.length() > Config.intFormatLengthFirstName) {
            blnErrorPrenom = true;
            lblInvalidPrenom.setText("Longueur exacte ou inférieur à " + Config.intFormatLengthFirstName + " charactères!");
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
        
        visite.setIntNoVisite(intNoVisite);
        visite.setIntTypeDose(intTypeDose);
        visite.setStrHeureVisite(strHeureVisite);
        visite.setStrDateVisite(strDateVisite);
        visite.setStrNom(txtFieldNom.getPlainText());
        visite.setStrPrenom(txtFieldPrenom.getPlainText());
        visite.setStrCourriel(txtFieldCourriel.getPlainText());

        // System.out.println("\n\nVisite: '" + visite + "'\n\n");

        ReturnGetVisiteFromTextFields returnGetVisiteFromTextFields = new ReturnGetVisiteFromTextFields(visite, blnErrorNoVisite, blnErrorTypeDose, blnErrorHeureVisite, blnErrorDateVisite, blnErrorNom, blnErrorPrenom, blnErrorCourriel);
        
        return returnGetVisiteFromTextFields;
    }
    private boolean blnTextFieldsDiscardChanges(boolean ...blnArrOptionalShowAlertConfirm) {
        boolean blnShowAlertConfirm = true;
        if (blnArrOptionalShowAlertConfirm != null && blnArrOptionalShowAlertConfirm.length > 0)
            blnShowAlertConfirm = blnArrOptionalShowAlertConfirm[0];

        Visite visiteFromTextFields = getVisiteFromTextFields().getVisite();
        logger.log(Level.INFO, "currentListViewSelectedVisite: " + currentListViewSelectedVisite + "\n");
        logger.log(Level.INFO, "visiteFromTextFields: " + visiteFromTextFields + "\n");

        if (currentListViewSelectedVisite == null || !(currentListViewSelectedVisite instanceof Visite)) {
            currentListViewSelectedVisite = new Visite();
        }

        // check if textfields have changed & if user input new text value
        boolean isTextFieldsChanged;
        try {
            isTextFieldsChanged = !(currentListViewSelectedVisite.equalsExactly(visiteFromTextFields)); //getVisiteFromTextFields());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "currentListViewSelectedVisite ne correspond pas exactement à visiteFromTextFields!\n");
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
                    if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visite && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        if (blnTextFieldsDiscardChanges()) {
                            resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                            clearAllFields();

                            Alert alertInfo = new Alert(AlertType.INFORMATION, "Les champs de texte ont été vidés!\n\nVeuillez remplir les champs de texte et ensuite ajouter la nouvelle visite\n", ButtonType.OK);
                            alertInfo.showAndWait();
                        }
                    }
                    else {
                        ReturnGetVisiteFromTextFields returnGetVisiteFromTextFields = getVisiteFromTextFields();

                        Visite visiteFromTxtFields = returnGetVisiteFromTextFields.getVisite();
                        boolean blnErrorNoVisite = returnGetVisiteFromTextFields.getBlnErrorNoVisite(), blnErrorTypeDose = returnGetVisiteFromTextFields.getBlnErrorTypeDose(), blnErrorHeureVisite = returnGetVisiteFromTextFields.getBlnErrorHeureVisite(), blnErrorDateVisite = returnGetVisiteFromTextFields.getBlnErrorDateVisite(), blnErrorNom = returnGetVisiteFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetVisiteFromTextFields.getBlnErrorPrenom(), blnErrorCourriel = returnGetVisiteFromTextFields.getBlnErrorCourriel();

                        System.out.println("\n\nVisite from TextFields: " + visiteFromTxtFields + "\n\n");

                        // if textfields contains error, dont add new visite to textfield
                        if (blnErrorNoVisite || blnErrorTypeDose || blnErrorHeureVisite || blnErrorDateVisite || blnErrorNom || blnErrorPrenom || blnErrorCourriel) {
                            String strAlertError = "Les champs de texte suivants sont erronés ou invalides:\n\n";

                            if (blnErrorNoVisite)
                                strAlertError += "Champs de texte Numéro de Réservation\n";

                            if (blnErrorHeureVisite)
                                strAlertError += "Champs de texte Heure de Visite\n";

                            if (blnErrorDateVisite)
                                strAlertError += "Champs de texte Date de Visite\n";

                            if (blnErrorNom)
                                strAlertError += "Champs de texte Nom\n";

                            if (blnErrorPrenom)
                                strAlertError += "Champs de texte Prénom\n";

                            if (blnErrorTypeDose)
                                strAlertError += "Champs de texte Type de Dose\n";

                            if (blnErrorCourriel)
                                strAlertError += "Champs de texte Courriel\n";

                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alertError.showAndWait();
                        }
                        // if textfields are valid, add new visite
                        else {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment AJOUTER la visite planifiée ci-dessous qui a comme information ?\n\n" + visiteFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Ajouter!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Visite> arrVisites = Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites();

                                if (arrVisites != null && arrVisites.size() > 0) {
                                    boolean blnVisiteMatch = false;
                                    loopVisiteMatch:for (Visite b : arrVisites)
                                        if (b.equalsNoVisite(visiteFromTxtFields)) {
                                            blnVisiteMatch = true;
                                            break loopVisiteMatch;
                                        }
            
                                    if (blnVisiteMatch) {
                                        logger.log(Level.WARNING, "La visite planifiée EXISTE DÉJÀ dans le fichier Visites.csv!\nVeuillez ajouter une visite qui a un numéro de réservation différents des numéros de réservation qui se trouvent parmi la liste des visites planifiées!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "La visite planifiée EXISTE DÉJÀ dans le fichier Visites.csv!\n\nVeuillez ajouter une visite qui a un numéro de réservation différent des autres visites planifiées!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrVisites, false);
                                    }
                                    else {
                                        String strCourriel = visiteFromTxtFields.getEmailConfirmation(false);

                                        Visite.crudDataListVisites(logger, EnumTypeCRUD.UPDATE, visiteFromTxtFields);
                                        resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                                        clearAllFields();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                }
                                else {
                                    String strCourriel = visiteFromTxtFields.getEmailConfirmation(false);

                                    // create data file and add new account if file doesnt exist
                                    ReturnCrudDataListVisites returnCrudDataListVisites = Visite.crudDataListVisites(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                    boolean blnAlertErrorDataFolder = returnCrudDataListVisites.getBlnAlertErrorDataFolder(), 
                                            blnAlertErrorDataFileVisite = returnCrudDataListVisites.getBlnAlertErrorDataFileVisite();

                                    if (blnAlertErrorDataFolder || blnAlertErrorDataFileVisite) {
                                        String strAlertError = "";

                                        if (blnAlertErrorDataFolder)
                                            strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                        else
                                            strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                        if (blnAlertErrorDataFileVisite)
                                            strAlertError += "Création d'un nouveau fichier de donnés 'Visites.csv' pour les visites planifiées!\n\n";

                                        Visite.crudDataListVisites(logger, EnumTypeCRUD.UPDATE, visiteFromTxtFields);
                                        resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                                        clearAllFields();

                                        Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                        alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                        alertError.showAndWait();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                    else {
                                        resetListView(arrVisites, true);
                                        Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Visites.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                        alertError.showAndWait();
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case MODIFY: {
                    if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visite && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // only act & modify if changes to textfields have been made
                        if (!blnTextFieldsDiscardChanges(false)) {
                            ReturnGetVisiteFromTextFields returnGetVisiteFromTextFields = getVisiteFromTextFields();

                            Visite visiteFromTxtFields = returnGetVisiteFromTextFields.getVisite();
                            boolean blnErrorNoVisite = returnGetVisiteFromTextFields.getBlnErrorNoVisite(), blnErrorTypeDose = returnGetVisiteFromTextFields.getBlnErrorTypeDose(), blnErrorHeureVisite = returnGetVisiteFromTextFields.getBlnErrorHeureVisite(), blnErrorDateVisite = returnGetVisiteFromTextFields.getBlnErrorDateVisite(), blnErrorNom = returnGetVisiteFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetVisiteFromTextFields.getBlnErrorPrenom(), blnErrorCourriel = returnGetVisiteFromTextFields.getBlnErrorCourriel();

                            // if textfields contains error, dont add new visite to textfield
                            if (blnErrorNoVisite || blnErrorTypeDose || blnErrorHeureVisite || blnErrorDateVisite || blnErrorNom || blnErrorPrenom || blnErrorCourriel) {
                                String strAlertError = "Les champs de texte suivants sont erronés ou invalides:\n\n";

                                if (blnErrorNoVisite)
                                    strAlertError += "Champs de texte Numéro de Réservation\n";

                                if (blnErrorHeureVisite)
                                    strAlertError += "Champs de texte Heure de Visite\n";

                                if (blnErrorDateVisite)
                                    strAlertError += "Champs de texte Date de Visite\n";

                                if (blnErrorNom)
                                    strAlertError += "Champs de texte Nom\n";

                                if (blnErrorPrenom)
                                    strAlertError += "Champs de texte Prénom\n";

                                if (blnErrorTypeDose)
                                    strAlertError += "Champs de texte Type de Dose\n";

                                if (blnErrorCourriel)
                                    strAlertError += "Champs de texte Courriel\n";

                                Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alertError.showAndWait();
                            }
                            // if textfields are valid, add new visite
                            else {
                                Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment MODIFIER la visite planifiée ci-dessous qui a comme information ?\n\n" + visiteFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Modifier!");
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                confirm.showAndWait();

                                if (confirm.getResult() == ButtonType.YES) {
                                    ArrayList<Visite> arrVisites = Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites();

                                    if (arrVisites != null && arrVisites.size() > 0) {
                                        boolean blnVisiteMatch = false;
                                        loopVisiteMatch:for (Visite b : arrVisites)
                                            if (b.equalsNoVisite(visiteFromTxtFields)) {
                                                blnVisiteMatch = true;
                                                break loopVisiteMatch;
                                            }
                
                                        if (blnVisiteMatch) {
                                            String strCourriel = visiteFromTxtFields.getEmailConfirmation(false);

                                            Visite.crudDataListVisites(logger, EnumTypeCRUD.UPDATE, visiteFromTxtFields);
                                            resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                                            clearAllFields();

                                            Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                            confirmEmail.showAndWait();
                                        }
                                        else {
                                            String strCourriel = visiteFromTxtFields.getEmailConfirmation(false);

                                            logger.log(Level.WARNING, "La visite planifiée choisi comme numéro de réservation '" + Integer.toString(visiteFromTxtFields.getIntNoVisite()) + "' ne se trouve pas dans le fichier Visites.csv!\nVoulez-vous CRÉER cette visite planifiée ci-dessous qui a comme information ?\n" + visiteFromTxtFields.getListViewAccountInfos(false) + "\n");

                                            Alert confirmCreateVisite = new Alert(AlertType.CONFIRMATION, "La visite planifiée choisi comme numéro de réservation '" + Integer.toString(visiteFromTxtFields.getIntNoVisite()) + "' ne se trouve pas dans le fichier Visites.csv!\nVoulez-vous CRÉER cette visite planifiée ci-dessous qui a comme information ?\n\n" + visiteFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                            ((Button) confirmCreateVisite.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Créer!");
                                            ((Button) confirmCreateVisite.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                            confirmCreateVisite.showAndWait();

                                            if (confirmCreateVisite.getResult() == ButtonType.YES) {                                                
                                                System.out.println("\n\n" + visiteFromTxtFields + "\n\n");
                                                Visite.crudDataListVisites(logger, EnumTypeCRUD.UPDATE, visiteFromTxtFields);
                                                resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                                                clearAllFields();

                                                Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                                confirmEmail.showAndWait();
                                            }
                                        }
                                    }
                                    else {
                                        String strCourriel = visiteFromTxtFields.getEmailConfirmation(false);

                                        // create data file and add new account if file doesnt exist
                                        ReturnCrudDataListVisites returnCrudDataListVisites = Visite.crudDataListVisites(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                        boolean blnAlertErrorDataFolder = returnCrudDataListVisites.getBlnAlertErrorDataFolder(), 
                                                blnAlertErrorDataFileVisite = returnCrudDataListVisites.getBlnAlertErrorDataFileVisite();

                                        if (blnAlertErrorDataFolder || blnAlertErrorDataFileVisite) {
                                            String strAlertError = "";

                                            if (blnAlertErrorDataFolder)
                                                strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                            else
                                                strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                            if (blnAlertErrorDataFileVisite)
                                                strAlertError += "Création d'un nouveau fichier de donnés 'Visites.csv' pour les visites planifiées!\n\n";

                                            Visite.crudDataListVisites(logger, EnumTypeCRUD.UPDATE, visiteFromTxtFields);
                                            resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                                            clearAllFields();

                                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                            alertError.showAndWait();

                                            Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                            confirmEmail.showAndWait();
                                        }
                                        else {
                                            resetListView(arrVisites, true);
                                            Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Visites.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                            alertError.showAndWait();
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            logger.log(Level.INFO, "Aucun changement n'a été effectué sur les champs de texte!\nVeuillez modifier les champs de texte avant de vouloir modifier cette visite planifiée!\n");

                            Alert alertError = new Alert(AlertType.WARNING, "Aucun changement n'a été effectué sur les champs de texte!\n\nVeuillez modifier les champs de texte avant de vouloir modifier cette visite planifiée!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner une visite parmi la liste des visites planifiées!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner une visite parmi la liste des visites planifiées!", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                break;

            case DELETE: {
                    if (currentListViewSelectedVisite != null && currentListViewSelectedVisite instanceof Visite && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Visite && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // if (currentListViewSelectedVisite.getLngNoCompte() == currentUserLngNoCompte) {
                        //     logger.log(Level.WARNING, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\nVeuillez sélectioner un autre compte visite parmi la liste des visites ou créer un nouveau compte visite, se connecter à ce compte et ensuite supprimer celui-ci!\n");

                        //     Alert alertError = new Alert(AlertType.ERROR, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\n\nVeuillez sélectioner un autre compte visite parmi la liste des visites ou créer un nouveau compte visite, se connecter à ce compte et ensuite supprimer celui-ci!\n", ButtonType.OK);
                        //     alertError.showAndWait();
                        // }
                        // else
                        if (blnTextFieldsDiscardChanges()) {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment SUPPRIMER la visite planifiée ci-dessous qui a comme information ?\n\n" + currentListViewSelectedVisite.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Supprimer!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Visite> arrVisites = Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites();

                                if (arrVisites != null && arrVisites.size() > 0) {
                                    boolean blnVisiteMatch = false;
                                    loopVisiteMatch:for (Visite b : arrVisites)
                                        if (b.equalsNoVisite(currentListViewSelectedVisite)) {
                                            blnVisiteMatch = true;
                                            break loopVisiteMatch;
                                        }
            
                                    if (blnVisiteMatch) {
                                        String strCourriel = currentListViewSelectedVisite.getEmailConfirmation(false);

                                        Visite.crudDataListVisites(logger, EnumTypeCRUD.DELETE, currentListViewSelectedVisite);
                                        resetListView(Visite.crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites(), true);
                                        clearAllFields();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au visiteur ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                    else {
                                        logger.log(Level.WARNING, "La visite planifiée choisi ne se trouve plus dans le fichier Visites.csv!\nVeuillez sélectionné à nouveau une visite parmi la liste des visites planifiées!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "La visite planifiée choisi ne se trouve plus dans le fichier Visites.csv!\n\nVeuillez sélectionné à nouveau une visite parmi la liste des visites planifiées!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrVisites, true);
                                    }
                                }
                                else 
                                    resetListView(arrVisites, true);
                            }
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner une visite parmi la liste des visites planifiées!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner une visite parmi la liste des visites planifiées!", ButtonType.OK);
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
