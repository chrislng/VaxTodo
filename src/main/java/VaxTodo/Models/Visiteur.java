package VaxTodo.Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/** Used for manipulating Visiteur objects and read, write to csv file and compare between objects
 * Is sub class of Personne
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class Visiteur extends Personne {
    private final static String strDataFileVisiteur = Config.strNewDataFileVisiteurs;

    private String strDateNaissance; //, strCodeQR;

    public Visiteur() {
        this.strDateNaissance = "";
        // this.strCodeQR = "";
    }
    public Visiteur(String strDateNaissance) {
        this.strDateNaissance = strDateNaissance;
        // this.strCodeQR = "";
    }
    public Visiteur(long lngNoCompte, long lngNoTel, String strNom, String strPrenom, String strAdresse, String strCodePostal, String strVille, String strCourriel, String strDateNaissance) {
        this.strDateNaissance = strDateNaissance;
        // this.strCodeQR = "";

        this.setLngNoCompte(lngNoCompte);
        this.setLngNoTel(lngNoTel);
        this.setStrNom(strNom);
        this.setStrPrenom(strPrenom);
        this.setStrAdresse(strAdresse);
        this.setStrCodePostal(strCodePostal);
        this.setStrVille(strVille);
        this.setStrCourriel(strCourriel);
    }

    public String getStrDateNaissance() {
        return this.strDateNaissance;
    }
    public void setStrDateNaissance(String strDateNaissance) {
        this.strDateNaissance = strDateNaissance;
    }

    // public String getStrCodeQR() {
    //     return this.strCodeQR;
    // }
    // public void setStrCodeQR(String strCodeQR) {
    //     this.strCodeQR = strCodeQR;
    // }

    // public String getInfosPersonnelles() {
    //     return getStrDateNaissance();
    // }
    public String getInfosPersonne() {
        return getLngNoCompte() + Config.strCSVMainSeparator + getLngNoTel() + Config.strCSVMainSeparator + getStrNom() + Config.strCSVMainSeparator + getStrPrenom() + Config.strCSVMainSeparator + getStrAdresse() + Config.strCSVMainSeparator + getStrCodePostal() + Config.strCSVMainSeparator + getStrVille() + Config.strCSVMainSeparator + getStrCourriel() + Config.strCSVMainSeparator + getStrDateNaissance();
    }
    // getInfosPersonnelles() + Config.strCSVMainSeparator +
    public String printInfosVisiteur() {
        return getInfosPersonne();
    }
    public String getFullName() {
        return getStrNom() + Config.strCSVMainSeparator + " " + getStrPrenom();
    }

    public String getListViewAccountInfos(boolean blnShowUnderline) {
        String strUnderline = "";
        if (blnShowUnderline)
            strUnderline = "---------------------------------------\n";
        // for(int i=0; i<getFullName().length()+1; i++)
        //     strUnderline += "-";

        return getFullName() + "\n" + strUnderline + "Numéro du Compte:\t" + Long.toString(getLngNoCompte()) + "\n";
    }
    public String getEmailConfirmation(boolean blnShowUnderline) {
        String strUnderline = "";
        if (blnShowUnderline)
            strUnderline = "---------------------------------------\n";
        // for(int i=0; i<getFullName().length()+1; i++)
        //     strUnderline += "-";

        return getFullName() + "\n" + strUnderline + "Numéro du Compte:\t" + Long.toString(getLngNoCompte()) + (getStrCourriel().trim().isEmpty() ? "\n" : "\nCourriel:\t\t\t\t" + getStrCourriel().trim() + "\n");
    }

    @Override
    public String toString() {
        return "{lngNoCompte='" + getLngNoCompte() + "'" +
                ", lngNoTel='" + getLngNoTel() + "'" +
                ", strNom='" + getStrNom() + "'" +
                ", strPrenom='" + getStrPrenom() + "'" +
                ", strAdresse='" + getStrAdresse() + "'" +
                ", strCodePostal='" + getStrCodePostal() + "'" +
                ", strVille='" + getStrVille() + "'" +
                ", strCourriel='" + getStrCourriel() + "'" +
                ", strDateNaissance='" + getStrDateNaissance() + "'" +
                "}";
    }

    public boolean equalsExactly(Object object) {
        if (object == null || !(object instanceof Visiteur))
            return false;

        // if (object == this) {
        //     System.out.println("\nInside Equals of Same Object THIS\n");
        //     return true;
        // }

        // System.out.println("\nInside Exact Equals of Object\n");
        Visiteur v = (Visiteur) object;
        
        return this.getLngNoCompte() == v.getLngNoCompte() &&
                this.getLngNoTel() == v.getLngNoTel() &&
                this.getStrNom().trim().equals(v.getStrNom().trim()) &&
                this.getStrPrenom().trim().equals(v.getStrPrenom().trim()) &&
                this.getStrAdresse().trim().equals(v.getStrAdresse().trim()) &&
                this.getStrCodePostal().trim().equals(v.getStrCodePostal().trim()) &&
                this.getStrVille().trim().equals(v.getStrVille().trim()) && 
                this.getStrCourriel().trim().equals(v.getStrCourriel().trim()) && 
                this.getStrDateNaissance().equals(v.getStrDateNaissance());
    }
    public boolean equalsNoCompte(Object object) {
        if (object == null || !(object instanceof Visiteur))
            return false;

        // if (object == this)
        //     return true;

        // System.out.println("\nInside Equals No Compte of Object\n");
        return this.getLngNoCompte() == ((Visiteur) object).getLngNoCompte();
    }

    public static class ReturnCrudDataListVisiteurs {
        private ArrayList<Visiteur> arrVisiteurs;
        private boolean blnAlertErrorDataFolder, blnAlertErrorDataFileVisiteur;

        public ReturnCrudDataListVisiteurs() {
            this.arrVisiteurs = new ArrayList<>();

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileVisiteur = false;
        }
        public ReturnCrudDataListVisiteurs(ArrayList<Visiteur> arrVisiteurs) {
            this.arrVisiteurs = arrVisiteurs;

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileVisiteur = false;
        }
        public ReturnCrudDataListVisiteurs(boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileVisiteur) {
            this.arrVisiteurs = new ArrayList<>();

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileVisiteur = blnAlertErrorDataFileVisiteur;
        } 
        public ReturnCrudDataListVisiteurs(ArrayList<Visiteur> arrVisiteurs, boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileVisiteur) {
            this.arrVisiteurs = arrVisiteurs;

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileVisiteur = blnAlertErrorDataFileVisiteur;
        }

        public ArrayList<Visiteur> getArrVisiteurs() {
            return this.arrVisiteurs;
        }
        public boolean getBlnAlertErrorDataFolder() {
            return this.blnAlertErrorDataFolder;
        }
        public boolean getBlnAlertErrorDataFileVisiteur() {
            return this.blnAlertErrorDataFileVisiteur;
        }

        public void setArrVisiteurs(ArrayList<Visiteur> arrVisiteurs) {
            this.arrVisiteurs = arrVisiteurs;
        }
        public void setBlnAlertErrorDataFolder(boolean blnAlertErrorDataFolder) {
            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
        }
        public void setBlnAlertErrorDataFileVisiteur(boolean blnAlertErrorDataFileVisiteur) {
            this.blnAlertErrorDataFileVisiteur = blnAlertErrorDataFileVisiteur;
        }
    }
    public static ReturnCrudDataListVisiteurs crudDataListVisiteurs(Logger logger, EnumTypeCRUD enumTypeCRUD, Visiteur userInputVisiteur, boolean ...blnArrOptional) {
        System.out.println();

        boolean blnShowVerbose = true, blnInitDataFile = false, blnInitTempVisiteur = false;
        if (blnArrOptional != null && blnArrOptional.length > 0) {
            if (blnArrOptional.length == 1)
                blnShowVerbose = blnArrOptional[0];
            else if (blnArrOptional.length == 2) {
                blnShowVerbose = blnArrOptional[0];
                blnInitDataFile = blnArrOptional[1];
            }
            else {
                blnShowVerbose = blnArrOptional[0];
                blnInitDataFile = blnArrOptional[1];
                blnInitTempVisiteur = blnArrOptional[2];
            }
        }

        // System.out.println("\n" + enumTypeCRUD + "\n");

        if (enumTypeCRUD == EnumTypeCRUD.CREATE) {
            Path pathVaxTodoDataFolder = Paths.get(strDataFileVisiteur).getParent();

            try {
                // ArrayList<Visiteur> arrVisiteurs = new ArrayList<>();
                ReturnCrudDataListVisiteurs returnCrudDataListVisiteurs = new ReturnCrudDataListVisiteurs(); //! generates error if class is not static

                // check if Data folder exist
                if (Files.isDirectory(pathVaxTodoDataFolder)) {
                    logger.log(Level.INFO, "Dossier des données EXISTE.\nEmplacement du dossier des données: '" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n\n");

                    // File DataFileVisiteur = new File(strFileName + "Visiteurs.csv");
                    ArrayList<Visiteur> arrCheckIfVisiteursPresentInFile = crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs();

                    // // System.out.println("\n" + strFileName + "Visiteurs.csv\n");
                    // System.out.println("\n");
                    // // for (Visiteur e : arrCheckIfVisiteursPresentInFile)
                    // //     System.out.println(e);
                    // // System.out.println("\n\n");
                    // System.out.println("Is Regular File: " + Boolean.toString(Files.isRegularFile(Paths.get(strFileName + "Visiteurs.csv"))));
                    // System.out.println("Array is Null: " + Boolean.toString(arrCheckIfVisiteursPresentInFile == null));
                    // // System.out.println("Array is Empty: " + Boolean.toString(arrCheckIfVisiteursPresentInFile.size()>0));
                    // System.out.println("\n");

                    if (Files.isRegularFile(Paths.get(strDataFileVisiteur)) && arrCheckIfVisiteursPresentInFile != null && arrCheckIfVisiteursPresentInFile.size()>0) { //DataFileVisiteur.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Visiteurs.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Visiteurs.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les comptes visiteurs!\n" + strDataFileVisiteur /*+ "Visiteurs.csv"*/ + "\n");
                        // returnCrudDataListVisiteurs.setBlnAlertErrorDataFileVisiteur(crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, strFileName + "Visiteurs.csv", null, true, true).getBlnAlertErrorDataFileVisiteur());
                        returnCrudDataListVisiteurs = crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempVisiteur);
                    }

                    return returnCrudDataListVisiteurs;
                }
                else {
                    logger.log(Level.WARNING, "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n");
                    System.out.println("");

                    Files.createDirectories(pathVaxTodoDataFolder);

                    // File DataFileVisiteur = new File(strFileName + "Visiteurs.csv");
                    ArrayList<Visiteur> arrCheckIfVisiteursPresentInFile = crudDataListVisiteurs(logger, EnumTypeCRUD.READ, null).getArrVisiteurs();

                    if (Files.isRegularFile(Paths.get(strDataFileVisiteur)) && arrCheckIfVisiteursPresentInFile != null && arrCheckIfVisiteursPresentInFile.size()>0) { //DataFileVisiteur.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Visiteurs.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Visiteurs.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les comptes visiteurs!\n" + strDataFileVisiteur /*+ "Visiteurs.csv"*/ + "\n");
                        // returnCrudDataListVisiteurs.setBlnAlertErrorDataFileVisiteur(crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, strFileName + "Visiteurs.csv", null, true, true).getBlnAlertErrorDataFileVisiteur());
                        returnCrudDataListVisiteurs = crudDataListVisiteurs(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempVisiteur);
                    }

                    // boolean blnAlertErrorDataFolder = true;
                    returnCrudDataListVisiteurs.setBlnAlertErrorDataFolder(true);
                    return returnCrudDataListVisiteurs;
                }
            }
            catch (IOException ioe) {
                // System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + ioe.toString());
                // logger.log(Level.SEVERE, "Failed to create new Data folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nIO Exception for folder: '" + strFileName + "'\n", ioe);
                logger.log(Level.SEVERE, "ERREUR lors de la création d'un nouveau dossier des données! " + Config.strRestartAppInstruction + "\nIO Exception pour le dossier:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n", ioe);
                System.out.println("");

                // Alert alertError = new Alert(AlertType.ERROR, "Failed to create new Data folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nIO Exception for folder:\n'" + strFileName + "'\n", ButtonType.OK);
                Alert alertError = new Alert(AlertType.ERROR, "ERREUR lors de la création d'un nouveau dossier des données! " + Config.strRestartAppInstruction + "\nIO Exception pour le dossier:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n", ButtonType.OK);
                alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alertError.showAndWait();
    
                // System.exit(0);
            }
            catch (Exception e) {
                // logger.log(Level.SEVERE, "Unkown Error Encountered while creating Data folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nException for folder: '" + strFileName + "'\n", e);
                logger.log(Level.SEVERE, "ERREUR INCONNUE RENCONTRÉE lors de la création d'un nouveau dossier des données! " + Config.strRestartAppInstruction + "\nException pour le dossier:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n", e);
                System.out.println("");

                // Alert alertError = new Alert(AlertType.ERROR, "Unkown Error Encountered while creating Data folder. Please RESTART APP or create a ticket and contact your system administrator.\n\nException for folder:\n '" + strFileName + "'", ButtonType.OK);
                Alert alertError = new Alert(AlertType.ERROR, "ERREUR INCONNUE RENCONTRÉE lors de la création d'un nouveau dossier des données! " + Config.strRestartAppInstruction + "\nException pour le dossier:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n", ButtonType.OK);
                // alertError.initStyle(StageStyle.UTILITY);
                alertError.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alertError.showAndWait();
    
                // System.exit(0);
            }
        }
        else if (enumTypeCRUD == EnumTypeCRUD.READ || enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
            ReturnCrudDataListVisiteurs returnCrudDataListVisiteurs = new ReturnCrudDataListVisiteurs();
            ArrayList<Visiteur> arrVisiteurs = new ArrayList<>();

            // System.out.println("Inside " + enumTypeCRUD);

            String tempFile = "";
            // File oldFile = null;
            File newFile = null;
            if (enumTypeCRUD == EnumTypeCRUD.READ || // ? will stay until fixed ?
                enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                tempFile = Paths.get(strDataFileVisiteur).getParent().toString() + Config.strFileSeparator + "tempVisiteurs.csv"; //Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
                
                // oldFile = new File(strFileName);
                newFile = new File(tempFile);

                // System.out.println("\n--->PATH OF TEMP FILE:\n" + Paths.get(tempFile) + "\n");
                // System.out.println("\n--->PATH OF NEW FILE:\n" + Paths.get(tempFile) + "\n");
                // System.out.println("\n--->PATH OF OLD FILE:\n" + Paths.get(strFileName) + "\n");

                // delete temp file if it exist
                try {
                    Files.deleteIfExists(Paths.get(tempFile));
                }
                catch (IOException ioe) {
                    logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier temporaire des visiteurs '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
                    System.out.println("");
                }
            }

            int intCurrentLineNumber = 0;

            // BufferedReader bufferedReader = null;
            // PrintWriter printWriter = null;
            try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, true)))) {
                // if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)
                //     printWriter = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, true)));
                
                BufferedReader bufferedReader = null;

                if (blnInitDataFile && userInputVisiteur == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    try {
                        // reader = new BufferedReader(new FileReader(new File(strFileName)));
                        bufferedReader = new BufferedReader(new FileReader(strDataFileVisiteur));
                    }
                    catch (FileNotFoundException fnf) {
                        // System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());
            
                        //System.exit(0);
        
                        // System.out.println();
                        // logger.log(Level.SEVERE, "Fichier '" + strFileName + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);

                        Visiteur defaultVisiteur = new Visiteur(Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com", LocalDate.now().minusDays(1).format(Config.DATE_TIME_FORMATTER).toString());

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier '" + strDataFileVisiteur + "' INTROUVABLE.\nCréation d'un compte visiteur par défaut qui a comme information: " + defaultVisiteur + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListVisiteurs(EnumTypeCRUD.UPDATE, strFileName, defaultVisiteur, false, true);
                        
                        // if modifying visiteur, add visiteur to end of file
                        printWriter.println("#! lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance");

                        if (blnInitTempVisiteur)
                            printWriter.println(defaultVisiteur.printInfosVisiteur());

                        // Damn you Windows! The OS requires that the connection is closed before deleting or renaming files!
                        printWriter.close();

                        // out.flush();

                        // delete old csv file if it exist so that newly created one can be renamed to old csv file
                        try {
                            // boolean blnDeleteOldFile = oldFile.delete();

                            // if (!blnDeleteOldFile)
                            //     logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer l'ancien fichier ['" + strFileName + "']!\n");
                            // else 
                            //     logger.log(Level.INFO, "SUCCÈS pour avoir supprimer l'ancien fichier ['" + strFileName + "'].\n");

                            Files.deleteIfExists(Paths.get(strDataFileVisiteur));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des visiteurs ['" + strDataFileVisiteur + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des visiteurs '" + strDataFileVisiteur + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileVisiteur + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileVisiteur);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisiteur + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisiteur + "'].\n");

                        // System.out.println("\n--->PATH OF OLD FILE:\n" + oldFile.getPath() + "\n");
                        // System.out.println("\n--->PATH OF DUMP FILE:\n" + dump.getPath() + "\n");
                        // System.out.println("\n--->PATH OF NEW FILE:\n" + newFile.getPath() + "\n");

                        //! boolean blnAlertErrorDataFileVisiteur = true;
                        returnCrudDataListVisiteurs.setBlnAlertErrorDataFileVisiteur(true);
                        return returnCrudDataListVisiteurs;
                    }
                }
                // else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                //     try {
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileVisiteur));
                //     }
                //     catch (FileNotFoundException fnf) {
                //         // if updating accounts & data file doesnt exist, create a new data file & continue execution
                //         crudDataListVisiteurs(logger, EnumTypeCRUD.CREATE, null);
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileVisiteur));
                //     }
                // }
                else 
                    bufferedReader = new BufferedReader(new FileReader(strDataFileVisiteur));

                String strCurrentLine = null;
                // read & ignore first line since it contains comments on how to structure csv file
                strCurrentLine = bufferedReader.readLine();
                intCurrentLineNumber++;

                // print first line if updating or deleting data from file
                if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE))
                    printWriter.println(strCurrentLine);

                while ((strCurrentLine = bufferedReader.readLine()) != null) {
                    intCurrentLineNumber++;
                    
                    // ignore empty lines in csv file
                    if (strCurrentLine.trim().length() > 0) {
                        // (strCurrentLine.trim().split("\\s*" + Miscellaneous.strCSVSecondarySeparator + "\\s*")[0]).split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")
    
                        String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVMainSeparator + "\\s*");
                        // System.out.println(arrCurrentLigne.length + "\n" + Arrays.toString(arrCurrentLigne));
    
                        Visiteur visiteur = new Visiteur();

                        // read infos
                        if(arrCurrentLigne.length > 0) {
                            for(int i=0; i<arrCurrentLigne.length; i++) {
                                if (i==0) {
                                    long lngNoCompte;
                                    try {
                                        // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                        lngNoCompte = Long.parseLong(arrCurrentLigne[i]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'lngNoCompte' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            logger.log(Level.WARNING, "Error 'lngNoCompte' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        lngNoCompte = 0;
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'lngNoCompte' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'lngNoCompte' Index Out Of Bound at line " + intCurrentLineNumber); // 
                                        }
                                        lngNoCompte = 0;
                                    }
                                    visiteur.setLngNoCompte(lngNoCompte);
                                }
                                else if(i==1) {
                                    long lngNoTel;
                                    try {
                                        lngNoTel = Long.parseLong(arrCurrentLigne[i]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            logger.log(Level.WARNING, "Error 'lngNoTel' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        lngNoTel = 0;
                                    }
                                    visiteur.setLngNoTel(lngNoTel);
                                }
                                else if(i==2) {
                                    String strNom;
                                    try {
                                        strNom = arrCurrentLigne[i];
                                    } 
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strNom' Index Out Of Bound at line " + intCurrentLineNumber); // 
                                        }
                                        strNom = "";
                                    }
                                    visiteur.setStrNom(strNom);
                                }
                                else if(i==3) {
                                    String strPrenom;
                                    try {
                                        strPrenom = arrCurrentLigne[i];
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strPrenom = "";
                                    }
                                    visiteur.setStrPrenom(strPrenom);
                                }
                                else if(i==4) {
                                    String strAdresse;
                                    try {
                                        strAdresse = arrCurrentLigne[i];
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strAdresse = "";
                                    }
                                    visiteur.setStrAdresse(strAdresse);
                                }
                                else if(i==5) {
                                    String strCodePostal;
                                    try {
                                        strCodePostal = arrCurrentLigne[i];
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strCodePostal = "";
                                    }
                                    visiteur.setStrCodePostal(strCodePostal);
                                }
                                else if(i==6) {
                                    String strVille;
                                    try {
                                        strVille = arrCurrentLigne[i];
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strVille' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strVille = "";
                                    }
                                    visiteur.setStrVille(strVille);
                                }
                                else if(i==7) {
                                    String strCourriel;
                                    try {
                                        strCourriel = arrCurrentLigne[i];
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strCourriel = "";
                                    }
                                    visiteur.setStrCourriel(strCourriel);
                                }
                                else if (i==8) {
                                    String strDateNaissance;
                                    try {
                                        LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], Config.DATE_TIME_FORMATTER);
                                        strDateNaissance = localDate.toString();

                                        // System.out.println("\n\n\n----->READ LocalDate: '" + localDate.toString() + "'\n\n");

                                        if(strDateNaissance.trim().isEmpty()) {
                                            logger.log(Level.WARNING, "Error: 'strDateNaissance' is empty at line " + intCurrentLineNumber);
                                            throw new Exception("'strDateNaissance' est vide!");
                                        }

                                        Date dateNaissance = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        // if(date.compareTo(LocalDate.now()) < 0) 

                                        Calendar calendarToday = Calendar.getInstance();

                                        calendarToday.clear(Calendar.HOUR); 
                                        calendarToday.clear(Calendar.MINUTE); 
                                        calendarToday.clear(Calendar.SECOND);
                                        calendarToday.clear(Calendar.MILLISECOND);
                                        calendarToday.clear(Calendar.HOUR_OF_DAY);
                                        // calendarToday.clear();

                                        calendarToday.set(Calendar.HOUR, 0);
                                        calendarToday.set(Calendar.HOUR, 0); 
                                        calendarToday.set(Calendar.MINUTE, 0); 
                                        calendarToday.set(Calendar.SECOND, 0);
                                        calendarToday.set(Calendar.MILLISECOND, 0);
                                        calendarToday.set(Calendar.HOUR_OF_DAY, 0);
                                        // calendarToday.setTimeZone(ZoneId.systemDefault());

                                        // System.out.println("\n" + ZoneId.systemDefault());
                                        // System.out.println(calendarToday.getTimeZone().getID() + "\n");

                                        Date todayDate = calendarToday.getTime();

                                        // System.out.println("\nCSV Date: " + dateRendezVous.toString() + "\nToday Date: " + todayDate.toString());
                                        // System.out.println("After Comparison: " + Boolean.toString(todayDate.compareTo(dateRendezVous) <= 0));

                                        // verifie si la date de naissance est plus grand que la date du jour
                                        if(dateNaissance.compareTo(todayDate) >= 0) {
                                            logger.log(Level.WARNING, "Error: 'strDateNaissance' est plus grand que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                            throw new Exception("'dateNaissance' est plus grand que la date d'aujourd'hui!");
                                        }
                                    }
                                    catch (DateTimeParseException dtpe) {
                                        if (blnShowVerbose)
                                            logger.log(Level.WARNING, "Error: La date de naissance doit être une date valide (yyyy-mm-dd) et plus petite à la date d'aujourd'hui, 'dateNaissance' Date Time Parse Exception at line " + intCurrentLineNumber);
                                        strDateNaissance = "";
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose)
                                            logger.log(Level.WARNING, "Error: 'dateNaissance' Index Out Of Bound at line " + intCurrentLineNumber);
                                        strDateNaissance = "";
                                    }
                                    catch (Exception e) {
                                        if (blnShowVerbose)
                                            logger.log(Level.WARNING, "Error: 'strDate' Exception at line " + intCurrentLineNumber);
                                        strDateNaissance = "";
                                    }
                                    visiteur.setStrDateNaissance(strDateNaissance);
                                }
                            }
                        }

                        if (enumTypeCRUD == EnumTypeCRUD.READ || (blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE)) {
                            if (visiteur.getLngNoCompte()>0)
                                arrVisiteurs.add(visiteur); // check if visiteur has code identification & no compte, then it is a valid visiteur
                        }
                        else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            if (visiteur.getLngNoCompte()>0 && visiteur.getLngNoCompte() == userInputVisiteur.getLngNoCompte()) {
                                // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(visiteur.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                arrVisiteurs.add(visiteur);
                                
                                // System.out.println("Modification de ce compte dans le fichier");
                                System.out.println();
                                logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(visiteur.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileVisiteur + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nModification de ce compte dans le fichier\n"));
                            }
                            else
                                printWriter.println(strCurrentLine);
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            if (visiteur.getLngNoCompte()>0 && visiteur.getLngNoCompte() == userInputVisiteur.getLngNoCompte()) {
                                // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(visiteur.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                arrVisiteurs.add(visiteur);

                                // System.out.println("Suppression de ce compte dans le fichier");
                                System.out.println();
                                logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(visiteur.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileVisiteur + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nSuppression de ce compte dans le fichier\n"));
                            }
                            else 
                                printWriter.println(strCurrentLine);
                        }
                    }
                    // else if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)) { 
                    //     // if updating or deleting Data from file, copy empty line & write into new temp file
                    //     printWriter.println(strCurrentLine);
                    // }
                }

                // Damn you Windows! The OS requires that the connection is closed before deleting or renaming files!
                bufferedReader.close();

                if (blnInitDataFile && userInputVisiteur == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                        // System.out.println("\nEntering Inside Function\n");

                        Visiteur defaultVisiteur = new Visiteur(Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com", LocalDate.now().minusDays(1).format(Config.DATE_TIME_FORMATTER).toString());

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier des donnés Visiteurs.csv VIDE (Nombre Total de Visiteurs dans le fichier = '" + arrVisiteurs.size() + "')\nCréation d'un compte visiteur par défaut qui a comme information: " + defaultVisiteur + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListVisiteurs(EnumTypeCRUD.UPDATE, strFileName, defaultVisiteur, false, true);
                        
                        // if modifying visiteur, add visiteur to end of file
                        printWriter.println("#! lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance");

                        if (blnInitTempVisiteur)
                            printWriter.println(defaultVisiteur.printInfosVisiteur());

                        // Damn you Windows! The OS requires that the connection is closed before deleting or renaming files!
                        printWriter.close();
                        
                        // out.flush();
                        
                        // delete old csv file if it exist so that newly created one can be renamed to old csv file
                        try {
                            // boolean blnDeleteOldFile = oldFile.delete();

                            // if (!blnDeleteOldFile)
                            //     logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer l'ancien fichier ['" + strFileName + "']!\n");
                            // else 
                            //     logger.log(Level.INFO, "SUCCÈS pour avoir supprimer l'ancien fichier ['" + strFileName + "'].\n");

                            Files.deleteIfExists(Paths.get(strDataFileVisiteur));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des visiteurs ['" + strDataFileVisiteur + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des visiteurs '" + strDataFileVisiteur + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileVisiteur + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileVisiteur);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisiteur + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisiteur + "'].\n");

                        //! boolean blnAlertErrorDataFileVisiteur = true;
                        returnCrudDataListVisiteurs.setBlnAlertErrorDataFileVisiteur(true);
                        return returnCrudDataListVisiteurs;
                    }
                }
                else if (enumTypeCRUD == EnumTypeCRUD.READ) { // ? will stay until fixed ?)
                    // delete temp file that is not needed or used when reading Data from file
                    // newFile.delete();

                    printWriter.close();

                    try {
                        Files.deleteIfExists(Paths.get(tempFile));
                        logger.log(Level.INFO, "Mode Lecture: SUCCÈS pour avoir supprimer le fichier temporaire des visiteurs ['" + tempFile + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "Mode Lecture: IMPOSSIBLE de supprimer le fichier temporaire des visiteurs '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
                        System.out.println("");
                    }
                }
                else if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)) {
                    // if modifying visiteur, add visiteur to end of file
                    if (enumTypeCRUD == EnumTypeCRUD.UPDATE) 
                        printWriter.println(userInputVisiteur.printInfosVisiteur());

                    // Damn you Windows! The OS requires that the connection is closed before deleting or renaming files!
                    printWriter.close();

                    // out.flush();
                    
                    // delete old csv file if it exist so that newly created one can be renamed to old csv file
                    try {
                        // boolean blnDeleteOldFile = oldFile.delete();

                        // if (!blnDeleteOldFile)
                        //     logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer l'ancien fichier ['" + strFileName + "']!\n");
                        // else 
                        //     logger.log(Level.INFO, "SUCCÈS pour avoir supprimer l'ancien fichier ['" + strFileName + "'].\n");

                        Files.deleteIfExists(Paths.get(strDataFileVisiteur));
                        logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des visiteurs ['" + strDataFileVisiteur + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des visiteurs '" + strDataFileVisiteur + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileVisiteur + "'\n", ioe);
                        System.out.println("");
                    }

                    File dump = new File(strDataFileVisiteur);
                    boolean blnRenameNewFile = newFile.renameTo(dump);

                    if (!blnRenameNewFile)
                        logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisiteur + "']!\n");
                    else 
                        logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisiteur + "'].\n");

                    // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

                    if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                        // System.out.println("\nSuppression du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Suppression du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
                    }
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                        // System.out.println("\nModification du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Modification du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
                    }

                    if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            // System.out.println("LA LISTE DES COMPTES VISITEURS SUPPRIMÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES COMPTES VISITEURS SUPPRIMÉS EST VIDE!\n");
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            // System.out.println("LA LISTE DES COMPTES VISITEURS MODIFIÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES COMPTES VISITEURS MODIFIÉS EST VIDE!\n");
                        }
                    }
                    else {
                        String strStyleListe = "";
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            strStyleListe = "Liste des comptes visiteurs supprimés dans le fichier:";
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            strStyleListe = "Liste des comptes visiteurs modifiés dans le fichier:";
                        }
                        
                        String strStyleListeDash = "", strStyleListeDoubleDash = "";
                        for (int j=0; j<strStyleListe.length(); j++) {
                            strStyleListeDash += "-";
                            strStyleListeDoubleDash += "=";
                        }
                        
                        String strToLog = "";
                        strToLog += (strStyleListeDoubleDash + "\n");
                        strToLog += (strStyleListe + "\n");
                        strToLog += (strStyleListeDash + "\n");
                        for (Visiteur e : arrVisiteurs)
                            strToLog += (e + "\n");
                        // System.out.println();

                        logger.log(Level.INFO, "\n" + strToLog + "\n\n");
                    }
                }
                else // close file writing just in case it was not closed by above if/else
                    printWriter.close();
            }
            catch (FileNotFoundException fnf) {
                // System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());
    
                //System.exit(0);

                System.out.println();
                logger.log(Level.SEVERE, "Fichier '" + strDataFileVisiteur + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);
            }
            catch (IOException io) {
                // System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + io.toString());
    
                //System.exit(0);

                System.out.println();
                logger.log(Level.SEVERE, "IOException pour le fichier '" + strDataFileVisiteur + "'!\n", io);
            }

            // System.out.println("\nList Visiteurs.csv");
            // for (Visiteur e : arrVisiteurs)
            //     System.out.println(e);
            // System.out.println("\n");
            System.out.println("");

            returnCrudDataListVisiteurs.setArrVisiteurs(arrVisiteurs);
            return returnCrudDataListVisiteurs;
        }

        return null;
    }
}
