package VaxTodo.Controllers;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import VaxTodo.Models.EnumTypeMenuInterface;
import VaxTodo.Models.Visiteur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/** Controller used to navigate between menus
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class ControllerMenusNaviguation {
    public static class MenuInfos {
        private String strFXMLLoaderPath;
        private ControllerParent controllerParent;

        private String strFullName;
        private Long lngNoCompte;
        private int intCodeIdentification;

        private String strTitle;

        private Visiteur visiteur;

        public MenuInfos() {
            this.strFXMLLoaderPath = "";
            this.controllerParent = null;

            this.strFullName = "";
            this.lngNoCompte = Long.parseLong("0");
            this.intCodeIdentification = 0;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(Long lngNoCompte) {
            this.strFXMLLoaderPath = "";
            this.controllerParent = null;

            this.strFullName = "";
            this.lngNoCompte = lngNoCompte;
            this.intCodeIdentification = 0;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(int intCodeIdentification) {
            this.strFXMLLoaderPath = "";
            this.controllerParent = null;

            this.strFullName = "";
            this.lngNoCompte = Long.parseLong("0");
            this.intCodeIdentification = intCodeIdentification;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(String strFXMLLoaderPath) {
            this.strFXMLLoaderPath = strFXMLLoaderPath;
            this.controllerParent = null;

            this.strFullName = "";
            this.lngNoCompte = Long.parseLong("0");
            this.intCodeIdentification = 0;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(ControllerParent controllerParent) {
            this.strFXMLLoaderPath = "";
            this.controllerParent = controllerParent;

            this.strFullName = "";
            this.lngNoCompte = Long.parseLong("0");
            this.intCodeIdentification = 0;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(String strFXMLLoaderPath, ControllerParent controllerParent) {
            this.strFXMLLoaderPath = strFXMLLoaderPath;
            this.controllerParent = controllerParent;

            this.strFullName = "";
            this.lngNoCompte = Long.parseLong("0");
            this.intCodeIdentification = 0;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(String strFullName, Long lngNoCompte, int intCodeIdentification) {
            this.strFXMLLoaderPath = "";
            this.controllerParent = null;

            this.strFullName = strFullName;
            this.lngNoCompte = lngNoCompte;
            this.intCodeIdentification = intCodeIdentification;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(String strFullName, Long lngNoCompte, int intCodeIdentification, Visiteur visiteur) {
            this.strFXMLLoaderPath = "";
            this.controllerParent = null;

            this.strFullName = strFullName;
            this.lngNoCompte = lngNoCompte;
            this.intCodeIdentification = intCodeIdentification;
            this.strTitle = "";

            this.visiteur = visiteur;
        }
        public MenuInfos(String strFXMLLoaderPath, String strFullName, Long lngNoCompte, int intCodeIdentification) {
            this.strFXMLLoaderPath = strFXMLLoaderPath;
            this.controllerParent = null;

            this.strFullName = strFullName;
            this.lngNoCompte = lngNoCompte;
            this.intCodeIdentification = intCodeIdentification;
            this.strTitle = "";

            this.visiteur = null;
        }
        public MenuInfos(String strFXMLLoaderPath, String strFullName, Long lngNoCompte, int intCodeIdentification, String strTitle) {
            this.strFXMLLoaderPath = strFXMLLoaderPath;
            this.controllerParent = null;

            this.strFullName = strFullName;
            this.lngNoCompte = lngNoCompte;
            this.intCodeIdentification = intCodeIdentification;
            this.strTitle = strTitle;

            this.visiteur = null;
        }
        public MenuInfos(String strFXMLLoaderPath, ControllerParent controllerParent, String strFullName, Long lngNoCompte, int intCodeIdentification) {
            this.strFXMLLoaderPath = strFXMLLoaderPath;
            this.controllerParent = controllerParent;

            this.strFullName = strFullName;
            this.lngNoCompte = lngNoCompte;
            this.intCodeIdentification = intCodeIdentification;
            this.strTitle = "";

            this.visiteur = null;
        }

        public String getStrFXMLLoaderPath() {
            return strFXMLLoaderPath;
        }
        public ControllerParent getControllerParent() {
            return controllerParent;
        }
        public String getStrFullName() {
            return strFullName;
        }
        public Long getLngNoCompte() {
            return lngNoCompte;
        }
        public int getIntCodeIdentification() {
            return intCodeIdentification;
        }
        public String getStrTitle() {
            return strTitle;
        }
        public Visiteur getVisiteur() {
            return this.visiteur;
        }
        
        public void setStrFXMLLoaderPath(String strFXMLLoaderPath) {
            this.strFXMLLoaderPath = strFXMLLoaderPath;
        }
        public void setControllerParent(ControllerParent controllerParent) {
            this.controllerParent = controllerParent;
        }
        public void setStrFullName(String strFullName) {
            this.strFullName = strFullName;
        }
        public void setLngNoCompte(Long lngNoCompte) {
            this.lngNoCompte = lngNoCompte;
        }
        public void setIntCodeIdentification(int intCodeIdentification) {
            this.intCodeIdentification = intCodeIdentification;
        }
        public void setStrTitle(String strTitle) {
            this.strTitle = strTitle;
        }
        public void setVisiteur(Visiteur visiteur) {
            this.visiteur = visiteur;
        }
    }

    public static void gotoMenu(Stage stage, Logger logger, EnumTypeMenuInterface enumTypeMenuInterface, EnumTypeMenuInterface enumTypeMenuInterfaceReturnBack, MenuInfos menuInfos, boolean ...blnArrOptional) {
        boolean blnShowConfirmation = false;
        if (blnArrOptional != null && blnArrOptional.length > 0)
            blnShowConfirmation = blnArrOptional[0];

        switch(enumTypeMenuInterface) {
            case QUIT_APP: {
                    Alert confirm = new Alert(AlertType.CONFIRMATION, "Quitter l'Application ?", ButtonType.YES, ButtonType.CANCEL);
                    ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Quitter!");
                    ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                    confirm.showAndWait();

                    if (confirm.getResult() == ButtonType.YES) {
                        System.out.println("");
                        // System.out.println("Au revoir!\n");
                        logger.log(Level.INFO, "Au revoir!\n");

                        // Stage stage = (Stage) lblCopyright.getScene().getWindow();
                        stage.close();
                    }
                }
                break;

            //! Does not work properly & crash sometimes
            /*case RETURN_BACK: {
                    Alert confirm = new Alert(AlertType.CONFIRMATION, "Retourner au Menu Précédent ?", ButtonType.YES, ButtonType.CANCEL);
                    ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Retourner!");
                    ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                    confirm.showAndWait();

                    if (confirm.getResult() == ButtonType.YES) {
                        System.out.println("");
                        // System.out.println("Goto Menu Principal\n");
                        logger.log(Level.INFO, "Goto Previous Menu '" + menuInfos.getStrFXMLLoaderPath() + "'\n");
                        
                        //* I cannot believe this worked on first exection. WOW!
                        try {
                            // System.out.println(Config.strInterfaceViewMenuEmploye);
                            FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(menuInfos.getStrFXMLLoaderPath()));
                            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                            scene.getStylesheets().add(Config.strCssFile);

                            //* Again, WOW!
                            ControllerParent controllerParent = (ControllerParent) fxmlLoader.getController();
                            controllerParent.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());

                            stage.setTitle(menuInfos.getStrTitle());
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                        catch (IOException ioe) {
                            // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                            logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                            System.out.println("");

                            System.exit(0);
                        }
                        catch (Exception e) {
                            // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                            logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                            System.out.println("");

                            System.exit(0);
                        }
                    }
                }
                break;*/

            case LOGIN: {
                    if (blnShowConfirmation) {
                        Alert confirm = new Alert(AlertType.CONFIRMATION, "Retourner au Menu Principal ?", ButtonType.YES, ButtonType.CANCEL);
                        ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Retourner au Menu Principal!");
                        ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                        confirm.showAndWait();

                        if (confirm.getResult() == ButtonType.YES) {
                            try {
                                System.out.println("");
                                logger.log(Level.INFO, "Goto Menu Principal\n");
        
                                FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewLogin));
                                // FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuEmploye));
                                Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                                scene.getStylesheets().add(Config.strCssFile);
        
                                // blnAlertErrorLogs = false;
                                // createLogsFolder(Config.strLogsFolderPath);
                                // if (blnAlertErrorLogs) {
                                //     new Alert(AlertType.WARNING, "Emplacement du dossier des journaux INTROUVABLE.\n\nCréation d'un nouveau dossier des journaux à l'emplacement:\n'" + Config.strLogsFolderPath + "'\n\nRedémarrage de l'application!\n", ButtonType.OK).showAndWait();
        
                                //     restartApp(stage);
                                // }
        
                                ControllerLogin controllerLogin = (ControllerLogin) fxmlLoader.getController();
                                controllerLogin.setStageAndSetupListeners(stage, "", Long.parseLong("0"), 0);
                                // ControllerMenuEmploye controllerMenuEmploye = (ControllerMenuEmploye) fxmlLoader.getController();
                                // controllerMenuEmploye.setStageAndSetupListeners(stage, "Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222);
        
                                stage.setTitle("VaxTodo");
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.show();
                            }
                            catch (IOException ioe) {
                                // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                                logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                                System.out.println("\n");
        
                                System.exit(0);
                            }
                            catch (Exception e) {
                                // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                                logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                                System.out.println("\n");
        
                                System.exit(0);
                            }
                        }
                    }
                    else {
                        try {
                            System.out.println("");
                            logger.log(Level.INFO, "Goto Menu Principal\n");
    
                            FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewLogin));
                            // FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuEmploye));
                            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                            scene.getStylesheets().add(Config.strCssFile);
    
                            // blnAlertErrorLogs = false;
                            // createLogsFolder(Config.strLogsFolderPath);
                            // if (blnAlertErrorLogs) {
                            //     new Alert(AlertType.WARNING, "Emplacement du dossier des journaux INTROUVABLE.\n\nCréation d'un nouveau dossier des journaux à l'emplacement:\n'" + Config.strLogsFolderPath + "'\n\nRedémarrage de l'application!\n", ButtonType.OK).showAndWait();
    
                            //     restartApp(stage);
                            // }
    
                            ControllerLogin controllerLogin = (ControllerLogin) fxmlLoader.getController();
                            controllerLogin.setStageAndSetupListeners(stage, "", Long.parseLong("0"), 0);
                            // ControllerMenuEmploye controllerMenuEmploye = (ControllerMenuEmploye) fxmlLoader.getController();
                            // controllerMenuEmploye.setStageAndSetupListeners(stage, "Gggyyy, Mohamed", Long.parseLong("222222222222"), 222222222);
    
                            stage.setTitle("VaxTodo");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                        catch (IOException ioe) {
                            // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                            logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                            System.out.println("\n");
    
                            System.exit(0);
                        }
                        catch (Exception e) {
                            // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                            logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                            System.out.println("\n");
    
                            System.exit(0);
                        }
                    }
                }
                break;

            case ACCOUNT_INFOS:
                try {
                    System.out.println("");
                    logger.log(Level.INFO, "Goto Menu Forgot Password\n");

                    // System.out.println(Config.strInterfaceViewMenuEmploye);
                    FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuForgotPasswd));
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                    scene.getStylesheets().add(Config.strCssFile);

                    ControllerForgotPasswd controllerForgotPasswd = (ControllerForgotPasswd) fxmlLoader.getController();
                    controllerForgotPasswd.setStageAndSetupListeners(stage, "", Long.parseLong("0"), 0);

                    stage.setTitle("Lister les informations de connexion d'un compte");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
                catch (IOException ioe) {
                    // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                    logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                    System.out.println("");

                    System.exit(0);
                }
                catch (Exception e) {
                    // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                    logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                    System.out.println("");

                    System.exit(0);
                }
                break;

            case EMPLOYE: {
                    if (blnShowConfirmation) {
                        Alert confirm = new Alert(AlertType.CONFIRMATION, "Retourner au Menu Précédent ?", ButtonType.YES, ButtonType.CANCEL);
                        ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Retourner!");
                        ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                        confirm.showAndWait();

                        if (confirm.getResult() == ButtonType.YES) {
                            try {
                                System.out.println("");
                                logger.log(Level.INFO, "Goto Menu Employe\n");
        
                                // System.out.println(Config.strInterfaceViewMenuEmploye);
                                FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuEmploye));
                                Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                                scene.getStylesheets().add(Config.strCssFile);
        
                                ControllerMenuEmploye controllerMenuEmploye = (ControllerMenuEmploye) fxmlLoader.getController();
                                controllerMenuEmploye.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());
        
                                stage.setTitle("Menu Employé");
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.show();
                            }
                            catch (IOException ioe) {
                                // logger.log(Level.SEVERE, "Failed to create new Window. Please RESTART APP or create a ticket and contact your system administrator.\n", ioe);
                                logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                                System.out.println("");
        
                                // System.exit(0);
                            }
                            catch (Exception e) {
                                // logger.log(Level.SEVERE, "Unkown Error Encountered. Please RESTART APP or create a ticket and contact your system administrator.\n", e);
                                logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                                System.out.println("");
        
                                // System.exit(0);
                            }
                        }
                    }
                    else {
                        try {
                            System.out.println("");
                            logger.log(Level.INFO, "Goto Menu Employe\n");
    
                            // System.out.println(Config.strInterfaceViewMenuEmploye);
                            FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuEmploye));
                            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                            scene.getStylesheets().add(Config.strCssFile);
    
                            ControllerMenuEmploye controllerMenuEmploye = (ControllerMenuEmploye) fxmlLoader.getController();
                            controllerMenuEmploye.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());
    
                            stage.setTitle("Menu Employé");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                        catch (IOException ioe) {
                            // logger.log(Level.SEVERE, "Failed to create new Window. Please RESTART APP or create a ticket and contact your system administrator.\n", ioe);
                            logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                            System.out.println("");
    
                            // System.exit(0);
                        }
                        catch (Exception e) {
                            // logger.log(Level.SEVERE, "Unkown Error Encountered. Please RESTART APP or create a ticket and contact your system administrator.\n", e);
                            logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                            System.out.println("");
    
                            // System.exit(0);
                        }   
                    }
                }
                break;

            case BENEVOLE: {
                    if (blnShowConfirmation) {
                        Alert confirm = new Alert(AlertType.CONFIRMATION, "Retourner au Menu Précédent ?", ButtonType.YES, ButtonType.CANCEL);
                        ((Button) confirm.getDialogPane().lookupButton(ButtonType.YES)).setText("Oui, Retourner!");
                        ((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuler");
                        confirm.showAndWait();

                        if (confirm.getResult() == ButtonType.YES) {
                            try {
                                System.out.println("");
                                logger.log(Level.INFO, "Goto Menu Benevole\n");
        
                                // System.out.println(Config.strInterfaceViewMenuBenevole);
                                FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuBenevole));
                                Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                                scene.getStylesheets().add(Config.strCssFile);
        
                                ControllerMenuBenevole controllerMenuBenevole = (ControllerMenuBenevole) fxmlLoader.getController();
                                controllerMenuBenevole.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());
        
                                stage.setTitle("Menu Bénévole");
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.show();
                            }
                            catch (IOException ioe) {
                                // logger.log(Level.SEVERE, "Failed to create new Window. Please RESTART APP or create a ticket and contact your system administrator.\n", ioe);
                                logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                                System.out.println("");
        
                                // System.exit(0);
                            }
                            catch (Exception e) {
                                // logger.log(Level.SEVERE, "Unkown Error Encountered. Please RESTART APP or create a ticket and contact your system administrator.\n", e);
                                logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                                System.out.println("");
        
                                // System.exit(0);
                            }
                        }
                    }
                    else {
                        try {
                            System.out.println("");
                            logger.log(Level.INFO, "Goto Menu Benevole\n");
    
                            // System.out.println(Config.strInterfaceViewMenuBenevole);
                            FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuBenevole));
                            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                            scene.getStylesheets().add(Config.strCssFile);
    
                            ControllerMenuBenevole controllerMenuBenevole = (ControllerMenuBenevole) fxmlLoader.getController();
                            controllerMenuBenevole.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());
    
                            stage.setTitle("Menu Bénévole");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                        catch (IOException ioe) {
                            // logger.log(Level.SEVERE, "Failed to create new Window. Please RESTART APP or create a ticket and contact your system administrator.\n", ioe);
                            logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                            System.out.println("");
    
                            // System.exit(0);
                        }
                        catch (Exception e) {
                            // logger.log(Level.SEVERE, "Unkown Error Encountered. Please RESTART APP or create a ticket and contact your system administrator.\n", e);
                            logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                            System.out.println("");
    
                            // System.exit(0);
                        }
                    }
                }
                break;

            case GESTION_EMPLOYES:
                try {
                    System.out.println("");
                    logger.log(Level.INFO, "Goto Menu Gestion Employes\n");

                    // System.out.println(Config.strInterfaceViewMenuEmploye);
                    FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuGestionEmployes));
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                    scene.getStylesheets().add(Config.strCssFile);

                    ControllerMenuGestionEmployes controllerMenuGestionEmployes = (ControllerMenuGestionEmployes) fxmlLoader.getController();
                    controllerMenuGestionEmployes.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());

                    stage.setTitle("Menu Gestion des Employés");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
                catch (IOException ioe) {
                    // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                    logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                    System.out.println("");

                    System.exit(0);
                }
                catch (Exception e) {
                    // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                    logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                    System.out.println("");

                    System.exit(0);
                }
                break;

            case GESTION_BENEVOLES: {
                    try {
                        System.out.println("");
                        logger.log(Level.INFO, "Goto Menu Gestion Benevoles\n");

                        // System.out.println(Config.strInterfaceViewMenuEmploye);
                        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuGestionBenevoles));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                        scene.getStylesheets().add(Config.strCssFile);

                        ControllerMenuGestionBenevoles controllerMenuGestionBenevoles = (ControllerMenuGestionBenevoles) fxmlLoader.getController();
                        controllerMenuGestionBenevoles.setStageAndSetupListeners(stage, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());

                        stage.setTitle("Menu Gestion des Bénévoles");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    }
                    catch (IOException ioe) {
                        // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                        logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                        System.out.println("");

                        System.exit(0);
                    }
                    catch (Exception e) {
                        // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                        logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                        System.out.println("");

                        System.exit(0);
                    }
                }
                break;

            case GESTION_VISITEURS: {
                    try {
                        System.out.println("");
                        logger.log(Level.INFO, "Goto Menu Gestion Visiteurs\n");

                        // System.out.println(Config.strInterfaceViewMenuEmploye);
                        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuGestionVisiteurs));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                        scene.getStylesheets().add(Config.strCssFile);

                        ControllerMenuGestionVisiteurs controllerMenuGestionVisiteurs = (ControllerMenuGestionVisiteurs) fxmlLoader.getController();

                        if (enumTypeMenuInterfaceReturnBack != null) 
                            controllerMenuGestionVisiteurs.setStageAndSetupListeners(stage, enumTypeMenuInterfaceReturnBack, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification(), blnShowConfirmation);
                        else
                            controllerMenuGestionVisiteurs.setStageAndSetupListeners(stage, EnumTypeMenuInterface.EMPLOYE, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification(), blnShowConfirmation);

                        stage.setTitle("Menu Gestion des Visiteurs");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    }
                    catch (IOException ioe) {
                        // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                        logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                        System.out.println("");

                        System.exit(0);
                    }
                    catch (Exception e) {
                        // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                        logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                        System.out.println("");

                        System.exit(0);
                    }
                }
                break;

            case GESTION_CALENDRIER: {
                    try {
                        System.out.println("");
                        logger.log(Level.INFO, "Goto Menu Gestion Visites Planifiées\n");

                        // System.out.println(Config.strInterfaceViewMenuEmploye);
                        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuGestionCalendrier));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                        scene.getStylesheets().add(Config.strCssFile);

                        ControllerMenuGestionCalendrier controllerMenuGestionCalendrier = (ControllerMenuGestionCalendrier) fxmlLoader.getController();

                        if (enumTypeMenuInterfaceReturnBack != null) 
                            controllerMenuGestionCalendrier.setStageAndSetupListeners(stage, enumTypeMenuInterfaceReturnBack, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());
                        else
                            controllerMenuGestionCalendrier.setStageAndSetupListeners(stage, EnumTypeMenuInterface.LOGIN, menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());

                        stage.setTitle("Menu Gestion des Visites Planifiées");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    }
                    catch (IOException ioe) {
                        // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                        logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                        System.out.println("");

                        System.exit(0);
                    }
                    catch (Exception e) {
                        // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                        logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                        System.out.println("");

                        System.exit(0);
                    }
                }
                break;

            case GESTION_FORMULAIRES: {
                    try {
                        System.out.println("");
                        logger.log(Level.INFO, "Goto Menu Gestion Formulaires\n");

                        // System.out.println(Config.strInterfaceViewMenuEmploye);
                        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMenusNaviguation.class.getResource(Config.strInterfaceViewMenuGestionFormulaires));
                        Scene scene = new Scene(fxmlLoader.load(), 1000, 965);
                        scene.getStylesheets().add(Config.strCssFile);

                        ControllerMenuGestionFormulaires controllerMenuGestionFormulaires = (ControllerMenuGestionFormulaires) fxmlLoader.getController();

                        if (enumTypeMenuInterfaceReturnBack != null) 
                            controllerMenuGestionFormulaires.setStageAndSetupListeners(stage, enumTypeMenuInterfaceReturnBack, menuInfos.getVisiteur(), menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());
                        else
                            controllerMenuGestionFormulaires.setStageAndSetupListeners(stage, EnumTypeMenuInterface.LOGIN, menuInfos.getVisiteur(), menuInfos.getStrFullName(), menuInfos.getLngNoCompte(), menuInfos.getIntCodeIdentification());

                        stage.setTitle("Menu Gestion des Formulaires et des Entrevues");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    }
                    catch (IOException ioe) {
                        // logger.log(Level.SEVERE, "Failed to create new Window. Please create a ticket and contact system administrator.\n", ioe);
                        logger.log(Level.SEVERE, "IMPOSSIBLE de créer une nouvelle fenêtre! " + Config.strRestartAppInstruction, ioe);
                        System.out.println("");

                        System.exit(0);
                    }
                    catch (Exception e) {
                        // logger.log(Level.SEVERE, "Unkown Error Encountered. Please create a ticket and contact system administrator.\n", e);
                        logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE! " + Config.strRestartAppInstruction, e);
                        System.out.println("");

                        System.exit(0);
                    }
                }
                break;

            default:
                System.out.println("");
                logger.log(Level.SEVERE, "No valid EnumTypeMenuInterface was given in parameter 'gotoMenu(EnumTypeMenuInterface)'!\nExiting Application Now!\n");
                stage.close();
                break;
        }
    }
}
