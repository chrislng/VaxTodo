package VaxTodo.Controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.EnumTypeBoolean;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Models.EnumTypeVaccin;
import VaxTodo.Models.Formulaire;
import VaxTodo.Models.Formulaire.ReturnCrudDataListFormulaires;
import VaxTodo.Models.Visiteur;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/** Controller of Interface ViewMenuGestionFormulaires.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * Let the user add, modify or delete Formulaires survey
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenuGestionFormulaires {
    private final Logger logger = Logger.getLogger(ControllerMenuGestionFormulaires.class.getName());

    private Stage stage;

    private EnumTypeMenuInterface enumTypeMenuInterfaceReturnBack;
    
    private String currentUserStrFullName;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    private boolean blnDisableAllButtons;

    private EnumTypeCRUD crudREAD;
    
    @FXML
    private Label lblFullName, lblAccountInfos, lblListViewInfos,
                    lblInvalidNoCompte, lblInvalidNoTel, lblInvalidCourriel, lblInvalidNom, lblInvalidPrenom, lblInvalidAdresse, lblInvalidCodePostal, lblInvalidVille, lblInvalidDateNaissance,
                    lblInvalidCarteAssuranceMaladie, lblInvalidDateVisite, lblInvalidHadCovid, lblInvalidHasAllergies, lblInvalidTypeVaccin,
                    lblInvalidFirstDose, lblInvalidHasCovidSymptoms, lblInvalidGotVaccinated, lblInvalidTypeDose;

    @FXML
    private Button btnMenuAjouterFormulaire, btnMenuModifierFormulaire, btnMenuSupprimerFormulaire, btnRestoreAllFields, btnClearAllFields, btnClearDatePicker, btnClearDatePickerVisite;

    @FXML
    private ListView<Formulaire> lvAccountInfos;

    private int intListViewSelectedIndex;
    private boolean blnListViewSelectedTwice;
    private Formulaire currentListViewSelectedFormulaire;

    @FXML
    private MaskedTextField txtFieldNoCompte, txtFieldNoTel, txtFieldCourriel, txtFieldNom, txtFieldPrenom, txtFieldAdresse, txtFieldCodePostal, txtFieldVille, 
                            txtFieldCarteAssuranceMaladie;
    @FXML
    private TextField txtFieldCodeVaccin;

    @FXML
    private DatePicker datePickerDateNaissance, datePickerDateVisite;

    @FXML
    private ComboBox<EnumTypeBoolean> cbHadCovid, cbHasAllergies,
                                        cbFirstDose, cbHasCovidSymptoms, cbGotVaccinated;
    @FXML
    private ComboBox<EnumTypeVaccin> cbTypeVaccin;
    @FXML
    private ComboBox<Integer> cbTypeDose;

    public void setStageAndSetupListeners(Stage stage, EnumTypeMenuInterface enumTypeMenuInterface, Visiteur visiteurSent, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification, boolean ...blnArrOptional) {
        // this.visiteurParent = null;
        // if (visiteurSent != null)
        //     this.visiteurParent = visiteurSent;
        this.crudREAD = EnumTypeCRUD.READ;
        //! if (visiteurSent != null) // not fully test
        //!     this.crudREAD = EnumTypeCRUD.READ_SAME; // not fully test
        
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
        String strMaskNoCompte = "", strMaskNoTel = "", strMaskEmail = "", strMaskNom = "", strMaskPrenom = "", strMaskAdresse = "", strMaskCodePostal = "", strMaskVille = "",
                strMaskCarteAssuranceMaladie = "";

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

        strMaskCarteAssuranceMaladie = "MMMM MMMM MMMM";
        // for (int i=0; i<Config.intFormatLengthCodeVaccin; i++)
        //     strMaskCodeVaccin += "E";

        txtFieldNoCompte.setMask(strMaskNoCompte);
        txtFieldNoTel.setMask(strMaskNoTel);
        txtFieldCourriel.setMask(strMaskEmail);
        txtFieldNom.setMask(strMaskNom);
        txtFieldPrenom.setMask(strMaskPrenom);
        txtFieldAdresse.setMask(strMaskAdresse);
        txtFieldCodePostal.setMask(strMaskCodePostal);
        txtFieldVille.setMask(strMaskVille);

        txtFieldCarteAssuranceMaladie.setMask(strMaskCarteAssuranceMaladie);
        // txtFieldCodeVaccin.setMask(strMaskCodeVaccin);

        // init valid date of birth
        datePickerDateNaissance.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean blnVide) {
                super.updateItem(localDate, blnVide);
                
                setDisable(blnVide || localDate.compareTo(LocalDate.now())>=0);
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

        datePickerDateVisite.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean blnVide) {
                super.updateItem(localDate, blnVide);
                
                if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(blnVide || true);
                }
                else 
                    setDisable(blnVide || localDate.compareTo(LocalDate.now())>=0);
                // else {
                //     int intLocalHour = LocalTime.now().getHour();
                //     setDisable(blnVide || (intLocalHour>Config.intFormatLengthHourMax ? localDate.compareTo(LocalDate.now())<=0  : localDate.compareTo(LocalDate.now())<0));
                // }
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
        // currentListViewSelectedFormulaire = null;
        lvAccountInfos.setCellFactory(cell -> new ListCell<Formulaire>() {
            private ImageView imageView;

            @Override
            public void updateItem(Formulaire formulaire, boolean blnVide) {
                super.updateItem(formulaire, blnVide);

                if (blnVide || formulaire == null || formulaire.getListViewAccountInfos(true) == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent");
                    setGraphic(null);
                }
                else {
                    imageView = new ImageView(new Image(getClass().getResourceAsStream(Config.strImageFileAccountInfos)));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);

                    setText(formulaire.getListViewAccountInfos(true));
                    setStyle("");
                }
            }
        });
        lvAccountInfos.getSelectionModel().selectedItemProperty().addListener(changeListener -> {
            try {
                if (!blnListViewSelectedTwice && blnTextFieldsDiscardChanges()) {
                    currentListViewSelectedFormulaire = lvAccountInfos.getSelectionModel().getSelectedItem();
                    intListViewSelectedIndex = lvAccountInfos.getSelectionModel().getSelectedIndex();

                    if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire) {
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
                        resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), false);

                        // if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire) {
                        //     logger.log(Level.INFO, "ListView Hard Reset\n");
                        //     resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                        // }
                        // else {
                        //     logger.log(Level.INFO, "ListView Soft Reset\n");
                        //     resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), false);
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
        datePickerDateVisite.getEditor().setDisable(true);
        datePickerDateVisite.getEditor().setOpacity(1);
        resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);

        if (blnDisableAllButtons) {
            btnMenuAjouterFormulaire.setDisable(true);
            btnMenuModifierFormulaire.setDisable(true);
            btnMenuSupprimerFormulaire.setDisable(true);
            btnRestoreAllFields.setDisable(true);
            btnClearAllFields.setDisable(true);
            btnClearDatePicker.setDisable(true);
            btnClearDatePickerVisite.setDisable(true);
        }
    }

    private void resetListView(ArrayList<Formulaire> arrFormulaires, boolean blnResetHard, boolean ...blnArrOptional) {
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // reset hard by also clearing textfields & reseting currentListViewSelectedFormulaire
        if (blnResetHard) {
            // System.out.println("\n--->Textfields cleared\n");
            clearAllFields();

            intListViewSelectedIndex = -1;
            blnListViewSelectedTwice = false;

            // System.out.println("\n--->New Formulaire\n");
            currentListViewSelectedFormulaire = new Formulaire();
        }

        // System.out.println("\nInside method resetListView\n");
        lvAccountInfos.getItems().clear();
        // System.out.println("\nCleared ListView items\n");

        // lvAccountInfos.getSelectionModel().clearSelection();
        // ArrayList<Formulaire> arrFormulaires = Formulaire.crudDataListFormulaires(logger, crudREAD, Config.strNewDataFileFormulaires, null).getArrFormulaires();

        // System.out.println("\n\nArrFormulaires Size: " + arrFormulaires.size() + "\n\n");

        if (arrFormulaires != null && arrFormulaires.size() > 0) {
            if (!blnDisableAllButtons) {
                btnMenuModifierFormulaire.setDisable(false);
                btnMenuSupprimerFormulaire.setDisable(false);
            }
            else {
                btnMenuAjouterFormulaire.setDisable(true);
                btnMenuModifierFormulaire.setDisable(true);
                btnMenuSupprimerFormulaire.setDisable(true);
                btnRestoreAllFields.setDisable(true);
                btnClearAllFields.setDisable(true);
                btnClearDatePicker.setDisable(true);
                btnClearDatePickerVisite.setDisable(true);
            }

            lblListViewInfos.setText("Liste des Formulaires");
            // for (int i=0; i<15;i++)
            lvAccountInfos.getItems().addAll(arrFormulaires);
        }
        else {
            if (!blnDisableAllButtons) {
                btnMenuModifierFormulaire.setDisable(true);
                btnMenuSupprimerFormulaire.setDisable(true);
            }
            else {
                btnMenuAjouterFormulaire.setDisable(true);
                btnMenuModifierFormulaire.setDisable(true);
                btnMenuSupprimerFormulaire.setDisable(true);
                btnRestoreAllFields.setDisable(true);
                btnClearAllFields.setDisable(true);
                btnClearDatePicker.setDisable(true);
                btnClearDatePickerVisite.setDisable(true);
            }

            lblListViewInfos.setText("Liste des Formulaires VIDES!");

            //  ou se déconnecter pour que l'application créé un formulaire automatiquement
            logger.log(Level.SEVERE, "Aucun formulaire VALIDE présent dans le fichier Formulaires.csv!\nVeuillez ajouter un nouveau formulaire!\n");

            // ou se déconnecter pour que l'application créé un formulaire automatiquement
            Alert alertError = new Alert(AlertType.ERROR, "Aucun formulaire VALIDE présent dans le fichier Formulaires.csv\n\nVeuillez ajouter un nouveau formulaire", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    private void clearAllFields(boolean ...blnArrOptional) {
        // disable textfield No Compte if it is current selected formulaire
        boolean blnClearListBeforetextFields = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnClearListBeforetextFields = blnArrOptional[0];

        // System.out.println("\n\nblnClearListBeforetextFields: " + blnClearListBeforetextFields + "\n\n");

        if (blnClearListBeforetextFields) {
            currentListViewSelectedFormulaire = null;
            // lvAccountInfos.getItems().clear(); // Seriously WTF, fuck ListViews & fuck Java for calling 'selectedItemProperty()' when deleting list view

            System.out.println("\n\nCleared List View Before TextFields!\n\n");
        }

        // if an account from list view is selected, disable Textfield No Compte
        // if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire && currentListViewSelectedFormulaire.getLngNoCompte() == currentUserLngNoCompte && !(lvAccountInfos.getSelectionModel().isEmpty()))
        // /*&& lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Formulaire*/
        //     // if current user formulaire is selected in listview, disable Textfield No Compte
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
        txtFieldNom.clear();
        txtFieldPrenom.clear();
        txtFieldAdresse.clear();
        txtFieldCodePostal.clear();
        txtFieldVille.clear();
        txtFieldCodeVaccin.clear();

        txtFieldCarteAssuranceMaladie.clear();

        // set Textfields text to empty
        txtFieldNoTel.setText("");
        txtFieldCourriel.setText("");
        txtFieldNom.setText("");
        txtFieldPrenom.setText("");
        txtFieldAdresse.setText("");
        txtFieldCodePostal.setText("");
        txtFieldVille.setText("");
        txtFieldCodeVaccin.setText("");

        txtFieldCarteAssuranceMaladie.setText("");

        // set Textfields plaintext to empty
        txtFieldNoTel.setPlainText("");
        txtFieldCourriel.setPlainText("");
        txtFieldNom.setPlainText("");
        txtFieldPrenom.setPlainText("");
        txtFieldAdresse.setPlainText("");
        txtFieldCodePostal.setPlainText("");
        txtFieldVille.setPlainText("");
        
        txtFieldCarteAssuranceMaladie.setPlainText("");

        datePickerDateNaissance.getEditor().clear();
        datePickerDateNaissance.setValue(null);

        datePickerDateVisite.getEditor().clear();
        datePickerDateVisite.setValue(null);

        cbFirstDose.getItems().clear();
        Arrays.asList(EnumTypeBoolean.values()).forEach(cbFirstDose.getItems()::add);

        cbHadCovid.getItems().clear();
        Arrays.asList(EnumTypeBoolean.values()).forEach(cbHadCovid.getItems()::add);

        cbHasAllergies.getItems().clear();
        Arrays.asList(EnumTypeBoolean.values()).forEach(cbHasAllergies.getItems()::add);

        cbHasCovidSymptoms.getItems().clear();
        Arrays.asList(EnumTypeBoolean.values()).forEach(cbHasCovidSymptoms.getItems()::add);

        cbGotVaccinated.getItems().clear();
        Arrays.asList(EnumTypeBoolean.values()).forEach(cbGotVaccinated.getItems()::add);

        cbTypeVaccin.getItems().clear();
        Arrays.asList(EnumTypeVaccin.values()).forEach(cbTypeVaccin.getItems()::add);

        cbTypeDose.getItems().clear();
        IntStream.rangeClosed(1, Config.intFormatVaccinNbDoseMax).boxed().forEach(cbTypeDose.getItems()::add);
        

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

        lblInvalidCarteAssuranceMaladie.setText("Carte d'Assurance Maladie");
        lblInvalidDateVisite.setText("");
        lblInvalidHadCovid.setText("A Déjà Eu Covid ?");
        lblInvalidHasAllergies.setText("A Des Réactions Allergique ?");
        lblInvalidTypeVaccin.setText("Choisir Nom du Vaccin");
        lblInvalidFirstDose.setText("Première Dose ?");
        lblInvalidHasCovidSymptoms.setText("A Des Symptômes du Covid ?");
        lblInvalidGotVaccinated.setText("A Déjà Été Vacciné ?");
        lblInvalidTypeDose.setText("Choisir Type de Dose");

        // clear Textfields Style
        txtFieldNoCompte.setStyle(Config.strStyleSuccessBorder);
        txtFieldNoTel.setStyle(Config.strStyleSuccessBorder);
        txtFieldCourriel.setStyle(Config.strStyleSuccessBorder);
        txtFieldNom.setStyle(Config.strStyleSuccessBorder);
        txtFieldPrenom.setStyle(Config.strStyleSuccessBorder);
        txtFieldAdresse.setStyle(Config.strStyleSuccessBorder);
        txtFieldCodePostal.setStyle(Config.strStyleSuccessBorder);
        txtFieldVille.setStyle(Config.strStyleSuccessBorder);

        txtFieldCarteAssuranceMaladie.setStyle(Config.strStyleSuccessBorder);

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

        lblInvalidCarteAssuranceMaladie.setStyle(Config.strStyleSuccessMessage);
        lblInvalidDateVisite.setStyle(Config.strStyleSuccessMessage);
        lblInvalidHadCovid.setStyle(Config.strStyleSuccessMessage);
        lblInvalidHasAllergies.setStyle(Config.strStyleSuccessMessage);
        lblInvalidTypeVaccin.setStyle(Config.strStyleSuccessMessage);
        lblInvalidFirstDose.setStyle(Config.strStyleSuccessMessage);
        lblInvalidHasCovidSymptoms.setStyle(Config.strStyleSuccessMessage);
        lblInvalidGotVaccinated.setStyle(Config.strStyleSuccessMessage);
        lblInvalidTypeDose.setStyle(Config.strStyleSuccessMessage);
    }
    private void restoreAllFields(boolean ...blnArrOptional) {
        clearAllFields();

        System.out.println("\n\ncurrentListViewSelectedFormulaire: " + currentListViewSelectedFormulaire + "\n\n");

        // init Textfields with data from selected Formulaire
        txtFieldNoCompte.setPlainText(Long.toString(currentListViewSelectedFormulaire.getLngNoCompte()));
        txtFieldNoTel.setPlainText(Long.toString(currentListViewSelectedFormulaire.getLngNoTel()));
        txtFieldCourriel.setPlainText(currentListViewSelectedFormulaire.getStrCourriel());
        txtFieldNom.setPlainText(currentListViewSelectedFormulaire.getStrNom());
        txtFieldPrenom.setPlainText(currentListViewSelectedFormulaire.getStrPrenom());
        txtFieldAdresse.setPlainText(currentListViewSelectedFormulaire.getStrAdresse());
        txtFieldCodePostal.setPlainText(currentListViewSelectedFormulaire.getStrCodePostal());
        txtFieldVille.setPlainText(currentListViewSelectedFormulaire.getStrVille());
        txtFieldCodeVaccin.setText(currentListViewSelectedFormulaire.getStrCodeVaccin());

        txtFieldCarteAssuranceMaladie.setPlainText(currentListViewSelectedFormulaire.getStrCarteAssuranceMaladie());

        try {
            LocalDate localDate = LocalDate.parse(currentListViewSelectedFormulaire.getStrDateNaissance(), Config.DATE_TIME_FORMATTER);
            // System.out.println("\n\n--->Date Naissance Formulaire: '" + localDate.toString() + "'\n\n");
            datePickerDateNaissance.setValue(localDate);
        }
        catch (DateTimeParseException dtpe) {
            System.out.println();
            logger.log(Level.SEVERE, "Impossible d'analyser et de PARSE la date de naissance du formulaire [No Compte: '" + currentListViewSelectedFormulaire.getLngNoCompte() + "']\nDate de Naissance: '" + currentListViewSelectedFormulaire.getStrDateNaissance() + "'!\n");
            System.out.println("");

            // Alert alertError = new Alert(AlertType.ERROR, "Impossible d'analyser la date de naissance du formulaire [No Compte: '" + currentListViewSelectedFormulaire.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedFormulaire.getIntCodeIdentification() + "']\nDate de Naissance: '" + currentListViewSelectedFormulaire.getStrDateNaissance() + "'\n", ButtonType.OK);
            // alertError.showAndWait();

            datePickerDateNaissance.getEditor().clear();
            datePickerDateNaissance.setValue(null);
        }

        try {
            LocalDate localDate = LocalDate.parse(currentListViewSelectedFormulaire.getStrDateVisite(), Config.DATE_TIME_FORMATTER);
            // System.out.println("\n\n--->Date Naissance Visite: '" + localDate.toString() + "'\n\n");
            datePickerDateVisite.setValue(localDate);
        }
        catch (DateTimeParseException dtpe) {
            System.out.println();
            logger.log(Level.SEVERE, "Impossible d'analyser et de PARSE la date de visite du formulaire [No Compte: '" + Long.toString(currentListViewSelectedFormulaire.getLngNoCompte()) + "']\nDate de Visite: '" + currentListViewSelectedFormulaire.getStrDateVisite() + "'!\n");
            System.out.println("");

            // Alert alertError = new Alert(AlertType.ERROR, "Impossible d'analyser la date de naissance du compte visite [No Compte: '" + currentListViewSelectedVisite.getLngNoCompte() + "', Code Identification: '" + currentListViewSelectedVisite.getIntCodeIdentification() + "']\nDate de Naissance: '" + currentListViewSelectedVisite.getStrDateNaissance() + "'\n", ButtonType.OK);
            // alertError.showAndWait();

            datePickerDateVisite.getEditor().clear();
            datePickerDateVisite.setValue(null);
        }

        cbFirstDose.setValue(currentListViewSelectedFormulaire.getEtbFirstDose());
        cbHadCovid.setValue(currentListViewSelectedFormulaire.getEtbHadCovid());
        cbHasCovidSymptoms.setValue(currentListViewSelectedFormulaire.getEtbHasCovidSymptoms());
        cbHasAllergies.setValue(currentListViewSelectedFormulaire.getEtbHasAllergies());
        cbGotVaccinated.setValue(currentListViewSelectedFormulaire.getEtbGotVaccinated());
        cbTypeVaccin.setValue(currentListViewSelectedFormulaire.getEnumTypeVaccin());
        cbTypeDose.setValue(currentListViewSelectedFormulaire.getIntTypeDose());
    }

    @FXML
    private void btnDisconnectClick() {
        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos(), true);
    }

    @FXML
    private void btnReturnClick() {
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.RETURN_BACK, new MenuInfos(Config.strInterfaceViewMenuFormulaire, currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification, "Menu Employé"));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), true);
        ControllerMenusNaviguation.gotoMenu(stage, logger, enumTypeMenuInterfaceReturnBack, null, new MenuInfos(currentUserStrFullName, currentUserLngNoCompte, currentUserIntCodeIdentification), (enumTypeMenuInterfaceReturnBack==EnumTypeMenuInterface.EMPLOYE ? true : false));
    }

    @FXML
    private void btnMenuAjouterFormulaireClick() {
        crudAccountInfos(EnumTypeMenuInterface.ADD);
    }
    @FXML
    private void btnMenuModifierFormulaireClick() {
        crudAccountInfos(EnumTypeMenuInterface.MODIFY);
    }
    @FXML
    private void btnMenuSupprimerFormulaireClick() {
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
        if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Formulaire && !(lvAccountInfos.getSelectionModel().isEmpty())) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment RESTAURER tous les champs de texte ?\n\n", ButtonType.YES, ButtonType.CANCEL);
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Restaurer!");
            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
            confirm.showAndWait();
    
            if (confirm.getResult() == ButtonType.YES)
                restoreAllFields();
        
        }
        else {
            logger.log(Level.INFO, "Veuillez sélectioner un formulaire parmi la liste des formulaires!\n");

            Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un formulaire parmi la liste des formulaires!", ButtonType.OK);
            alertError.showAndWait();
        }
    }
    @FXML
    private void btnClearDatePickerClick() {
        datePickerDateNaissance.getEditor().clear();
        datePickerDateNaissance.setValue(null);
    }
    @FXML
    private void btnClearDatePickerVisiteClick() {
        datePickerDateVisite.getEditor().clear();
        datePickerDateVisite.setValue(null);
    }

    private class ReturnGetFormulaireFromTextFields{
        // #! lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance, strCarteAssuranceMaladie, strDateVisite, etbFirstDose, etbHadCovid, etbHasCovidSymptoms, etbHasAllergies, etbGotVaccinated, enumTypeVaccin, intTypeDose, strCodeVaccin

        private Formulaire formulaire;
        private boolean blnErrorNoCompte, blnErrorNoTel, blnErrorCourriel, blnErrorDateNaissance, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille,
                        blnErrorCarteAssuranceMaladie, blnErrorDateVisite, blnErrorFirstDose, blnErrorHadCovid, blnErrorHasCovidSymptoms, blnErrorHasAllergies, blnErrorGotVaccinated, blnErrorTypeVaccin, blnErrorTypeDose;

        public ReturnGetFormulaireFromTextFields() {
            this.formulaire = new Formulaire();

            this.blnErrorNoCompte = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorDateNaissance = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;

            this.blnErrorCarteAssuranceMaladie = false;
            this.blnErrorDateVisite = false;
            this.blnErrorFirstDose = false;
            this.blnErrorHadCovid = false;
            this.blnErrorHasCovidSymptoms = false;
            this.blnErrorHasAllergies = false;
            this.blnErrorGotVaccinated = false;
            this.blnErrorTypeVaccin = false;
            this.blnErrorTypeDose = false;
        }
        public ReturnGetFormulaireFromTextFields(Formulaire formulaire) {
            this.formulaire = formulaire;

            this.blnErrorNoCompte = false;
            this.blnErrorNoTel = false;
            this.blnErrorCourriel = false;
            this.blnErrorDateNaissance = false;
            this.blnErrorNom = false;
            this.blnErrorPrenom = false;
            this.blnErrorAdresse = false;
            this.blnErrorCodePostal = false;
            this.blnErrorVille = false;

            this.blnErrorCarteAssuranceMaladie = false;
            this.blnErrorDateVisite = false;
            this.blnErrorFirstDose = false;
            this.blnErrorHadCovid = false;
            this.blnErrorHasCovidSymptoms = false;
            this.blnErrorHasAllergies = false;
            this.blnErrorGotVaccinated = false;
            this.blnErrorTypeVaccin = false;
            this.blnErrorTypeDose = false;
        }
        public ReturnGetFormulaireFromTextFields(boolean blnErrorNoCompte, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorDateNaissance, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille, boolean blnErrorCarteAssuranceMaladie, boolean blnErrorDateVisite, boolean blnErrorFirstDose, boolean blnErrorHadCovid, boolean blnErrorHasCovidSymptoms, boolean blnErrorHasAllergies, boolean blnErrorGotVaccinated, boolean blnErrorTypeVaccin, boolean blnErrorTypeDose) {
            this.formulaire = new Formulaire();

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorDateNaissance = blnErrorDateNaissance;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;

            this.blnErrorCarteAssuranceMaladie = blnErrorCarteAssuranceMaladie;
            this.blnErrorDateVisite = blnErrorDateVisite;
            this.blnErrorFirstDose = blnErrorFirstDose;
            this.blnErrorHadCovid = blnErrorHadCovid;
            this.blnErrorHasCovidSymptoms = blnErrorHasCovidSymptoms;
            this.blnErrorHasAllergies = blnErrorHasAllergies;
            this.blnErrorGotVaccinated = blnErrorGotVaccinated;
            this.blnErrorTypeVaccin = blnErrorTypeVaccin;
            this.blnErrorTypeDose = blnErrorTypeDose;
        }
        public ReturnGetFormulaireFromTextFields(Formulaire formulaire, boolean blnErrorNoCompte, boolean blnErrorNoTel, boolean blnErrorCourriel, boolean blnErrorDateNaissance, boolean blnErrorNom, boolean blnErrorPrenom, boolean blnErrorAdresse, boolean blnErrorCodePostal, boolean blnErrorVille, boolean blnErrorCarteAssuranceMaladie, boolean blnErrorDateVisite, boolean blnErrorFirstDose, boolean blnErrorHadCovid, boolean blnErrorHasCovidSymptoms, boolean blnErrorHasAllergies, boolean blnErrorGotVaccinated, boolean blnErrorTypeVaccin, boolean blnErrorTypeDose) {
            this.formulaire = formulaire;

            this.blnErrorNoCompte = blnErrorNoCompte;
            this.blnErrorNoTel = blnErrorNoTel;
            this.blnErrorCourriel = blnErrorCourriel;
            this.blnErrorDateNaissance = blnErrorDateNaissance;
            this.blnErrorNom = blnErrorNom;
            this.blnErrorPrenom = blnErrorPrenom;
            this.blnErrorAdresse = blnErrorAdresse;
            this.blnErrorCodePostal = blnErrorCodePostal;
            this.blnErrorVille = blnErrorVille;

            this.blnErrorCarteAssuranceMaladie = blnErrorCarteAssuranceMaladie;
            this.blnErrorDateVisite = blnErrorDateVisite;
            this.blnErrorFirstDose = blnErrorFirstDose;
            this.blnErrorHadCovid = blnErrorHadCovid;
            this.blnErrorHasCovidSymptoms = blnErrorHasCovidSymptoms;
            this.blnErrorHasAllergies = blnErrorHasAllergies;
            this.blnErrorGotVaccinated = blnErrorGotVaccinated;
            this.blnErrorTypeVaccin = blnErrorTypeVaccin;
            this.blnErrorTypeDose = blnErrorTypeDose;
        }

        public Formulaire getFormulaire() {
            return this.formulaire;
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

        public boolean getBlnErrorCarteAssuranceMaladie() {
            return this.blnErrorCarteAssuranceMaladie;
        }
        public boolean getBlnErrorDateVisite() {
            return this.blnErrorDateVisite;
        }
        public boolean getBlnErrorFirstDose() {
            return this.blnErrorFirstDose;
        }
        public boolean getBlnErrorHadCovid() {
            return this.blnErrorHadCovid;
        }
        public boolean getBlnErrorHasCovidSymptoms() {
            return this.blnErrorHasCovidSymptoms;
        }
        public boolean getBlnErrorHasAllergies() {
            return this.blnErrorHasAllergies;
        }
        public boolean getBlnErrorGotVaccinated() {
            return this.blnErrorGotVaccinated;
        }
        public boolean getBlnErrorTypeVaccin() {
            return this.blnErrorTypeVaccin;
        }
        public boolean getBlnErrorTypeDose() {
            return this.blnErrorTypeDose;
        }

        // public void setFormulaire(Formulaire formulaire) {
        //     this.formulaire = formulaire;
        // }
        // public void setBlnErrorNoCompte(boolean blnErrorNoCompte) {
        //     this.blnErrorNoCompte = blnErrorNoCompte;
        // }
        // public void setBlnErrorNoTel(boolean blnErrorNoTel) {
        //     this.blnErrorNoTel = blnErrorNoTel;
        // }
        // public void setBlnErrorCourriel(boolean blnErrorCourriel) {
        //     this.blnErrorCourriel = blnErrorCourriel;
        // }
        // public void setBlnErrorDateNaissance(boolean blnErrorDateNaissance) {
        //     this.blnErrorDateNaissance = blnErrorDateNaissance;
        // }
        // public void setBlnErrorNom(boolean blnErrorNom) {
        //     this.blnErrorNom = blnErrorNom;
        // }
        // public void setBlnErrorPrenom(boolean blnErrorPrenom) {
        //     this.blnErrorPrenom = blnErrorPrenom;
        // }
        // public void setBlnErrorAdresse(boolean blnErrorAdresse) {
        //     this.blnErrorAdresse = blnErrorAdresse;
        // }
        // public void setBlnErrorCodePostal(boolean blnErrorCodePostal) {
        //     this.blnErrorCodePostal = blnErrorCodePostal;
        // }
        // public void setBlnErrorVille(boolean blnErrorVille) {
        //     this.blnErrorVille = blnErrorVille;
        // }
    }
    private ReturnGetFormulaireFromTextFields getFormulaireFromTextFields() {
        // Set All TextFields to error style, and only change when input is valid
        txtFieldNoCompte.setStyle(Config.strStyleErrorBorder);
        txtFieldNoTel.setStyle(Config.strStyleErrorBorder);
        txtFieldCourriel.setStyle(Config.strStyleErrorBorder);
        txtFieldNom.setStyle(Config.strStyleErrorBorder);
        txtFieldPrenom.setStyle(Config.strStyleErrorBorder);
        txtFieldAdresse.setStyle(Config.strStyleErrorBorder);
        txtFieldCodePostal.setStyle(Config.strStyleErrorBorder);
        txtFieldVille.setStyle(Config.strStyleErrorBorder);

        txtFieldCarteAssuranceMaladie.setStyle(Config.strStyleErrorBorder);

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

        lblInvalidCarteAssuranceMaladie.setStyle(Config.strStyleErrorMessage);
        lblInvalidDateVisite.setStyle(Config.strStyleErrorMessage);
        lblInvalidHadCovid.setStyle(Config.strStyleErrorMessage);
        lblInvalidHasAllergies.setStyle(Config.strStyleErrorMessage);
        lblInvalidTypeVaccin.setStyle(Config.strStyleErrorMessage);
        lblInvalidFirstDose.setStyle(Config.strStyleErrorMessage);
        lblInvalidHasCovidSymptoms.setStyle(Config.strStyleErrorMessage);
        lblInvalidGotVaccinated.setStyle(Config.strStyleErrorMessage);
        lblInvalidTypeDose.setStyle(Config.strStyleErrorMessage);

        // init all variables that will be returned
        Formulaire formulaire = new Formulaire();

        boolean blnErrorNoCompte = false, blnErrorNoTel = false, blnErrorCourriel = false, blnErrorDateNaissance = false, blnErrorNom = false, blnErrorPrenom = false, blnErrorAdresse = false, blnErrorCodePostal = false, blnErrorVille = false;
        boolean blnErrorCarteAssuranceMaladie = false, blnErrorDateVisite = false, blnErrorFirstDose = false, blnErrorHadCovid = false, blnErrorHasCovidSymptoms = false, blnErrorHasAllergies = false, blnErrorGotVaccinated = false, blnErrorTypeVaccin = false, blnErrorTypeDose = false;

        // init all variables that will be used inside this function
        long lngNoCompte = Long.parseLong("0"), lngNoTel = Long.parseLong("0");

        String strNoCompte = "", strNoTel = "", strDateNaissance = "";
        String strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        int intTypeDose = 0;
        String strCarteAssuranceMaladie = "", strDateVisite = "", strTypeDose = "";
        // EnumTypeBoolean etbFirstDose = EnumTypeBoolean.NON, etbHadCovid = EnumTypeBoolean.NON, etbHasCovidSymptoms = EnumTypeBoolean.NON, etbHasAllergies = EnumTypeBoolean.NON, etbGotVaccinated = EnumTypeBoolean.NON;
        EnumTypeBoolean etbFirstDose = null, etbHadCovid = null, etbHasCovidSymptoms = null, etbHasAllergies = null, etbGotVaccinated = null;
        EnumTypeVaccin enumTypeVaccin = null;
        
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

        strCarteAssuranceMaladie = txtFieldCarteAssuranceMaladie.getPlainText();
        if (!strCarteAssuranceMaladie.trim().isEmpty() && strCarteAssuranceMaladie.length() == Config.intFormatLengthCarteAssuranceMaladie) {
            txtFieldCarteAssuranceMaladie.setStyle(Config.strStyleSuccessBorder);
            lblInvalidCarteAssuranceMaladie.setText("Carte d'Assurance Maladie Valide");
            lblInvalidCarteAssuranceMaladie.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strCarteAssuranceMaladie.trim().isEmpty()) {
            blnErrorCarteAssuranceMaladie = true;
            lblInvalidCarteAssuranceMaladie.setText("Carte d'Assurance Maladie VIDE!");
        }
        else if (strCarteAssuranceMaladie.length() != Config.intFormatLengthCarteAssuranceMaladie) {
            blnErrorCarteAssuranceMaladie = true;
            lblInvalidCarteAssuranceMaladie.setText("Longueur exacte à " + Config.intFormatLengthCarteAssuranceMaladie + " charactères!");
        }

        System.out.println("\n\nCarte Assurance Maladie: '" + strCarteAssuranceMaladie + "'\n\n");

        LocalDate localDateVisite = datePickerDateVisite.getValue();
        if (localDateVisite == null)
            strDateVisite = "";
        else
            strDateVisite = localDateVisite.toString();
        if (!(strDateVisite.trim().isEmpty()) && strDateVisite.length() == Config.intFormatLengthDate) {
            lblInvalidDateVisite.setText("Date de Visite Valide");
            lblInvalidDateVisite.setStyle(Config.strStyleSuccessMessage);
        }
        else if (strDateVisite.trim().isEmpty()) {
            lblInvalidDateVisite.setText("");
            lblInvalidDateVisite.setStyle(Config.strStyleSuccessMessage);
        }
        else if (!(strDateVisite.trim().isEmpty()) && strDateVisite.length() != Config.intFormatLengthDate) {
            blnErrorDateVisite = true;
            lblInvalidDateVisite.setText("Format Date: yyyy-mm-dd");
            lblInvalidDateVisite.setStyle(Config.strStyleErrorMessage);
        }

        if (cbFirstDose.getValue() == null)
            etbFirstDose = null;
        else
            etbFirstDose = cbFirstDose.getValue();
        if (etbFirstDose != null) {
            lblInvalidFirstDose.setText("Choix Première Dose Valide");
            lblInvalidFirstDose.setStyle(Config.strStyleSuccessMessage);
        }
        else {
            blnErrorFirstDose = true;
            lblInvalidFirstDose.setText("Choix Première Dose ERRONÉ!");
        }

        if (cbHadCovid.getValue() == null)
            etbHadCovid = null;
        else
            etbHadCovid = cbHadCovid.getValue();
        if (etbHadCovid != null) {
            lblInvalidHadCovid.setText("Choix A Eu Covid Valide");
            lblInvalidHadCovid.setStyle(Config.strStyleSuccessMessage);
        }
        else {
            blnErrorHadCovid = true;
            lblInvalidHadCovid.setText("Choix A Eu Covid ERRONÉ!");
        }

        if (cbHasCovidSymptoms.getValue() == null)
            etbHasCovidSymptoms = null;
        else
            etbHasCovidSymptoms = cbHasCovidSymptoms.getValue();
        if (etbHasCovidSymptoms != null) {
            lblInvalidHasCovidSymptoms.setText("Choix A Symptômes Covid Valide");
            lblInvalidHasCovidSymptoms.setStyle(Config.strStyleSuccessMessage);
        }
        else {
            blnErrorHasCovidSymptoms = true;
            lblInvalidHasCovidSymptoms.setText("Choix A Symptômes Covid ERRONÉ!");
        }

        if (cbHasAllergies.getValue() == null)
            etbHasAllergies = null;
        else
            etbHasAllergies = cbHasAllergies.getValue();
        if (etbHasAllergies != null) {
            lblInvalidHasAllergies.setText("Choix A Réactions Allergiques Valide");
            lblInvalidHasAllergies.setStyle(Config.strStyleSuccessMessage);
        }
        else {
            blnErrorHasAllergies = true;
            lblInvalidHasAllergies.setText("Choix A Réactions Allergiques ERRONÉ!");
        }

        if (cbGotVaccinated.getValue() == null)
            etbGotVaccinated = null;
        else
            etbGotVaccinated = cbGotVaccinated.getValue();
        if (etbGotVaccinated != null) {
            lblInvalidGotVaccinated.setText("Choix A Eu Vaccin Valide");
            lblInvalidGotVaccinated.setStyle(Config.strStyleSuccessMessage);
        }
        else {
            blnErrorGotVaccinated = true;
            lblInvalidGotVaccinated.setText("Choix A Eu Vaccin ERRONÉ!");
        }

        if (cbTypeVaccin.getValue() == null)
            enumTypeVaccin = null;
        else
            enumTypeVaccin = cbTypeVaccin.getValue();
        if (enumTypeVaccin != null) {
            lblInvalidTypeVaccin.setText("Choix Nom Vaccin Valide");
            lblInvalidTypeVaccin.setStyle(Config.strStyleSuccessMessage);
        }
        else {
            blnErrorTypeVaccin = true;
            lblInvalidTypeVaccin.setText("Choix Nom Vaccin ERRONÉ!");
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
        
        // #! lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance
        formulaire.setLngNoCompte(lngNoCompte);
        formulaire.setLngNoTel(lngNoTel);
        formulaire.setStrCourriel(txtFieldCourriel.getPlainText());
        formulaire.setStrDateNaissance(strDateNaissance); //datePickerDateNaissance.getValue().format(Config.DATE_TIME_FORMATTER).toString());
        formulaire.setStrNom(txtFieldNom.getPlainText());
        formulaire.setStrPrenom(txtFieldPrenom.getPlainText());
        formulaire.setStrAdresse(txtFieldAdresse.getPlainText());
        formulaire.setStrCodePostal(txtFieldCodePostal.getPlainText());
        formulaire.setStrVille(txtFieldVille.getPlainText());

        // #! strCarteAssuranceMaladie, strDateVisite, etbFirstDose, etbHadCovid, etbHasCovidSymptoms, etbHasAllergies, etbGotVaccinated, enumTypeVaccin, intTypeDose, strCodeVaccin
        formulaire.setStrCarteAssuranceMaladie(txtFieldCarteAssuranceMaladie.getPlainText()); //strCarteAssuranceMaladie);
        formulaire.setStrDateVisite(strDateVisite);
        formulaire.setEtbFirstDose(etbFirstDose);
        formulaire.setEtbHadCovid(etbHadCovid);
        formulaire.setEtbHasCovidSymptoms(etbHasCovidSymptoms);
        formulaire.setEtbHasAllergies(etbHasAllergies);
        formulaire.setEtbGotVaccinated(etbGotVaccinated);
        formulaire.setEnumTypeVaccin(enumTypeVaccin);
        formulaire.setIntTypeDose(intTypeDose);

        if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Formulaire && !(lvAccountInfos.getSelectionModel().isEmpty()))
            formulaire.setStrCodeVaccin(currentListViewSelectedFormulaire.getStrCodeVaccin());
        

        ReturnGetFormulaireFromTextFields returnGetFormulaireFromTextFields = new ReturnGetFormulaireFromTextFields(formulaire, blnErrorNoCompte, blnErrorNoTel, blnErrorCourriel, blnErrorDateNaissance, blnErrorNom, blnErrorPrenom, blnErrorAdresse, blnErrorCodePostal, blnErrorVille, blnErrorCarteAssuranceMaladie, blnErrorDateVisite, blnErrorFirstDose, blnErrorHadCovid, blnErrorHasCovidSymptoms, blnErrorHasAllergies, blnErrorGotVaccinated, blnErrorTypeVaccin, blnErrorTypeDose);
        // ReturnGetFormulaireFromTextFields returnGetFormulaireFromTextFields = new ReturnGetFormulaireFromTextFields(formulaire, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        
        return returnGetFormulaireFromTextFields;
    }
    private boolean blnTextFieldsDiscardChanges(boolean ...blnArrOptionalShowAlertConfirm) {
        boolean blnShowAlertConfirm = true;
        if (blnArrOptionalShowAlertConfirm != null && blnArrOptionalShowAlertConfirm.length > 0)
            blnShowAlertConfirm = blnArrOptionalShowAlertConfirm[0];

        Formulaire formulaireFromTextFields = getFormulaireFromTextFields().getFormulaire();
        logger.log(Level.INFO, "currentListViewSelectedFormulaire: " + currentListViewSelectedFormulaire + "\n");
        logger.log(Level.INFO, "formulaireFromTextFields: " + formulaireFromTextFields + "\n");

        if (currentListViewSelectedFormulaire == null || !(currentListViewSelectedFormulaire instanceof Formulaire)) {
            currentListViewSelectedFormulaire = new Formulaire();
        }

        // check if textfields have changed & if user input new text value
        boolean isTextFieldsChanged;
        try {
            isTextFieldsChanged = !(currentListViewSelectedFormulaire.equalsExactly(formulaireFromTextFields)); //getFormulaireFromTextFields());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "currentListViewSelectedFormulaire ne correspond pas exactement à formulaireFromTextFields!\n");
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
                    if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Formulaire && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        if (blnTextFieldsDiscardChanges()) {
                            resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                            clearAllFields();

                            Alert alertInfo = new Alert(AlertType.INFORMATION, "Les champs de texte ont été vidés!\n\nVeuillez remplir les champs de texte et ensuite ajouter le nouveau formulaire\n", ButtonType.OK);
                            alertInfo.showAndWait();
                        }
                    }
                    else {
                        ReturnGetFormulaireFromTextFields returnGetFormulaireFromTextFields = getFormulaireFromTextFields();

                        Formulaire formulaireFromTxtFields = returnGetFormulaireFromTextFields.getFormulaire();
                        boolean blnErrorNoCompte = returnGetFormulaireFromTextFields.getBlnErrorNoCompte(), blnErrorNoTel = returnGetFormulaireFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetFormulaireFromTextFields.getBlnErrorCourriel(), blnErrorDateNaissance = returnGetFormulaireFromTextFields.getBlnErrorDateNaissance(), blnErrorNom = returnGetFormulaireFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetFormulaireFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetFormulaireFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetFormulaireFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetFormulaireFromTextFields.getBlnErrorVille();
                        boolean blnErrorCarteAssuranceMaladie = returnGetFormulaireFromTextFields.getBlnErrorCarteAssuranceMaladie(), blnErrorDateVisite = returnGetFormulaireFromTextFields.getBlnErrorDateVisite(), blnErrorFirstDose = returnGetFormulaireFromTextFields.getBlnErrorFirstDose(), blnErrorHadCovid = returnGetFormulaireFromTextFields.getBlnErrorHadCovid(), blnErrorHasCovidSymptoms = returnGetFormulaireFromTextFields.getBlnErrorHasCovidSymptoms(), blnErrorHasAllergies = returnGetFormulaireFromTextFields.getBlnErrorHasAllergies(), blnErrorGotVaccinated = returnGetFormulaireFromTextFields.getBlnErrorGotVaccinated(), blnErrorTypeVaccin = returnGetFormulaireFromTextFields.getBlnErrorTypeVaccin(), blnErrorTypeDose = returnGetFormulaireFromTextFields.getBlnErrorTypeDose();

                        // if textfields contains error, dont add new formulaire to textfield
                        if (blnErrorNoCompte || blnErrorNoTel || blnErrorCourriel || blnErrorDateNaissance || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille || blnErrorCarteAssuranceMaladie || blnErrorDateVisite || blnErrorFirstDose || blnErrorHadCovid || blnErrorHasCovidSymptoms || blnErrorHasAllergies || blnErrorGotVaccinated || blnErrorTypeVaccin || blnErrorTypeDose) {
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

                            if (blnErrorCarteAssuranceMaladie)
                                strAlertError += "Champs de texte Carte d'Assurance Maladie\n";

                            if (blnErrorDateVisite)
                                strAlertError += "Champs de texte Date de Visite\n";

                            if (blnErrorFirstDose)
                                strAlertError += "Champs de texte Première Dose ?\n";

                            if (blnErrorHadCovid)
                                strAlertError += "Champs de texte A Déjà Eu Covid ?\n";

                            if (blnErrorHasCovidSymptoms)
                                strAlertError += "Champs de texte A Des Symptômes du Covid ?\n";

                            if (blnErrorHasAllergies)
                                strAlertError += "Champs de texte A Des Réactions Allergique ?\n";

                            if (blnErrorGotVaccinated)
                                strAlertError += "Champs de texte A Déjà Été Vacciné ?\n";

                            if (blnErrorTypeVaccin)
                                strAlertError += "Champs de texte Choisir Nom du Vaccin\n";

                            if (blnErrorTypeDose)
                                strAlertError += "Champs de texte Choisir Type de Dose\n";

                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alertError.showAndWait();
                        }
                        // if textfields are valid, add new formulaire
                        else {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment AJOUTER le formulaire ci-dessous qui a comme information ?\n\n" + formulaireFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Ajouter!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Formulaire> arrFormulaires = Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires();

                                if (arrFormulaires != null && arrFormulaires.size() > 0) {
                                    boolean blnFormulaireMatch = false;
                                    loopFormulaireMatch:for (Formulaire b : arrFormulaires)
                                        if (b.equalsNoCompte(formulaireFromTxtFields)) {
                                            blnFormulaireMatch = true;
                                            break loopFormulaireMatch;
                                        }
            
                                    if (blnFormulaireMatch) {
                                        logger.log(Level.WARNING, "Le formulaire EXISTE DÉJÀ dans le fichier Formulaires.csv!\nVeuillez ajouter un formulaire qui a un numéro de compte différents des numéros de comptes qui se trouvent parmi la liste des formulaires!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "Le formulaire EXISTE DÉJÀ dans le fichier Formulaires.csv!\n\nVeuillez ajouter un formulaire qui a un numéro de compte différents des autres formulaires!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrFormulaires, false);
                                    }
                                    else {
                                        String strCourriel = formulaireFromTxtFields.getEmailConfirmation(false);
                                        formulaireFromTxtFields.setStrCodeVaccin("########################");

                                        Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.UPDATE, formulaireFromTxtFields);
                                        resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                                        clearAllFields();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au formulaire ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                }
                                else {
                                    String strCourriel = formulaireFromTxtFields.getEmailConfirmation(false);
                                    formulaireFromTxtFields.setStrCodeVaccin("########################");

                                    // create data file and add new account if file doesnt exist
                                    ReturnCrudDataListFormulaires returnCrudDataListFormulaires = Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                    boolean blnAlertErrorDataFolder = returnCrudDataListFormulaires.getBlnAlertErrorDataFolder(), 
                                            blnAlertErrorDataFileFormulaire = returnCrudDataListFormulaires.getBlnAlertErrorDataFileFormulaire();

                                    if (blnAlertErrorDataFolder || blnAlertErrorDataFileFormulaire) {
                                        String strAlertError = "";

                                        if (blnAlertErrorDataFolder)
                                            strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                        else
                                            strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                        if (blnAlertErrorDataFileFormulaire)
                                            strAlertError += "Création d'un nouveau fichier de donnés 'Formulaires.csv' pour les comptes formulaires!\n\n";

                                        Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.UPDATE, formulaireFromTxtFields);
                                        resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                                        clearAllFields();

                                        Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                        alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                        alertError.showAndWait();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au formulaire ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                    else {
                                        resetListView(arrFormulaires, true);
                                        Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Formulaires.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                        alertError.showAndWait();
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case MODIFY: {
                    if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Formulaire && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // only act & modify if changes to textfields have been made
                        if (!blnTextFieldsDiscardChanges(false)) {
                            ReturnGetFormulaireFromTextFields returnGetFormulaireFromTextFields = getFormulaireFromTextFields();

                            Formulaire formulaireFromTxtFields = returnGetFormulaireFromTextFields.getFormulaire();
                            boolean blnErrorNoCompte = returnGetFormulaireFromTextFields.getBlnErrorNoCompte(), blnErrorNoTel = returnGetFormulaireFromTextFields.getBlnErrorNoTel(), blnErrorCourriel = returnGetFormulaireFromTextFields.getBlnErrorCourriel(), blnErrorDateNaissance = returnGetFormulaireFromTextFields.getBlnErrorDateNaissance(), blnErrorNom = returnGetFormulaireFromTextFields.getBlnErrorNom(), blnErrorPrenom = returnGetFormulaireFromTextFields.getBlnErrorPrenom(), blnErrorAdresse = returnGetFormulaireFromTextFields.getBlnErrorAdresse(), blnErrorCodePostal = returnGetFormulaireFromTextFields.getBlnErrorCodePostal(), blnErrorVille = returnGetFormulaireFromTextFields.getBlnErrorVille();
                            boolean blnErrorCarteAssuranceMaladie = returnGetFormulaireFromTextFields.getBlnErrorCarteAssuranceMaladie(), blnErrorDateVisite = returnGetFormulaireFromTextFields.getBlnErrorDateVisite(), blnErrorFirstDose = returnGetFormulaireFromTextFields.getBlnErrorFirstDose(), blnErrorHadCovid = returnGetFormulaireFromTextFields.getBlnErrorHadCovid(), blnErrorHasCovidSymptoms = returnGetFormulaireFromTextFields.getBlnErrorHasCovidSymptoms(), blnErrorHasAllergies = returnGetFormulaireFromTextFields.getBlnErrorHasAllergies(), blnErrorGotVaccinated = returnGetFormulaireFromTextFields.getBlnErrorGotVaccinated(), blnErrorTypeVaccin = returnGetFormulaireFromTextFields.getBlnErrorTypeVaccin(), blnErrorTypeDose = returnGetFormulaireFromTextFields.getBlnErrorTypeDose();

                            // if textfields contains error, dont add new formulaire to textfield
                            if (blnErrorNoCompte || blnErrorNoTel || blnErrorCourriel || blnErrorDateNaissance || blnErrorNom || blnErrorPrenom || blnErrorAdresse || blnErrorCodePostal || blnErrorVille || blnErrorCarteAssuranceMaladie || blnErrorDateVisite || blnErrorFirstDose || blnErrorHadCovid || blnErrorHasCovidSymptoms || blnErrorHasAllergies || blnErrorGotVaccinated || blnErrorTypeVaccin || blnErrorTypeDose) {
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

                                if (blnErrorCarteAssuranceMaladie)
                                    strAlertError += "Champs de texte Carte d'Assurance Maladie\n";

                                if (blnErrorDateVisite)
                                    strAlertError += "Champs de texte Date de Visite\n";

                                if (blnErrorFirstDose)
                                    strAlertError += "Champs de texte Première Dose ?\n";

                                if (blnErrorHadCovid)
                                    strAlertError += "Champs de texte A Déjà Eu Covid ?\n";

                                if (blnErrorHasCovidSymptoms)
                                    strAlertError += "Champs de texte A Des Symptômes du Covid ?\n";

                                if (blnErrorHasAllergies)
                                    strAlertError += "Champs de texte A Des Réactions Allergique ?\n";

                                if (blnErrorGotVaccinated)
                                    strAlertError += "Champs de texte A Déjà Été Vacciné ?\n";

                                if (blnErrorTypeVaccin)
                                    strAlertError += "Champs de texte Choisir Nom du Vaccin\n";

                                if (blnErrorTypeDose)
                                    strAlertError += "Champs de texte Choisir Type de Dose\n";

                                Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alertError.showAndWait();
                            }
                            // if textfields are valid, add new formulaire
                            else {
                                Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment MODIFIER le formulaire ci-dessous qui a comme information ?\n\n" + formulaireFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Modifier!");
                                ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                confirm.showAndWait();

                                if (confirm.getResult() == ButtonType.YES) {
                                    ArrayList<Formulaire> arrFormulaires = Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires();

                                    if (arrFormulaires != null && arrFormulaires.size() > 0) {
                                        boolean blnFormulaireMatch = false;
                                        loopFormulaireMatch:for (Formulaire b : arrFormulaires)
                                            if (b.equalsNoCompte(formulaireFromTxtFields)) {
                                                blnFormulaireMatch = true;
                                                break loopFormulaireMatch;
                                            }
                
                                        if (blnFormulaireMatch) {
                                            String strCourriel = formulaireFromTxtFields.getEmailConfirmation(false);
                                            formulaireFromTxtFields.setStrCodeVaccin("########################");

                                            Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.UPDATE, formulaireFromTxtFields);
                                            resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                                            clearAllFields();

                                            Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au formulaire ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                            confirmEmail.showAndWait();
                                        }
                                        else {
                                            String strCourriel = formulaireFromTxtFields.getEmailConfirmation(false);

                                            logger.log(Level.WARNING, "Le formulaire choisi comme numéro de compte '" + Long.toString(formulaireFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Formulaires.csv!\nVoulez-vous CRÉER ce formulaire ci-dessous qui a comme information ?\n" + formulaireFromTxtFields.getListViewAccountInfos(false) + "\n");

                                            Alert confirmCreateFormulaire = new Alert(AlertType.CONFIRMATION, "Le formulaire choisi comme numéro de compte '" + Long.toString(formulaireFromTxtFields.getLngNoCompte()) + "' ne se trouve pas dans le fichier Formulaires.csv!\nVoulez-vous CRÉER ce formulaire ci-dessous qui a comme information ?\n\n" + formulaireFromTxtFields.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                                            ((Button) confirmCreateFormulaire.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Créer!");
                                            ((Button) confirmCreateFormulaire.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                                            confirmCreateFormulaire.showAndWait();

                                            if (confirmCreateFormulaire.getResult() == ButtonType.YES) {                                                
                                                formulaireFromTxtFields.setStrCodeVaccin("########################");

                                                System.out.println("\n\n" + formulaireFromTxtFields + "\n\n");
                                                Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.UPDATE, formulaireFromTxtFields);
                                                resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                                                clearAllFields();

                                                Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au formulaire ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                                confirmEmail.showAndWait();
                                            }
                                        }
                                    }
                                    else {
                                        String strCourriel = formulaireFromTxtFields.getEmailConfirmation(false);
                                        formulaireFromTxtFields.setStrCodeVaccin("########################");

                                        // create data file and add new account if file doesnt exist
                                        ReturnCrudDataListFormulaires returnCrudDataListFormulaires = Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.CREATE, null, true, false, false);
                                        boolean blnAlertErrorDataFolder = returnCrudDataListFormulaires.getBlnAlertErrorDataFolder(), 
                                                blnAlertErrorDataFileFormulaire = returnCrudDataListFormulaires.getBlnAlertErrorDataFileFormulaire();

                                        if (blnAlertErrorDataFolder || blnAlertErrorDataFileFormulaire) {
                                            String strAlertError = "";

                                            if (blnAlertErrorDataFolder)
                                                strAlertError += "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + Config.strDataFolderPath + "'\n\n";
                                            else
                                                strAlertError += "Emplacement du dossier des données:\n'" + Config.strDataFolderPath + "'\n\n";

                                            if (blnAlertErrorDataFileFormulaire)
                                                strAlertError += "Création d'un nouveau fichier de donnés 'Formulaires.csv' pour les comptes formulaires!\n\n";

                                            Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.UPDATE, formulaireFromTxtFields);
                                            resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                                            clearAllFields();

                                            Alert alertError = new Alert(AlertType.WARNING, strAlertError, ButtonType.OK);
                                            alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                            alertError.showAndWait();

                                            Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au formulaire ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                            confirmEmail.showAndWait();
                                        }
                                        else {
                                            resetListView(arrFormulaires, true);
                                            Alert alertError = new Alert(AlertType.WARNING, "Fichier de données Formulaires.csv VIDE ou INEXISTANT!\nImpossible de créer un nouveau fichier de données\n\n" + Config.strRestartAppInstruction, ButtonType.OK);
                                            alertError.showAndWait();
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            logger.log(Level.INFO, "Aucun changement n'a été effectué sur les champs de texte!\nVeuillez modifier les champs de texte avant de vouloir modifier ce formulaire!\n");

                            Alert alertError = new Alert(AlertType.WARNING, "Aucun changement n'a été effectué sur les champs de texte!\n\nVeuillez modifier les champs de texte avant de vouloir modifier ce formulaire!", ButtonType.OK);
                            alertError.showAndWait();
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un formulaire parmi la liste des formulaires!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un formulaire parmi la liste des formulaires!", ButtonType.OK);
                        alertError.showAndWait();
                    }
                }
                break;

            case DELETE: {
                    if (currentListViewSelectedFormulaire != null && currentListViewSelectedFormulaire instanceof Formulaire && lvAccountInfos.getSelectionModel().getSelectedItem() instanceof Formulaire && !(lvAccountInfos.getSelectionModel().isEmpty())) {
                        // if (currentListViewSelectedFormulaire.getLngNoCompte() == currentUserLngNoCompte) {
                        //     logger.log(Level.WARNING, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\nVeuillez sélectioner un autre formulaire parmi la liste des formulaires ou créer un nouveau formulaire, se connecter à ce compte et ensuite supprimer celui-ci!\n");

                        //     Alert alertError = new Alert(AlertType.ERROR, "Vous ne pouvez pas vous AUTO-SUPPRIMER!\n\nVeuillez sélectioner un autre formulaire parmi la liste des formulaires ou créer un nouveau formulaire, se connecter à ce compte et ensuite supprimer celui-ci!\n", ButtonType.OK);
                        //     alertError.showAndWait();
                        // }
                        // else
                        if (blnTextFieldsDiscardChanges()) {
                            Alert confirm = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment SUPPRIMER le formulaire ci-dessous qui a comme information ?\n\n" + currentListViewSelectedFormulaire.getListViewAccountInfos(false), ButtonType.YES, ButtonType.CANCEL);
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Supprimer!");
                            ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                            confirm.showAndWait();

                            if (confirm.getResult() == ButtonType.YES) {
                                ArrayList<Formulaire> arrFormulaires = Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires();

                                if (arrFormulaires != null && arrFormulaires.size() > 0) {
                                    boolean blnFormulaireMatch = false;
                                    loopFormulaireMatch:for (Formulaire b : arrFormulaires)
                                        if (b.equalsNoCompte(currentListViewSelectedFormulaire)) {
                                            blnFormulaireMatch = true;
                                            break loopFormulaireMatch;
                                        }
            
                                    if (blnFormulaireMatch) {
                                        String strCourriel = currentListViewSelectedFormulaire.getEmailConfirmation(false);

                                        Formulaire.crudDataListFormulaires(logger, EnumTypeCRUD.DELETE, currentListViewSelectedFormulaire);
                                        resetListView(Formulaire.crudDataListFormulaires(logger, crudREAD, (crudREAD==EnumTypeCRUD.READ_SAME ? currentListViewSelectedFormulaire : null)).getArrFormulaires(), true);
                                        clearAllFields();

                                        Alert confirmEmail = new Alert(AlertType.INFORMATION, "Envoi d'un courriel de confirmation au formulaire ci-dessous qui a comme information\n\n" + strCourriel, ButtonType.OK);
                                        confirmEmail.showAndWait();
                                    }
                                    else {
                                        logger.log(Level.WARNING, "Le formulaire choisi ne se trouve plus dans le fichier Formulaires.csv!\nVeuillez sélectionné à nouveau un formulaire parmi la liste des formulaires!\n");
            
                                        Alert alertError = new Alert(AlertType.ERROR, "Le formulaire choisi ne se trouve plus dans le fichier Formulaires.csv!\n\nVeuillez sélectionné à nouveau un formulaire parmi la liste des formulaires!", ButtonType.OK);
                                        alertError.showAndWait();
            
                                        resetListView(arrFormulaires, true);
                                    }
                                }
                                else 
                                    resetListView(arrFormulaires, true);
                            }
                        }
                    }
                    else {
                        logger.log(Level.INFO, "Veuillez sélectioner un formulaire parmi la liste des formulaires!\n");

                        Alert alertError = new Alert(AlertType.INFORMATION, "Veuillez sélectioner un formulaire parmi la liste des formulaires!", ButtonType.OK);
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
