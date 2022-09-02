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
import VaxTodo.Models.Benevole_OLD_OLD;
import VaxTodo.Models.EnumTypeCRUD;

/** Old Menu Gestion Benevoles to add, modify or delete Benevoles accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class MenuGestionBenevoles {
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    // Menu Gestion Benevole
    public void run(long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;
        this.currentUserLngNoCompte = currentUserLngNoCompte;

        Scanner optionMenuGestionBenevoles = new Scanner(System.in);

        String strStyleMenuName = "Menu Gestion des Bénévoles", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionBenevoles = true;
            while (blnLoopMenuGestionBenevoles) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                System.out.println("[1] Lister tous les Bénévoles");
                System.out.println("[2] Ajouter un Bénévole");
                System.out.println("[3] Modifier un Bénévole");
                System.out.println("[4] Supprimer un Bénévole");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuGestionBenevoles.nextLine()) {
                    // Menu Lister Benevole
                    case "1":
                        System.out.println("=========================================");
                        ArrayList<Benevole_OLD_OLD> arrBenevoles = readDataListBenevoles(Config.strDataFileBenevoles, true);
                        System.out.println("-----------------------------------------");
                                                
                        if (arrBenevoles == null || arrBenevoles.size() <= 0) {
                            System.out.println("\nLISTE DES BÉNÉVOLES VIDES!");
                            System.out.println("=========================================");
                        }
                        else {
                            System.out.println("\nListe valide des bénévoles\n");
                            for(int i=0; i<arrBenevoles.size(); i++)
                                System.out.println("["+i+"] " + arrBenevoles.get(i).toString() + "\n");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Ajouter Benevole
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenuGestionBenevoles = false;
                        menuAjouterBenevole();
                        break;

                    // Menu Modifier Benevole
                    case "3":
                        System.out.println("=========================================");
                        blnLoopMenuGestionBenevoles = false;
                        menuModifierBenevole();
                        break;

                    // Menu Supprimer Benevole
                    case "4":
                        System.out.println("=========================================");
                        blnLoopMenuGestionBenevoles = false;
                        menuSupprimerBenevole();
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionBenevoles = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionBenevoles = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionBenevoles = false;
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
        }
        finally {
            optionMenuGestionBenevoles.close();
        }
    }

    // reads benevoles csv data file and returns an arraylist of them
    private ArrayList<Benevole_OLD_OLD> readDataListBenevoles(String strFileName, boolean blnShowVerbose) {
        ArrayList<Benevole_OLD_OLD> arrBenevoles = new ArrayList<>();

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

                    // String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");

                    Benevole_OLD_OLD benevole = new Benevole_OLD_OLD();

                    String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVMainSeparator + "\\s*");

                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            if(i==0) {
                                String strDateNaissance;
                                try {
                                    strDateNaissance = arrCurrentLigne[i];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strDateNaissance' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strDateNaissance = "";
                                }
                                benevole.setStrDateNaissance(strDateNaissance);
                            }
                            else if (i==1) {
                                long lngNoCompte;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    lngNoCompte = Long.parseLong(arrCurrentLigne[i]);
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
                                benevole.setLngNoCompte(lngNoCompte);
                            }
                            else if(i==2) {
                                long lngNoTel;
                                try {
                                    lngNoTel = Long.parseLong(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    lngNoTel = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'lngNoTel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    lngNoTel = 0;
                                }
                                benevole.setLngNoTel(lngNoTel);
                            }
                            else if(i==3) {
                                String strNom;
                                try {
                                    strNom = arrCurrentLigne[i];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strNom = "";
                                }
                                benevole.setStrNom(strNom);
                            }
                            else if(i==4) {
                                String strPrenom;
                                try {
                                    strPrenom = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strPrenom = "";
                                }
                                benevole.setStrPrenom(strPrenom);
                            }
                            else if(i==5) {
                                String strAdresse;
                                try {
                                    strAdresse = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strAdresse = "";
                                }
                                benevole.setStrAdresse(strAdresse);
                            }
                            else if(i==6) {
                                String strCodePostal;
                                try {
                                    strCodePostal = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strCodePostal = "";
                                }
                                benevole.setStrCodePostal(strCodePostal);
                            }
                            else if(i==7) {
                                String strVille;
                                try {
                                    strVille = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strVille = "";
                                }
                                benevole.setStrVille(strVille);
                            }
                            else if(i==8) {
                                String strCourriel;
                                try {
                                    strCourriel = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strCourriel = "";
                                }
                                benevole.setStrCourriel(strCourriel);
                            }
                        }
                    }

                    if (benevole.getLngNoCompte()>0)
                        arrBenevoles.add(benevole);
                    // break;
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

        return arrBenevoles;
    }

    private void menuAjouterBenevole() {
        Scanner userInputAjouterBenevoles = new Scanner(System.in);

        String strStyleMenuName = "Menu Ajouter un Bénévole", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir ajouter un nouveau bénévole qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        long lngNoCompte = 0, lngNoTel = 0;
        String strDateNaissance = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        String regexPostalCodeValidator = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]?[0-9][A-Z][0-9]$";

        try {
            boolean blnLoopErrorMenuAjouterBenevole = true;

             while (blnLoopErrorMenuAjouterBenevole) {                                
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
                
                if (strDateNaissance == null || strDateNaissance.trim().isEmpty()) {
                    System.out.print("Date disponible: ");
                    strDateNaissance = userInputAjouterBenevoles.nextLine().trim();

                    switch(strDateNaissance) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    // System.out.println("\n" + Boolean.toString(strDateNaissance.matches(regexPasswordValidator)));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strDateNaissance).matches()));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strDateNaissance).find()) + "\n");
                    
                    if(strDateNaissance == null || strDateNaissance.trim().isEmpty()) {
                        // System.out.println(!strDateNaissance.matches(regexPasswordValidator));
                        System.out.println("\nLa date disponible ne doit pas être vide!");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (strDateNaissance != null && !strDateNaissance.trim().isEmpty()) 
                    System.out.println("Date disponible: " + strDateNaissance);
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(lngNoTel == 0 || String.valueOf(lngNoTel).length() != 10) {
                    System.out.print("Numéro de Téléphone: ");
                    String strNoTel = userInputAjouterBenevoles.nextLine().trim();

                    switch(strNoTel) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
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
                    strNom = userInputAjouterBenevoles.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
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
                    strPrenom = userInputAjouterBenevoles.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
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
                    strAdresse = userInputAjouterBenevoles.nextLine().trim();

                    switch(strAdresse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
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
                    strCodePostal = userInputAjouterBenevoles.nextLine().trim();

                    switch(strCodePostal) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
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
                    strVille = userInputAjouterBenevoles.nextLine().trim();

                    switch(strVille) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterBenevole = false;
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
                strCourriel = userInputAjouterBenevoles.nextLine().trim();

                Benevole_OLD_OLD benevole = new Benevole_OLD_OLD(strDateNaissance, lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel);


                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(benevole);

                System.out.print("\nConfirmer l'ajouter [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputAjouterBenevoles.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuAjouterBenevole = false;
                        writeDataListBenevoles(benevole, Config.strDataFileBenevoles);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.out.println("\nAjout du nouveau compte bénévole réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuAjouterBenevole = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuAjouterBenevole = false;
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
            userInputAjouterBenevoles.close();
        }
    }

    // write & append benevole object into benevole csv data file
    private void writeDataListBenevoles(Benevole_OLD_OLD userInputBenevole, String strFileName) {
        try(FileWriter fw = new FileWriter(strFileName, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(userInputBenevole.printInfosBenevole());
            System.out.println("\nAjout du nouveau compte bénévole réussi!");
        } 
        catch (IOException ioe) {
            System.err.println("\nError: Ajout du nouveau compte bénévole réfusé par l'application!\n" + ioe.toString());
        }
    }

    // Menu Supprimer Benevole
    private void menuSupprimerBenevole() {
        Scanner optionMenuSupprimerBenevoles = new Scanner(System.in);

        String strStyleMenuName = "Menu Supprimer un Bénévole", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir supprimer le bénévole qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            boolean blnLoopMenuSupprimerBenevoles = true,
                    blnAutoSupprimer = false;

            while (blnLoopMenuSupprimerBenevoles) {
                blnAutoSupprimer = false;

                // System.out.println("=========================================");
                ArrayList<Benevole_OLD_OLD> arrBenevoles = readDataListBenevoles(Config.strDataFileBenevoles, false);
                System.out.println("-----------------------------------------");

                if (arrBenevoles == null || arrBenevoles.size() <= 0) {
                    System.out.println("\nLISTE DES BÉNÉVOLES VIDES!");
                    
                    blnLoopMenuSupprimerBenevoles = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    //! On prend en compte que seulement l'employe peut se connecter au systeme
                    //! ne pas afficher le compte utilisateur courant pour ne pas s'auto-supprimer
                    //! loopRemoveCurrentUser: for(Benevole e : arrBenevoles)
                    //!     if (e.getLngNoCompte() == this.currentUserLngNoCompte) { //&& e.getIntCodeIdentification() == this.currentUserIntCodeIdentification) {
                    //!         arrBenevoles.remove(e);
                    //!         blnAutoSupprimer = true;
                    //!         break loopRemoveCurrentUser;
                    //!     }

                    System.out.println("\nListe valide des bénévoles\n");
                    for(int i=0; i<arrBenevoles.size(); i++)
                        System.out.println("["+i+"] " + arrBenevoles.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }
                
                //! On prend en compte que seulement l'employe peut se connecter au systeme
                //! if (blnAutoSupprimer && (arrBenevoles == null || arrBenevoles.size() <= 0)) {
                //!     System.out.println("VOUS NE POUVEZ PAS VOUS SUPPRIMER!\n");

                //!     blnLoopMenuSupprimerBenevoles = false;
                //!     System.out.println("Retour au Menu Précédent");
                //!     System.out.println("=========================================");
                //!     run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                //! }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayBenevoleIndex;

                System.out.print("\nChoisissez l'index de le bénévole à supprimer parmi la liste: ");
                String strBenevoleIndex = optionMenuSupprimerBenevoles.nextLine().trim();

                switch(strBenevoleIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuSupprimerBenevoles = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuSupprimerBenevoles = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuSupprimerBenevoles = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayBenevoleIndex = Integer.parseInt(strBenevoleIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de le bénévole doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayBenevoleIndex = -1;
                    continue;
                }
                if (intArrayBenevoleIndex >= 0 && intArrayBenevoleIndex < arrBenevoles.size()) {
                    Benevole_OLD_OLD benevole = arrBenevoles.get(intArrayBenevoleIndex);

                    System.out.println();
                    System.out.println(strStyleConfirmationDoubleDash + "\n");
                    System.out.println(strStyleConfirmation);
                    System.out.println(strStyleConfirmationDash + "\n");
                    System.out.println(benevole);
    
                    System.out.print("\nConfirmer la suppression [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                    String strUserConfirmation = optionMenuSupprimerBenevoles.nextLine().trim();

                    switch(strUserConfirmation) {
                        case "":
                        case Config.strAppAccept:
					    case Config.strAppAcceptAlternative:
                            blnLoopMenuSupprimerBenevoles = false;
                            changeDataListBenevoles(EnumTypeCRUD.DELETE, benevole, Config.strDataFileBenevoles, false);
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                        
                        case Config.strAppRefuse:
					    case Config.strAppRefuseAlternative:
                            blnLoopMenuSupprimerBenevoles = false;
                            System.out.println("\nSuppression du compte bénévole réfusé!");
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
    
                        case Config.strAppExit:
                            System.out.println("Au Revoir!");
                            System.out.println("=========================================");
                            blnLoopMenuSupprimerBenevoles = false;
                            System.exit(0);
                        break;
    
                        case Config.strAppDisconnect:
                            blnLoopMenuSupprimerBenevoles = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;
    
                        case Config.strAppMenuBack:
                            blnLoopMenuSupprimerBenevoles = false;
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
                    System.err.println("\nErreur! L'index de le bénévole doit se situer entre [0] et [" + (arrBenevoles.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        } finally {
            optionMenuSupprimerBenevoles.close();
        }
    }

    // removes benevole from data csv file
    private void changeDataListBenevoles(EnumTypeCRUD enumCRUD, Benevole_OLD_OLD userInputBenevole, String strFileName, boolean blnShowVerbose) {
        String tempFile = Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
        File oldFile = new File(strFileName),
                newFile = new File(tempFile);

        // System.out.println("\n\noldFile: " + oldFile.getAbsolutePath() + " \nnewFile: " + newFile.getAbsolutePath() + "\n");

        ArrayList<Benevole_OLD_OLD> arrBenevoleChanged = new ArrayList<>();

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
                    // String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");

                    Benevole_OLD_OLD benevole = new Benevole_OLD_OLD();

                    String[] arrCurrentLigne = strCurrentLine.split("\\s*" + Config.strCSVMainSeparator + "\\s*");
                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            if(i==0) {
                                String strDateNaissance;
                                try {
                                    strDateNaissance = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strDateNaissance' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strDateNaissance = "";
                                }
                                benevole.setStrDateNaissance(strDateNaissance);
                            }
                            else if (i==1) {
                                long lngNoCompte;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    lngNoCompte = Long.parseLong(arrCurrentLigne[i]);
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
                                benevole.setLngNoCompte(lngNoCompte);
                            }
                            else if(i==2) {
                                long lngNoTel;
                                try {
                                    lngNoTel = Long.parseLong(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'lngNoTel' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    lngNoTel = 0;
                                }
                                benevole.setLngNoTel(lngNoTel);
                            }
                            else if(i==3) {
                                String strNom;
                                try {
                                    strNom = arrCurrentLigne[i];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strNom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strNom = "";
                                }
                                benevole.setStrNom(strNom);
                            }
                            else if(i==4) {
                                String strPrenom;
                                try {
                                    strPrenom = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strPrenom' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strPrenom = "";
                                }
                                benevole.setStrPrenom(strPrenom);
                            }
                            else if(i==5) {
                                String strAdresse;
                                try {
                                    strAdresse = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strAdresse' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strAdresse = "";
                                }
                                benevole.setStrAdresse(strAdresse);
                            }
                            else if(i==6) {
                                String strCodePostal;
                                try {
                                    strCodePostal = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strCodePostal' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strCodePostal = "";
                                }
                                benevole.setStrCodePostal(strCodePostal);
                            }
                            else if(i==7) {
                                String strVille;
                                try {
                                    strVille = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strVille' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strVille = "";
                                }
                                benevole.setStrVille(strVille);
                            }
                            else if(i==8) {
                                String strCourriel;
                                try {
                                    strCourriel = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strCourriel = "";
                                }
                                benevole.setStrCourriel(strCourriel);
                            }
                        }
                    }

                    if (enumCRUD == EnumTypeCRUD.DELETE) {
                        if (benevole.getLngNoCompte()>0 && benevole.getLngNoCompte() == userInputBenevole.getLngNoCompte()) {
                            System.out.println("\nSuccès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                            arrBenevoleChanged.add(benevole);

                            System.out.println("Suppression de ce compte dans le fichier");
                        }
                        else 
                            out.println(strCurrentLine);
                    }
                    else if (enumCRUD == EnumTypeCRUD.UPDATE) {
                        if (benevole.getLngNoCompte()>0 && benevole.getLngNoCompte() == userInputBenevole.getLngNoCompte()) {
                            System.out.println("\nSuccès: L'utilisateur '" + Long.toString(benevole.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                            arrBenevoleChanged.add(benevole);
                            
                            System.out.println("Modification de ce compte dans le fichier");
                        }
                        else
                            out.println(strCurrentLine);
                    }
                }
                else { // copy empty line & write into new temp file
                    out.println(strCurrentLine);
                }
            }

            // if modifying benevole, add benevole to end of file
            if (enumCRUD == EnumTypeCRUD.UPDATE)
                out.println(userInputBenevole.printInfosBenevole());

            // out.flush();
            oldFile.delete();
            File dump = new File(strFileName);
            newFile.renameTo(dump);

            // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

            if (enumCRUD == EnumTypeCRUD.DELETE)
                System.out.println("\nSuppression du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réussi!\n");
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.out.println("\nModification du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réussi!\n");

            if (arrBenevoleChanged == null || arrBenevoleChanged.size() <= 0) {
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    System.out.println("LA LISTE DES COMPTES BÉNÉVOLES SUPPRIMER EST VIDE!");
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    System.out.println("LA LISTE DES COMPTES BÉNÉVOLES MODIFIER EST VIDE!");
            }
            else {
                String strStyleListe = "";
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    strStyleListe = "Liste des comptes bénévoles supprimer du fichier:";
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    strStyleListe = "Liste des comptes bénévoles modifier du fichier:";

                String strStyleListeDash = "", strStyleListeDoubleDash = "";
                for(int j=0; j<strStyleListe.length(); j++) {
                    strStyleListeDash += "-";
                    strStyleListeDoubleDash += "=";
                }
                
                System.out.println(strStyleListeDoubleDash + "\n");
                System.out.println(strStyleListe);
                System.out.println(strStyleListeDash + "\n");
                for(Benevole_OLD_OLD e : arrBenevoleChanged)
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
                System.err.println("\nError: Suppression du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.err.println("\nError: Modification du compte bénévole '" + Long.toString(userInputBenevole.getLngNoCompte()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
        }
    }

    private void menuModifierBenevole() {
        Scanner optionMenuModifierBenevoles = new Scanner(System.in);

        String strStyleMenuName = "Menu Modifier un Bénévole", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuModifierBenevoles = true;

            while(blnLoopMenuModifierBenevoles) {
                ArrayList<Benevole_OLD_OLD> arrBenevoles = readDataListBenevoles(Config.strDataFileBenevoles, false);
                System.out.println("-----------------------------------------");

                if (arrBenevoles == null || arrBenevoles.size() <= 0) {
                    System.out.println("\nLISTE DES BÉNÉVOLES VIDES!");
                    
                    blnLoopMenuModifierBenevoles = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.out.println("\nListe valide des bénévoles\n");
                    for(int i=0; i<arrBenevoles.size(); i++)
                        System.out.println("["+i+"] " + arrBenevoles.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayBenevoleIndex;

                System.out.print("\nChoisissez l'index de le bénévole à modifier parmi la liste: ");
                String strBenevoleIndex = optionMenuModifierBenevoles.nextLine().trim();

                switch(strBenevoleIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierBenevoles = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierBenevoles = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierBenevoles = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayBenevoleIndex = Integer.parseInt(strBenevoleIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de le bénévole doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayBenevoleIndex = -1;
                    continue;
                }

                if (intArrayBenevoleIndex >= 0 && intArrayBenevoleIndex < arrBenevoles.size()) {
                    Benevole_OLD_OLD benevole = arrBenevoles.get(intArrayBenevoleIndex);

                    menuModifierDetailsBenevole(benevole);
                }
                else {
                    System.err.println("\nErreur! L'index de le bénévole doit se situer entre [0] et [" + (arrBenevoles.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierBenevoles.close();
        }
    }

    private void menuModifierDetailsBenevole(Benevole_OLD_OLD benevole) {
        boolean blnLoopErrorMenuModifierDetailsBenevole = true;
        Scanner userInputModifierBenevoles = new Scanner(System.in);

        // long lngNoCompte = 0;
        long lngNoTel = 0;
        String strDateNaissance = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        //"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String regexPostalCodeValidator = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]?[0-9][A-Z][0-9]$";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir modifier le bénévole qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            System.out.println("=========================================\n");
            while(blnLoopErrorMenuModifierDetailsBenevole) {
                System.out.println("Modification du compte bénévole:\n");
                System.out.println(benevole + "\n\n");

                System.out.print("Numéro de compte (Non Modifiable): " + Long.toString(benevole.getLngNoCompte()) + "\n");

                if (strDateNaissance == null || strDateNaissance.trim().isEmpty()) {
                    System.out.print("Date disponible [" + benevole.getStrDateNaissance() + "]: ");
                    strDateNaissance = userInputModifierBenevoles.nextLine().trim();

                    switch(strDateNaissance) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    // System.out.println("\n" + Boolean.toString(strMotDePasse.matches(regexPasswordValidator)));
                    // System.out.println(Boolean.toString(Pattern.compile(regexPasswordValidator).matcher(strMotDePasse).matches()));
                    // System.out.println(Boolean.toString() + "\n");
                    
                    if (strDateNaissance.trim().isEmpty())
                    strDateNaissance = benevole.getStrDateNaissance();
                    // else if( || !) {
                    //     // System.out.println(!strMotDePasse.matches(regexPasswordValidator));
                    //     System.out.println("\nLe mot de passe doit être composé d'au moins 8 caractères contenant au moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial parmis [! ? - @ # $ % ^ & + =]");
                    //     System.out.println("=========================================");
                    //     continue;
                    // }

                    benevole.setStrDateNaissance(strDateNaissance);
                }
                else if (strDateNaissance != null && !strDateNaissance.trim().isEmpty()) {
                    System.out.println("Date disponible: " + strDateNaissance);
                    benevole.setStrDateNaissance(strDateNaissance);
                }
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(lngNoTel == 0 || String.valueOf(lngNoTel).length() != 10) {
                    System.out.print("Numéro de Téléphone [" + Long.toString(benevole.getLngNoTel()) + "]: ");
                    String strNoTel = userInputModifierBenevoles.nextLine().trim();

                    switch(strNoTel) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    try {
                        if (strNoTel.trim().isEmpty())
                            lngNoTel = benevole.getLngNoTel();
                        else
                            lngNoTel = Long.parseLong(strNoTel);

                        if(String.valueOf(lngNoTel).length() != 10) {
                            System.err.println("\nLe numéro de téléphone doit être d'une longueur exacte de 10 charactère!\n");
                            System.out.println("=========================================");
                            continue;
                        }

                        benevole.setLngNoTel(lngNoTel);
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe numéro de téléphone doit être un chiffre entier et d'une longueur exacte de 10 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (lngNoTel > 0 && String.valueOf(lngNoTel).length() == 10) {
                    System.out.println("Numéro de Téléphone: " + String.valueOf(lngNoTel));
                    benevole.setLngNoTel(lngNoTel);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                    System.out.print("Nom [" + benevole.getStrNom() + "]: ");
                    strNom = userInputModifierBenevoles.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strNom.trim().isEmpty())
                        strNom = benevole.getStrNom();
                    else if(strNom.trim().length() > 50) {
                        System.out.println("\nLe nom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    benevole.setStrNom(strNom);
                }
                else if(!strNom.trim().isEmpty() && strNom.trim().length() <= 50) {
                    System.out.println("Nom: " + strNom);
                    benevole.setStrNom(strNom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strPrenom.trim().isEmpty() || strPrenom.trim().length() > 50) {
                    System.out.print("Prénom [" + benevole.getStrPrenom() + "]: ");
                    strPrenom = userInputModifierBenevoles.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strPrenom.trim().isEmpty())
                        strPrenom = benevole.getStrPrenom();
                    else if(strPrenom.trim().length() > 50) {
                        System.out.println("\nLe prénom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    benevole.setStrPrenom(strPrenom);
                }
                else if(!strPrenom.trim().isEmpty() && strPrenom.trim().length() <= 50) {
                    System.out.println("Prénom: " + strPrenom);
                    benevole.setStrPrenom(strPrenom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strAdresse.trim().isEmpty() || strAdresse.trim().length() > 100) {
                    System.out.print("Adresse [" + benevole.getStrAdresse() + "]: ");
                    strAdresse = userInputModifierBenevoles.nextLine().trim();

                    switch(strAdresse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strAdresse.trim().isEmpty())
                        strAdresse = benevole.getStrAdresse();
                    else if(strAdresse.trim().length() > 100) {
                        System.out.println("\nL'adresse ne doit pas être vide et ne pas dépasser 100 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    benevole.setStrAdresse(strAdresse);
                }
                else if(!strAdresse.trim().isEmpty() && strAdresse.trim().length() <= 100) {
                    System.out.println("Adresse: " + strAdresse);
                    benevole.setStrAdresse(strAdresse);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strCodePostal.trim().isEmpty() || strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.print("Code Postal [" + benevole.getStrCodePostal() + "]: ");
                    strCodePostal = userInputModifierBenevoles.nextLine().trim();

                    switch(strCodePostal) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strCodePostal.trim().isEmpty())
                        strCodePostal = benevole.getStrCodePostal();
                    else if(strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                        System.out.println("\nPour que le code postal soit valide:\n-Ne doit pas être vide (A1A1A1)\n-Lettres en majuscules\n-Ne pas avoir les lettres [D, F, I, O, Q, U]\n-Ne pas avoir les lettres [W, Z] en première position\n-Ne pas dépasser 6 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    benevole.setStrCodePostal(strCodePostal);
                }
                else if(!strCodePostal.trim().isEmpty() && strCodePostal.trim().length() <= 6 && Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.println("Code Postal: " + strCodePostal);
                    benevole.setStrCodePostal(strCodePostal);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strVille.trim().isEmpty() || strVille.trim().length() > 50) {
                    System.out.print("Ville [" + benevole.getStrVille() + "]: ");
                    strVille = userInputModifierBenevoles.nextLine().trim();

                    switch(strVille) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsBenevole = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strVille.trim().isEmpty())
                        strVille = benevole.getStrVille();
                    else if(strVille.trim().length() > 50) {
                        System.out.println("\nLa ville ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    benevole.setStrVille(strVille);
                }
                else if(!strVille.trim().isEmpty() && strVille.trim().length() <= 50) {
                    System.out.println("Ville: " + strVille);
                    benevole.setStrVille(strVille);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if (strCourriel.trim().isEmpty()) {
                    System.out.print("Adresse Courriel [" + benevole.getStrCourriel() + "]: ");
                    strCourriel = userInputModifierBenevoles.nextLine().trim();

                    if (strCourriel.trim().isEmpty())
                        strCourriel = benevole.getStrCourriel();
                }
                benevole.setStrCourriel(strCourriel);

                // if (benevole == null)
                //     continue;

                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(benevole);

                System.out.print("\nConfirmer la modification [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputModifierBenevoles.nextLine().trim(); //optionMenuModifierBenevoles.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        changeDataListBenevoles(EnumTypeCRUD.UPDATE, benevole, Config.strDataFileBenevoles, false);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.out.println("\nModification du compte bénévole réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuModifierDetailsBenevole = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuModifierDetailsBenevole = false;
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
            userInputModifierBenevoles.close();
        }
    }
}
