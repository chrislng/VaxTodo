package VaxTodo.Views.CommandLine;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import VaxTodo.Configs.Config;
import VaxTodo.Models.Employe;
import VaxTodo.Models.EnumTypePersonne;

/** Old Login
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class CliLogin {
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    public void run() {
        initData(Config.strResourceFolder);

        Scanner option = new Scanner(System.in);
        
        String strStyleAppName = "VaxToDo:", strStyleDashApp = "", strStyleMenuName = "Menu Principal", strStyleDash = "";
        for(int i=0; i<strStyleAppName.length(); i++)
            strStyleDashApp += "=";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenu = true;

            while(blnLoopMenu) {
                // System.out.println(Boolean.toString(option == null));

                // if (!option.hasNextLine())
                //     option = new Scanner(System.in);

                System.out.println("\n\n" + strStyleAppName);
                System.out.println(strStyleDashApp);
                System.out.println("\n" + strStyleMenuName);
                System.out.println(strStyleDash + "\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppHelp + "] Information sur l'application\n");
                System.out.println("[1] Connexion Employé");
                System.out.println("[2] Connexion Bénévole");
                System.out.print("\nChoisissez une option parmi la liste: ");

                // String optionUserChoice = "";
                // loopOptionHasNextLine: while(option.hasNextLine()) {
                //     optionUserChoice = option.nextLine();
                //     break loopOptionHasNextLine;
                // }
                
                switch(option.nextLine().trim()) {
                    // Menu Connexion Employe
                    case "1":
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        menuConnexionEmploye();
                        break;

                    // Menu Connexion Benevole
                    case "2":
                        System.out.println("=========================================");
                        // blnLoopMenu = false;
                        menuConnexionBenevole();
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        System.exit(0);
                        break;

                    case "?":
                        System.out.println("=========================================");
                        // option.close();
                        menuAide();
                        break;

                    default:
                        System.out.println("\nVeuillez entrer un menu valide!");
                        System.out.println("=========================================");
                        break;
                }
            }
        }
        finally {
            option.close();
        }

        // return false;
    }

    // ToDo implemente menuAide()
    // Appel le menu d'aide pour fournir des informations sur le systeme
    private void menuAide() {
        System.err.println("!!! Not yet implemented !!!\nMaybe next version ¯\\_(ツ)_/¯");
        System.err.println("=========================================");
    }

    // Appel le Menu Connexion Employe
    private void menuConnexionEmploye() {
        Scanner userInput = new Scanner(System.in);

        Console console;
        char[] charPasswd;

        String strStyleMenuName = "Menu Connexion Employé", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopError = true, blnShowPasswd = true;

            while(blnLoopError) {
                String strUsername = "", strPasswd = "";

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash + "\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Revenir au Menu Principal\n");
                System.out.println("[" + Config.strAppHidePassword + "] Cacher/Voir Mot de Passe\n");

                System.out.print("Code d'identification: ");
                strUsername = userInput.nextLine().trim();
                // loopInputUsername: while(userInput.hasNextLine()) {
                //     strUsername = userInput.nextLine();
                //     break loopInputUsername;
                // }

                switch(strUsername) {
                    case Config.strAppHidePassword:
                        if (blnShowPasswd) {
                            blnShowPasswd = false;
                            System.out.println("\nLe mot de passe est maintenant caché!");
                            System.out.println("=========================================");
                        }
                        else {
                            blnShowPasswd = true;
                            System.out.println("\nLe mot de passe est maintenant visible!");
                            System.out.println("=========================================");
                        }
                        continue;
                        // break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopError = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopError = false;
                        System.out.println("Retour au Menu Principal");
                        System.out.println("=======================================================================");
                        run();
                        break;
                        // break loopCheckUserCredentials;
                }

                // System.out.println();
                // System.out.print("Mot de passe: ");
                // strPasswd = userInput.nextLine();

                if (blnShowPasswd) {
                    System.out.print("Mot de passe: ");
                    strPasswd = userInput.nextLine().trim();
                }
                else {
                    // console.readPassword("[%s]", "Password:");
                    if ((console = System.console()) != null && (charPasswd = console.readPassword("%s", "Mot de passe: ")) != null) {
                        strPasswd = String.valueOf(charPasswd).trim();
                        Arrays.fill(charPasswd, ' ');
                    }
                }

                switch(strPasswd) {
                    case Config.strAppHidePassword:
                        if (blnShowPasswd) {
                            blnShowPasswd = false;
                            System.out.println("\nLe mot de passe est maintenant caché!");
                            System.out.println("=========================================");
                        }
                        else {
                            blnShowPasswd = true;
                            System.out.println("\nLe mot de passe est maintenant visible!");
                            System.out.println("=========================================");
                        }
                        continue;
                        // break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopError = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopError = false;
                        System.out.println("Retour au Menu Principal");
                        System.out.println("=======================================================================");
                        run();
                        break;
                        // break loopCheckUserCredentials;
                }

                // System.out.println("\n'" + strUsername + "'\n'" + strPasswd + "'\n");
                System.out.println();

                int intTempNoIdentification;
                try {
                    intTempNoIdentification = Integer.parseInt(strUsername);
                }
                catch (NumberFormatException nfe) {
                    System.out.println("\nError: Code d'identification n'est pas un nombre entier!\n" + nfe.toString() + "\n");
                    intTempNoIdentification = 0;
                }

                if ((strUsername == null || strUsername.trim().isEmpty()) || (strPasswd == null || strPasswd.trim().isEmpty()))
                    System.out.println("Aucun champs (code d'identification et/ou mot de passe) ne doit être vide");
                else if (intTempNoIdentification == 0)
                    System.out.println("Le code d'identification doit être un chiffre entier et d'une longueur exacte de 9 charactère!");
                // check if input not empty and user credentials exists
                else if (intTempNoIdentification>0 && checkCredentials(EnumTypePersonne.EMPLOYE, intTempNoIdentification, strPasswd)) { // checkCredentials(EnumPersonneType.EMPLOYE, strUsername, strPasswd)
                    // Call respective menu
                    blnLoopError = false;
                    System.out.println("=======================================================================");

                    new MenuEmploye().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.out.println("Les informations de connexion entrées pour l'utilisateur '" + strUsername + "' n'existent pas!\nVeuillez svp entrer les bonnes informations de connexion.");

                    System.err.println("\nLe code d'identification doit être un chiffre entier et d'une longueur exacte de 9 charactère!");
                    System.out.println("Le mot de passe doit être composé d'au moins 8 caractères contenant au moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial parmis [! ? - @ # $ % ^ & + =]");
                }

                System.out.println("=======================================================================");
            }
        }
        finally {
            userInput.close();
        }
    }

    // Appel le Menu Connexion Benevole
    private void menuConnexionBenevole() {
        System.err.println("!!! Not yet implemented !!!");
    }

    // validate user login credentials with csv data file
    private boolean checkCredentials(EnumTypePersonne enumUserType, String strUsername, String strPasswd) {
        // System.out.println("\n");
        // for(String[] s : readDataList(Miscellaneous.strDataFileEmployes, EnumPersonneType.EMPLOYE))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println("\n");
        // for(String[] s : readDataList(Miscellaneous.strDataFileBenevoles))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println("\n");
        // for(String[] s : readDataList(Miscellaneous.strDataFileVisiteurs))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println();

        switch(enumUserType) {
            case EMPLOYE:
                for(String[] s : readDataList(Config.strDataFileEmployes, EnumTypePersonne.EMPLOYE))
                    if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
                        System.out.println("Informations de l'employé '" + strUsername + "' sont valides");
                        return true;
                    }
                return false;

            // case BENEVOLE:
            //     for(String[] s : readDataList(Config.strDataFileBenevoles))
            //         if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
            //             System.out.println("Informations du bénévole '" + strUsername + "' sont valides");
            //             return true;
            //         }
            //     return false;

            // case VISITEUR:
            //     for(String[] s : readDataList(Config.strDataFileVisiteurs))
            //         if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
            //             System.out.println("Informations du visiteur '" + strUsername + "' sont valides");
            //             return true;
            //         }
            //     return false;

            default:
                System.err.println("Have to choose an already defined EnumType");
                return false;
        }

        // return false;
    }
    // validate user login credentials with csv data file
    private boolean checkCredentials(EnumTypePersonne enumUserType, int intCodeIdentification, String strPasswd) {
        // System.out.println("\n");
        // for(String[] s : readDataList(Miscellaneous.strDataFileEmployes, EnumPersonneType.EMPLOYE))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println("\n");
        // for(String[] s : readDataList(Miscellaneous.strDataFileBenevoles))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println("\n");
        // for(String[] s : readDataList(Miscellaneous.strDataFileVisiteurs))
        //     System.out.println(s.length + "\n" + Arrays.toString(s));
        // System.out.println();

        switch(enumUserType) {
            case EMPLOYE:
                for(Employe employe : readDataListEmployes(Config.strDataFileEmployes, false))
                    if (employe.getLngNoCompte()>0 && employe.getIntCodeIdentification()>0 &&
                        employe.getIntCodeIdentification() == intCodeIdentification && employe.getStrMotDePasse().equals(strPasswd)){
                        this.currentUserLngNoCompte = employe.getLngNoCompte();
                        this.currentUserIntCodeIdentification = employe.getIntCodeIdentification();

                        System.out.println("Informations de l'employé '" + Long.toString(this.currentUserLngNoCompte) + "' sont valides");
                        return true;
                    }
                return false;

            // case BENEVOLE:
            //     for(String[] s : readDataList(Config.strDataFileBenevoles))
            //         if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
            //             System.out.println("Informations du bénévole '" + strUsername + "' sont valides");
            //             return true;
            //         }
            //     return false;

            // case VISITEUR:
            //     for(String[] s : readDataList(Config.strDataFileVisiteurs))
            //         if (s.length==2 && s[0].equals(strUsername) && s[1].equals(strPasswd)) {
            //             System.out.println("Informations du visiteur '" + strUsername + "' sont valides");
            //             return true;
            //         }
            //     return false;

            default:
                System.err.println("\n!!! Have to choose an already defined EnumType !!!\n");
                return false;
        }
    }

    // returns array list of csv data file separated by comma ","
    private ArrayList<String[]> readDataList(String strFileName, EnumTypePersonne... enumUserType) {
        ArrayList<String[]> arrUserLoginCredentials = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(new File(strFileName)))) {
            // read & ignore first line since it contains comments on how to structure csv file
            reader.readLine();

            String strCurrentLine = null;
            while((strCurrentLine = reader.readLine()) != null) {
                // ignore empty lines in csv file
                if (strCurrentLine.trim().length() > 0) {
                    // System.out.println(Arrays.toString((strCurrentLine.trim().split("\\s*" + Miscellaneous.strCSVSecondarySeparator + "\\s*")[0]).split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")));
                    
                    // if reading Employes.csv, separate info of first half with ";" then "," to get login credentials
                    if(enumUserType != null && enumUserType.length > 0 && enumUserType[0] == EnumTypePersonne.EMPLOYE)
                        arrUserLoginCredentials.add((strCurrentLine.trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*")[0]).split("\\s*" + Config.strCSVMainSeparator + "\\s*"));
                    else
                        arrUserLoginCredentials.add(strCurrentLine.trim().split("\\s*" + Config.strCSVMainSeparator + "\\s*"));
                }
            }
        }
        catch (FileNotFoundException fnf) {
            System.err.println("\n\nFile '" + strFileName + "' not found\n\nError:\t" + fnf.toString());

            //System.exit(0);
        }
        catch (IOException ioe) {
            System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + ioe.toString());

            //System.exit(0);
        }

        return arrUserLoginCredentials;
    }

    private void initData(String strFileName) {
        try {
            Files.createDirectories(Paths.get(strFileName));
        }
        catch (IOException ioe) {
            System.err.println("\n\nIO Exception for file '" + strFileName + "'\n\nError:\t" + ioe.toString());
        }
    }

    // reads employes csv data file and returns an arraylist of them
    private ArrayList<Employe> readDataListEmployes(String strFileName, boolean blnShowVerbose) {
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
                                        System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
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
                                        System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);

                                String strMotDePasse;
                                try {
                                    strMotDePasse = strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[1];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                        System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
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
                                        System.err.println("Error 'intCodeIdentification' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intCodeIdentification = 0;
                                }
                                employe.setIntCodeIdentification(intCodeIdentification);

                                String strMotDePasse;
                                try {
                                    strMotDePasse = strLogin.split("\\s*" + Config.strCSVMainSeparator + "\\s*")[1];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error 'strMotDePasse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'lngNoCompte' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                            lngNoCompte = 0;
                                        }
                                        catch (IndexOutOfBoundsException ioobe) {
                                            if (blnShowVerbose)
                                                System.err.println("Error 'lngNoCompte' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
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
                                                System.err.println("Error 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
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
                                                System.err.println("Error 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                            strCourriel = "";
                                        }
                                        employe.setStrCourriel(strCourriel);
                                    }
                                }
                            }

                            if (employe.getIntCodeIdentification()>0 && employe.getLngNoCompte()>0)
                                arrEmployes.add(employe);
                            break;
                        
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
}
