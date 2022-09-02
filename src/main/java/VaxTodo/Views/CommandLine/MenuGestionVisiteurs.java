package VaxTodo.Views.CommandLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import VaxTodo.Configs.Config;
import VaxTodo.Models.Entrevue;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeVaccin;
import VaxTodo.Models.Visiteur;

/** Old Menu Gestion Visiteurs to add, modify or delete Visiteurs accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class MenuGestionVisiteurs {
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    // Menu Gestion Visiteur
    public void run(long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;
        this.currentUserLngNoCompte = currentUserLngNoCompte;

        Scanner optionMenuGestionVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Gestion des Visiteurs", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionVisiteurs = true;
            while (blnLoopMenuGestionVisiteurs) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                System.out.println("[1] Lister tous les Visiteurs");
                System.out.println("[2] Ajouter un Visiteur");
                System.out.println("[3] Modifier un Visiteur");
                System.out.println("[4] Supprimer un Visiteur");
                System.out.println("[5] Gérer un Visiteur");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuGestionVisiteurs.nextLine()) {
                    // Menu Lister Visiteur
                    case "1":
                        System.out.println("=========================================");
                        ArrayList<Visiteur> arrVisiteurs = readDataListVisiteurs(Config.strDataFileVisiteurs, true);
                        System.out.println("-----------------------------------------");
                                                
                        if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                            System.out.println("\nLISTE DES VISITEURS VIDES!");
                            System.out.println("=========================================");
                        }
                        else {
                            System.out.println("\nListe valide des visiteurs\n");
                            for(int i=0; i<arrVisiteurs.size(); i++)
                                System.out.println("["+i+"] " + arrVisiteurs.get(i).toString() + "\n");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Ajouter Visiteur
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        menuAjouterVisiteur();
                        break;

                    // Menu Modifier Visiteur
                    case "3":
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        menuModifierVisiteur();
                        break;

                    // Menu Supprimer Visiteur
                    case "4":
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        menuSupprimerVisiteur();
                        break;

                    case "5":
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        menuGestionCompteVisiteur();
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionVisiteurs = false;
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
            optionMenuGestionVisiteurs.close();
        }
    }

    // reads visiteurs csv data file and returns an arraylist of them
    private ArrayList<Visiteur> readDataListVisiteurs(String strFileName, boolean blnShowVerbose) {
        ArrayList<Visiteur> arrVisiteurs = new ArrayList<>();

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

                    Visiteur visiteur = new Visiteur();

                    String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVMainSeparator + "\\s*");

                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            // if(i==0) {
                            //     int intRapportVaccination;
                            //     try {
                            //         // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                            //         intRapportVaccination = Integer.parseInt(arrCurrentLigne[i]);
                            //     } 
                            //     catch (NumberFormatException nfe) {
                            //         if (blnShowVerbose)
                            //             System.err.println("Error: 'intRapportVaccination' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                            //         intRapportVaccination = 0;
                            //     }
                            //     catch (IndexOutOfBoundsException ioobe) {
                            //         if (blnShowVerbose)
                            //             System.err.println("Error: 'intRapportVaccination' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                            //         intRapportVaccination = 0;
                            //     }
                            //     visiteur.setIntRapportVaccination(intRapportVaccination);
                            // }
                            if(i==0) {
                                String strDateNaissance;
                                try {
                                    LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    strDateNaissance = localDate.toString();

                                    if(strDateNaissance.trim().isEmpty()) {
                                        System.out.println("Error: 'strDateNaissance' is empty at line " + intCurrentLineNumber);
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
                                    if(todayDate.compareTo(dateNaissance) <= 0) {
                                        System.out.println("Error: 'strDateNaissance' est plus grand que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                        throw new Exception("'dateNaissance' est plus grand que la date d'aujourd'hui!");
                                    }
                                }
                                catch (DateTimeParseException dtpe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: La date de naissance doit être une date valide (yyyy-mm-dd) et plus petite à la date d'aujourd'hui, 'dateNaissance' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
                                    strDateNaissance = "";
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'dateNaissance' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strDateNaissance = "";
                                }
                                catch (Exception e) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strDate' Exception at line " + intCurrentLineNumber + ":\t" + e.toString());
                                    strDateNaissance = "";
                                }
                                visiteur.setStrDateNaissance(strDateNaissance);
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
                                visiteur.setLngNoCompte(lngNoCompte);
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
                                visiteur.setLngNoTel(lngNoTel);
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
                                visiteur.setStrNom(strNom);
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
                                visiteur.setStrPrenom(strPrenom);
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
                                visiteur.setStrAdresse(strAdresse);
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
                                visiteur.setStrCodePostal(strCodePostal);
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
                                visiteur.setStrVille(strVille);
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
                                visiteur.setStrCourriel(strCourriel);
                            }
                        }
                    }

                    if (visiteur.getLngNoCompte()>0)
                        arrVisiteurs.add(visiteur);
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

        return arrVisiteurs;
    }

    private void menuAjouterVisiteur() {
        Scanner userInputAjouterVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Ajouter un Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir ajouter un nouveau visiteur qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        long lngNoCompte = 0, lngNoTel = 0;
        // int intRapportVaccination = 0;
        String strDateNaissance = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        String regexPostalCodeValidator = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]?[0-9][A-Z][0-9]$";

        try {
            boolean blnLoopErrorMenuAjouterVisiteur = true;

             while (blnLoopErrorMenuAjouterVisiteur) {                                
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");

                if(lngNoCompte == 0) {
                    ArrayList<Visiteur> arrVisiteurs = readDataListVisiteurs(Config.strDataFileVisiteurs, false);
                    
                    boolean blnLngVisiteurUniqueNotFound = true;
                    loopFindVisiteurUnique : while (blnLngVisiteurUniqueNotFound) {
                        lngNoCompte = ThreadLocalRandom.current().nextLong(Long.parseLong(Config.strNoVisiteurMinRnd), Long.parseLong(Config.strNoVisiteurMaxRnd)+1);

                        boolean blnFound = true;
                        loopNestedCompareVisiteurUnique: for (Visiteur v : arrVisiteurs)
                            if (lngNoCompte == v.getLngNoCompte()) {
                                blnFound = false;
                                break loopNestedCompareVisiteurUnique;
                            }
                                                     
                        if(blnFound) {
                            blnLngVisiteurUniqueNotFound = false;
                            break loopFindVisiteurUnique;
                        }
                    }
                    // lngNoCompte = ThreadLocalRandom.current().nextLong(Long.parseLong(Config.strNoCompteMinRnd), Long.parseLong(Config.strNoCompteMaxRnd)+1);
                }
                // System.out.println(Long.toString(lngNoCompte));
                
                System.out.print("Numéro de compte (Assigné automatiquement): " + Long.toString(lngNoCompte) + "\n");
                
                // if(intRapportVaccination <= 0) {
                //     System.out.print("Rapport de Vaccination: ");
                //     String strRapportVaccination = userInputAjouterVisiteurs.nextLine().trim();

                //     switch(strRapportVaccination) {
                //         case Config.strAppExit:
                //         System.out.println("Au Revoir!");
                //         System.out.println("=========================================");
                //         blnLoopErrorMenuAjouterVisiteur = false;
                //         System.exit(0);
                //         break;

                //         case Config.strAppDisconnect:
                //             blnLoopErrorMenuAjouterVisiteur = false;
                //             System.out.println("Déconnexion et Retour au Menu Principal");
                //             System.out.println("=========================================");
                //             new Login().run();
                //             break;

                //         case Config.strAppMenuBack:
                //             blnLoopErrorMenuAjouterVisiteur = false;
                //             System.out.println("Retour au Menu Précédent");
                //             System.out.println("=========================================");
                //             run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                //             break;
                //     }

                //     try {
                //         intRapportVaccination = Integer.parseInt(strRapportVaccination);

                //         // if(String.valueOf(intRapportVaccination).length() != 10) {
                //         //     System.err.println("\nLe numéro de téléphone doit être d'une longueur exacte de 10 charactère!\n");
                //         //     System.out.println("=========================================");
                //         //     continue;
                //         // }

                //         if (intRapportVaccination <= 0) {
                //             System.err.println("\nLe rapport de vaccin ne doit pas être vide!\n");
                //             System.out.println("=========================================");
                //             continue;
                //         }
                //     }
                //     catch (NumberFormatException nfe) {
                //         System.err.println("\nLe numéro de téléphone doit être un chiffre entier et d'une longueur exacte de 10 charactère!\n\n" + nfe.toString() + "\n");
                //         System.out.println("=========================================");
                //         continue;
                //     }
                // }
                // else if (intRapportVaccination > 0)
                //     System.out.println("Rapport de Vaccination: " + String.valueOf(intRapportVaccination));
                // else {
                //     System.out.println("=========================================");
                //     continue; 
                // }

                if (strDateNaissance == null || strDateNaissance.trim().isEmpty()) {
                    System.out.print("Date de naissance: ");
                    strDateNaissance = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strDateNaissance) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }
                    
                    try {
                        LocalDate localDate = LocalDate.parse(strDateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        // strDate = localDate.toString();

                        if(strDateNaissance.trim().isEmpty()) {
                            System.out.println("La date de naissance ne doit pas être vide!");
                            throw new Exception("'dateNaissance' vide!");
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

                        // verifie si la date du rendez-vous est plus petit que la date du jour
                        if(todayDate.compareTo(dateNaissance) <= 0) {
                            System.out.println("Error: 'dateNaissance' doit être plus petite à la date d'aujourd'hui!");
                            throw new Exception("'dateNaissance' est plus grand que la date d'aujourd'hui!");
                        }
                    }
                    catch (DateTimeParseException dtpe) {
                        System.err.println("Error: La date de naissance doit être une date valide (yyyy-mm-dd) et plus petite à la date d'aujourd'hui, 'strDate' Date Time Parse Exception:\t" + dtpe.toString());
                        System.out.println("=========================================");
                        strDateNaissance = "";
                        continue;
                    }
                    catch (IndexOutOfBoundsException ioobe) {
                        System.err.println("Error: 'dateNaissance' Index Out Of Bound:\t" + ioobe.toString());
                        System.out.println("=========================================");
                        strDateNaissance = "";
                        continue;
                    }
                    catch (Exception e) {
                        System.err.println("Error: 'dateNaissance' Exception at line:\t" + e.toString());
                        System.out.println("=========================================");
                        strDateNaissance = "";
                        continue;
                    }
                }
                else if (strDateNaissance != null && !strDateNaissance.trim().isEmpty()) 
                    System.out.println("Date de naissance: " + strDateNaissance);
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(lngNoTel == 0 || String.valueOf(lngNoTel).length() != 10) {
                    System.out.print("Numéro de Téléphone: ");
                    String strNoTel = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strNoTel) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
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
                    strNom = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
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
                    strPrenom = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
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
                    strAdresse = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strAdresse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
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
                    strCodePostal = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strCodePostal) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
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
                    strVille = userInputAjouterVisiteurs.nextLine().trim();

                    switch(strVille) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterVisiteur = false;
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
                strCourriel = userInputAjouterVisiteurs.nextLine().trim();

                Visiteur visiteur = new Visiteur(/*intRapportVaccination,*/ lngNoCompte, lngNoTel, strNom, strPrenom, strAdresse, strCodePostal, strVille, strCourriel, strDateNaissance);


                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(visiteur);

                System.out.print("\nConfirmer l'ajouter [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputAjouterVisiteurs.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuAjouterVisiteur = false;
                        writeDataListVisiteurs(visiteur, Config.strDataFileVisiteurs);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.out.println("\nAjout du nouveau compte visiteur réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuAjouterVisiteur = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuAjouterVisiteur = false;
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
            userInputAjouterVisiteurs.close();
        }
    }

    // write & append visiteur object into visiteur csv data file
    private void writeDataListVisiteurs(Visiteur userInputVisiteur, String strFileName) {
        try(FileWriter fw = new FileWriter(strFileName, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(userInputVisiteur.printInfosVisiteur());
            System.out.println("\nAjout du nouveau compte visiteur réussi!");
        } 
        catch (IOException ioe) {
            System.err.println("\nError: Ajout du nouveau compte visiteur réfusé par l'application!\n" + ioe.toString());
        }
    }

    // Menu Supprimer Visiteur
    private void menuSupprimerVisiteur() {
        Scanner optionMenuSupprimerVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Supprimer un Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir supprimer le visiteur qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            boolean blnLoopMenuSupprimerVisiteurs = true,
                    blnAutoSupprimer = false;

            while (blnLoopMenuSupprimerVisiteurs) {
                blnAutoSupprimer = false;

                // System.out.println("=========================================");
                ArrayList<Visiteur> arrVisiteurs = readDataListVisiteurs(Config.strDataFileVisiteurs, false);
                System.out.println("-----------------------------------------");

                if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                    System.out.println("\nLISTE DES VISITEURS VIDES!");
                    
                    blnLoopMenuSupprimerVisiteurs = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    //! On prend en compte que seulement l'employe peut se connecter au systeme
                    //! ne pas afficher le compte utilisateur courant pour ne pas s'auto-supprimer
                    //! loopRemoveCurrentUser: for(Visiteur e : arrVisiteurs)
                    //!     if (e.getLngNoCompte() == this.currentUserLngNoCompte) { //&& e.getIntCodeIdentification() == this.currentUserIntCodeIdentification) {
                    //!         arrVisiteurs.remove(e);
                    //!         blnAutoSupprimer = true;
                    //!         break loopRemoveCurrentUser;
                    //!     }

                    System.out.println("\nListe valide des visiteurs\n");
                    for(int i=0; i<arrVisiteurs.size(); i++)
                        System.out.println("["+i+"] " + arrVisiteurs.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }
                
                //! On prend en compte que seulement l'employe peut se connecter au systeme
                //! if (blnAutoSupprimer && (arrVisiteurs == null || arrVisiteurs.size() <= 0)) {
                //!     System.out.println("VOUS NE POUVEZ PAS VOUS SUPPRIMER!\n");

                //!     blnLoopMenuSupprimerVisiteurs = false;
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
                
                int intArrayVisiteurIndex;

                System.out.print("\nChoisissez l'index de le visiteur à supprimer parmi la liste: ");
                String strVisiteurIndex = optionMenuSupprimerVisiteurs.nextLine().trim();

                switch(strVisiteurIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuSupprimerVisiteurs = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuSupprimerVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuSupprimerVisiteurs = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayVisiteurIndex = Integer.parseInt(strVisiteurIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de le visiteur doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayVisiteurIndex = -1;
                    continue;
                }
                if (intArrayVisiteurIndex >= 0 && intArrayVisiteurIndex < arrVisiteurs.size()) {
                    Visiteur visiteur = arrVisiteurs.get(intArrayVisiteurIndex);

                    System.out.println();
                    System.out.println(strStyleConfirmationDoubleDash + "\n");
                    System.out.println(strStyleConfirmation);
                    System.out.println(strStyleConfirmationDash + "\n");
                    System.out.println(visiteur);
    
                    System.out.print("\nConfirmer la suppression [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                    String strUserConfirmation = optionMenuSupprimerVisiteurs.nextLine().trim();

                    switch(strUserConfirmation) {
                        case "":
                        case Config.strAppAccept:
					    case Config.strAppAcceptAlternative:
                            blnLoopMenuSupprimerVisiteurs = false;
                            changeDataListVisiteurs(EnumTypeCRUD.DELETE, visiteur, Config.strDataFileVisiteurs, false);
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                        
                        case Config.strAppRefuse:
					    case Config.strAppRefuseAlternative:
                            blnLoopMenuSupprimerVisiteurs = false;
                            System.out.println("\nSuppression du compte visiteur réfusé!");
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
    
                        case Config.strAppExit:
                            System.out.println("Au Revoir!");
                            System.out.println("=========================================");
                            blnLoopMenuSupprimerVisiteurs = false;
                            System.exit(0);
                        break;
    
                        case Config.strAppDisconnect:
                            blnLoopMenuSupprimerVisiteurs = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;
    
                        case Config.strAppMenuBack:
                            blnLoopMenuSupprimerVisiteurs = false;
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
                    System.err.println("\nErreur! L'index de le visiteur doit se situer entre [0] et [" + (arrVisiteurs.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        } finally {
            optionMenuSupprimerVisiteurs.close();
        }
    }

    // removes visiteur from data csv file
    private void changeDataListVisiteurs(EnumTypeCRUD enumCRUD, Visiteur userInputVisiteur, String strFileName, boolean blnShowVerbose) {
        String tempFile = Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
        File oldFile = new File(strFileName),
                newFile = new File(tempFile);

        // System.out.println("\n\noldFile: " + oldFile.getAbsolutePath() + " \nnewFile: " + newFile.getAbsolutePath() + "\n");

        ArrayList<Visiteur> arrVisiteurChanged = new ArrayList<>();

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

                    Visiteur visiteur = new Visiteur();

                    String[] arrCurrentLigne = strCurrentLine.split("\\s*" + Config.strCSVMainSeparator + "\\s*");
                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            // if(i==0) {
                            //     int intRapportVaccination;
                            //     try {
                            //         // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                            //         intRapportVaccination = Integer.parseInt(arrCurrentLigne[i]);
                            //     } 
                            //     catch (NumberFormatException nfe) {
                            //         if (blnShowVerbose)
                            //             System.err.println("Error: 'intRapportVaccination' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                            //         intRapportVaccination = 0;
                            //     }
                            //     catch (IndexOutOfBoundsException ioobe) {
                            //         if (blnShowVerbose)
                            //             System.err.println("Error: 'intRapportVaccination' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                            //         intRapportVaccination = 0;
                            //     }
                            //     visiteur.setIntRapportVaccination(intRapportVaccination);
                            // }
                            if(i==0) {
                                String strDateNaissance;
                                try {
                                    LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    strDateNaissance = localDate.toString();

                                    if(strDateNaissance.trim().isEmpty()) {
                                        System.out.println("Error: 'strDateNaissance' is empty at line " + intCurrentLineNumber);
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
                                    if(todayDate.compareTo(dateNaissance) <= 0) {
                                        System.out.println("Error: 'strDateNaissance' est plus grand que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                        throw new Exception("'dateNaissance' est plus grand que la date d'aujourd'hui!");
                                    }
                                }
                                catch (DateTimeParseException dtpe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: La date de naissance doit être une date valide (yyyy-mm-dd) et plus petite à la date d'aujourd'hui, 'dateNaissance' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
                                    strDateNaissance = "";
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'dateNaissance' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strDateNaissance = "";
                                }
                                catch (Exception e) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strDate' Exception at line " + intCurrentLineNumber + ":\t" + e.toString());
                                    strDateNaissance = "";
                                }
                                visiteur.setStrDateNaissance(strDateNaissance);
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
                                visiteur.setLngNoCompte(lngNoCompte);
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
                                visiteur.setLngNoTel(lngNoTel);
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
                                visiteur.setStrNom(strNom);
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
                                visiteur.setStrPrenom(strPrenom);
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
                                visiteur.setStrAdresse(strAdresse);
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
                                visiteur.setStrCodePostal(strCodePostal);
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
                                visiteur.setStrVille(strVille);
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
                                visiteur.setStrCourriel(strCourriel);
                            }
                        }
                    }

                    if (enumCRUD == EnumTypeCRUD.DELETE) {
                        if (visiteur.getLngNoCompte()>0 && visiteur.getLngNoCompte() == userInputVisiteur.getLngNoCompte()) {
                            System.out.println("\nSuccès: L'utilisateur '" + Long.toString(visiteur.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                            arrVisiteurChanged.add(visiteur);

                            System.out.println("Suppression de ce compte dans le fichier");
                        }
                        else 
                            out.println(strCurrentLine);
                    }
                    else if (enumCRUD == EnumTypeCRUD.UPDATE) {
                        if (visiteur.getLngNoCompte()>0 && visiteur.getLngNoCompte() == userInputVisiteur.getLngNoCompte()) {
                            System.out.println("\nSuccès: L'utilisateur '" + Long.toString(visiteur.getLngNoCompte()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                            arrVisiteurChanged.add(visiteur);
                            
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

            // if modifying visiteur, add visiteur to end of file
            if (enumCRUD == EnumTypeCRUD.UPDATE)
                out.println(userInputVisiteur.printInfosVisiteur());

            // out.flush();
            oldFile.delete();
            File dump = new File(strFileName);
            newFile.renameTo(dump);

            // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

            if (enumCRUD == EnumTypeCRUD.DELETE)
                System.out.println("\nSuppression du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.out.println("\nModification du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");

            if (arrVisiteurChanged == null || arrVisiteurChanged.size() <= 0) {
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    System.out.println("LA LISTE DES COMPTES VISITEURS SUPPRIMER EST VIDE!");
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    System.out.println("LA LISTE DES COMPTES VISITEURS MODIFIER EST VIDE!");
            }
            else {
                String strStyleListe = "";
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    strStyleListe = "Liste des comptes visiteurs supprimer du fichier:";
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    strStyleListe = "Liste des comptes visiteurs modifier du fichier:";

                String strStyleListeDash = "", strStyleListeDoubleDash = "";
                for(int j=0; j<strStyleListe.length(); j++) {
                    strStyleListeDash += "-";
                    strStyleListeDoubleDash += "=";
                }
                
                System.out.println(strStyleListeDoubleDash + "\n");
                System.out.println(strStyleListe);
                System.out.println(strStyleListeDash + "\n");
                for(Visiteur e : arrVisiteurChanged)
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
                System.err.println("\nError: Suppression du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.err.println("\nError: Modification du compte visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
        }
    }

    private void menuModifierVisiteur() {
        Scanner optionMenuModifierVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Modifier un Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuModifierVisiteurs = true;

            while(blnLoopMenuModifierVisiteurs) {
                ArrayList<Visiteur> arrVisiteurs = readDataListVisiteurs(Config.strDataFileVisiteurs, false);
                System.out.println("-----------------------------------------");

                if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                    System.out.println("\nLISTE DES VISITEURS VIDES!");
                    
                    blnLoopMenuModifierVisiteurs = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.out.println("\nListe valide des visiteurs\n");
                    for(int i=0; i<arrVisiteurs.size(); i++)
                        System.out.println("["+i+"] " + arrVisiteurs.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayVisiteurIndex;

                System.out.print("\nChoisissez l'index de le visiteur à modifier parmi la liste: ");
                String strVisiteurIndex = optionMenuModifierVisiteurs.nextLine().trim();

                switch(strVisiteurIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierVisiteurs = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierVisiteurs = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayVisiteurIndex = Integer.parseInt(strVisiteurIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de le visiteur doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayVisiteurIndex = -1;
                    continue;
                }

                if (intArrayVisiteurIndex >= 0 && intArrayVisiteurIndex < arrVisiteurs.size()) {
                    Visiteur visiteur = arrVisiteurs.get(intArrayVisiteurIndex);

                    System.out.println("=========================================");
                    menuModifierDetailsVisiteur(visiteur);
                }
                else {
                    System.err.println("\nErreur! L'index de le visiteur doit se situer entre [0] et [" + (arrVisiteurs.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierVisiteurs.close();
        }
    }

    private void menuModifierDetailsVisiteur(Visiteur visiteur) {
        boolean blnLoopErrorMenuModifierDetailsVisiteur = true;
        Scanner userInputModifierVisiteurs = new Scanner(System.in);

        // long lngNoCompte = 0;
        long lngNoTel = 0;
        // int intRapportVaccination = 0;
        String strDateNaissance = "", strNom = "", strPrenom = "", strAdresse = "", strCodePostal = "", strVille = "", strCourriel = "";

        //"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String regexPostalCodeValidator = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z]?[0-9][A-Z][0-9]$";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir modifier le visiteur qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            System.out.println("=========================================\n");
            while(blnLoopErrorMenuModifierDetailsVisiteur) {
                System.out.println("Modification du compte visiteur:\n");
                System.out.println(visiteur + "\n\n");

                System.out.print("Numéro de compte (Non Modifiable): " + Long.toString(visiteur.getLngNoCompte()) + "\n");

                if (strDateNaissance == null || strDateNaissance.trim().isEmpty()) {
                    System.out.print("Date de naissance [" + visiteur.getStrDateNaissance() + "]: ");
                    strDateNaissance = userInputModifierVisiteurs.nextLine().trim();

                    switch(strDateNaissance) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }
                    
                    if (strDateNaissance.trim().isEmpty())
                        strDateNaissance = visiteur.getStrDateNaissance();
                    else {
                        try {
                            LocalDate localDate = LocalDate.parse(strDateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            // strDate = localDate.toString();
    
                            if(strDateNaissance.trim().isEmpty()) {
                                System.out.println("La date de naissance ne doit pas être vide!");
                                throw new Exception("'dateNaissance' vide!");
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
    
                            // verifie si la date du rendez-vous est plus petit que la date du jour
                            if(todayDate.compareTo(dateNaissance) <= 0) {
                                System.out.println("Error: 'dateNaissance' doit être plus petite à la date d'aujourd'hui!");
                                throw new Exception("'dateNaissance' est plus grand que la date d'aujourd'hui!");
                            }
                        }
                        catch (DateTimeParseException dtpe) {
                            System.err.println("Error: La date de naissance doit être une date valide (yyyy-mm-dd) et plus petite à la date d'aujourd'hui, 'strDate' Date Time Parse Exception:\t" + dtpe.toString());
                            System.out.println("=========================================");
                            strDateNaissance = "";
                            continue;
                        }
                        catch (IndexOutOfBoundsException ioobe) {
                            System.err.println("Error: 'dateNaissance' Index Out Of Bound:\t" + ioobe.toString());
                            System.out.println("=========================================");
                            strDateNaissance = "";
                            continue;
                        }
                        catch (Exception e) {
                            System.err.println("Error: 'dateNaissance' Exception at line:\t" + e.toString());
                            System.out.println("=========================================");
                            strDateNaissance = "";
                            continue;
                        }
                    }

                    visiteur.setStrDateNaissance(strDateNaissance);
                }
                else if (strDateNaissance != null && !strDateNaissance.trim().isEmpty()) {
                    System.out.println("Date de naissance: " + strDateNaissance);
                    visiteur.setStrDateNaissance(strDateNaissance);
                }
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(lngNoTel == 0 || String.valueOf(lngNoTel).length() != 10) {
                    System.out.print("Numéro de Téléphone [" + Long.toString(visiteur.getLngNoTel()) + "]: ");
                    String strNoTel = userInputModifierVisiteurs.nextLine().trim();

                    switch(strNoTel) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    try {
                        if (strNoTel.trim().isEmpty())
                            lngNoTel = visiteur.getLngNoTel();
                        else
                            lngNoTel = Long.parseLong(strNoTel);

                        if(String.valueOf(lngNoTel).length() != 10) {
                            System.err.println("\nLe numéro de téléphone doit être d'une longueur exacte de 10 charactère!\n");
                            System.out.println("=========================================");
                            continue;
                        }

                        visiteur.setLngNoTel(lngNoTel);
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe numéro de téléphone doit être un chiffre entier et d'une longueur exacte de 10 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (lngNoTel > 0 && String.valueOf(lngNoTel).length() == 10) {
                    System.out.println("Numéro de Téléphone: " + String.valueOf(lngNoTel));
                    visiteur.setLngNoTel(lngNoTel);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                    System.out.print("Nom [" + visiteur.getStrNom() + "]: ");
                    strNom = userInputModifierVisiteurs.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strNom.trim().isEmpty())
                        strNom = visiteur.getStrNom();
                    else if(strNom.trim().length() > 50) {
                        System.out.println("\nLe nom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    visiteur.setStrNom(strNom);
                }
                else if(!strNom.trim().isEmpty() && strNom.trim().length() <= 50) {
                    System.out.println("Nom: " + strNom);
                    visiteur.setStrNom(strNom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strPrenom.trim().isEmpty() || strPrenom.trim().length() > 50) {
                    System.out.print("Prénom [" + visiteur.getStrPrenom() + "]: ");
                    strPrenom = userInputModifierVisiteurs.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strPrenom.trim().isEmpty())
                        strPrenom = visiteur.getStrPrenom();
                    else if(strPrenom.trim().length() > 50) {
                        System.out.println("\nLe prénom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    visiteur.setStrPrenom(strPrenom);
                }
                else if(!strPrenom.trim().isEmpty() && strPrenom.trim().length() <= 50) {
                    System.out.println("Prénom: " + strPrenom);
                    visiteur.setStrPrenom(strPrenom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strAdresse.trim().isEmpty() || strAdresse.trim().length() > 100) {
                    System.out.print("Adresse [" + visiteur.getStrAdresse() + "]: ");
                    strAdresse = userInputModifierVisiteurs.nextLine().trim();

                    switch(strAdresse) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strAdresse.trim().isEmpty())
                        strAdresse = visiteur.getStrAdresse();
                    else if(strAdresse.trim().length() > 100) {
                        System.out.println("\nL'adresse ne doit pas être vide et ne pas dépasser 100 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    visiteur.setStrAdresse(strAdresse);
                }
                else if(!strAdresse.trim().isEmpty() && strAdresse.trim().length() <= 100) {
                    System.out.println("Adresse: " + strAdresse);
                    visiteur.setStrAdresse(strAdresse);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strCodePostal.trim().isEmpty() || strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.print("Code Postal [" + visiteur.getStrCodePostal() + "]: ");
                    strCodePostal = userInputModifierVisiteurs.nextLine().trim();

                    switch(strCodePostal) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strCodePostal.trim().isEmpty())
                        strCodePostal = visiteur.getStrCodePostal();
                    else if(strCodePostal.trim().length() > 6 || !Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                        System.out.println("\nPour que le code postal soit valide:\n-Ne doit pas être vide (A1A1A1)\n-Lettres en majuscules\n-Ne pas avoir les lettres [D, F, I, O, Q, U]\n-Ne pas avoir les lettres [W, Z] en première position\n-Ne pas dépasser 6 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    visiteur.setStrCodePostal(strCodePostal);
                }
                else if(!strCodePostal.trim().isEmpty() && strCodePostal.trim().length() <= 6 && Pattern.compile(regexPostalCodeValidator).matcher(strCodePostal).find()) {
                    System.out.println("Code Postal: " + strCodePostal);
                    visiteur.setStrCodePostal(strCodePostal);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strVille.trim().isEmpty() || strVille.trim().length() > 50) {
                    System.out.print("Ville [" + visiteur.getStrVille() + "]: ");
                    strVille = userInputModifierVisiteurs.nextLine().trim();

                    switch(strVille) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsVisiteur = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                            break;
                    }

                    if (strVille.trim().isEmpty())
                        strVille = visiteur.getStrVille();
                    else if(strVille.trim().length() > 50) {
                        System.out.println("\nLa ville ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    visiteur.setStrVille(strVille);
                }
                else if(!strVille.trim().isEmpty() && strVille.trim().length() <= 50) {
                    System.out.println("Ville: " + strVille);
                    visiteur.setStrVille(strVille);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if (strCourriel.trim().isEmpty()) {
                    System.out.print("Adresse Courriel [" + visiteur.getStrCourriel() + "]: ");
                    strCourriel = userInputModifierVisiteurs.nextLine().trim();

                    if (strCourriel.trim().isEmpty())
                        strCourriel = visiteur.getStrCourriel();
                }
                visiteur.setStrCourriel(strCourriel);

                // if (visiteur == null)
                //     continue;

                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(visiteur);

                System.out.print("\nConfirmer la modification [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputModifierVisiteurs.nextLine().trim(); //optionMenuModifierVisiteurs.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        changeDataListVisiteurs(EnumTypeCRUD.UPDATE, visiteur, Config.strDataFileVisiteurs, false);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.out.println("\nModification du compte visiteur réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuModifierDetailsVisiteur = false;
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
            userInputModifierVisiteurs.close();
        }
    }

    // Menu Gerer Compte Visiteur
    private void menuGestionCompteVisiteur() {
        Scanner optionMenuModifierVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Gestion d'un Compte Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuModifierVisiteurs = true;

            while(blnLoopMenuModifierVisiteurs) {
                ArrayList<Visiteur> arrVisiteurs = readDataListVisiteurs(Config.strDataFileVisiteurs, false);
                System.out.println("-----------------------------------------");

                if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                    System.out.println("\nLISTE DES VISITEURS VIDES!");
                    
                    blnLoopMenuModifierVisiteurs = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.out.println("\nListe valide des visiteurs\n");
                    for(int i=0; i<arrVisiteurs.size(); i++)
                        System.out.println("["+i+"] " + arrVisiteurs.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayVisiteurIndex;

                System.out.print("\nChoisissez l'index de le visiteur à modifier parmi la liste: ");
                String strVisiteurIndex = optionMenuModifierVisiteurs.nextLine().trim();

                switch(strVisiteurIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierVisiteurs = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierVisiteurs = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayVisiteurIndex = Integer.parseInt(strVisiteurIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de le visiteur doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayVisiteurIndex = -1;
                    continue;
                }

                if (intArrayVisiteurIndex >= 0 && intArrayVisiteurIndex < arrVisiteurs.size()) {
                    Visiteur visiteur = arrVisiteurs.get(intArrayVisiteurIndex);

                    System.out.println("=========================================");
                    menuGestionCompteDetailsVisiteur(visiteur);
                }
                else {
                    System.err.println("\nErreur! L'index de le visiteur doit se situer entre [0] et [" + (arrVisiteurs.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierVisiteurs.close();
        }
    }

    // Menu Gerer Compte Details Visiteur
    private void menuGestionCompteDetailsVisiteur(Visiteur visiteur) {
        Scanner optionMenuGestionCompteDetailsVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Gestion d'un Compte Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionCompteDetailsVisiteurs = true;
            while (blnLoopMenuGestionCompteDetailsVisiteurs) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                System.out.println("[1] Lister Infos du Visiteur");
                System.out.println("[2] Gérer Entrevue du Visiteur");
                System.out.println("[3] Gérer Rapport Vaccination du Visiteur");
                System.out.println("[4] Gérer Suivi Vaccination du Visiteur");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuGestionCompteDetailsVisiteurs.nextLine()) {
                    // Menu Infos Visiteur
                    case "1":
                        System.out.println("=========================================");
                        // ArrayList<Visiteur> arrVisiteurs = readDataListVisiteurs(Config.strDataFileVisiteurs, true);
                        // System.out.println("-----------------------------------------");
                                                
                        // if (arrVisiteurs == null || arrVisiteurs.size() <= 0) {
                        //     System.out.println("\nLISTE DES VISITEURS VIDES!");
                        //     System.out.println("=========================================");
                        // }
                        // else {
                        //     System.out.println("\nListe valide des visiteurs\n");
                        //     for(int i=0; i<arrVisiteurs.size(); i++)
                        //         System.out.println("["+i+"] " + arrVisiteurs.get(i).toString() + "\n");
                        //     System.out.println("=========================================");
                        // }
                        System.out.println("\n" + visiteur + "\n");
                        System.out.println("=========================================");
                        break;

                    // Menu Entrevue Visiteur
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenuGestionCompteDetailsVisiteurs = false;
                        menuEntrevueVisiteur(visiteur);
                        break;

                    // Menu Rapport Vaccination Visiteur
                    case "3":
                        System.out.println("=========================================");
                        // blnLoopMenuGestionCompteDetailsVisiteurs = false;
                        menuRapportVaccinationVisiteur(visiteur);
                        break;

                    // Menu Suivi Vaccination Visiteur
                    case "4":
                        System.out.println("=========================================");
                        // blnLoopMenuGestionCompteDetailsVisiteurs = false;
                        menuSuiviVaccinationVisiteur(visiteur);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionCompteDetailsVisiteurs = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionCompteDetailsVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionCompteDetailsVisiteurs = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    default:
                        System.out.println("Veuillez entrer un menu valide!");
                        System.out.println("=========================================");
                        break;
                }
            }
        }
        finally {
            optionMenuGestionCompteDetailsVisiteurs.close();
        }
    }

    private void menuEntrevueVisiteur(Visiteur visiteur) {
        Scanner optionMenuGestionVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Entrevue du Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionVisiteurs = true;
            while (blnLoopMenuGestionVisiteurs) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                System.out.println("[1] Lister toutes les Entrevues du Visiteur");
                System.out.println("[2] Ajouter un Entrevue Visiteur");
                System.out.println("[3] Modifier un Entrevue Visiteur");
                System.out.println("[4] Supprimer un Entrevue Visiteur");
                System.out.print("\nChoisissez une option parmi la liste: ");

                //! crash
                //! ArrayList<Entrevue> arrEntrevues = crudDataListEntrevues(EnumTypeCRUD.READ, visiteur, Config.strDataFileEntrevues, true);
                switch (optionMenuGestionVisiteurs.nextLine()) {
                    // Menu Lister Entrvue Visiteur
                    case "1":
                        System.out.println("=========================================");
                        ArrayList<Entrevue> arrEntrevues1 = crudDataListEntrevues(EnumTypeCRUD.READ, visiteur, Config.strDataFileEntrevues, true);
                        System.out.println("-----------------------------------------");
                                                
                        if (arrEntrevues1 == null || arrEntrevues1.size() <= 0) {
                            System.out.println("\nLISTE DES ENTREVUES VIDES!");
                            System.out.println("=========================================");
                        }
                        else {
                            System.out.println("\nListe valide des entrevues\n");
                            for(int i=0; i<arrEntrevues1.size(); i++)
                                System.out.println("["+i+"] " + arrEntrevues1.get(i).toString() + "\n");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Ajouter Entrveue Visiteur
                    case "2":
                        System.out.println("=========================================");
                        ArrayList<Entrevue> arrEntrevues2 = crudDataListEntrevues(EnumTypeCRUD.READ, visiteur, Config.strDataFileEntrevues, false);
                        if (arrEntrevues2 == null || arrEntrevues2.size() <= 0) {
                            // blnLoopMenuGestionVisiteurs = false;
                            menuAjouterEntrevueVisiteur(visiteur);
                        }
                        else {
                            System.out.println("\nVOUS NE POUVEZ AVOIR QU'UNE SEULE ENTREVUE AVEC LE VISITEUR!\n");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Modifier Entrevue Visiteur
                    case "3":
                        // System.out.println("=========================================");               
                        // if (arrEntrevues == null || arrEntrevues.size() <= 0) {
                        System.out.println("=========================================");
                        // blnLoopMenuGestionVisiteurs = false;
                        menuModifierEntrevueVisiteur(visiteur);
                        // }
                        // else {
                        //     System.out.println("\nVOUS NE POUVEZ AVOIR QU'UNE SEULE ENTREVUE AVEC LE VISITEUR!\n");
                        //     System.out.println("=========================================");
                        // }
                        break;

                    // Menu Supprimer Entrevue Visiteur
                    case "4":
                        // System.out.println("=========================================");
                        // ArrayList<Entrevue> arrEntrevues4 = crudDataListEntrevues(EnumTypeCRUD.READ, visiteur, Config.strDataFileEntrevues, true);
                        // if (arrEntrevues4 != null && arrEntrevues4.size() > 0) {
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        menuSupprimerEntrevueVisiteur(visiteur);
                        // }
                        // else {
                        //     System.out.println("\nIMPOSSIBLE DE SUPPRIMER UNE ENTREVUE QUI N'EXISTE PAS!\nAUCUNE ENTREVUE DISPONIBLE POUR CE VISITEUR!\n");
                        //     System.out.println("=========================================");
                        // }
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionVisiteurs = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionVisiteurs = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        menuGestionCompteDetailsVisiteur(visiteur);
                        break;

                    default:
                        System.out.println("Veuillez entrer un menu valide!");
                        System.out.println("=========================================");
                        break;
                }
            }
        }
        finally {
            optionMenuGestionVisiteurs.close();
        }
    }

    // Reads CSV File Entrevues
    private ArrayList<Entrevue> crudDataListEntrevues(EnumTypeCRUD enumTypeCRUD, Visiteur userInputVisiteur, String strFileName, boolean blnShowVerbose, Entrevue...userInputEntrevue) {
        if ((enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) && (userInputEntrevue == null || userInputEntrevue.length <= 0 || userInputEntrevue[0] == null))
            return null;

        // String tempFile = "";
        // File oldFile = null, newFile = null;
        // if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE){
        //     tempFile = Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
        //     oldFile = new File(strFileName);
        //     newFile = new File(tempFile);
        // }
        String tempFile = Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
        File oldFile = new File(strFileName), newFile = new File(tempFile);

        ArrayList<Entrevue> arrEntrevues = new ArrayList<>();

        int intCurrentLineNumber = 0;

        try (FileWriter fw = new FileWriter(tempFile, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw);) {

            FileReader fr = new FileReader(strFileName);
            BufferedReader reader = new BufferedReader(fr);

            // PrintWriter out = null;
            // if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
            //     // FileWriter fw = new FileWriter(tempFile, true); 
            //     // BufferedWriter bw = new BufferedWriter(fw); 
            //     // out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, true)));
            // }

            // BufferedReader reader = null;
            // if (enumTypeCRUD == EnumTypeCRUD.READ) {
            //     reader = new BufferedReader(new FileReader(new File(strFileName)));
            // }
            // else if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
            //     FileReader fr = new FileReader(strFileName);
            //     reader = new BufferedReader(fr);
            // }

            if (enumTypeCRUD == EnumTypeCRUD.READ) {
                // read & ignore first line since it contains comments on how to structure csv file
                reader.readLine();
                intCurrentLineNumber++;
            }

            String strCurrentLine = null;
            while((strCurrentLine = reader.readLine()) != null) {
                intCurrentLineNumber++;
            
                // ignore empty lines in csv file
                if (strCurrentLine.trim().length() > 0) {
                    Entrevue entrevue = new Entrevue();

                    String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVMainSeparator + "\\s*");

                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            if(i==0) {
                                // System.out.println("\n" + arrCurrentLigne[i] + "\n");
                                int intEntrevue;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    intEntrevue = Integer.parseInt(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intEntrevue' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intEntrevue = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intEntrevue' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    intEntrevue = 0;
                                }
                                entrevue.setIntEntrevue(intEntrevue);
                            }
                            else if(i==1) {
                                long lngNoCompteVisiteur;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    lngNoCompteVisiteur = Long.parseLong(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'lngNoCompteVisiteur' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    lngNoCompteVisiteur = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'lngNoCompteVisiteur' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    lngNoCompteVisiteur = 0;
                                }
                                entrevue.setLngNoCompteVisiteur(lngNoCompteVisiteur);
                            }
                            else if(i==2) {
                                int intNombreDeDosesRecu;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    intNombreDeDosesRecu = Integer.parseInt(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intNombreDeDosesRecu' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intNombreDeDosesRecu = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intNombreDeDosesRecu' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    intNombreDeDosesRecu = 0;
                                }
                                entrevue.setIntNombreDeDosesRecu(intNombreDeDosesRecu);
                            }
                            else if(i==3) {
                                String strDate;
                                try {
                                    LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    strDate = localDate.toString();

                                    if(strDate.trim().isEmpty()) {
                                        System.out.println("Error: 'strDate' is empty at line " + intCurrentLineNumber);
                                        throw new Exception("'dateEntrevue' est vide!");
                                    }

                                    Date dateEntrevue = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

                                    // verifie si la date du rendez-vous est plus petit que la date du jour
                                    if(todayDate.compareTo(dateEntrevue) > 0) {
                                        System.out.println("Error: 'dateEntrevue' est plus petite que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                        throw new Exception("'dateEntrevue' est plus petit que la date d'aujourd'hui!");
                                    }
                                }
                                catch (DateTimeParseException dtpe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: La dateEntrevue doit être une date valide (yyyy-mm-dd) et plus grande ou égale à la date d'aujourd'hui, 'strDate' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
                                    strDate = "";
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strDate' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strDate = "";
                                }
                                catch (Exception e) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strDate' Exception at line " + intCurrentLineNumber + ":\t" + e.toString());
                                    strDate = "";
                                }
                                entrevue.setStrDate(strDate);
                            }
                            else if(i==4) {
                                EnumTypeVaccin enumTypeVaccin;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    enumTypeVaccin = EnumTypeVaccin.valueOf(arrCurrentLigne[i].toUpperCase());                                        
                                }
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'enumTypeVaccin' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    enumTypeVaccin = null;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'enumTypeVaccin' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    enumTypeVaccin = null;
                                }
                                catch (IllegalArgumentException iae) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'enumTypeVaccin' Illegal Argument Exception at line " + intCurrentLineNumber + ":\t" + iae.toString());
                                    enumTypeVaccin = null;
                                }
                                catch (RuntimeException re) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'enumTypeVaccin' Runtime Exception at line " + intCurrentLineNumber + ":\t" + re.toString());
                                    enumTypeVaccin = null;
                                }
                                entrevue.setEnumMarqueVaccin(enumTypeVaccin);
                            }
                            else if(i==5) {
                                String strNoAssuranceMaladie;
                                try {
                                    strNoAssuranceMaladie = arrCurrentLigne[i];
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strNoAssuranceMaladie' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strNoAssuranceMaladie = "";
                                }
                                entrevue.setStrNoAssuranceMaladie(strNoAssuranceMaladie);
                            }
                            else if(i==6) {
                                String[] strSymptomes;
                                try {
                                    // remove first and last brackets [], then split and put in string array
                                    strSymptomes = arrCurrentLigne[i].substring(1, arrCurrentLigne[i].length()-1).trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");

                                    // System.out.println("\n" + arrCurrentLigne[i].substring(1, arrCurrentLigne[i].length()-1).trim().split(";")[0]);
                                    // System.out.println(Arrays.toString(arrCurrentLigne[i].substring(1, arrCurrentLigne[i].length()-1).trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*")));
                                    // System.out.println(Arrays.toString(strSymptomes));

                                    if(strSymptomes == null || strSymptomes.length <= 0)
                                        throw new NullPointerException();
                                }
                                catch (NullPointerException npe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strSymptomes' NullPointerException at line " + intCurrentLineNumber + ":\t" + npe.toString());
                                    strSymptomes = null;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strSymptomes' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strSymptomes = null;
                                }
                                entrevue.setArrSymptomes(strSymptomes);
                                // System.out.println(entrevue.getStrSymptomes() + "\n");
                            }
                            else if(i==7) {
                                String[] strAllergies;
                                try {
                                    // remove first and last brackets [], then split and put in string array
                                    strAllergies = arrCurrentLigne[i].substring(1, arrCurrentLigne[i].length()-1).trim().split("\\s*" + Config.strCSVSecondarySeparator + "\\s*");

                                    if(strAllergies == null || strAllergies.length <= 0)
                                        throw new NullPointerException();
                                }
                                catch (NullPointerException npe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strAllergies' NullPointerException at line " + intCurrentLineNumber + ":\t" + npe.toString());
                                    strAllergies = null;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strAllergies' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strAllergies = null;
                                }
                                entrevue.setArrAllergies(strAllergies);
                            }
                            else if(i==8) {
                                boolean blnDejaEuCovid;
                                try {
                                    blnDejaEuCovid = Boolean.parseBoolean(arrCurrentLigne[i]);
                                } 
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'blnDejaEuCovid' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    blnDejaEuCovid = false;
                                }
                                entrevue.setBlnDejaEuCovid(blnDejaEuCovid);
                            }
                        }
                    }

                    if (enumTypeCRUD == EnumTypeCRUD.READ) {
                        // Ignore validity of date for now, but check intEntrevue>0 and if NoCompte of EntrevueVisiteur & NoCompte of UserInputVisiteur match
                        if (entrevue.getIntEntrevue()>0 && entrevue.getLngNoCompteVisiteur()==userInputVisiteur.getLngNoCompte() /*&& !entrevue.getStrDate().trim().isEmpty()*/)
                            arrEntrevues.add(entrevue);
                    }
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                        if (entrevue.getIntEntrevue()>0 && entrevue.getIntEntrevue() == userInputEntrevue[0].getIntEntrevue()) {
                            System.out.println("\nSuccès: L'entrevue visiteur '" + Integer.toString(entrevue.getIntEntrevue()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));

                            arrEntrevues.add(entrevue);

                            if (enumTypeCRUD == EnumTypeCRUD.UPDATE)
                                System.out.println("Modification de l'entrevue visiteur dans le fichier");
                            if (enumTypeCRUD == EnumTypeCRUD.DELETE)
                                System.out.println("Suppression de l'entrevue visiteur dans le fichier");
                        }
                        else 
                            out.println(strCurrentLine);
                    }
                }
                else if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                    out.println(strCurrentLine);
                }
            }

            if (enumTypeCRUD == EnumTypeCRUD.UPDATE || enumTypeCRUD == EnumTypeCRUD.DELETE) {
                // if modifying visiteur, add visiteur to end of file
                if (enumTypeCRUD == EnumTypeCRUD.UPDATE)
                    out.println(userInputEntrevue[0].printInfosEntrevue());

                // out.flush();
                oldFile.delete();
                File dump = new File(strFileName);
                newFile.renameTo(dump);

                if (enumTypeCRUD == EnumTypeCRUD.DELETE)
                    System.out.println("\nSuppression de l'entrevue visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
                else if (enumTypeCRUD == EnumTypeCRUD.UPDATE)
                    System.out.println("\nModification de l'entrevue visiteur '" + Long.toString(userInputVisiteur.getLngNoCompte()) + "' réussi!\n");
                
                if (arrEntrevues == null || arrEntrevues.size() <= 0) {
                    if (enumTypeCRUD == EnumTypeCRUD.DELETE)
                        System.out.println("LA LISTE DES ENTREVUES VISITEURS SUPPRIMER EST VIDE!");
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE)
                        System.out.println("LA LISTE DES ENTREVUES VISITEURS MODIFIER EST VIDE!");
                }
                else {
                    String strStyleListe = "";
                    if (enumTypeCRUD == EnumTypeCRUD.DELETE)
                        strStyleListe = "Liste des entrevues visiteurs supprimer du fichier:";
                    else if (enumTypeCRUD == EnumTypeCRUD.UPDATE)
                        strStyleListe = "Liste des entrevues visiteurs modifier du fichier:";

                    String strStyleListeDash = "", strStyleListeDoubleDash = "";
                    for(int j=0; j<strStyleListe.length(); j++) {
                        strStyleListeDash += "-";
                        strStyleListeDoubleDash += "=";
                    }
                    
                    System.out.println(strStyleListeDoubleDash + "\n");
                    System.out.println(strStyleListe);
                    System.out.println(strStyleListeDash + "\n");
                    for(Entrevue e : arrEntrevues)
                        System.out.println(e + "\n");
                    // System.out.println();
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

        return arrEntrevues;
    }

    private void menuAjouterEntrevueVisiteur(Visiteur visiteur) {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    private void menuModifierEntrevueVisiteur(Visiteur visiteur) {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    private void menuSupprimerEntrevueVisiteur(Visiteur visiteur) {
        Scanner optionMenuSupprimerEntrevueVisiteurs = new Scanner(System.in);

        String strStyleMenuName = "Menu Supprimer Entrevue du Visiteur", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir supprimer l'entrevue visiteur qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            boolean blnLoopMenuSupprimerEntrevueVisiteurs = true,
                    blnAutoSupprimer = false;

            while (blnLoopMenuSupprimerEntrevueVisiteurs) {
                blnAutoSupprimer = false;

                // System.out.println("=========================================");
                ArrayList<Entrevue> arrEntrevues = crudDataListEntrevues(EnumTypeCRUD.READ, visiteur, Config.strDataFileEntrevues, false);
                System.out.println("-----------------------------------------");

                if (arrEntrevues == null || arrEntrevues.size() <= 0) {
                    System.out.println("\nLISTE DES ENTREVUES VISITEURS VIDES!");
                    
                    blnLoopMenuSupprimerEntrevueVisiteurs = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    menuEntrevueVisiteur(visiteur);
                }
                else {
                    //! On prend en compte que seulement l'employe peut se connecter au systeme
                    //! ne pas afficher le compte utilisateur courant pour ne pas s'auto-supprimer
                    //! loopRemoveCurrentUser: for(Visiteur e : arrVisiteurs)
                    //!     if (e.getLngNoCompte() == this.currentUserLngNoCompte) { //&& e.getIntCodeIdentification() == this.currentUserIntCodeIdentification) {
                    //!         arrVisiteurs.remove(e);
                    //!         blnAutoSupprimer = true;
                    //!         break loopRemoveCurrentUser;
                    //!     }

                    System.out.println("\nListe valide des Entrevues Visiteurs\n");
                    for(int i=0; i<arrEntrevues.size(); i++)
                        System.out.println("["+i+"] " + arrEntrevues.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }
                
                //! On prend en compte que seulement l'employe peut se connecter au systeme
                //! if (blnAutoSupprimer && (arrVisiteurs == null || arrVisiteurs.size() <= 0)) {
                //!     System.out.println("VOUS NE POUVEZ PAS VOUS SUPPRIMER!\n");

                //!     blnLoopMenuSupprimerVisiteurs = false;
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
                
                int intArrayVisiteurIndex;

                System.out.print("\nChoisissez l'index de l'entrevue à supprimer parmi la liste: ");
                String strVisiteurIndex = optionMenuSupprimerEntrevueVisiteurs.nextLine().trim();

                switch(strVisiteurIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuSupprimerEntrevueVisiteurs = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuSupprimerEntrevueVisiteurs = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuSupprimerEntrevueVisiteurs = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        menuEntrevueVisiteur(visiteur);
                        break;
                }

                try {
                    intArrayVisiteurIndex = Integer.parseInt(strVisiteurIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de l'entrevue doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    System.out.println("=========================================");
                    intArrayVisiteurIndex = -1;
                    continue;
                }
                if (intArrayVisiteurIndex >= 0 && intArrayVisiteurIndex < arrEntrevues.size()) {
                    Entrevue entrevue = arrEntrevues.get(intArrayVisiteurIndex);

                    System.out.println();
                    System.out.println(strStyleConfirmationDoubleDash + "\n");
                    System.out.println(strStyleConfirmation);
                    System.out.println(strStyleConfirmationDash + "\n");
                    System.out.println(entrevue);
    
                    System.out.print("\nConfirmer la suppression [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                    String strUserConfirmation = optionMenuSupprimerEntrevueVisiteurs.nextLine().trim();

                    switch(strUserConfirmation) {
                        case "":
                        case Config.strAppAccept:
					    case Config.strAppAcceptAlternative:
                            blnLoopMenuSupprimerEntrevueVisiteurs = false;
                            crudDataListEntrevues(EnumTypeCRUD.DELETE, visiteur, Config.strDataFileEntrevues, false, entrevue);
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            menuEntrevueVisiteur(visiteur);
                            break;
                        
                        case Config.strAppRefuse:
					    case Config.strAppRefuseAlternative:
                            blnLoopMenuSupprimerEntrevueVisiteurs = false;
                            System.out.println("\nSuppression de l'entrevue visiteur réfusé!");
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            menuEntrevueVisiteur(visiteur);
                            break;
    
                        case Config.strAppExit:
                            System.out.println("Au Revoir!");
                            System.out.println("=========================================");
                            blnLoopMenuSupprimerEntrevueVisiteurs = false;
                            System.exit(0);
                        break;
    
                        case Config.strAppDisconnect:
                            blnLoopMenuSupprimerEntrevueVisiteurs = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;
    
                        case Config.strAppMenuBack:
                            blnLoopMenuSupprimerEntrevueVisiteurs = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            menuEntrevueVisiteur(visiteur);
                            break;

                        default:
                            System.out.println("Veuillez entrer une option valide!");
                            System.out.println("=========================================");
                            continue;
                    }
                }
                else {
                    System.out.println("=========================================");
                    System.err.println("\nErreur! L'index de l'entrevue visiteur doit se situer entre [0] et [" + (arrEntrevues.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        } finally {
            optionMenuSupprimerEntrevueVisiteurs.close();
        }
    }

    private void menuRapportVaccinationVisiteur(Visiteur visiteur) {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    private void menuSuiviVaccinationVisiteur(Visiteur visiteur) {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }
}
