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
import java.time.LocalTime;
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

/** Used for manipulating Visite objects and read, write to csv file and compare between objects
 * Is a sub class of Personne
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class Visite {
    private final static String strDataFileVisite = Config.strNewDataFileVisites;

    private int intNoVisite, intTypeDose;
    private String strHeureVisite, strDateVisite, strNom, strPrenom, strCourriel;

    public Visite() {
        this.intNoVisite = 0;
        this.intTypeDose = 0;
        this.strHeureVisite = "";
        this.strDateVisite = "";
        this.strNom = "";
        this.strPrenom = "";
        this.strCourriel = "";
    }
    public Visite(int intNoVisite, int intTypeDose, String strHeureVisite, String strDateVisite, String strNom, String strPrenom, String strCourriel) {
        this.intNoVisite = intNoVisite;
        this.intTypeDose = intTypeDose;
        this.strHeureVisite = strHeureVisite;
        this.strDateVisite = strDateVisite;
        this.strNom = strNom;
        this.strPrenom = strPrenom;
        this.strCourriel = strCourriel;
    }

    public int getIntNoVisite() {
        return this.intNoVisite;
    }
    public void setIntNoVisite(int intNoVisite) {
        this.intNoVisite = intNoVisite;
    }

    public int getIntTypeDose() {
        return this.intTypeDose;
    }
    public void setIntTypeDose(int intTypeDose) {
        this.intTypeDose = intTypeDose;
    }

    public String getStrHeureVisite() {
        return this.strHeureVisite;
    }
    public void setStrHeureVisite(String strHeureVisite) {
        this.strHeureVisite = strHeureVisite;
    }

    public String getStrDateVisite() {
        return this.strDateVisite;
    }
    public void setStrDateVisite(String strDateVisite) {
        this.strDateVisite = strDateVisite;
    }

    public String getStrNom() {
        return this.strNom;
    }
    public void setStrNom(String strNom) {
        this.strNom = strNom;
    }

    public String getStrPrenom() {
        return this.strPrenom;
    }
    public void setStrPrenom(String strPrenom) {
        this.strPrenom = strPrenom;
    }

    public String getStrCourriel() {
        return this.strCourriel;
    }

    public void setStrCourriel(String strCourriel) {
        this.strCourriel = strCourriel;
    }

    public String printInfosVisite() {
        return getIntNoVisite() + Config.strCSVMainSeparator + getIntTypeDose() + Config.strCSVMainSeparator + getStrHeureVisite() + Config.strCSVMainSeparator + getStrDateVisite() + Config.strCSVMainSeparator + getStrNom() + Config.strCSVMainSeparator + getStrPrenom() + Config.strCSVMainSeparator + getStrCourriel();
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

        return getFullName() + "\n" + strUnderline + "Numéro de Réservation:\t" + getIntNoVisite() + "\nHeure de la Visite:\t\t" + getStrHeureVisite() + "\nDate de la Visite:\t\t" + getStrDateVisite() + "\nType de Dose:\t\t\t" + getIntTypeDose() + "\n";
    }
    public String getEmailConfirmation(boolean blnShowUnderline) {
        String strUnderline = "";
        if (blnShowUnderline)
            strUnderline = "---------------------------------------\n";
        // for(int i=0; i<getFullName().length()+1; i++)
        //     strUnderline += "-";

        return getFullName() + "\n" + strUnderline + "Numéro de Réservation:\t" + getIntNoVisite() + (getStrCourriel().trim().isEmpty() ? "\n" : "\nCourriel:\t\t\t\t" + getStrCourriel().trim() + "\n");
    }

    @Override
    public String toString() {
        return "{intNoVisite='" + getIntNoVisite() + "'" +
                ", intTypeDose='" + getIntTypeDose() + "'" +
                ", strHeureVisite='" + getStrHeureVisite() + "'" +
                ", strDateVisite='" + getStrDateVisite() + "'" +
                ", strNom='" + getStrNom() + "'" +
                ", strPrenom='" + getStrPrenom() + "'" +
                ", strCourriel='" + getStrCourriel() + "'" +
                "}";
    }

    public boolean equalsExactly(Object object) {
        if (object == null || !(object instanceof Visite))
            return false;

        // System.out.println("\nInside Exact Equals of Object\n");
        Visite v = (Visite) object;
        
        return this.getIntNoVisite() == v.getIntNoVisite() &&
                this.getIntTypeDose() == v.getIntTypeDose() &&
                this.getStrHeureVisite().equals(v.getStrHeureVisite()) &&
                this.getStrDateVisite().equals(v.getStrDateVisite()) &&
                this.getStrNom().trim().equals(v.getStrNom().trim()) &&
                this.getStrPrenom().trim().equals(v.getStrPrenom().trim()) &&
                this.getStrCourriel().trim().equals(v.getStrCourriel().trim());
    }
    public boolean equalsNoVisite(Object object) {
        if (object == null || !(object instanceof Visite))
            return false;

        // System.out.println("\nInside Equals No Visite of Object\n");
        return this.getIntNoVisite() == ((Visite) object).getIntNoVisite();
    }

    public static class ReturnCrudDataListVisites {
        private ArrayList<Visite> arrVisites;
        private boolean blnAlertErrorDataFolder, blnAlertErrorDataFileVisite;

        public ReturnCrudDataListVisites() {
            this.arrVisites = new ArrayList<>();

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileVisite = false;
        }
        public ReturnCrudDataListVisites(ArrayList<Visite> arrVisites) {
            this.arrVisites = arrVisites;

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileVisite = false;
        }
        public ReturnCrudDataListVisites(boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileVisite) {
            this.arrVisites = new ArrayList<>();

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileVisite = blnAlertErrorDataFileVisite;
        } 
        public ReturnCrudDataListVisites(ArrayList<Visite> arrVisites, boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileVisite) {
            this.arrVisites = arrVisites;

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileVisite = blnAlertErrorDataFileVisite;
        }

        public ArrayList<Visite> getArrVisites() {
            return this.arrVisites;
        }
        public boolean getBlnAlertErrorDataFolder() {
            return this.blnAlertErrorDataFolder;
        }
        public boolean getBlnAlertErrorDataFileVisite() {
            return this.blnAlertErrorDataFileVisite;
        }

        public void setArrVisites(ArrayList<Visite> arrVisites) {
            this.arrVisites = arrVisites;
        }
        public void setBlnAlertErrorDataFolder(boolean blnAlertErrorDataFolder) {
            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
        }
        public void setBlnAlertErrorDataFileVisite(boolean blnAlertErrorDataFileVisite) {
            this.blnAlertErrorDataFileVisite = blnAlertErrorDataFileVisite;
        }
    }
    public static ReturnCrudDataListVisites crudDataListVisites(Logger logger, EnumTypeCRUD enumTypeCRUD, Visite userInputVisite, boolean ...blnArrOptional) {
        System.out.println();

        boolean blnShowVerbose = true, blnInitDataFile = false, blnInitTempVisite = false;
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
                blnInitTempVisite = blnArrOptional[2];
            }
        }

        // System.out.println("\n" + enumTypeCRUD + "\n");

        if (enumTypeCRUD == EnumTypeCRUD.CREATE) {
            Path pathVaxTodoDataFolder = Paths.get(strDataFileVisite).getParent();

            try {
                // ArrayList<Visite> arrVisites = new ArrayList<>();
                ReturnCrudDataListVisites returnCrudDataListVisites = new ReturnCrudDataListVisites(); //! generates error if class is not static

                // check if Data folder exist
                if (Files.isDirectory(pathVaxTodoDataFolder)) {
                    logger.log(Level.INFO, "Dossier des données EXISTE.\nEmplacement du dossier des données: '" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n\n");

                    // File DataFileVisite = new File(strFileName + "Visites.csv");
                    ArrayList<Visite> arrCheckIfVisitesPresentInFile = crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites();

                    // // System.out.println("\n" + strFileName + "Visites.csv\n");
                    // System.out.println("\n");
                    // // for (Visite e : arrCheckIfVisitesPresentInFile)
                    // //     System.out.println(e);
                    // // System.out.println("\n\n");
                    // System.out.println("Is Regular File: " + Boolean.toString(Files.isRegularFile(Paths.get(strFileName + "Visites.csv"))));
                    // System.out.println("Array is Null: " + Boolean.toString(arrCheckIfVisitesPresentInFile == null));
                    // // System.out.println("Array is Empty: " + Boolean.toString(arrCheckIfVisitesPresentInFile.size()>0));
                    // System.out.println("\n");

                    if (Files.isRegularFile(Paths.get(strDataFileVisite)) && arrCheckIfVisitesPresentInFile != null && arrCheckIfVisitesPresentInFile.size()>0) { //DataFileVisite.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Visites.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Visites.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les visites planifiées!\n" + strDataFileVisite /*+ "Visites.csv"*/ + "\n");
                        // returnCrudDataListVisites.setBlnAlertErrorDataFileVisite(crudDataListVisites(logger, EnumTypeCRUD.UPDATE, strFileName + "Visites.csv", null, true, true).getBlnAlertErrorDataFileVisite());
                        returnCrudDataListVisites = crudDataListVisites(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempVisite);
                    }

                    return returnCrudDataListVisites;
                }
                else {
                    logger.log(Level.WARNING, "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n");
                    System.out.println("");

                    Files.createDirectories(pathVaxTodoDataFolder);

                    // File DataFileVisite = new File(strFileName + "Visites.csv");
                    ArrayList<Visite> arrCheckIfVisitesPresentInFile = crudDataListVisites(logger, EnumTypeCRUD.READ, null).getArrVisites();

                    if (Files.isRegularFile(Paths.get(strDataFileVisite)) && arrCheckIfVisitesPresentInFile != null && arrCheckIfVisitesPresentInFile.size()>0) { //DataFileVisite.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Visites.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Visites.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les visites planifiées!\n" + strDataFileVisite /*+ "Visites.csv"*/ + "\n");
                        // returnCrudDataListVisites.setBlnAlertErrorDataFileVisite(crudDataListVisites(logger, EnumTypeCRUD.UPDATE, strFileName + "Visites.csv", null, true, true).getBlnAlertErrorDataFileVisite());
                        returnCrudDataListVisites = crudDataListVisites(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempVisite);
                    }

                    // boolean blnAlertErrorDataFolder = true;
                    returnCrudDataListVisites.setBlnAlertErrorDataFolder(true);
                    return returnCrudDataListVisites;
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
            ReturnCrudDataListVisites returnCrudDataListVisites = new ReturnCrudDataListVisites();
            ArrayList<Visite> arrVisites = new ArrayList<>();

            // System.out.println("Inside " + enumTypeCRUD);

            String tempFile = "";
            // File oldFile = null;
            File newFile = null;
            if (enumTypeCRUD == EnumTypeCRUD.READ || // ? will stay until fixed ?
                enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                tempFile = Paths.get(strDataFileVisite).getParent().toString() + Config.strFileSeparator + "tempVisites.csv"; //Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
                
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
                    logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier temporaire des visites '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
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

                if (blnInitDataFile && userInputVisite == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    try {
                        // reader = new BufferedReader(new FileReader(new File(strFileName)));
                        bufferedReader = new BufferedReader(new FileReader(strDataFileVisite));
                    }
                    catch (FileNotFoundException fnf) {
                        Visite defaultVisite = new Visite(111111, 0, Integer.toString(LocalTime.now().getHour())+":00", LocalDate.now().format(Config.DATE_TIME_FORMATTER).toString(), "Test-Nom", "Test-Prenom", "Test-Courriel@test.com");

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier '" + strDataFileVisite + "' INTROUVABLE.\nCréation d'une visite planifiée par défaut qui a comme information: " + defaultVisite + "\n");
                        
                        // if modifying visite, add visite to end of file
                        printWriter.println("#! intNoCompte, intTypeDose, strHeureVisite, strDateVisite, strNom, strPrenom, strCourriel");

                        if (blnInitTempVisite)
                            printWriter.println(defaultVisite.printInfosVisite());

                        // Damn you Windows! The OS requires that the connection is closed before deleting or renaming files!
                        printWriter.close();

                        // out.flush();

                        // delete old csv file if it exist so that newly created one can be renamed to old csv file
                        try {
                            Files.deleteIfExists(Paths.get(strDataFileVisite));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des visites ['" + strDataFileVisite + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des visites '" + strDataFileVisite + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileVisite + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileVisite);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisite + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisite + "'].\n");

                        // System.out.println("\n--->PATH OF OLD FILE:\n" + oldFile.getPath() + "\n");
                        // System.out.println("\n--->PATH OF DUMP FILE:\n" + dump.getPath() + "\n");
                        // System.out.println("\n--->PATH OF NEW FILE:\n" + newFile.getPath() + "\n");

                        //! boolean blnAlertErrorDataFileVisite = true;
                        returnCrudDataListVisites.setBlnAlertErrorDataFileVisite(true);
                        return returnCrudDataListVisites;
                    }
                }
                // else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                //     try {
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileVisite));
                //     }
                //     catch (FileNotFoundException fnf) {
                //         // if updating accounts & data file doesnt exist, create a new data file & continue execution
                //         crudDataListVisites(logger, EnumTypeCRUD.CREATE, null);
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileVisite));
                //     }
                // }
                else 
                    bufferedReader = new BufferedReader(new FileReader(strDataFileVisite));

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
    
                        Visite visite = new Visite();

                        // read infos
                        if(arrCurrentLigne.length > 0) {
                            for(int i=0; i<arrCurrentLigne.length; i++) {
                                if (i==0) {
                                    int intNoVisite;
                                    try {
                                        intNoVisite = Integer.parseInt(arrCurrentLigne[i]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            logger.log(Level.WARNING, "Error 'intNoVisite' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        intNoVisite = 0;
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            logger.log(Level.WARNING, "Error 'intNoVisite' Index Out Of Bound at line " + intCurrentLineNumber); // 
                                        }
                                        intNoVisite = 0;
                                    }
                                    visite.setIntNoVisite(intNoVisite);
                                }
                                else if (i==1) {
                                    int intTypeDose;
                                    try {
                                        intTypeDose = Integer.parseInt(arrCurrentLigne[i]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            logger.log(Level.WARNING, "Error 'intTypeDose' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        intTypeDose = 0;
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            logger.log(Level.WARNING, "Error 'intTypeDose' Index Out Of Bound at line " + intCurrentLineNumber); // 
                                        }
                                        intTypeDose = 0;
                                    }
                                    visite.setIntTypeDose(intTypeDose);
                                }
                                else if(i==2) {
                                    String strHeureVisite;
                                    try {
                                        strHeureVisite = arrCurrentLigne[i];
                                    } 
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            logger.log(Level.WARNING, "Error 'strHeureVisite' Index Out Of Bound at line " + intCurrentLineNumber); // 
                                        }
                                        strHeureVisite = "";
                                    }
                                    visite.setStrHeureVisite(strHeureVisite);
                                }
                                else if(i==3) {
                                    String strDateVisite;
                                    try {
                                        LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], Config.DATE_TIME_FORMATTER);
                                        strDateVisite = localDate.toString();

                                        // System.out.println("\n\n\n----->READ LocalDate: '" + localDate.toString() + "'\n\n");

                                        if(strDateVisite.trim().isEmpty()) {
                                            logger.log(Level.WARNING, "Error: 'strDateVisite' is empty at line " + intCurrentLineNumber);
                                            throw new Exception("'strDateVisite' est vide!");
                                        }

                                        Date dateVisite = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
                                        if(dateVisite.compareTo(todayDate) < 0) {
                                            logger.log(Level.WARNING, "Error: 'strDateVisite' est plus petite que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                            throw new Exception("'dateVisite' est plus grande que la date d'aujourd'hui!");
                                        }
                                    }
                                    catch (DateTimeParseException dtpe) {
                                        if (blnShowVerbose)
                                            logger.log(Level.WARNING, "Error: La date de visite doit être une date valide (yyyy-mm-dd) et plus grande à la date d'aujourd'hui, 'dateVisite' Date Time Parse Exception at line " + intCurrentLineNumber);
                                        strDateVisite = "";
                                    }
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose)
                                            logger.log(Level.WARNING, "Error: 'dateVisite' Index Out Of Bound at line " + intCurrentLineNumber);
                                            strDateVisite = "";
                                    }
                                    catch (Exception e) {
                                        if (blnShowVerbose)
                                            logger.log(Level.WARNING, "Error: 'strDate' Exception at line " + intCurrentLineNumber);
                                        strDateVisite = "";
                                    }
                                    visite.setStrDateVisite(strDateVisite);
                                }
                                else if(i==4) {
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
                                    visite.setStrNom(strNom);
                                }
                                else if(i==5) {
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
                                    visite.setStrPrenom(strPrenom);
                                }
                                else if(i==6) {
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
                                    visite.setStrCourriel(strCourriel);
                                }
                            }
                        }

                        if (enumTypeCRUD == EnumTypeCRUD.READ || (blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE)) {
                            if (visite.getIntNoVisite()>0)
                                arrVisites.add(visite); // check if visite has code identification & no compte, then it is a valid visite
                        }
                        else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            if (visite.getIntNoVisite()>0 && visite.getIntNoVisite() == userInputVisite.getIntNoVisite()) {
                                // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(visite.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                arrVisites.add(visite);
                                
                                // System.out.println("Modification de ce compte dans le fichier");
                                System.out.println();
                                logger.log(Level.INFO, ("Succès: La visite planifiée '" + Long.toString(visite.getIntNoVisite()) + "' est trouvée dans le fichier '" + strDataFileVisite + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nModification de cette visite dans le fichier\n"));
                            }
                            else
                                printWriter.println(strCurrentLine);
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            if (visite.getIntNoVisite()>0 && visite.getIntNoVisite() == userInputVisite.getIntNoVisite()) {
                                // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(visite.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                arrVisites.add(visite);

                                // System.out.println("Suppression de ce compte dans le fichier");
                                System.out.println();
                                logger.log(Level.INFO, ("Succès: La visite planifiée '" + Long.toString(visite.getIntNoVisite()) + "' est trouvée dans le fichier '" + strDataFileVisite + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nSuppression de cette visite dans le fichier\n"));
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

                if (blnInitDataFile && userInputVisite == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    if (arrVisites == null || arrVisites.size() <= 0) {
                        // System.out.println("\nEntering Inside Function\n");

                        Visite defaultVisite = new Visite(111111, 0, Integer.toString(LocalTime.now().getHour())+":00", LocalDate.now().format(Config.DATE_TIME_FORMATTER).toString(), "Test-Nom", "Test-Prenom", "Test-Courriel@test.com");

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier des donnés Visites.csv VIDE (Nombre Total de Visites dans le fichier = '" + arrVisites.size() + "')\nCréation d'une visite planifiée par défaut qui a comme information: " + defaultVisite + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListVisites(EnumTypeCRUD.UPDATE, strFileName, defaultVisite, false, true);
                        
                        // if modifying visite, add visite to end of file
                        printWriter.println("#! intNoVisite, intTypeDose, strHeureVisite, strDateVisite, strNom, strPrenom, strCourriel");

                        if (blnInitTempVisite)
                            printWriter.println(defaultVisite.printInfosVisite());

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

                            Files.deleteIfExists(Paths.get(strDataFileVisite));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des visites ['" + strDataFileVisite + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des visites '" + strDataFileVisite + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileVisite + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileVisite);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisite + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisite + "'].\n");

                        //! boolean blnAlertErrorDataFileVisite = true;
                        returnCrudDataListVisites.setBlnAlertErrorDataFileVisite(true);
                        return returnCrudDataListVisites;
                    }
                }
                else if (enumTypeCRUD == EnumTypeCRUD.READ) { // ? will stay until fixed ?)
                    // delete temp file that is not needed or used when reading Data from file
                    // newFile.delete();

                    printWriter.close();

                    try {
                        Files.deleteIfExists(Paths.get(tempFile));
                        logger.log(Level.INFO, "Mode Lecture: SUCCÈS pour avoir supprimer le fichier temporaire des visites ['" + tempFile + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "Mode Lecture: IMPOSSIBLE de supprimer le fichier temporaire des visites '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
                        System.out.println("");
                    }
                }
                else if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)) {
                    // if modifying visite, add visite to end of file
                    if (enumTypeCRUD == EnumTypeCRUD.UPDATE) 
                        printWriter.println(userInputVisite.printInfosVisite());

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

                        Files.deleteIfExists(Paths.get(strDataFileVisite));
                        logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des visites ['" + strDataFileVisite + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des visites '" + strDataFileVisite + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileVisite + "'\n", ioe);
                        System.out.println("");
                    }

                    File dump = new File(strDataFileVisite);
                    boolean blnRenameNewFile = newFile.renameTo(dump);

                    if (!blnRenameNewFile)
                        logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisite + "']!\n");
                    else 
                        logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileVisite + "'].\n");

                    // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

                    if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                        // System.out.println("\nSuppression du compte visite '" + Long.toString(userInputVisite.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Suppression de la visite planifiée '" + Long.toString(userInputVisite.getIntNoVisite()) + "' réussi!\n");
                    }
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                        // System.out.println("\nModification du compte visite '" + Long.toString(userInputVisite.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Modification de la visite planifiée '" + Long.toString(userInputVisite.getIntNoVisite()) + "' réussi!\n");
                    }

                    if (arrVisites == null || arrVisites.size() <= 0) {
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            // System.out.println("LA LISTE DES COMPTES VISITEURS SUPPRIMÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES VISITES PLANIFIÉES SUPPRIMÉS EST VIDE!\n");
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            // System.out.println("LA LISTE DES COMPTES VISITEURS MODIFIÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES VISITES PLANIFIÉES MODIFIÉS EST VIDE!\n");
                        }
                    }
                    else {
                        String strStyleListe = "";
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            strStyleListe = "Liste des visites planifiées supprimés dans le fichier:";
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            strStyleListe = "Liste des visites planifiées modifiés dans le fichier:";
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
                        for (Visite e : arrVisites)
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
                logger.log(Level.SEVERE, "Fichier '" + strDataFileVisite + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);
            }
            catch (IOException io) {
                // System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + io.toString());
    
                //System.exit(0);

                System.out.println();
                logger.log(Level.SEVERE, "IOException pour le fichier '" + strDataFileVisite + "'!\n", io);
            }

            // System.out.println("\nList Visites.csv");
            // for (Visite e : arrVisites)
            //     System.out.println(e);
            // System.out.println("\n");
            System.out.println("");

            returnCrudDataListVisites.setArrVisites(arrVisites);
            return returnCrudDataListVisites;
        }

        return null;
    }
}
