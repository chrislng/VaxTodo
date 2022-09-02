package VaxTodo.Configs;

import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

import javafx.util.StringConverter;

/** Holds main configurations variables that are staticly used throughout the whole project
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
//! Do not change this config file. You have been warned!
public final class Config {
    public final static String strPasswdHash = "582E21D1ACDA2BD8F8890DE922B528115CFDA00A3AAB38AEA81DB553BA99BB5B",
                                strSystemAdministratorEmail = "support@vaxtodo.ca";

    //! Auto-generated, do not edit. All changes will be undone!
    public final static String strFileSeparator = FileSystems.getDefault().getSeparator(),
                                strRestartAppInstruction = "Veuillez REDÉMARRER l'Application ou créer un ticket et contacter votre administrateur système à l'adresse courriel '" + Config.strSystemAdministratorEmail + "'!\n",

                                // ↓↓↓ OLD Command Line Interface ↓↓↓
                                strAppExit = "!",
                                strAppHelp = "?",
                                strAppAccept = "y",
                                strAppRefuse = "n",
                                strAppAcceptAlternative = "Y",
                                strAppRefuseAlternative = "N",
                                strAppDisconnect = "-",
                                strAppMenuBack = "+",
                                strAppHidePassword = "*",
                                strNoCompteMinRnd = "100000000000",
                                strNoCompteMaxRnd = "999999999999",
                                strNoVisiteurMinRnd = "1",
                                strNoVisiteurMaxRnd = "10000",
                                strNoRendezVousMinRnd = "1",
                                strNoRendezVousMaxRnd = "10000",
                                strNoEntrevueMinRnd = "1",
                                strNoEntrevueMaxRnd = "10000",
                                strNoRapportVaccinMinRnd = "1",
                                strNoRapportVaccinMaxRnd = "10000",
                                strNoSuiviVaccinMinRnd = "1",
                                strNoSuiviVaccinMaxRnd = "10000",
                                // ↑↑↑ OLD Command Line Interface ↑↑↑

                                strCSVMainSeparator = ",",
                                strCSVSecondarySeparator = ";",
                                // strCSVTertiarySeparator = "|",

                                // ↓↓↓ OLD File Location ↓↓↓
                                strCSVHoursSeparator = ":",
                                strResourceFolder = "src" + strFileSeparator + "OLD_Data" + strFileSeparator,
                                strDataFileEmployes = strResourceFolder + "Employes.csv",
                                strDataFileBenevoles = strResourceFolder + "Benevoles.csv",
                                strDataFileVisiteurs = strResourceFolder + "Visiteurs.csv",
                                strDataFileCalendriers = strResourceFolder + "Calendriers.csv",
                                strDataFileEntrevues = strResourceFolder + "Entrevues.csv",
                                strDataFileRapportsVaccination = strResourceFolder + "RapportsVaccination.csv",
                                strDataFileRendezVous = strResourceFolder + "RendezVous.csv",
                                strDataFileSuivisVaccination = strResourceFolder + "SuivisVaccination.csv",

                                // ↑↑↑ OLD File Location ↑↑↑ // ↓↓↓ NEW File Location ↓↓↓
                                
                                strImageFilePathRelative =              "/" + "VaxTodo" + "/" + "images" + "/",
                                strImageFileEyeClosed =                 strImageFilePathRelative + "eye-closed-colorized.png",
                                strImageFileEyeOpened =                 strImageFilePathRelative + "eye-opened-colorized.png",
                                strImageFileAccountInfos =              strImageFilePathRelative + "account-infos-colorized.png",

                                // strProjectPath + strFileSeparator + "src" + strFileSeparator +
                                strCssFilePathRelative =                "VaxTodo" + "/" + "Views" + "/" + "CSS" + "/",
                                strCssFileString =                      strCssFilePathRelative + "CssMain.css",

                                strInterfacePathRelative =              "/" + "VaxTodo" + "/" + "Views" + "/" + "Interface" + "/",
                                strInterfaceViewLogin =                 strInterfacePathRelative + "ViewLogin.fxml",
                                strInterfaceViewLoginHtml =             strInterfacePathRelative + "Login.html",
                                strInterfaceViewMenuForgotPasswd =      strInterfacePathRelative + "ViewMenuForgotPasswd.fxml",
                                strInterfaceViewMenuEmploye =           strInterfacePathRelative + "ViewMenuEmploye.fxml",
                                strInterfaceViewMenuBenevole =          strInterfacePathRelative + "ViewMenuBenevole.fxml",
                                strInterfaceViewMenuGestionEmployes =   strInterfacePathRelative + "ViewMenuGestionEmployes.fxml",
                                strInterfaceViewMenuGestionBenevoles =  strInterfacePathRelative + "ViewMenuGestionBenevoles.fxml",
                                strInterfaceViewMenuGestionVisiteurs =  strInterfacePathRelative + "ViewMenuGestionVisiteurs.fxml",
                                strInterfaceViewMenuGestionCalendrier = strInterfacePathRelative + "ViewMenuGestionCalendrier.fxml",
                                strInterfaceViewMenuGestionFormulaires =strInterfacePathRelative + "ViewMenuGestionFormulaires.fxml",

                                // strProjectPathAbsolute = Paths.get(".").toAbsolutePath().normalize().toString(),
                                // strNewResourceFolderPath = Paths.get(strProjectPathAbsolute) /*.getParent().toString()*/ + strFileSeparator + "Data" + strFileSeparator,
                                strNewResourceFolderPathAbsolute =      Paths.get(".").toAbsolutePath().normalize().toString() + strFileSeparator + "VaxTodo_Folder" + strFileSeparator,

                                strLogsFolderPath =                     strNewResourceFolderPathAbsolute + "Logs" + strFileSeparator,
                                strDataFolderPath =                     strNewResourceFolderPathAbsolute + "Data" + strFileSeparator,

                                strNewDataFileEmployes =                strDataFolderPath + "Employes.csv",
                                strNewDataFileBenevoles =               strDataFolderPath + "Benevoles.csv",
                                strNewDataFileVisiteurs =               strDataFolderPath + "Visiteurs.csv",
                                strNewDataFileCalendriers =             strDataFolderPath + "Calendriers.csv",
                                strNewDataFileEntrevues =               strDataFolderPath + "Entrevues.csv",
                                strNewDataFileRapportsVaccination =     strDataFolderPath + "RapportsVaccination.csv",
                                strNewDataFileRendezVous =              strDataFolderPath + "RendezVous.csv",
                                strNewDataFileSuivisVaccination =       strDataFolderPath + "SuivisVaccination.csv",
                                strNewDataFileVisites =                 strDataFolderPath + "Visites.csv",
                                strNewDataFileFormulaires =             strDataFolderPath + "Formulaires.csv";

    // Config Validation Visite
    // Config Validation Formulaire
    // Config Validation Compte Utilisateur
    // Config Validation Benevole
    // Config Validation Visiteur
    public final static int intFormatLengthNoCompte =                   12,
                                intFormatLengthCodeIdentification =     9,
                                intFormatLengthPassword =               8,
                                intFormatLengthTelephone =              10,
                                intFormatLengthEmail =                  100,
                                intFormatLengthDate =                   10,
                                intFormatLengthFirstName =              50,
                                intFormatLengthLastName =               50,
                                intFormatLengthAdresse =                100,
                                intFormatLengthCodePostal =             6,
                                intFormatLengthVille =                  50,
                                intFormatLengthNoReservation =          6,
                                intFormatVaccinNbDoseMax =              2,
                                intFormatLengthCarteAssuranceMaladie =  12,
                                intFormatLengthCodeVaccin =             24,
                                intFormatLengthHourMin =                8,
                                intFormatLengthHourMax =                18;

    public final static String strFormatDateSeparator =                 "-",
                                strFormatDate =                         "yyyy" + strFormatDateSeparator + "MM" + strFormatDateSeparator + "dd",

                                strFormatHourSeparator =                ":",
                                strFormatHour =                         "HH" + strFormatHourSeparator + "mm",
                                strFormatMotDePasse =                   "Composé d'au moins 8 caractères contenant au moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial parmis ! ? @ # $ % ^ & + =";

    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(strFormatDate); //.withResolverStyle(ResolverStyle.STRICT);

    // Config Validation Interface
    // Strings which hold css elements to easily re-use in the application
    public final static String strCssFile = ClassLoader.getSystemClassLoader().getResource(strCssFileString).toExternalForm(),
                                // strCSS = Config.class.getClassLoader().getResource("Views/CSS/main.css").toExternalForm(),
                                strStyleSuccessMessage = String.format("-fx-text-fill: #5cb85c;"),
                                strStyleErrorMessage = String.format("-fx-text-fill: #d9534f;"),
                                strStyleVoidMessage = String.format("-fx-text-fill: #354749"),

                                strStyleSuccessBorder = String.format("-fx-border-color: #354749; -fx-border-width: 2; -fx-border-radius: 5;"),
                                strStyleErrorBorder = String.format("-fx-border-color: #d9534f; -fx-border-width: 2; -fx-border-radius: 5;"),
                                strStyleVoidBorder = String.format("-fx-border-color:  #354749; -fx-border-width: 2; -fx-border-radius: 5;");
}
