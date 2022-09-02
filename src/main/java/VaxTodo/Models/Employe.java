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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import VaxTodo.Configs.Config;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/** Used for manipulating Employes objects and read, write to csv file and compare between objects
 * Is a sub class of Personne
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class Employe extends Personne {
    private final static String strDataFileEmploye = Config.strNewDataFileEmployes;

    private int intCodeIdentification;
    private String strMotDePasse; 
    // Le mot de passe devra être composé d'au moins 8 caractères contenant au moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial.

    public Employe() {
        this.intCodeIdentification = 0;
        this.strMotDePasse = "";
    }
    private Employe(int intCodeIdentification) {
        this.intCodeIdentification = intCodeIdentification;
        this.strMotDePasse = "";
        // this.strMotDePasse = AES_GCM_Athentication.encrypt("");
    }
    private Employe(String strMotDePasse) {
        this.intCodeIdentification = 0;
        this.strMotDePasse = strMotDePasse;
    }
    public Employe(int intCodeIdentification, String strMotDePasse) {
        this.intCodeIdentification = intCodeIdentification;
        this.strMotDePasse = strMotDePasse;
    }
    public Employe(int intCodeIdentification, String strMotDePasse, long lngNoCompte, long lngNoTel, String strNom, String strPrenom, String strAdresse, String strCodePostal, String strVille, String strCourriel) {
        this.intCodeIdentification = intCodeIdentification;
        this.strMotDePasse = strMotDePasse;

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

    public String getInfosConnexion() {
        return getIntCodeIdentification() + Config.strCSVMainSeparator + getStrMotDePasse();
    }
    public String getInfosPersonne() {
        return getLngNoCompte() + Config.strCSVMainSeparator + getLngNoTel() + Config.strCSVMainSeparator + getStrNom() + Config.strCSVMainSeparator + getStrPrenom() + Config.strCSVMainSeparator + getStrAdresse() + Config.strCSVMainSeparator + getStrCodePostal() + Config.strCSVMainSeparator + getStrVille() + Config.strCSVMainSeparator + getStrCourriel();
    }
    public String printInfosEmploye() {
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
            // "], " + super.toString() + "}";
            "], lngNoCompte='" + getLngNoCompte() + "'" +
            ", lngNoTel='" + getLngNoTel() + "'" +
            ", strNom='" + getStrNom() + "'" +
            ", strPrenom='" + getStrPrenom() + "'" +
            ", strAdresse='" + getStrAdresse() + "'" +
            ", strCodePostal='" + getStrCodePostal() + "'" +
            ", strVille='" + getStrVille() + "'" +
            ", strCourriel='" + getStrCourriel() + "'" +
            "}";
    }
    public boolean equalsExactly(Object object) throws Exception {
        if (object == null || !(object instanceof Employe))
            return false;

        // if (object == this) {
        //     System.out.println("\nInside Equals of Same Object THIS\n");
        //     return true;
        // }

        // System.out.println("\nInside Exact Equals of Object\n");
        Employe e = (Employe) object;
        
        return this.getIntCodeIdentification() == e.getIntCodeIdentification() &&
                // this.getStrMotDePasse().trim().equals(e.getStrMotDePasse().trim()) &&
                // AES_GCM_Athentication.decrypt(this.getStrMotDePasse()).trim().equals(AES_GCM_Athentication.decrypt(e.getStrMotDePasse()).trim()) &&
                this.getStrMotDePasseDecryption().equals(e.getStrMotDePasseDecryption()) &&
                this.getLngNoCompte() == e.getLngNoCompte() &&
                this.getLngNoTel() == e.getLngNoTel() &&
                this.getStrNom().trim().equals(e.getStrNom().trim()) &&
                this.getStrPrenom().trim().equals(e.getStrPrenom().trim()) &&
                this.getStrAdresse().trim().equals(e.getStrAdresse().trim()) &&
                this.getStrCodePostal().trim().equals(e.getStrCodePostal().trim()) &&
                this.getStrVille().trim().equals(e.getStrVille().trim()) && 
                this.getStrCourriel().trim().equals(e.getStrCourriel().trim());
    }
    public boolean equalsNoCompte(Object object) {
        if (object == null || !(object instanceof Employe))
            return false;

        // if (object == this)
        //     return true;

        // System.out.println("\nInside Equals No Compte of Object\n");
        return this.getLngNoCompte() == ((Employe) object).getLngNoCompte();
    }

    public static class ReturnCrudDataListEmployes {
        private ArrayList<Employe> arrEmployes;
        private boolean blnAlertErrorDataFolder, blnAlertErrorDataFileEmploye;

        public ReturnCrudDataListEmployes() {
            this.arrEmployes = new ArrayList<>();

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileEmploye = false;
        }
        public ReturnCrudDataListEmployes(ArrayList<Employe> arrEmployes) {
            this.arrEmployes = arrEmployes;

            this.blnAlertErrorDataFolder = false;
            this.blnAlertErrorDataFileEmploye = false;
        }
        public ReturnCrudDataListEmployes(boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileEmploye) {
            this.arrEmployes = new ArrayList<>();

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileEmploye = blnAlertErrorDataFileEmploye;
        } 
        public ReturnCrudDataListEmployes(ArrayList<Employe> arrEmployes, boolean blnAlertErrorDataFolder, boolean blnAlertErrorDataFileEmploye) {
            this.arrEmployes = arrEmployes;

            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
            this.blnAlertErrorDataFileEmploye = blnAlertErrorDataFileEmploye;
        }

        public ArrayList<Employe> getArrEmployes() {
            return this.arrEmployes;
        }
        public boolean getBlnAlertErrorDataFolder() {
            return this.blnAlertErrorDataFolder;
        }
        public boolean getBlnAlertErrorDataFileEmploye() {
            return this.blnAlertErrorDataFileEmploye;
        }

        public void setArrEmployes(ArrayList<Employe> arrEmployes) {
            this.arrEmployes = arrEmployes;
        }
        public void setBlnAlertErrorDataFolder(boolean blnAlertErrorDataFolder) {
            this.blnAlertErrorDataFolder = blnAlertErrorDataFolder;
        }
        public void setBlnAlertErrorDataFileEmploye(boolean blnAlertErrorDataFileEmploye) {
            this.blnAlertErrorDataFileEmploye = blnAlertErrorDataFileEmploye;
        }
    }
    public static ReturnCrudDataListEmployes crudDataListEmployes(Logger logger, EnumTypeCRUD enumTypeCRUD, Employe userInputEmploye, boolean ...blnArrOptional) {
        System.out.println();

        boolean blnShowVerbose = true, blnInitDataFile = false, blnInitTempEmploye = false;
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
                blnInitTempEmploye = blnArrOptional[2];
            }
        }

        // System.out.println("\n" + enumTypeCRUD + "\n");

        if (enumTypeCRUD == EnumTypeCRUD.CREATE) {
            Path pathVaxTodoDataFolder = Paths.get(strDataFileEmploye).getParent();

            try {
                // ArrayList<Employe> arrEmployes = new ArrayList<>();
                ReturnCrudDataListEmployes returnCrudDataListEmployes = new ReturnCrudDataListEmployes(); //! generates error if class is not static

                // check if Data folder exist
                if (Files.isDirectory(pathVaxTodoDataFolder)) {
                    logger.log(Level.INFO, "Dossier des données EXISTE.\nEmplacement du dossier des données: '" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n\n");

                    // File DataFileEmploye = new File(strFileName + "Employes.csv");
                    ArrayList<Employe> arrCheckIfEmployesPresentInFile = crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                    // // System.out.println("\n" + strFileName + "Employes.csv\n");
                    // System.out.println("\n");
                    // // for (Employe e : arrCheckIfEmployesPresentInFile)
                    // //     System.out.println(e);
                    // // System.out.println("\n\n");
                    // System.out.println("Is Regular File: " + Boolean.toString(Files.isRegularFile(Paths.get(strFileName + "Employes.csv"))));
                    // System.out.println("Array is Null: " + Boolean.toString(arrCheckIfEmployesPresentInFile == null));
                    // // System.out.println("Array is Empty: " + Boolean.toString(arrCheckIfEmployesPresentInFile.size()>0));
                    // System.out.println("\n");

                    if (Files.isRegularFile(Paths.get(strDataFileEmploye)) && arrCheckIfEmployesPresentInFile != null && arrCheckIfEmployesPresentInFile.size()>0) { //DataFileEmploye.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Employes.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Employes.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les comptes employés!\n" + strDataFileEmploye /*+ "Employes.csv"*/ + "\n");
                        // returnCrudDataListEmployes.setBlnAlertErrorDataFileEmploye(crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, strFileName + "Employes.csv", null, true, true).getBlnAlertErrorDataFileEmploye());
                        returnCrudDataListEmployes = crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempEmploye);
                    }

                    return returnCrudDataListEmployes;
                }
                else {
                    logger.log(Level.WARNING, "Emplacement du dossier des données INTROUVABLE.\nCréation d'un nouveau dossier des données à l'emplacement:\n'" + pathVaxTodoDataFolder.toAbsolutePath().toString() + "'\n");
                    System.out.println("");

                    Files.createDirectories(pathVaxTodoDataFolder);

                    // File DataFileEmploye = new File(strFileName + "Employes.csv");
                    ArrayList<Employe> arrCheckIfEmployesPresentInFile = crudDataListEmployes(logger, EnumTypeCRUD.READ, null).getArrEmployes();

                    if (Files.isRegularFile(Paths.get(strDataFileEmploye)) && arrCheckIfEmployesPresentInFile != null && arrCheckIfEmployesPresentInFile.size()>0) { //DataFileEmploye.length() > 1) {
                        logger.log(Level.INFO, "Fichier des donnés Employes.csv EXISTE.\n");
                    }
                    else {
                        logger.log(Level.WARNING, "Fichier des donnés Employes.csv INTROUVABLE ou VIDE!\nCréation d'un nouveau fichier de donnés pour les comptes employés!\n" + strDataFileEmploye /*+ "Employes.csv"*/ + "\n");
                        // returnCrudDataListEmployes.setBlnAlertErrorDataFileEmploye(crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, strFileName + "Employes.csv", null, true, true).getBlnAlertErrorDataFileEmploye());
                        returnCrudDataListEmployes = crudDataListEmployes(logger, EnumTypeCRUD.UPDATE, null, true, true, blnInitTempEmploye);
                    }

                    // boolean blnAlertErrorDataFolder = true;
                    returnCrudDataListEmployes.setBlnAlertErrorDataFolder(true);
                    return returnCrudDataListEmployes;
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
            ReturnCrudDataListEmployes returnCrudDataListEmployes = new ReturnCrudDataListEmployes();
            ArrayList<Employe> arrEmployes = new ArrayList<>();

            // System.out.println("Inside " + enumTypeCRUD);

            String tempFile = "";
            // File oldFile = null;
            File newFile = null;
            if (enumTypeCRUD == EnumTypeCRUD.READ || // ? will stay until fixed ?
                enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                tempFile = Paths.get(strDataFileEmploye).getParent().toString() + Config.strFileSeparator + "tempEmployes.csv"; //Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
                
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
                    logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier temporaire des employés '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
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

                if (blnInitDataFile && userInputEmploye == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    try {
                        // reader = new BufferedReader(new FileReader(new File(strFileName)));
                        bufferedReader = new BufferedReader(new FileReader(strDataFileEmploye));
                    }
                    catch (FileNotFoundException fnf) {
                        // System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());
            
                        //System.exit(0);
        
                        // System.out.println();
                        // logger.log(Level.SEVERE, "Fichier '" + strFileName + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);

                        Employe defaultEmploye = new Employe(Integer.parseInt("111111111"), "x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK", Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com");

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier '" + strDataFileEmploye + "' INTROUVABLE.\nCréation d'un compte employé par défaut qui a comme information: " + defaultEmploye + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListEmployes(EnumTypeCRUD.UPDATE, strFileName, defaultEmploye, false, true);
                        
                        // if modifying employe, add employe to end of file
                        printWriter.println("#! intCodeIdentification, strMotDePasse; lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel");

                        if (blnInitTempEmploye)
                            printWriter.println(defaultEmploye.printInfosEmploye());

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

                            Files.deleteIfExists(Paths.get(strDataFileEmploye));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des employés ['" + strDataFileEmploye + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des employés '" + strDataFileEmploye + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileEmploye + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileEmploye);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileEmploye + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileEmploye + "'].\n");

                        // System.out.println("\n--->PATH OF OLD FILE:\n" + oldFile.getPath() + "\n");
                        // System.out.println("\n--->PATH OF DUMP FILE:\n" + dump.getPath() + "\n");
                        // System.out.println("\n--->PATH OF NEW FILE:\n" + newFile.getPath() + "\n");

                        //! boolean blnAlertErrorDataFileEmploye = true;
                        returnCrudDataListEmployes.setBlnAlertErrorDataFileEmploye(true);
                        return returnCrudDataListEmployes;
                    }
                }
                // else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                //     try {
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileEmploye));
                //     }
                //     catch (FileNotFoundException fnf) {
                //         // if updating accounts & data file doesnt exist, create a new data file & continue execution
                //         crudDataListEmployes(logger, EnumTypeCRUD.CREATE, null);
                //         bufferedReader = new BufferedReader(new FileReader(strDataFileEmploye));
                //     }
                // }
                else 
                    bufferedReader = new BufferedReader(new FileReader(strDataFileEmploye));

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
    
                        Employe employe;
                        // String strLogin, strInfos;
    
                        // System.out.println(arrCurrentLigne[0].split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*").length);
                        switch (arrCurrentLigne.length) {
                            case 1:
                                employe = new Employe();
    
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
                                    employe.setIntCodeIdentification(intCodeIdentification);
                                    employe.setStrMotDePasse("");
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
                                    employe.setIntCodeIdentification(intCodeIdentification);
    
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
                                    employe.setStrMotDePasse(strMotDePasse);
                                }
    
                                if (enumTypeCRUD == EnumTypeCRUD.READ || (blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE)) {
                                    if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0) 
                                        arrEmployes.add(employe); // check if employe has code identification & no compte, then it is a valid employe
                                }
                                else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                                    if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0 && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrEmployes.add(employe);
                                        
                                        // System.out.println("Modification de ce compte dans le fichier");

                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileEmploye + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nModification de ce compte dans le fichier\n"));
                                    }
                                    else
                                        printWriter.println(strCurrentLine);
                                }
                                else if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                                    if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0 && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrEmployes.add(employe);
    
                                        // System.out.println("Suppression de ce compte dans le fichier");

                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileEmploye + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nSuppression de ce compte dans le fichier\n"));
                                    }
                                    else 
                                        printWriter.println(strCurrentLine);
                                }

                                break;
                            
                            case 2:
                                employe = new Employe();
    
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
                                    employe.setIntCodeIdentification(intCodeIdentification);
                                    employe.setStrMotDePasse("");
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
                                    employe.setIntCodeIdentification(intCodeIdentification);
    
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
                                    employe.setStrMotDePasse(strMotDePasse);
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
                                            employe.setLngNoCompte(lngNoCompte);
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
                                            employe.setLngNoTel(lngNoTel);
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
                                            employe.setStrNom(strNom);
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
                                            employe.setStrPrenom(strPrenom);
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
                                            employe.setStrAdresse(strAdresse);
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
                                            employe.setStrCodePostal(strCodePostal);
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
                                            employe.setStrVille(strVille);
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
                                            employe.setStrCourriel(strCourriel);
                                        }
                                    }
                                }

                                if (enumTypeCRUD == EnumTypeCRUD.READ || (blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE)) {
                                    if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0)
                                        arrEmployes.add(employe); // check if employe has code identification & no compte, then it is a valid employe
                                }
                                else if (!blnInitDataFile && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                                    if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0 && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrEmployes.add(employe);
                                        
                                        // System.out.println("Modification de ce compte dans le fichier");
                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileEmploye + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nModification de ce compte dans le fichier\n"));
                                    }
                                    else
                                        printWriter.println(strCurrentLine);
                                }
                                else if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                                    if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0 && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                        // System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                                        arrEmployes.add(employe);
    
                                        // System.out.println("Suppression de ce compte dans le fichier");
                                        System.out.println();
                                        logger.log(Level.INFO, ("Succès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strDataFileEmploye + "' à la ligne " + Integer.toString(intCurrentLineNumber) + "\nSuppression de ce compte dans le fichier\n"));
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

                if (blnInitDataFile && userInputEmploye == null && enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                    if (arrEmployes == null || arrEmployes.size() <= 0) {
                        // System.out.println("\nEntering Inside Function\n");

                        Employe defaultEmploye = new Employe(Integer.parseInt("111111111"), "x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK", Long.parseLong("111111111111"), Long.parseLong("1234567890"), "Test-Nom", "Test-Prenom", "123 Test-Adresse", "A0A0A0", "Test-Ville", "Test-Courriel@test.com");

                        System.out.println();
                        logger.log(Level.SEVERE, "Fichier des donnés Employes.csv VIDE (Nombre Total d'Employés dans le fichier = '" + arrEmployes.size() + "')\nCréation d'un compte employé par défaut qui a comme information: " + defaultEmploye + "\n");
                        // at CSV file line " + intCurrentLineNumber

                        // crudDataListEmployes(EnumTypeCRUD.UPDATE, strFileName, defaultEmploye, false, true);
                        
                        // if modifying employe, add employe to end of file
                        printWriter.println("#! intCodeIdentification, strMotDePasse; lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel");

                        if (blnInitTempEmploye)
                            printWriter.println(defaultEmploye.printInfosEmploye());

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

                            Files.deleteIfExists(Paths.get(strDataFileEmploye));
                            logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des employés ['" + strDataFileEmploye + "'].\n");
                        }
                        catch (IOException ioe) {
                            logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des employés '" + strDataFileEmploye + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileEmploye + "'\n", ioe);
                            System.out.println("");
                        }

                        File dump = new File(strDataFileEmploye);
                        boolean blnRenameNewFile = newFile.renameTo(dump);

                        if (!blnRenameNewFile)
                            logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileEmploye + "']!\n");
                        else 
                            logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileEmploye + "'].\n");

                        //! boolean blnAlertErrorDataFileEmploye = true;
                        returnCrudDataListEmployes.setBlnAlertErrorDataFileEmploye(true);
                        return returnCrudDataListEmployes;
                    }
                }
                else if (enumTypeCRUD == EnumTypeCRUD.READ) { // ? will stay until fixed ?)
                    // delete temp file that is not needed or used when reading Data from file
                    // newFile.delete();

                    printWriter.close();

                    try {
                        Files.deleteIfExists(Paths.get(tempFile));
                        logger.log(Level.INFO, "Mode Lecture: SUCCÈS pour avoir supprimer le fichier temporaire des employés ['" + tempFile + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "Mode Lecture: IMPOSSIBLE de supprimer le fichier temporaire des employés '" + tempFile + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + tempFile + "'\n", ioe);
                        System.out.println("");
                    }
                }
                else if (!blnInitDataFile && (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE)) {
                    // if modifying employe, add employe to end of file
                    if (enumTypeCRUD == EnumTypeCRUD.UPDATE) 
                        printWriter.println(userInputEmploye.printInfosEmploye());

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

                        Files.deleteIfExists(Paths.get(strDataFileEmploye));
                        logger.log(Level.INFO, "SUCCÈS pour avoir supprimer le fichier des employés ['" + strDataFileEmploye + "'].\n");
                    }
                    catch (IOException ioe) {
                        logger.log(Level.SEVERE, "IMPOSSIBLE de supprimer le fichier des employés '" + strDataFileEmploye + "'! " + Config.strRestartAppInstruction + "\nIO Exception pour le fichier: '" + strDataFileEmploye + "'\n", ioe);
                        System.out.println("");
                    }

                    File dump = new File(strDataFileEmploye);
                    boolean blnRenameNewFile = newFile.renameTo(dump);

                    if (!blnRenameNewFile)
                        logger.log(Level.SEVERE, "IMPOSSIBLE de renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileEmploye + "']!\n");
                    else 
                        logger.log(Level.INFO, "SUCCÈS pour avoir renommer le fichier temporaire ['" + tempFile + "'] vers ['" + strDataFileEmploye + "'].\n");

                    // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

                    if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                        // System.out.println("\nSuppression du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Suppression du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réussi!\n");
                    }
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                        // System.out.println("\nModification du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réussi!\n");
                        System.out.println();
                        logger.log(Level.INFO, "Modification du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réussi!\n");
                    }

                    if (arrEmployes == null || arrEmployes.size() <= 0) {
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            // System.out.println("LA LISTE DES COMPTES EMPLOYÉS SUPPRIMÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES COMPTES EMPLOYÉS SUPPRIMÉS EST VIDE!\n");
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            // System.out.println("LA LISTE DES COMPTES EMPLOYÉS MODIFIÉ EST VIDE!");
                            System.out.println();
                            logger.log(Level.WARNING, "LA LISTE DES COMPTES EMPLOYÉS MODIFIÉS EST VIDE!\n");
                        }
                    }
                    else {
                        String strStyleListe = "";
                        if (enumTypeCRUD == EnumTypeCRUD.DELETE) {
                            strStyleListe = "Liste des comptes employés supprimés dans le fichier:";
                        }
                        else if (enumTypeCRUD == EnumTypeCRUD.UPDATE) {
                            strStyleListe = "Liste des comptes employés modifiés dans le fichier:";
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
                        for (Employe e : arrEmployes)
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
                logger.log(Level.SEVERE, "Fichier '" + strDataFileEmploye + "' INTROUVABLE. Donnez le bon fichier comme paramètre!\n", fnf);
            }
            catch (IOException io) {
                // System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + io.toString());
    
                //System.exit(0);

                System.out.println();
                logger.log(Level.SEVERE, "IOException pour le fichier '" + strDataFileEmploye + "'!\n", io);
            }

            // System.out.println("\nList Employes.csv");
            // for (Employe e : arrEmployes)
            //     System.out.println(e);
            // System.out.println("\n");
            System.out.println("");

            returnCrudDataListEmployes.setArrEmployes(arrEmployes);
            return returnCrudDataListEmployes;
        }

        return null;
    }
}
