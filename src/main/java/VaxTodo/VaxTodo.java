package VaxTodo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import VaxTodo.Controllers.ControllerMenusNaviguation;
import VaxTodo.Controllers.ControllerMenusNaviguation.MenuInfos;
import VaxTodo.Models.EnumTypeMenuInterface;
import javafx.application.Application;
import javafx.stage.Stage;

/** Used to start the App when launched
 * Is sub class of Application
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class VaxTodo extends Application {
    private final Logger logger = Logger.getLogger(VaxTodo.class.getName());
    // private boolean blnAlertErrorLogs;


    public static void main(String[] args) throws Exception {
        // Path source = Paths.get(ClassLoader.getSystemResource("").toURI());
        // Path newFolder = Paths.get(source.toAbsolutePath() + "/Data/");
        // Files.createDirectories(newFolder);

        // if (args != null && args.length > 0)
        //     System.err.println("\nArguments are not currently implemented!\nMaybe next version ¯\\_(ツ)_/¯");

        // new Login().run();
        // new MenuEmploye().run(Long.parseLong("467801264703"), 979125678);
        // new MenuEmploye().run(Long.parseLong("184929656463"), 123456789);
        // new MenuEmploye().run(Long.parseLong("123456789012"), 123456789);

        // System.exit(0);

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        startApp(stage);
    }

    private void startApp(Stage stage) {
        System.out.println("\n\n");

        // check if Logs folder exist
        try {
            // create Logs folder if it doesn't exist
            if (!Files.isDirectory(Paths.get(Config.strLogsFolderPath)))
                Files.createDirectories(Paths.get(Config.strLogsFolderPath));
        } 
        catch (IOException ioe) {
            System.out.println("\n\nLogger Folder IO Exception:\n" + ioe.toString() + "\n\n");
        }

        ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.LOGIN, null, new MenuInfos());
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.EMPLOYE, null, new MenuInfos("Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.BENEVOLE, null, new MenuInfos("Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_EMPLOYES, EnumTypeMenuInterface.EMPLOYE, new MenuInfos("Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_VISITEURS, EnumTypeMenuInterface.EMPLOYE, new MenuInfos("Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_CALENDRIER, EnumTypeMenuInterface.EMPLOYE, new MenuInfos("Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222));
        // ControllerMenusNaviguation.gotoMenu(stage, logger, EnumTypeMenuInterface.GESTION_FORMULAIRES, EnumTypeMenuInterface.EMPLOYE, new MenuInfos("Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222));
    }
    
    private void restartApp(Stage stage) {
        startApp(stage);
    }

    // private void createLogsFolder(String strFileName) {
    //     try {
    //         // check if Logs folder exist
    //         if (Files.isDirectory(Paths.get(strFileName))) {
    //             logger.log(Level.INFO, "Dossier des journaux EXISTE.\nEmplacement du dossier des journaux: '" + strFileName + "'\n\n");
    //         }
    //         else {
    //             blnAlertErrorLogs = true;
    //             logger.log(Level.WARNING, "Emplacement du dossier des journaux INTROUVABLE.\nCréation d'un nouveau dossier des journaux à l'emplacement:\n'" + strFileName + "'\n");
    //             System.out.println("");

    //             Files.createDirectories(Paths.get(strFileName));
    //         }
    //     }
    //     catch (IOException ioe) {
    //         // System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + ioe.toString());
    //         logger.log(Level.SEVERE, "Failed to create new Logs folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nIO Exception for folder: '" + strFileName + "'\n", ioe);
    //         System.out.println("");

    //         Alert alertError = new Alert(AlertType.ERROR, "Failed to create new Logs folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nIO Exception for folder:\n'" + strFileName + "'\n", ButtonType.OK);
    //         alertError.showAndWait();

    //         // System.exit(0);
    //     }
    //     catch (Exception e) {
    //         logger.log(Level.SEVERE, "Unkown Error Encountered while creating Logs folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nException for folder: '" + strFileName + "'\n", e);
    //         System.out.println("");

    //         Alert alertError = new Alert(AlertType.ERROR, "Unkown Error Encountered while creating Logs folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nException for folder:\n '" + strFileName + "'", ButtonType.OK);
    //         // alertError.initStyle(StageStyle.UTILITY);
    //         alertError.showAndWait();

    //         // System.exit(0);
    //     }
    // }
}
