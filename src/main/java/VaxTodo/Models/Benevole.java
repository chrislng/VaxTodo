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

/** Used for manipulating Benevoles objects and read, write to csv file and compare between objects
 * Is sub class of Personne
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class Benevole extends Personne {
    private final static String strDataFileBenevole = Config.strNewDataFileBenevoles;

    private int intCodeIdentification;
    private String strMotDePasse;

    //* Un bénévole a aussi un horaire listant les jours où il est disponible pour venir au local.
    private String strDateNaissance;

    public Benevole() {
        this.intCodeIdentification = 0;
        this.strMotDePasse = "";
        this.strDateNaissance = "";
    }
    public Benevole(int intCodeIdentification, String strMotDePasse, String strDateNaissance) {
        this.intCodeIdentification = intCodeIdentification;
        this.strMotDePasse = strMotDePasse;
        this.strDateNaissance = strDateNaissance;
    }
    public Benevole(int intCodeIdentification, String strMotDePasse, long lngNoCompte, long lngNoTel, String strNom, String strPrenom, String strAdresse, String strCodePostal, String strVille, String strCourriel, String strDateNaissance) {
        this.intCodeIdentification = intCodeIdentification;
        this.strMotDePasse = strMotDePasse;
        this.strDateNaissance = strDateNaissance;

        this.setLngNoCompte(lngNoCompte);
        this.setLngNoTel(lngNoTel);
        this.setStrNom(strNom);
        this.setStrPrenom(strPrenom);
        this.setStrAdresse(strAdresse);
        this.setStrCodePostal(strCodePostal);
        this.setStrVille(strVille);
        this.setStrCourriel(strCourriel);
    }

    public int getIntCodeIdentification() {
        return this.intCodeIdentification;
    }

    public void setIntCodeIdentification(int intCodeIdentification) {
        this.intCodeIdentification = intCodeIdentification;
    }

    public String getStrMotDePasse() {
        return this.strMotDePasse;
    }
    public String getStrMotDePasseDecryption() throws Exception {
        return AES_GCM_Athentication.decrypt(this.strMotDePasse).trim();
    }

    public void setStrMotDePasse(String strMotDePasse) {
        this.strMotDePasse = strMotDePasse;
    }
    public void initStrMotDePasseEncryption(String strMotDePasse) throws Exception {
        this.strMotDePasse = AES_GCM_Athentication.encrypt(strMotDePasse.trim());
    }

    public String getStrDateNaissance() {
        return this.strDateNaissance;
    }

    public void setStrDateNaissance(String strDateNaissance) {
        this.strDateNaissance = strDateNaissance;
    }

    public String getInfosConnexion() {
        return getIntCodeIdentification() + Config.strCSVMainSeparator + getStrMotDePasse();
    }
    public String getInfosPersonne() {
        return getLngNoCompte() + Config.strCSVMainSeparator + getLngNoTel() + Config.strCSVMainSeparator + getStrNom() + Config.strCSVMainSeparator + getStrPrenom() + Config.strCSVMainSeparator + getStrAdresse() + Config.strCSVMainSeparator + getStrCodePostal() + Config.strCSVMainSeparator + getStrVille() + Config.strCSVMainSeparator + getStrCourriel() + Config.strCSVMainSeparator + getStrDateNaissance();
    }
    public String printInfosBenevole() {
        return getInfosConnexion() + Config.strCSVSecondarySeparator + getInfosPersonne();
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

        return getFullName() + "\n" + strUnderline + "Numéro du Compte:\t" + Long.toString(getLngNoCompte()) + "\nCode Identification:\t\t" + Integer.toString(getIntCodeIdentification());
    }

    @Override
    public String toString() {
        return "{[" +
            "intCodeIdentification='" + getIntCodeIdentification() + "'" +
            ", strMotDePasse='" + getStrMotDePasse() + "'" +
            // ", strDateNaissance='" + getStrDateNaissance() + "'" +
            "], lngNoCompte='" + getLngNoCompte() + "'" +
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
    public boolean equalsExactly(Object object) throws Exception {
        if (object == null || !(object instanceof Benevole))
            return false;

        // if (object == this) {
        //     System.out.println("\nInside Equals of Same Object THIS\n");
        //     return true;
        // }

        // System.out.println("\nInside Exact Equals of Object\n");
        Benevole b = (Benevole) object;
        
        return this.getIntCodeIdentification() == b.getIntCodeIdentification() &&
                // this.getStrMotDePasse().trim().equals(e.getStrMotDePasse().trim()) &&
                // AES_GCM_Athentication.decrypt(this.getStrMotDePasse()).trim().equals(AES_GCM_Athentication.decrypt(e.getStrMotDePasse()).trim()) &&
                this.getStrMotDePasseDecryption().equals(b.getStrMotDePasseDecryption()) &&
                // this.getStrDateNaissance().equals(e.getStrDateNaissance()) &&
                this.getLngNoCompte() == b.getLngNoCompte() &&
                this.getLngNoTel() == b.getLngNoTel() &&
                this.getStrNom().trim().equals(b.getStrNom().trim()) &&
                this.getStrPrenom().trim().equals(b.getStrPrenom().trim()) &&
                this.getStrAdresse().trim().equals(b.getStrAdresse().trim()) &&
                this.getStrCodePostal().trim().equals(b.getStrCodePostal().trim()) &&
                this.getStrVille().trim().equals(b.getStrVille().trim()) && 
                this.getStrCourriel().trim().equals(b.getStrCourriel().trim()) && 
                this.getStrDateNaissance().equals(b.getStrDateNaissance());
    }
    public boolean equalsNoCompte(Object object) {
        if (object == null || !(object instanceof Benevole))
            return false;

        // if (object == this)
        //     return true;

        // System.out.println("\nInside Equals No Compte of Object\n");
        return this.getLngNoCompte() == ((Benevole) object).getLngNoCompte();
    }

    public static class ReturnCrudDataListBenevoles {
        private ArrayList<Benevole> arrBenevoles;
        private boolean blnAlertErrorDataFolder, blnAlertErrorDataFileBenevole;

        public ReturnCrudDataListBenevoles() {
            this.arrBenevoles = new ArrayList<>();

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileBenevole = false;
        }
        public ReturnCrudDataListBenevoles(ArrayList<Benevole> arrBenevoles) {
            this.arrBenevoles = arrBenevoles;

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileBenevole = false;
        }
        public ReturnCrudDataListBenevoles(boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileBenevole) {
            this.arrBenevoles = new ArrayList<>();

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileBenevole = blnAlertErrorDataFileBenevole;
        } 
        public ReturnCrudDataListBenevoles(ArrayList<Benevole> arrBenevoles, boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileBenevole) {
            this.arrBenevoles = arrBenevoles;

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileBenevole = blnAlertErrorDataFileBenevole;
        }

        public ArrayList<Benevole> getArrBenevoles() {
            return this.arrBenevoles;
        }
        public boolean getBlnAlertErrorDataFolder() {
            return this.blnAlertErrorDataFolder;
        }
        public boolean getBlnAlertErrorDataFileBenevole() {
            return this.blnAlertErrorDataFileBenevole;
        }

        public void setArrBenevoles(ArrayList<Benevole> arrBenevoles) {
            this.arrBenevoles = arrBenevoles;
        }
        public void setBlnAlertErrorDataFolder(boolean blnAlertErrorDataFolder) {
            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
        }
        public void setBlnAlertErrorDataFileBenevole(boolean blnAlertErrorDataFileBenevole) {
            this.blnAlertErrorDataFileBenevole = blnAlertErrorDataFileBenevole;
        }
    }
    public static ReturnCrudDataListBenevoles crudDataListBenevoles(Logger logger, EnumTypeCRUD enumTypeCRUD, Benevole userInputBenevole, boolean ...blnArrOptional) {
        System.out.println();

        boolean blnShowVerbose = true, blnInitDataFile = false, blnInitTempBenevole = false;
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
                blnInitTempBenevole = blnArrOptional[2];
            }
        }

        // System.out.println("\n" + enumTypeCRUD + "\n");

        if (enumTypeCRUD == EnumTypeCRUD.CREATE) {
            Path pathVaxTodoDataFolder = Paths.get(strDataFileBenevole).getParent();

            try {
                // ArrayList<Benevole> arrBenevoles = new ArrayList<>();
                ReturnCrudDataListBenevoles returnCrudDataListBenevoles = new ReturnCrudDataListBenevoles(); //! generates error if class is not static

                // check if Data folder exist
                if (Files.isDirectory(pathVaxTodoDataFolder)) {
                    logger.log(Level.INFO, "Dossier des données EXISTE.\nEmplacement du dossier des données: '" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n\n");

                    // File DataFileBenevole = new File(strFileName + "Benevoles.csv");
                    ArrayList<Benevole> arrCheckIfBenevolesPresentInFile = crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();

                    // // System.out.println("\n" + strFileName + "Benevoles.csv\n");
                    // System.out.println("\n");
                    // // for (Benevole e : arrCheckIfBenevolesPresentInFile)
                    // //     System.out.println(e);
                    // // System.out.println("\n\n");
                    // System.out.println("Is Regular File: " + Boolean.toString(Files.isRegularFile(Paths.get(strFileName + "Benevoles.csv"))));
                    // System.out.println("Array is Null: " + Boolean.toString(arrCheckIfBenevolesPresentInFile == null));
                    // // System.out.println("Array is Empty: " + Boolean.toString(arrCheckIfBenevolesPresentInFile.size()>0));
                    // System.out.println("\n");

                    if (Files.isRegularFile(Paths.get(strDataFileBenevole)) && arrCheckIfBenevolesPresentInFile != null && arrCheckIfBenevolesPresentInFile.size()>0) { //DataFileBenevole.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Benevoles.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Benevoles.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les comptes bénévoles!\n" + strDataFileBenevole /*+ "Benevoles.csv"*/ + "\n");
                        // returnCrudDataListBenevoles.setBlnAlertErrorDataFileBenevole(crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, strFileName + "Benevoles.csv", null, true, true).getBlnAlertErrorDataFileBenevole());
                        returnCrudDataListBenevoles = crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempBenevole);
                    }

                    return returnCrudDataListBenevoles;
                }
                else {
                    logger.log(Level.WARNING, "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n");
                    System.out.println("");

                    Files.createDirectories(pathVaxTodoDataFolder);

                    // File DataFileBenevole = new File(strFileName + "Benevoles.csv");
                    ArrayList<Benevole> arrCheckIfBenevolesPresentInFile = crudDataListBenevoles(logger, EnumTypeCRUD.READ, null).getArrBenevoles();

                    if (Files.isRegularFile(Paths.get(strDataFileBenevole)) && arrCheckIfBenevolesPresentInFile != null && arrCheckIfBenevolesPresentInFile.size()>0) { //DataFileBenevole.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Benevoles.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Benevoles.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les comptes bénévoles!\n" + strDataFileBenevole /*+ "Benevoles.csv"*/ + "\n");
                        // returnCrudDataListBenevoles.setBlnAlertErrorDataFileBenevole(crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, strFileName + "Benevoles.csv", null, true, true).getBlnAlertErrorDataFileBenevole());
                        returnCrudDataListBenevoles = crudDataListBenevoles(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempBenevole);
                    }

                    // boolean blnAlertErrorDataFolder = true;
                    returnCrudDataListBenevoles.setBlnAlertErrorDataFolder(true);
                    return returnCrudDataListBenevoles;
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
            ReturnCrudDataListBenevoles returnCrudDataListBenevoles = new ReturnCrudDataListBenevoles();
            ArrayList<Benevole> arrBenevoles = new ArrayList<>();

            // System.out.println("Inside " + enumTypeCRUD);

            String tempFile = "";
            // File oldFile = null;
            File newFile = null;
            if (enumTypeCRUD == EnumTypeCRUD.READ || // ? will stay until fixed ?
                enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                tempFile = Paths.get(strDataFileBenevole).getParent().toString() + Config.strFileSeparator + "tempBenevoles.csv"; //Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
                
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
                    logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier temporaire des bénévoles '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
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

                if (blnInitDataFile && userInputBenevole == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    try {
                        // reader = new BufferedReader(new FileReader(new File(strFileName)));
                        bufferedReader = new BufferedReader(new FileReader(strDataFileBenevole));
                    }
                    catch (FileNotFoundException fnf) {
                        // System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());
            
                        //System.exit(0);
        
                        // System.out.println();
                        // logger.log(Level.SEVERE, "Fichier '" + strFileName + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);

                        Benevole defaultBenevole = new Benevole(Integer.parseInt("111111111"), "x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK", Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com", LocalDate.now().minusDays(1).format(Config.DATE_TIME_FORMATTER).toString());

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier '" + strDataFileBenevole + "' INTROUVABLE.\nCréation d'un compte bénévole par défaut qui a comme information: " + defaultBenevole + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListBenevoles(EnumTypeCRUD.UPDATE, strFileName, defaultBenevole, false, true);
                        
                        // if modifying benevole, add benevole to end of file
                        printWriter.println("#! intCodeIdentification, strMotDePasse; lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance");

                        if (blnInitTempBenevole)
                            printWriter.println(defaultBenevole.printInfosBenevole());

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

                            Files.deleteIfExists(Paths.get(strDataFileBenevole));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des bénévoles ['" + strDataFileBenevole + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des bénévoles '" + strDataFileBenevole + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileBenevole + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileBenevole);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileBenevole + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileBenevole + "'].\n");

                        // System.out.println("\n--->PATH OF OLD FILE:\n" + oldFile.getPath() + "\n");
                        // System.out.println("\n--->PATH OF DUMP FILE:\n" + dump.getPath() + "\n");
                        // System.out.println("\n--->PATH OF NEW FILE:\n" + newFile.getPath() + "\n");

                        //! boolean blnAlertErrorDataFileBenevole = true;
                        returnCrudDataListBenevoles.setBlnAlertErrorDataFileBenevole(true);
                        return returnCrudDataListBenevoles;
                    }
                }
                // else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                //     try {
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileBenevole));
                //     }
                //     catch (FileNotFoundException fnf) {
                //         // if updating accounts & data file doesnt exist, create a new data file & continue execution
                //         crudDataListBenevoles(logger, EnumTypeCRUD.CREATE, null);
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileBenevole));
                //     }
                // }
                else 
                    bufferedReader = new BufferedReader(new FileReader(strDataFileBenevole));

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
    
                        String[] strArrLogin, strArrInfos, arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");
                        // System.out.println(arrCurrentLigne.length + "\n" + Arrays.toString(arrCurrentLigne));
    
                        Benevole benevole;
                        // String strLogin, strInfos;
    
                        // System.out.println(arrCurrentLigne[0].split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*").length);
                        switch (arrCurrentLigne.length) {
                            case 1:
                                benevole = new Benevole();
    
                                // strLogin = arrCurrentLigne[0];
                                strArrLogin = arrCurrentLigne[0].split("\\s*" + Config.strCSVMainSeparator + "\\s*");
    
                                if(strArrLogin.length == 1) {
                                    int intCodeIdentification;
                                    try {
                                        intCodeIdentification = Integer.parseInt(strArrLogin[0]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            logger.log(Level.WARNING, "Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        intCodeIdentification = 0;
                                    }
                                    benevole.setIntCodeIdentification(intCodeIdentification);
                                    benevole.setStrMotDePasse("");
                                }
                                else if (strArrLogin.length == 2) {
                                    int intCodeIdentification;
                                    try {
                                        intCodeIdentification = Integer.parseInt(strArrLogin[0]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            logger.log(Level.WARNING, "Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        intCodeIdentification = 0;
                                    }
                                    benevole.setIntCodeIdentification(intCodeIdentification);
    
                                    String strMotDePasse;
                                    try {
                                        strMotDePasse = strArrLogin[1];
                                    } 
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strMotDePasse = "";
                                    }
                                    benevole.setStrMotDePasse(strMotDePasse);
                                }
    
                                if (enumTypeCRUD == EnumTypeCRUD.READ || (blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE)) {
                                    if (benevole.getIntCodeIdentification()>0 && benevole.getLngNoCompte()>0) 
                                        arrBenevoles.add(benevole); // check if benevole has code identification & no compte, then it is a valid benevole
                                }
                                else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                                    if (benevole.getIntCodeIdentification()>0 && benevole.getLngNoCompte()>0 && benevole.getLngNoCompte() == userInputBenevole.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrBenevoles.add(benevole);
                                        
                                        // System.out.println("Modification de ce compte dans le fichier");

                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileBenevole + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nModification de ce compte dans le fichier\n"));
                                    }
                                    else
                                        printWriter.println(strCurrentLine);
                                }
                                else if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                                    if (benevole.getIntCodeIdentification()>0 && benevole.getLngNoCompte()>0 && benevole.getLngNoCompte() == userInputBenevole.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrBenevoles.add(benevole);
    
                                        // System.out.println("Suppression de ce compte dans le fichier");

                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileBenevole + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nSuppression de ce compte dans le fichier\n"));
                                    }
                                    else 
                                        printWriter.println(strCurrentLine);
                                }

                                break;
                            
                            case 2:
                                benevole = new Benevole();
    
                                // strLogin = arrCurrentLigne[0];
                                // strInfos = arrCurrentLigne[1];
                                strArrLogin = arrCurrentLigne[0].split("\\s*" + Config.strCSVMainSeparator + "\\s*");
                                strArrInfos = arrCurrentLigne[1].split("\\s*" + Config.strCSVMainSeparator + "\\s*");
    
                                // read login
                                if(strArrLogin.length == 1) {
                                    int intCodeIdentification;
                                    try {
                                        intCodeIdentification = Integer.parseInt(strArrLogin[0]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            logger.log(Level.WARNING, "Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        intCodeIdentification = 0;
                                    }
                                    benevole.setIntCodeIdentification(intCodeIdentification);
                                    benevole.setStrMotDePasse("");
                                }
                                else if (strArrLogin.length == 2) {
                                    int intCodeIdentification;
                                    try {
                                        intCodeIdentification = Integer.parseInt(strArrLogin[0]);
                                    } 
                                    catch (NumberFormatException nfe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            logger.log(Level.WARNING, "Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber); //
                                        }
                                        intCodeIdentification = 0;
                                    }
                                    benevole.setIntCodeIdentification(intCodeIdentification);
    
                                    String strMotDePasse;
                                    try {
                                        strMotDePasse = strArrLogin[1];
                                    } 
                                    catch (IndexOutOfBoundsException ioobe) {
                                        if (blnShowVerbose) {
                                            // System.err.println("Error 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            logger.log(Level.WARNING, "Error 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber); //
                                        }
                                        strMotDePasse = "";
                                    }
                                    benevole.setStrMotDePasse(strMotDePasse);
                                }
                                
                                // read infos
                                if(strArrInfos.length > 0) {
                                    for(int i=0; i<strArrInfos.length; i++) {
                                        if (i==0) {
                                            long lngNoCompte;
                                            try {
                                                // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                                lngNoCompte = Long.parseLong(strArrInfos[i]);
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
                                            benevole.setLngNoCompte(lngNoCompte);
                                        }
                                        else if(i==1) {
                                            long lngNoTel;
                                            try {
                                                lngNoTel = Long.parseLong(strArrInfos[i]);
                                            } 
                                            catch (NumberFormatException nfe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                                    logger.log(Level.WARNING, "Error 'lngNoTel' Number Format at line " + intCurrentLineNumber); //
                                                }
                                                lngNoTel = 0;
                                            }
                                            benevole.setLngNoTel(lngNoTel);
                                        }
                                        else if(i==2) {
                                            String strNom;
                                            try {
                                                strNom = strArrInfos[i];
                                            } 
                                            catch (IndexOutOfBoundsException ioobe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                                    logger.log(Level.WARNING, "Error 'strNom' Index Out Of Bound at line " + intCurrentLineNumber); // 
                                                }
                                                strNom = "";
                                            }
                                            benevole.setStrNom(strNom);
                                        }
                                        else if(i==3) {
                                            String strPrenom;
                                            try {
                                                strPrenom = strArrInfos[i];
                                            }
                                            catch (IndexOutOfBoundsException ioobe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                                    logger.log(Level.WARNING, "Error 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber); //
                                                }
                                                strPrenom = "";
                                            }
                                            benevole.setStrPrenom(strPrenom);
                                        }
                                        else if(i==4) {
                                            String strAdresse;
                                            try {
                                                strAdresse = strArrInfos[i];
                                            }
                                            catch (IndexOutOfBoundsException ioobe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                                    logger.log(Level.WARNING, "Error 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber); //
                                                }
                                                strAdresse = "";
                                            }
                                            benevole.setStrAdresse(strAdresse);
                                        }
                                        else if(i==5) {
                                            String strCodePostal;
                                            try {
                                                strCodePostal = strArrInfos[i];
                                            }
                                            catch (IndexOutOfBoundsException ioobe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                                    logger.log(Level.WARNING, "Error 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber); //
                                                }
                                                strCodePostal = "";
                                            }
                                            benevole.setStrCodePostal(strCodePostal);
                                        }
                                        else if(i==6) {
                                            String strVille;
                                            try {
                                                strVille = strArrInfos[i];
                                            }
                                            catch (IndexOutOfBoundsException ioobe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                                    logger.log(Level.WARNING, "Error 'strVille' Index Out Of Bound at line " + intCurrentLineNumber); //
                                                }
                                                strVille = "";
                                            }
                                            benevole.setStrVille(strVille);
                                        }
                                        else if(i==7) {
                                            String strCourriel;
                                            try {
                                                strCourriel = strArrInfos[i];
                                            }
                                            catch (IndexOutOfBoundsException ioobe) {
                                                if (blnShowVerbose) {
                                                    // System.err.println("Error 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                                    logger.log(Level.WARNING, "Error 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber); //
                                                }
                                                strCourriel = "";
                                            }
                                            benevole.setStrCourriel(strCourriel);
                                        }
                                        else if (i==8) {
                                            String strDateNaissance;
                                            try {
                                                LocalDate localDate = LocalDate.parse(strArrInfos[i], Config.DATE_TIME_FORMATTER);
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
                                            benevole.setStrDateNaissance(strDateNaissance);
                                        }
                                    }
                                }

                                if (enumTypeCRUD == EnumTypeCRUD.READ || (blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE)) {
                                    if (benevole.getIntCodeIdentification()>0 && benevole.getLngNoCompte()>0)
                                        arrBenevoles.add(benevole); // check if benevole has code identification & no compte, then it is a valid benevole
                                }
                                else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                                    if (benevole.getIntCodeIdentification()>0 && benevole.getLngNoCompte()>0 && benevole.getLngNoCompte() == userInputBenevole.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrBenevoles.add(benevole);
                                        
                                        // System.out.println("Modification de ce compte dans le fichier");
                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileBenevole + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nModification de ce compte dans le fichier\n"));
                                    }
                                    else
                                        printWriter.println(strCurrentLine);
                                }
                                else if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                                    if (benevole.getIntCodeIdentification()>0 && benevole.getLngNoCompte()>0 && benevole.getLngNoCompte() == userInputBenevole.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrBenevoles.add(benevole);
    
                                        // System.out.println("Suppression de ce compte dans le fichier");
                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileBenevole + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nSuppression de ce compte dans le fichier\n"));
                                    }
                                    else 
                                        printWriter.println(strCurrentLine);
                                }

                                break;
                            
                            default:
                                if (blnShowVerbose) {
                                    // System.err.println("\nError: arrCurrentLigne.length is not 1 nor 2 | Actual Size of Array is '" + arrCurrentLigne.length + "' at CSV file line " + intCurrentLineNumber);
                                    System.out.println();
                                    logger.log(Level.SEVERE, "Error: arrCurrentLigne.length is not 1 (connexion info) nor 2 (connexion info & personal info) | Actual Size of Array is '" + arrCurrentLigne.length + "' at CSV file line " + intCurrentLineNumber);
                                }

                                break;
                        }
                    }
                    // else if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)) { 
                    //     // if updating or deleting Data from file, copy empty line & write into new temp file
                    //     printWriter.println(strCurrentLine);
                    // }
                }

                // Damn you Windows! The OS requires that the connection is closed before deleting or renaming files!
                bufferedReader.close();

                if (blnInitDataFile && userInputBenevole == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    if (arrBenevoles == null || arrBenevoles.size() <= 0) {
                        // System.out.println("\nEntering Inside Function\n");

                        Benevole defaultBenevole = new Benevole(Integer.parseInt("111111111"), "x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK", Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com", LocalDate.now().minusDays(1).format(Config.DATE_TIME_FORMATTER).toString());

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier des donnés Benevoles.csv VIDE (Nombre Total de Bénévoles dans le fichier = '" + arrBenevoles.size() + "')\nCréation d'un compte bénévole par défaut qui a comme information: " + defaultBenevole + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListBenevoles(EnumTypeCRUD.UPDATE, strFileName, defaultBenevole, false, true);
                        
                        // if modifying benevole, add benevole to end of file
                        printWriter.println("#! intCodeIdentification, strMotDePasse; lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance");

                        if (blnInitTempBenevole)
                            printWriter.println(defaultBenevole.printInfosBenevole());

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

                            Files.deleteIfExists(Paths.get(strDataFileBenevole));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des bénévoles ['" + strDataFileBenevole + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des bénévoles '" + strDataFileBenevole + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileBenevole + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileBenevole);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileBenevole + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileBenevole + "'].\n");

                        //! boolean blnAlertErrorDataFileBenevole = true;
                        returnCrudDataListBenevoles.setBlnAlertErrorDataFileBenevole(true);
                        return returnCrudDataListBenevoles;
                    }
                }
                else if (enumTypeCRUD == EnumTypeCRUD.READ) { // ? will stay until fixed ?)
                    // delete temp file that is not needed or used when reading Data from file
                    // newFile.delete();

                    printWriter.close();

                    try {
                        Files.deleteIfExists(Paths.get(tempFile));
                        logger.log(Level.INFO, "Mode Lecture: SUCCÈS pour avoir supprimer le fichier temporaire des bénévoles ['" + tempFile + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "Mode Lecture: IMPOSSIBLE de supprimer le fichier temporaire des bénévoles '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
                        System.out.println("");
                    }
                }
                else if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)) {
                    // if modifying benevole, add benevole to end of file
                    if (enumTypeCRUD == EnumTypeCRUD.UPDATE) 
                        printWriter.println(userInputBenevole.printInfosBenevole());

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

                        Files.deleteIfExists(Paths.get(strDataFileBenevole));
                        logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des bénévoles ['" + strDataFileBenevole + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des bénévoles '" + strDataFileBenevole + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileBenevole + "'\n", ioe);
                        System.out.println("");
                    }

                    File dump = new File(strDataFileBenevole);
                    boolean blnRenameNewFile = newFile.renameTo(dump);

                    if (!blnRenameNewFile)
                        logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileBenevole + "']!\n");
                    else 
                        logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileBenevole + "'].\n");

                    // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

                    if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                        // System.out.println("\nSuppression du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Suppression du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réussi!\n");
                    }
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                        // System.out.println("\nModification du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Modification du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réussi!\n");
                    }

                    if (arrBenevoles == null || arrBenevoles.size() <= 0) {
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            // System.out.println("LA LISTE DES COMPTES BÉNÉVOLES SUPPRIMÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES COMPTES BÉNÉVOLES SUPPRIMÉS EST VIDE!\n");
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            // System.out.println("LA LISTE DES COMPTES BÉNÉVOLES MODIFIÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES COMPTES BÉNÉVOLES MODIFIÉS EST VIDE!\n");
                        }
                    }
                    else {
                        String strStyleListe = "";
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            strStyleListe = "Liste des comptes bénévoles supprimés dans le fichier:";
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            strStyleListe = "Liste des comptes bénévoles modifiés dans le fichier:";
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
                        for (Benevole e : arrBenevoles)
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
                logger.log(Level.SEVERE, "Fichier '" + strDataFileBenevole + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);
            }
            catch (IOException io) {
                // System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + io.toString());
    
                //System.exit(0);

                System.out.println();
                logger.log(Level.SEVERE, "IOException pour le fichier '" + strDataFileBenevole + "'!\n", io);
            }

            // System.out.println("\nList Benevoles.csv");
            // for (Benevole e : arrBenevoles)
            //     System.out.println(e);
            // System.out.println("\n");
            System.out.println("");

            returnCrudDataListBenevoles.setArrBenevoles(arrBenevoles);
            return returnCrudDataListBenevoles;
        }

        return null;
    }
}
