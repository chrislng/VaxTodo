package VaxTodo.Views.CommandLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import VaxTodo.Configs.Config;
import VaxTodo.Models.Employe;
import VaxTodo.Models.EnumTypeCRUD;

/** Old Menu Gestion Employes to add, modify or delete Employes accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class MenuGestionEmployes {
    // private String strCurrentUsername;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    // Menu Gestion Employe
    public void run(long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;
        this.currentUserLngNoCompte = currentUserLngNoCompte;

        Scanner optionMenuGestionEmployes = new Scanner(System.in);

        String strStyleMenuName = "Menu Gestion des Employé", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionEmployes = true;

            while (blnLoopMenuGestionEmployes) {                                
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                System.out.println("[1] Lister tous les Employés");
                System.out.println("[2] Ajouter un Employé");
                System.out.println("[3] Modifier un Employé");
                System.out.println("[4] Supprimer un Employé");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuGestionEmployes.nextLine()) {
                    // Menu Lister Employe
                    case "1":
                        System.out.println("=========================================");
                        ArrayList<Employe> arrEmployes = readDataListEmployes(Config.strDataFileEmployes, true);
                        System.out.println("-----------------------------------------");
                                                
                        if (arrEmployes == null || arrEmployes.size() <= 0) {
                            System.out.println("\nLISTE DES EMPLOYÉS VIDES!");
                            System.out.println("=========================================");
                        }
                        else {
                            System.out.println("\nListe valide des employés\n");
                            for(int i=0; i<arrEmployes.size(); i++)
                                System.out.println("["+i+"] " + arrEmployes.get(i).toString() + "\n");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Ajouter Employe
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenuGestionEmployes = false;
                        menuAjouterEmploye();
                        break;

                    // Menu Modifier Employe
                    case "3":
                        System.out.println("=========================================");
                        blnLoopMenuGestionEmployes = false;
                        menuModifierEmploye();
                        break;

                    // Menu Supprimer Employe
                    case "4":
                        System.out.println("=========================================");
                        blnLoopMenuGestionEmployes = false;
                        menuSupprimerEmploye();
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionEmployes = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionEmployes = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionEmployes = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        new MenuEmploye().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    default:
                        System.out.println("Veuillez entrer un menu valide!");
                        System.out.println("=========================================");
                        break;
                }
            }
        } finally {
            optionMenuGestionEmployes.close();
        }
    }

    // reads employes csv data file and returns an arraylist of them
    public static ArrayList<Employe> readDataListEmployes(String strFileName, boolean blnShowVerbose) {
        ArrayList<Employe> arrEmployes = new ArrayList<>();

        int intCurrentLineNumber = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(strFileName)))) {
            // read & ignore first line since it contains comments on how to structure csv file
            reader.readLine();
            intCurrentLineNumber++;

            String strCurrentLine = null;
            while((strCurrentLine = reader.readLine()) != null) {
                intCurrentLineNumber++;
                
                // ignore empty lines in csv file
                if (strCurrentLine.trim().length() > 0) {
                    // (strCurrentLine.trim().split("\\s*" + Miscellaneous.strCSVSecondarySeparator + "\\s*")[0]).split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")

                    String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");
                    // System.out.println(arrCurrentLigne.length + "\n" + Arrays.toString(arrCurrentLigne));

                    Employe employe;
                    String strLogin, strInfos;

                    // System.out.println(arrCurrentLigne[0].split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*").length);
                    switch(arrCurrentLigne.length) {
                        case 1:
                            employe = new Employe();

                            strLogin = arrCurrentLigne[0];

                            if(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 1) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);
                                employe.setStrMotDePasse("");
                            }
                            else if (arrCurrentLigne[0].split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 2) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);

                                String strMotDePasse;
                                try {
                                    strMotDePasse = strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[1];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strMotDePasse = "";
                                }
                                employe.setStrMotDePasse(strMotDePasse);
                            }

                            // employe.setLngNoCompte(0);
                            // employe.setLngNoTel(0);
                            // employe.setStrNom("");
                            // employe.setStrPrenom("");
                            // employe.setStrAdresse("");
                            // employe.setStrCodePostal("");
                            // employe.setStrVille("");
                            // employe.setStrCourriel("");

                            if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0)
                                arrEmployes.add(employe);
                            break;
                        
                        case 2:
                            employe = new Employe();

                            strLogin = arrCurrentLigne[0];
                            strInfos = arrCurrentLigne[1];

                            if(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 1) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);
                                employe.setStrMotDePasse("");
                            }
                            else if (arrCurrentLigne[0].split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 2) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);

                                String strMotDePasse;
                                try {
                                    strMotDePasse = strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[1];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strMotDePasse = "";
                                }
                                employe.setStrMotDePasse(strMotDePasse);
                            }

                            // employe.setLngNoCompte(0);
                            // employe.setLngNoTel(0);
                            // employe.setStrNom("");
                            // employe.setStrPrenom("");
                            // employe.setStrAdresse("");
                            // employe.setStrCodePostal("");
                            // employe.setStrVille("");
                            // employe.setStrCourriel("");
                            if(strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length > 0) {
                                for(int i=0; i<strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length; i++) {
                                    if (i==0) {
                                        long lngNoCompte;
                                        try {
                                            // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                            lngNoCompte = Long.parseLong(strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i]);
                                        } 
                                        catch (NumberFormatException nfe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'lngNoCompte' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            lngNoCompte = 0;
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'lngNoCompte' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            lngNoCompte = 0;
                                        }
                                        employe.setLngNoCompte(lngNoCompte);
                                    }
                                    else if(i==1) {
                                        long lngNoTel;
                                        try {
                                            lngNoTel = Long.parseLong(strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i]);
                                        } 
                                        catch (NumberFormatException nfe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            lngNoTel = 0;
                                        }
                                        employe.setLngNoTel(lngNoTel);
                                    }
                                    else if(i==2) {
                                        String strNom;
                                        try {
                                            strNom = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        } 
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strNom = "";
                                        }
                                        employe.setStrNom(strNom);
                                    }
                                    else if(i==3) {
                                        String strPrenom;
                                        try {
                                            strPrenom = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strPrenom = "";
                                        }
                                        employe.setStrPrenom(strPrenom);
                                    }
                                    else if(i==4) {
                                        String strAdresse;
                                        try {
                                            strAdresse = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strAdresse = "";
                                        }
                                        employe.setStrAdresse(strAdresse);
                                    }
                                    else if(i==5) {
                                        String strCodePostal;
                                        try {
                                            strCodePostal = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strCodePostal = "";
                                        }
                                        employe.setStrCodePostal(strCodePostal);
                                    }
                                    else if(i==6) {
                                        String strVille;
                                        try {
                                            strVille = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strVille = "";
                                        }
                                        employe.setStrVille(strVille);
                                    }
                                    else if(i==7) {
                                        String strCourriel;
                                        try {
                                            strCourriel = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strCourriel = "";
                                        }
                                        employe.setStrCourriel(strCourriel);
                                    }
                                }
                            }

                            if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0)
                                arrEmployes.add(employe);

                            break; // part of the switch case
                        
                        default:
                            if (blnShowVerbose)
                                System.err.println("\nError: arrCurrentLigne.length is not 1 nor 2 | Actual Size of Array is '" + arrCurrentLigne.length + "' at CSV file line " + intCurrentLineNumber);
                            break;
                    }
                }
            }
        }
        catch (FileNotFoundException fnf) {
            System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());

            //System.exit(0);
        }
        catch (IOException io) {
            System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + io.toString());

            //System.exit(0);
        }

        System.out.println();

        return arrEmployes;
    }

    private void menuAjouterEmploye() {
        Scanner userInputAjouterEmployes = new Scanner(System.in);

        String strStyleMenuName = "Menu Ajouter un Employé", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir ajouter un nouveau employé qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        long lngNoCompte = 0, lngNoTel = 0;
        int intCodeIdentification = 0;
        String strMotDePasse = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        //"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String regexPasswordValidator = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[!?-@#$%^&+=])\\S{8,}\\z",
                regexPostalCodeValidator = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]?[0-9][A-Z][0-9]$";

        try {
            boolean blnLoopErrorMenuAjouterEmploye = true;

             while (blnLoopErrorMenuAjouterEmploye) {                                
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");

                if(lngNoCompte == 0)
                    lngNoCompte = ThreadLocalRandom.current().nextLong(Long.parseLong(Config.strNoCompteMinRnd), Long.parseLong(Config.strNoCompteMaxRnd)+1);
                // System.out.println(Long.toString(lngNoCompte));
                
                System.out.print("Numéro de compte (Assigné automatiquement): " + Long.toString(lngNoCompte) + "\n");

                if(intCodeIdentification == 0 || String.valueOf(intCodeIdentification).length() != 9) {
                    System.out.print("Code d'identification: ");
                    String strCodeIdentification = userInputAjouterEmployes.nextLine().trim();

                    switch(strCodeIdentification) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    try {
                        intCodeIdentification = Integer.parseInt(strCodeIdentification);

                        if(String.valueOf(intCodeIdentification).length() != 9) {
                            System.err.println("\nLe code d'identification doit être d'une longueur exacte de 9 charactère!\n");
                            System.out.println("=========================================");
                            continue;
                        }

                        // System.out.println("\nCode d'identification: " + String.valueOf(intCodeIdentification));
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe code d'identification doit être un chiffre entier et d'une longueur exacte de 9 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (intCodeIdentification > 0 && String.valueOf(intCodeIdentification).length() == 9)
                    System.out.println("Code d'identification: " + String.valueOf(intCodeIdentification));
                else {
                    System.out.println("=========================================");
                    continue;
                }
                
                if (strMotDePasse == null || strMotDePasse.trim().isEmpty() || strMotDePasse.length() < 8 || !Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) {
                    System.out.print("Mot de passe: ");
                    strMotDePasse = userInputAjouterEmployes.nextLine().trim();

                    switch(strMotDePasse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    // System.out.println("\n" + Boolean.toString(strMotDePasse.matches(regexPasswordValidator)));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).matches()));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) + "\n");
                    
                    if(strMotDePasse == null || strMotDePasse.trim().isEmpty() || strMotDePasse.length() < 8 || !Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) {
                        // System.out.println(!strMotDePasse.matches(regexPasswordValidator));
                        System.out.println("\nLe mot de passe doit être composé d'au moins 8 caractères contenant au moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial parmis [! ? - @ # $ % ^ & + =]");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (strMotDePasse.length() >= 8 && Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) 
                    System.out.println("Mot de passe: " + strMotDePasse);
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(lngNoTel == 0 || String.valueOf(lngNoTel).length() != 10) {
                    System.out.print("Numéro de Téléphone: ");
                    String strNoTel = userInputAjouterEmployes.nextLine().trim();

                    switch(strNoTel) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    try {
                        lngNoTel = Long.parseLong(strNoTel);

                        if(String.valueOf(lngNoTel).length() != 10) {
                            System.err.println("\nLe numéro de téléphone doit être d'une longueur exacte de 10 charactère!\n");
                            System.out.println("=========================================");
                            continue;
                        }
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe numéro de téléphone doit être un chiffre entier et d'une longueur exacte de 10 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (lngNoTel > 0 && String.valueOf(lngNoTel).length() == 10)
                    System.out.println("Numéro de Téléphone: " + String.valueOf(lngNoTel));
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                    System.out.print("Nom: ");
                    strNom = userInputAjouterEmployes.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                        System.out.println("\nLe nom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if(!strNom.trim().isEmpty() && strNom.trim().length() <= 50) 
                    System.out.println("Nom: " + strNom);
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strPrenom.trim().isEmpty() || strPrenom.trim().length() > 50) {
                    System.out.print("Prénom: ");
                    strPrenom = userInputAjouterEmployes.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if(strPrenom.trim().isEmpty() || strPrenom.trim().length() > 50) {
                        System.out.println("\nLe prénom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if(!strPrenom.trim().isEmpty() && strPrenom.trim().length() <= 50) 
                    System.out.println("Prénom: " + strPrenom);
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strAdresse.trim().isEmpty() || strAdresse.trim().length() > 100) {
                    System.out.print("Adresse: ");
                    strAdresse = userInputAjouterEmployes.nextLine().trim();

                    switch(strAdresse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if(strAdresse.trim().isEmpty() || strAdresse.trim().length() > 100) {
                        System.out.println("\nL'adresse ne doit pas être vide et ne pas dépasser 100 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if(!strAdresse.trim().isEmpty() && strAdresse.trim().length() <= 100) 
                    System.out.println("Adresse: " + strAdresse);
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strCodePostal.trim().isEmpty() || strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.print("Code Postal: ");
                    strCodePostal = userInputAjouterEmployes.nextLine().trim();

                    switch(strCodePostal) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if(strCodePostal.trim().isEmpty() || strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                        System.out.println("\nPour que le code postal soit valide:\n-Ne doit pas être vide (A1A1A1)\n-Lettres en majuscules\n-Ne pas avoir les lettres [D, F, I, O, Q, U]\n-Ne pas avoir les lettres [W, Z] en première position\n-Ne pas dépasser 6 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if(!strCodePostal.trim().isEmpty() && strCodePostal.trim().length() <= 6 && Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) 
                    System.out.println("Code Postal: " + strCodePostal);
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strVille.trim().isEmpty() || strVille.trim().length() > 50) {
                    System.out.print("Ville: ");
                    strVille = userInputAjouterEmployes.nextLine().trim();

                    switch(strVille) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if(strVille.trim().isEmpty() || strVille.trim().length() > 50) {
                        System.out.println("\nLa ville ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if(!strVille.trim().isEmpty() && strVille.trim().length() <= 50) 
                    System.out.println("Ville: " + strVille);
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                System.out.print("Adresse Courriel: ");
                strCourriel = userInputAjouterEmployes.nextLine().trim();

                Employe employe = new Employe(intCodeIdentification, strMotDePasse, lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel);


                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(employe);
                System.out.println("\n{Mot de passe: '" + employe.getStrMotDePasse() + "'}");

                System.out.print("\nConfirmer l'ajouter [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputAjouterEmployes.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuAjouterEmploye = false;
                        writeDataListEmployes(employe, Config.strDataFileEmployes);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.out.println("\nAjout du nouveau compte employé réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuAjouterEmploye = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    default:
                        System.out.println("Veuillez entrer une option valide!");
                        System.out.println("=========================================");
                        continue;
                }
            }
        } finally {
            userInputAjouterEmployes.close();
        }
    }

    // write & append employe object into employe csv data file
    private void writeDataListEmployes(Employe userInputEmploye, String strFileName) {
        try(FileWriter fw = new FileWriter(strFileName, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(userInputEmploye.printInfosEmploye());
            System.out.println("\nAjout du nouveau compte employé réussi!");
        } 
        catch (IOException ioe) {
            System.err.println("\nError: Ajout du nouveau compte employé réfusé par l'application!\n" + ioe.toString());
        }
    }

    // Menu Supprimer Employe
    private void menuSupprimerEmploye() {
        Scanner optionMenuSupprimerEmployes = new Scanner(System.in);

        String strStyleMenuName = "Menu Supprimer un Employé", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir supprimer l'employé qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            boolean blnLoopMenuSupprimerEmployes = true,
                    blnAutoSupprimer = false;

            while (blnLoopMenuSupprimerEmployes) {
                blnAutoSupprimer = false;

                // System.out.println("=========================================");
                ArrayList<Employe> arrEmployes = readDataListEmployes(Config.strDataFileEmployes, false);
                System.out.println("-----------------------------------------");

                if (arrEmployes == null || arrEmployes.size() <= 0) {
                    System.out.println("\nLISTE DES EMPLOYÉS VIDES!");
                    
                    blnLoopMenuSupprimerEmployes = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    // ne pas afficher le compte utilisateur courant pour ne pas s'auto-supprimer
                    loopRemoveCurrentUser: for(Employe e : arrEmployes)
                        if (e.getLngNoCompte() == this.currentUserLngNoCompte && e.getIntCodeIdentification() == this.currentUserIntCodeIdentification) {
                            arrEmployes.remove(e);
                            blnAutoSupprimer = true;
                            break loopRemoveCurrentUser;
                        }

                    System.out.println("\nListe valide des employés\n");
                    for(int i=0; i<arrEmployes.size(); i++)
                        System.out.println("["+i+"] " + arrEmployes.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }
                
                if (blnAutoSupprimer && (arrEmployes == null || arrEmployes.size() <= 0)) {
                    // System.out.println("\nLISTE DES EMPLOYÉS VIDES!");
                    System.out.println("VOUS NE POUVEZ PAS VOUS SUPPRIMER!\n");

                    blnLoopMenuSupprimerEmployes = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayEmployeIndex;

                System.out.print("\nChoisissez l'index de l'employé à supprimer parmi la liste: ");
                String strEmployeIndex = optionMenuSupprimerEmployes.nextLine().trim();

                switch(strEmployeIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuSupprimerEmployes = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuSupprimerEmployes = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuSupprimerEmployes = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayEmployeIndex = Integer.parseInt(strEmployeIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de l'employé doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayEmployeIndex = -1;
                    continue;
                }
                if (intArrayEmployeIndex >= 0 && intArrayEmployeIndex < arrEmployes.size()) {
                    Employe employe = arrEmployes.get(intArrayEmployeIndex);

                    System.out.println();
                    System.out.println(strStyleConfirmationDoubleDash + "\n");
                    System.out.println(strStyleConfirmation);
                    System.out.println(strStyleConfirmationDash + "\n");
                    System.out.println(employe);
    
                    System.out.print("\nConfirmer la suppression [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                    String strUserConfirmation = optionMenuSupprimerEmployes.nextLine().trim();

                    switch(strUserConfirmation) {
                        case "":
                        case Config.strAppAccept:
					    case Config.strAppAcceptAlternative:
                            blnLoopMenuSupprimerEmployes = false;
                            changeDataListEmployes(EnumTypeCRUD.DELETE, employe, Config.strDataFileEmployes, false);
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                        
                        case Config.strAppRefuse:
					    case Config.strAppRefuseAlternative:
                            blnLoopMenuSupprimerEmployes = false;
                            System.out.println("\nSuppression du compte employé réfusé!");
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
    
                        case Config.strAppExit:
                            System.out.println("Au Revoir!");
                            System.out.println("=========================================");
                            blnLoopMenuSupprimerEmployes = false;
                            System.exit(0);
                        break;
    
                        case Config.strAppDisconnect:
                            blnLoopMenuSupprimerEmployes = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;
    
                        case Config.strAppMenuBack:
                            blnLoopMenuSupprimerEmployes = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;

                        default:
                            System.out.println("Veuillez entrer une option valide!");
                            System.out.println("=========================================");
                            continue;
                    }
                }
                else {
                    System.err.println("\nErreur! L'index de l'employé doit se situer entre [0] et [" + (arrEmployes.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        } finally {
            optionMenuSupprimerEmployes.close();
        }
    }

    // removes employe from data csv file
    private void changeDataListEmployes(EnumTypeCRUD enumCRUD, Employe userInputEmploye, String strFileName, boolean blnShowVerbose) {
        String tempFile = Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
        File oldFile = new File(strFileName),
                newFile = new File(tempFile);

        // System.out.println("\n\noldFile: " + oldFile.getAbsolutePath() + " \nnewFile: " + newFile.getAbsolutePath() + "\n");

        ArrayList<Employe> arrEmployeChanged = new ArrayList<>();

        int intCurrentLineNumber = 0;
        
        // read oldFile, then write into newFile, then delete oldFile, then rename newFile to oldFile
        try (FileWriter fw = new FileWriter(tempFile, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            FileReader fr = new FileReader(strFileName);
            BufferedReader br = new BufferedReader(fr);

            String strCurrentLine = null;
            while((strCurrentLine = br.readLine()) != null) {
                intCurrentLineNumber++;

                // ignore empty lines in csv file
                if (strCurrentLine.trim().length() > 0) {
                    String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");

                    Employe employe;
                    String strLogin, strInfos;

                    // System.out.println("\n\narrCurrentLigne.length: " + Integer.toString(arrCurrentLigne.length) + "\n\n");
                    
                    switch(arrCurrentLigne.length) {
                        case 1:
                            employe = new Employe();

                            strLogin = arrCurrentLigne[0];

                            if(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 1) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);
                                employe.setStrMotDePasse("");
                            }
                            else if (arrCurrentLigne[0].split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 2) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);

                                String strMotDePasse;
                                try {
                                    strMotDePasse = strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[1];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strMotDePasse = "";
                                }
                                employe.setStrMotDePasse(strMotDePasse);
                            }

                            // employe.setLngNoCompte(0);
                            // employe.setLngNoTel(0);
                            // employe.setStrNom("");
                            // employe.setStrPrenom("");
                            // employe.setStrAdresse("");
                            // employe.setStrCodePostal("");
                            // employe.setStrVille("");
                            // employe.setStrCourriel("");

                            if (enumCRUD == EnumTypeCRUD.DELETE) {
                                // && employe.getLngNoCompte()>0 
                                if (employe.getIntCodeIdentification()>0 && employe.getIntCodeIdentification() == userInputEmploye.getIntCodeIdentification() && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                    System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                                    arrEmployeChanged.add(employe);

                                    System.out.println("Suppression de ce compte dans le fichier");
                                }
                                else 
                                    out.println(strCurrentLine);
                            }
                            else if (enumCRUD == EnumTypeCRUD.UPDATE) {
                                if (employe.getLngNoCompte()>0 && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                    System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                                    arrEmployeChanged.add(employe);
                                    
                                    System.out.println("Modification de ce compte dans le fichier");
                                }
                                else
                                    out.println(strCurrentLine);
                            }
                            break;
                        
                        case 2:
                            employe = new Employe();

                            strLogin = arrCurrentLigne[0];
                            strInfos = arrCurrentLigne[1];

                            if(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 1) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);
                                employe.setStrMotDePasse("");
                            }
                            else if (arrCurrentLigne[0].split("\\s*" + Config.strCSVMainSeparator + "\\s*").length == 2) {
                                int intCodeIdentification;
                                try {
                                    intCodeIdentification = Integer.parseInt(strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[0]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);

                                String strMotDePasse;
                                try {
                                    strMotDePasse = strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[1];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strMotDePasse = "";
                                }
                                employe.setStrMotDePasse(strMotDePasse);
                            }

                            // employe.setLngNoCompte(0);
                            // employe.setLngNoTel(0);
                            // employe.setStrNom("");
                            // employe.setStrPrenom("");
                            // employe.setStrAdresse("");
                            // employe.setStrCodePostal("");
                            // employe.setStrVille("");
                            // employe.setStrCourriel("");
                            if(strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length > 0) {
                                for(int i=0; i<strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*").length; i++) {
                                    if (i==0) {
                                        long lngNoCompte;
                                        try {
                                            // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                            lngNoCompte = Long.parseLong(strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i]);
                                        } 
                                        catch (NumberFormatException nfe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'lngNoCompte' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            lngNoCompte = 0;
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'lngNoCompte' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            lngNoCompte = 0;
                                        }
                                        employe.setLngNoCompte(lngNoCompte);
                                    }
                                    else if(i==1) {
                                        long lngNoTel;
                                        try {
                                            lngNoTel = Long.parseLong(strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i]);
                                        } 
                                        catch (NumberFormatException nfe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            lngNoTel = 0;
                                        }
                                        employe.setLngNoTel(lngNoTel);
                                    }
                                    else if(i==2) {
                                        String strNom;
                                        try {
                                            strNom = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        } 
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strNom = "";
                                        }
                                        employe.setStrNom(strNom);
                                    }
                                    else if(i==3) {
                                        String strPrenom;
                                        try {
                                            strPrenom = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strPrenom = "";
                                        }
                                        employe.setStrPrenom(strPrenom);
                                    }
                                    else if(i==4) {
                                        String strAdresse;
                                        try {
                                            strAdresse = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strAdresse = "";
                                        }
                                        employe.setStrAdresse(strAdresse);
                                    }
                                    else if(i==5) {
                                        String strCodePostal;
                                        try {
                                            strCodePostal = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strCodePostal = "";
                                        }
                                        employe.setStrCodePostal(strCodePostal);
                                    }
                                    else if(i==6) {
                                        String strVille;
                                        try {
                                            strVille = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strVille = "";
                                        }
                                        employe.setStrVille(strVille);
                                    }
                                    else if(i==7) {
                                        String strCourriel;
                                        try {
                                            strCourriel = strInfos.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[i];
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error: 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strCourriel = "";
                                        }
                                        employe.setStrCourriel(strCourriel);
                                    }
                                }
                            }

                            if (enumCRUD == EnumTypeCRUD.DELETE) {
                                if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0 && 
                                    (employe.getIntCodeIdentification() == userInputEmploye.getIntCodeIdentification() && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte())) {
                                    System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                                    arrEmployeChanged.add(employe);

                                    System.out.println("Suppression de ce compte dans le fichier");
                                }
                                else 
                                    out.println(strCurrentLine);
                            }
                            else if (enumCRUD == EnumTypeCRUD.UPDATE) {
                                if (employe.getLngNoCompte()>0 && employe.getLngNoCompte() == userInputEmploye.getLngNoCompte()) {
                                    System.out.println("\nSuccès: L'utilisateur '" + Long.toString(employe.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                                    arrEmployeChanged.add(employe);
                                    
                                    System.out.println("Modification de ce compte dans le fichier");
                                }
                                else
                                    out.println(strCurrentLine);
                            }
                            break;
                        
                        default:
                            if (blnShowVerbose)
                                System.err.println("\nError: arrCurrentLigne.length is not 1 nor 2 | Actual Size of Array is '" + arrCurrentLigne.length + "' at CSV file line " + intCurrentLineNumber);
                            out.println(strCurrentLine);
                            break;
                    }
                }
                else { // copy empty line & write into new temp file
                    out.println(strCurrentLine);
                }
            }

            // if modifying employe, add employe to end of file
            if (enumCRUD == EnumTypeCRUD.UPDATE)
                out.println(userInputEmploye.printInfosEmploye());

            // out.flush();
            oldFile.delete();
            File dump = new File(strFileName);
            newFile.renameTo(dump);

            // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

            if (enumCRUD == EnumTypeCRUD.DELETE)
                System.out.println("\nSuppression du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réussi!\n");
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.out.println("\nModification du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réussi!\n");

            if (arrEmployeChanged == null || arrEmployeChanged.size() <= 0) {
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    System.out.println("LA LISTE DES COMPTES EMPLOYÉS SUPPRIMER EST VIDE!");
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    System.out.println("LA LISTE DES COMPTES EMPLOYÉS MODIFIER EST VIDE!");
            }
            else {
                String strStyleListe = "";
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    strStyleListe = "Liste des comptes employés supprimer du fichier:";
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    strStyleListe = "Liste des comptes employés modifier du fichier:";

                String strStyleListeDash = "", strStyleListeDoubleDash = "";
                for(int j=0; j<strStyleListe.length(); j++) {
                    strStyleListeDash += "-";
                    strStyleListeDoubleDash += "=";
                }
                
                System.out.println(strStyleListeDoubleDash + "\n");
                System.out.println(strStyleListe);
                System.out.println(strStyleListeDash + "\n");
                for(Employe e : arrEmployeChanged)
                    System.out.println(e + "\n");
                // System.out.println();
            }
        }
        catch (FileNotFoundException fnf) {
            System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());

            //System.exit(0);
        }
        catch (IOException ioe) {
            if (enumCRUD == EnumTypeCRUD.DELETE)
                System.err.println("\nError: Suppression du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.err.println("\nError: Modification du compte employé '" + Long.toString(userInputEmploye.getLngNoCompte()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
        }
    }

    private void menuModifierEmploye() {
        Scanner optionMenuModifierEmployes = new Scanner(System.in);

        String strStyleMenuName = "Menu Modifier un Employé", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuModifierEmployes = true;

            while(blnLoopMenuModifierEmployes) {
                ArrayList<Employe> arrEmployes = readDataListEmployes(Config.strDataFileEmployes, false);
                System.out.println("-----------------------------------------");

                if (arrEmployes == null || arrEmployes.size() <= 0) {
                    System.out.println("\nLISTE DES EMPLOYÉS VIDES!");
                    
                    blnLoopMenuModifierEmployes = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.out.println("\nListe valide des employés\n");
                    for(int i=0; i<arrEmployes.size(); i++)
                        System.out.println("["+i+"] " + arrEmployes.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayEmployeIndex;

                System.out.print("\nChoisissez l'index de l'employé à modifier parmi la liste: ");
                String strEmployeIndex = optionMenuModifierEmployes.nextLine().trim();

                switch(strEmployeIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierEmployes = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierEmployes = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierEmployes = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayEmployeIndex = Integer.parseInt(strEmployeIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de l'employé doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayEmployeIndex = -1;
                    continue;
                }

                if (intArrayEmployeIndex >= 0 && intArrayEmployeIndex < arrEmployes.size()) {
                    Employe employe = arrEmployes.get(intArrayEmployeIndex);

                    menuModifierDetailsEmploye(employe);
                }
                else {
                    System.err.println("\nErreur! L'index de l'employé doit se situer entre [0] et [" + (arrEmployes.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierEmployes.close();
        }
    }

    private void menuModifierDetailsEmploye(Employe employe) {
        boolean blnLoopErrorMenuModifierDetailsEmploye = true;
        Scanner userInputModifierEmployes = new Scanner(System.in);

        // long lngNoCompte = 0;
        long lngNoTel = 0;
        int intCodeIdentification = 0;
        String strMotDePasse = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        //"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String regexPasswordValidator = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[!?-@#$%^&+=])\\S{8,}\\z",
                regexPostalCodeValidator = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]?[0-9][A-Z][0-9]$";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir modifier l'employé qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            System.out.println("=========================================\n");
            while(blnLoopErrorMenuModifierDetailsEmploye) {
                System.out.println("Modification du compte employé:\n");
                System.out.println(employe + "\n\n");

                System.out.print("Numéro de compte (Non Modifiable): " + Long.toString(employe.getLngNoCompte()) + "\n");

                if(intCodeIdentification == 0 || String.valueOf(intCodeIdentification).length() != 9) {
                    System.out.print("Code d'identification [" + Integer.toString(employe.getIntCodeIdentification()) + "]: ");
                    String strCodeIdentification = userInputModifierEmployes.nextLine().trim();

                    switch(strCodeIdentification) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    try {
                        if(strCodeIdentification.trim().isEmpty())
                            intCodeIdentification = employe.getIntCodeIdentification();
                        else
                            intCodeIdentification = Integer.parseInt(strCodeIdentification);

                        if(String.valueOf(intCodeIdentification).length() != 9) {
                            System.err.println("\nLe code d'identification doit être d'une longueur exacte de 9 charactère!\n");
                            System.out.println("=========================================");
                            continue;
                        }

                        employe.setIntCodeIdentification(intCodeIdentification);

                        // System.out.println("\nCode d'identification: " + String.valueOf(intCodeIdentification));
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe code d'identification doit être un chiffre entier et d'une longueur exacte de 9 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (intCodeIdentification > 0 && String.valueOf(intCodeIdentification).length() == 9) {
                    System.out.println("Code d'identification: " + String.valueOf(intCodeIdentification));
                    employe.setIntCodeIdentification(intCodeIdentification);
                }
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if (strMotDePasse == null || strMotDePasse.trim().isEmpty() || strMotDePasse.length() < 8 || !Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) {
                    System.out.print("Mot de passe [" + employe.getStrMotDePasse() + "]: ");
                    strMotDePasse = userInputModifierEmployes.nextLine().trim();

                    switch(strMotDePasse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    // System.out.println("\n" + Boolean.toString(strMotDePasse.matches(regexPasswordValidator)));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).matches()));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) + "\n");
                    
                    if (strMotDePasse.trim().isEmpty())
                        strMotDePasse = employe.getStrMotDePasse();
                    else if(strMotDePasse.length() < 8 || !Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) {
                        // System.out.println(!strMotDePasse.matches(regexPasswordValidator));
                        System.out.println("\nLe mot de passe doit être composé d'au moins 8 caractères contenant au moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial parmis [! ? - @ # $ % ^ & + =]");
                        System.out.println("=========================================");
                        continue;
                    }

                    employe.setStrMotDePasse(strMotDePasse);
                }
                else if (strMotDePasse.length() >= 8 && Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).find()) {
                    System.out.println("Mot de passe: " + strMotDePasse);
                    employe.setStrMotDePasse(strMotDePasse);
                }
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(lngNoTel == 0 || String.valueOf(lngNoTel).length() != 10) {
                    System.out.print("Numéro de Téléphone [" + Long.toString(employe.getLngNoTel()) + "]: ");
                    String strNoTel = userInputModifierEmployes.nextLine().trim();

                    switch(strNoTel) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    try {
                        if (strNoTel.trim().isEmpty())
                            lngNoTel = employe.getLngNoTel();
                        else
                            lngNoTel = Long.parseLong(strNoTel);

                        if(String.valueOf(lngNoTel).length() != 10) {
                            System.err.println("\nLe numéro de téléphone doit être d'une longueur exacte de 10 charactère!\n");
                            System.out.println("=========================================");
                            continue;
                        }

                        employe.setLngNoTel(lngNoTel);
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe numéro de téléphone doit être un chiffre entier et d'une longueur exacte de 10 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (lngNoTel > 0 && String.valueOf(lngNoTel).length() == 10) {
                    System.out.println("Numéro de Téléphone: " + String.valueOf(lngNoTel));
                    employe.setLngNoTel(lngNoTel);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                    System.out.print("Nom [" + employe.getStrNom() + "]: ");
                    strNom = userInputModifierEmployes.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strNom.trim().isEmpty())
                        strNom = employe.getStrNom();
                    else if(strNom.trim().length() > 50) {
                        System.out.println("\nLe nom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    employe.setStrNom(strNom);
                }
                else if(!strNom.trim().isEmpty() && strNom.trim().length() <= 50) {
                    System.out.println("Nom: " + strNom);
                    employe.setStrNom(strNom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strPrenom.trim().isEmpty() || strPrenom.trim().length() > 50) {
                    System.out.print("Prénom [" + employe.getStrPrenom() + "]: ");
                    strPrenom = userInputModifierEmployes.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strPrenom.trim().isEmpty())
                        strPrenom = employe.getStrPrenom();
                    else if(strPrenom.trim().length() > 50) {
                        System.out.println("\nLe prénom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    employe.setStrPrenom(strPrenom);
                }
                else if(!strPrenom.trim().isEmpty() && strPrenom.trim().length() <= 50) {
                    System.out.println("Prénom: " + strPrenom);
                    employe.setStrPrenom(strPrenom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strAdresse.trim().isEmpty() || strAdresse.trim().length() > 100) {
                    System.out.print("Adresse [" + employe.getStrAdresse() + "]: ");
                    strAdresse = userInputModifierEmployes.nextLine().trim();

                    switch(strAdresse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strAdresse.trim().isEmpty())
                        strAdresse = employe.getStrAdresse();
                    else if(strAdresse.trim().length() > 100) {
                        System.out.println("\nL'adresse ne doit pas être vide et ne pas dépasser 100 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    employe.setStrAdresse(strAdresse);
                }
                else if(!strAdresse.trim().isEmpty() && strAdresse.trim().length() <= 100) {
                    System.out.println("Adresse: " + strAdresse);
                    employe.setStrAdresse(strAdresse);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strCodePostal.trim().isEmpty() || strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.print("Code Postal [" + employe.getStrCodePostal() + "]: ");
                    strCodePostal = userInputModifierEmployes.nextLine().trim();

                    switch(strCodePostal) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strCodePostal.trim().isEmpty())
                        strCodePostal = employe.getStrCodePostal();
                    else if(strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                        System.out.println("\nPour que le code postal soit valide:\n-Ne doit pas être vide (A1A1A1)\n-Lettres en majuscules\n-Ne pas avoir les lettres [D, F, I, O, Q, U]\n-Ne pas avoir les lettres [W, Z] en première position\n-Ne pas dépasser 6 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    employe.setStrCodePostal(strCodePostal);
                }
                else if(!strCodePostal.trim().isEmpty() && strCodePostal.trim().length() <= 6 && Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.println("Code Postal: " + strCodePostal);
                    employe.setStrCodePostal(strCodePostal);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strVille.trim().isEmpty() || strVille.trim().length() > 50) {
                    System.out.print("Ville [" + employe.getStrVille() + "]: ");
                    strVille = userInputModifierEmployes.nextLine().trim();

                    switch(strVille) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsEmploye = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strVille.trim().isEmpty())
                        strVille = employe.getStrVille();
                    else if(strVille.trim().length() > 50) {
                        System.out.println("\nLa ville ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    employe.setStrVille(strVille);
                }
                else if(!strVille.trim().isEmpty() && strVille.trim().length() <= 50) {
                    System.out.println("Ville: " + strVille);
                    employe.setStrVille(strVille);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if (strCourriel.trim().isEmpty()) {
                    System.out.print("Adresse Courriel [" + employe.getStrCourriel() + "]: ");
                    strCourriel = userInputModifierEmployes.nextLine().trim();

                    if (strCourriel.trim().isEmpty())
                        strCourriel = employe.getStrCourriel();
                }
                employe.setStrCourriel(strCourriel);

                // if (employe == null)
                //     continue;

                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(employe);
                System.out.println("\n{Mot de passe: '" + employe.getStrMotDePasse() + "'}");

                System.out.print("\nConfirmer la modification [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputModifierEmployes.nextLine().trim(); //optionMenuModifierEmployes.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        changeDataListEmployes(EnumTypeCRUD.UPDATE, employe, Config.strDataFileEmployes, false);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.out.println("\nModification du compte employé réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuModifierDetailsEmploye = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    default:
                        System.out.println("Veuillez entrer une option valide!");
                        System.out.println("=========================================");
                        continue;
                }
            }
        }
        finally {
            userInputModifierEmployes.close();
        }
    }
}
