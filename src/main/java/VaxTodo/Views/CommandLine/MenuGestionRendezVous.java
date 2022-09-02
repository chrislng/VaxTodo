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

import VaxTodo.Configs.Config;
import VaxTodo.Models.EnumTypeCRUD;
import VaxTodo.Models.EnumTypeMenuCli;
import VaxTodo.Models.RendezVous;

/** Old Menu Gestion Rendez-Vous to add, modify or delete planned visites
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class MenuGestionRendezVous {
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    // Menu Gestion RendezVous
    public void run(long currentUserLngNoCompte, int currentUserIntCodeIdentification, EnumTypeMenuCli enumTypeMenu) {
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;
        this.currentUserLngNoCompte = currentUserLngNoCompte;

        Scanner optionMenuGestionRendezVous = new Scanner(System.in);

        String strStyleMenuName = "", strStyleDash = "";
        if (enumTypeMenu == EnumTypeMenuCli.MENUGESTIONRENDEZVOUS)
            strStyleMenuName = "Menu Gestion des RendezVous";
        else if (enumTypeMenu == EnumTypeMenuCli.MENUCALENDRIER)
            strStyleMenuName = "Menu Calendrier";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionRendezVous = true;
            while (blnLoopMenuGestionRendezVous) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");

                System.out.println("[1] Lister tous les RendezVous");
                System.out.println("[2] Ajouter un RendezVous");
                if (enumTypeMenu == EnumTypeMenuCli.MENUGESTIONRENDEZVOUS) {
                    System.out.println("[3] Modifier un RendezVous");
                    System.out.println("[4] Supprimer un RendezVous");    
                }
                System.out.println("[5] Envoyer Confirmation d'un RendezVous");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuGestionRendezVous.nextLine()) {
                    // Menu Lister RendezVous
                    case "1":
                        System.out.println("=========================================");
                        ArrayList<RendezVous> arrRendezVous = readDataListRendezVous(Config.strDataFileRendezVous, true);
                        System.out.println("-----------------------------------------");
                                                
                        if (arrRendezVous == null || arrRendezVous.size() <= 0) {
                            System.out.println("\nLISTE DES RENDEZ VOUS VIDES!");
                            System.out.println("=========================================");
                        }
                        else {
                            System.out.println("\nListe valide des Rendez Vous\n");
                            for(int i=0; i<arrRendezVous.size(); i++)
                                System.out.println("["+i+"] " + arrRendezVous.get(i).toString() + "\n");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Ajouter RendezVous
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenuGestionRendezVous = false;
                        menuAjouterRendezVous(enumTypeMenu);
                        break;

                    // Menu Modifier RendezVous
                    case "3":
                        if (enumTypeMenu == EnumTypeMenuCli.MENUGESTIONRENDEZVOUS) {
                            System.out.println("=========================================");
                            blnLoopMenuGestionRendezVous = false;
                            menuModifierRendezVous();
                        }
                        else {
                            System.out.println("Veuillez entrer un menu valide!");
                            System.out.println("=========================================");
                        }
                        break;

                    // Menu Supprimer RendezVous
                    case "4":
                        if (enumTypeMenu == EnumTypeMenuCli.MENUGESTIONRENDEZVOUS) {
                            System.out.println("=========================================");
                            blnLoopMenuGestionRendezVous = false;
                            menuSupprimerRendezVous();
                        }
                        else {
                            System.out.println("Veuillez entrer un menu valide!");
                            System.out.println("=========================================");
                        }
                        break;
                    
                    // Menu Confirmation RendezVous
                    case "5":
                        System.out.println("=========================================");
                        blnLoopMenuGestionRendezVous = false;
                        menuConfirmerRendezVous(enumTypeMenu);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionRendezVous = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionRendezVous = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionRendezVous = false;
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
            optionMenuGestionRendezVous.close();
        }
    }

    // reads rendezVous csv data file and returns an arraylist of them
    private ArrayList<RendezVous> readDataListRendezVous(String strFileName, boolean blnShowVerbose) {
        ArrayList<RendezVous> arrRendezVous = new ArrayList<>();

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

                    RendezVous rendezVous = new RendezVous();

                    String[] arrCurrentLigne = strCurrentLine.trim().split("\\s*" + Config.strCSVMainSeparator + "\\s*");

                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            if(i==0) {
                                int intRendezVous;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    intRendezVous = Integer.parseInt(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intRendezVous' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intRendezVous = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intRendezVous' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    intRendezVous = 0;
                                }
                                rendezVous.setIntRendezVous(intRendezVous);
                            }
                            else if (i==1) {
                                int intTypeDose;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    intTypeDose = Integer.parseInt(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intTypeDose' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intTypeDose = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intTypeDose' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    intTypeDose = 0;
                                }
                                rendezVous.setIntTypeDose(intTypeDose);
                            }
                            else if(i==2) {
                                String strDate;
                                try {
                                    LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    strDate = localDate.toString();

                                    if(strDate.trim().isEmpty()) {
                                        System.out.println("Error: 'strDate' is empty at line " + intCurrentLineNumber);
                                        throw new Exception("'dateRendezVous' est vide!");
                                    }

                                    Date dateRendezVous = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
                                    if(todayDate.compareTo(dateRendezVous) > 0) {
                                        System.out.println("Error: 'dateRendezVous' est plus petite que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                        throw new Exception("'dateRendezVous' est plus petit que la date d'aujourd'hui!");
                                    }
                                }
                                catch (DateTimeParseException dtpe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: La date de rendez vous doit être une date valide (yyyy-mm-dd) et plus grande ou égale à la date d'aujourd'hui, 'strDate' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
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
                                rendezVous.setStrDate(strDate);
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
                                rendezVous.setStrNom(strNom);
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
                                rendezVous.setStrPrenom(strPrenom);
                            }
                            else if(i==5) {
                                String strCourriel;
                                try {
                                    strCourriel = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strCourriel = "";
                                }
                                rendezVous.setStrCourriel(strCourriel);
                            }
                        }
                    }

                    // si [intRendezVous, intTypeDose, strDate] ne sont pas vides, ajoute le dans la liste des rendez vous valides
                    if (rendezVous.getIntRendezVous()>0 && rendezVous.getIntTypeDose()>0 && !rendezVous.getStrDate().trim().isEmpty())
                        arrRendezVous.add(rendezVous);
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

        return arrRendezVous;
    }

    private void menuAjouterRendezVous(EnumTypeMenuCli enumTypeMenu) {
        Scanner userInputAjouterRendezVous = new Scanner(System.in);

        String strStyleMenuName = "Menu Ajouter un RendezVous", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir ajouter un nouveau rendez vous qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        int intRendezVous = 0, intTypeDose = 0;
        String strDate = "", strNom = "", strPrenom = "", strCourriel = "";

        // ArrayList<Employe> arrEmployes = MenuGestionEmployes.readDataListEmployes(Config.strDataFileEmployes, false);

        try {
            boolean blnLoopErrorMenuAjouterRendezVous = true;

             while (blnLoopErrorMenuAjouterRendezVous) {                                
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");

                // Generate new intRendezVous different than what is on file
                if(intRendezVous == 0) {
                    ArrayList<RendezVous> arrRendezVous = readDataListRendezVous(Config.strDataFileRendezVous, false);
                    
                    boolean blnIntRendezVousUniqueNotFound = true;
                    loopFindRendezVousUnique : while (blnIntRendezVousUniqueNotFound) {
                        intRendezVous = ThreadLocalRandom.current().nextInt(Integer.parseInt(Config.strNoRendezVousMinRnd), Integer.parseInt(Config.strNoRendezVousMaxRnd)+1);

                        boolean blnFound = true;
                        loopNestedCompareRendezVousUnique: for (RendezVous rv : arrRendezVous)
                            if (intRendezVous == rv.getIntRendezVous()) {
                                blnFound = false;
                                break loopNestedCompareRendezVousUnique;
                            }
                                                     
                        if(blnFound) {
                            blnIntRendezVousUniqueNotFound = false;
                            break loopFindRendezVousUnique;
                        }
                    }
                    // intRendezVous = ThreadLocalRandom.current().nextInt(Integer.parseInt(Config.strNoRendezVousMinRnd), Integer.parseInt(Config.strNoRendezVousMaxRnd+1));
                }
                // System.out.println(Long.toString(lngNoCompte));
                
                System.out.print("Numéro de RendezVous (Assigné automatiquement): " + Integer.toString(intRendezVous) + "\n");
                
                if(intTypeDose <= 0 || intTypeDose > Config.intFormatVaccinNbDoseMax) {
                    System.out.print("Type de Dose: ");
                    String strTypeDose = userInputAjouterRendezVous.nextLine().trim();

                    switch(strTypeDose) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                            break;
                    }

                    try {
                        intTypeDose = Integer.parseInt(strTypeDose);

                        // if(String.valueOf(intRapportVaccination).length() != 10) {
                        //     System.err.println("\nLe numéro de téléphone doit être d'une longueur exacte de 10 charactère!\n");
                        //     System.out.println("=========================================");
                        //     continue;
                        // }

                        if (intTypeDose <= 0 || intTypeDose > Config.intFormatVaccinNbDoseMax) {
                            System.err.println("\nLe type de dose doit être un chiffre entier positif et aussi plus petit que le nombre maximal de dose '" + Config.intFormatVaccinNbDoseMax + "'!\n");
                            System.out.println("=========================================");
                            continue;
                        }
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe type de dose doit être un chiffre entier positif et aussi plus petit que le nombre maximal de dose '" + Config.intFormatVaccinNbDoseMax + "'!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (intTypeDose > 0 && intTypeDose <= Config.intFormatVaccinNbDoseMax)
                    System.out.println("Type de Dose: " + String.valueOf(intTypeDose));
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if (strDate == null || strDate.trim().isEmpty()) {
                    System.out.print("Date de Rendez Vous: ");
                    strDate = userInputAjouterRendezVous.nextLine().trim();

                    switch(strDate) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                            break;
                    }

                    try {
                        LocalDate localDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        // strDate = localDate.toString();

                        if(strDate.trim().isEmpty()) {
                            System.out.println("La date de rendez vous ne doit pas être vide!");
                            throw new Exception("'dateRendezVous' vide!");
                        }

                        Date dateRendezVous = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
                        if(todayDate.compareTo(dateRendezVous) > 0) {
                            System.out.println("Error: 'dateRendezVous' doit être plus grande ou égale à la date d'aujourd'hui!");
                            throw new Exception("'dateRendezVous' est plus petit que la date d'aujourd'hui!");
                        }
                    }
                    catch (DateTimeParseException dtpe) {
                        System.err.println("Error: La date de rendez vous doit être une date valide (yyyy-mm-dd) et plus grande ou égale à la date d'aujourd'hui, 'strDate' Date Time Parse Exception:\t" + dtpe.toString());
                        System.out.println("=========================================");
                        strDate = "";
                        continue;
                    }
                    catch (IndexOutOfBoundsException ioobe) {
                        System.err.println("Error: 'strDate' Index Out Of Bound:\t" + ioobe.toString());
                        System.out.println("=========================================");
                        strDate = "";
                        continue;
                    }
                    catch (Exception e) {
                        System.err.println("Error: 'strDate' Exception at line:\t" + e.toString());
                        System.out.println("=========================================");
                        strDate = "";
                        continue;
                    }
                }
                else if (strDate != null && !strDate.trim().isEmpty()) 
                    System.out.println("Date de Rendez Vous: " + strDate);
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                    System.out.print("Nom: ");
                    strNom = userInputAjouterRendezVous.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
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
                    strPrenom = userInputAjouterRendezVous.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuAjouterRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
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

                System.out.print("Adresse Courriel: ");
                strCourriel = userInputAjouterRendezVous.nextLine().trim();

                RendezVous rendezVous = new RendezVous(intRendezVous, intTypeDose, strDate, strNom, strPrenom, strCourriel);

                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(rendezVous);

                System.out.print("\nConfirmer l'ajouter [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputAjouterRendezVous.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuAjouterRendezVous = false;
                        writeDataListRendezVous(rendezVous, Config.strDataFileRendezVous);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.out.println("\nAjout du nouveau rendez vous réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuAjouterRendezVous = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                        break;

                    default:
                        System.out.println("Veuillez entrer une option valide!");
                        System.out.println("=========================================");
                        continue;
                }
            }
        } finally {
            userInputAjouterRendezVous.close();
        }
    }

    // write & append rendezVous object into rendezVous csv data file
    private void writeDataListRendezVous(RendezVous userInputRendezVous, String strFileName) {
        try(FileWriter fw = new FileWriter(strFileName, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(userInputRendezVous.printInfosRendezVous());

            userInputRendezVous.EnvoiConfirmationRendezVous();
            System.out.println("\nAjout du nouveau rendez vous réussi!");
        } 
        catch (IOException ioe) {
            System.err.println("\nError: Ajout du nouveau rendez vous réfusé par l'application!\n" + ioe.toString());
        }
    }

    // Menu Supprimer RendezVous
    private void menuSupprimerRendezVous() {
        Scanner optionMenuSupprimerRendezVous = new Scanner(System.in);

        String strStyleMenuName = "Menu Supprimer un RendezVous", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir supprimer le rendez vous qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            boolean blnLoopMenuSupprimerRendezVous = true,
                    blnAutoSupprimer = false;

            while (blnLoopMenuSupprimerRendezVous) {
                blnAutoSupprimer = false;

                // System.out.println("=========================================");
                ArrayList<RendezVous> arrRendezVous = readDataListRendezVous(Config.strDataFileRendezVous, false);
                System.out.println("-----------------------------------------");

                if (arrRendezVous == null || arrRendezVous.size() <= 0) {
                    System.out.println("\nLISTE DES RENDEZVOUS VIDES!");
                    
                    blnLoopMenuSupprimerRendezVous = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                }
                else {
                    //! On prend en compte que seulement l'employe peut se connecter au systeme
                    //! ne pas afficher le compte utilisateur courant pour ne pas s'auto-supprimer
                    //! loopRemoveCurrentUser: for(RendezVous e : arrRendezVous)
                    //!     if (e.getLngNoCompte() == this.currentUserLngNoCompte) { //&& e.getIntCodeIdentification() == this.currentUserIntCodeIdentification) {
                    //!         arrRendezVous.remove(e);
                    //!         blnAutoSupprimer = true;
                    //!         break loopRemoveCurrentUser;
                    //!     }

                    System.out.println("\nListe valide des rendez vous\n");
                    for(int i=0; i<arrRendezVous.size(); i++)
                        System.out.println("["+i+"] " + arrRendezVous.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }
                
                //! On prend en compte que seulement l'employe peut se connecter au systeme
                //! if (blnAutoSupprimer && (arrRendezVous == null || arrRendezVous.size() <= 0)) {
                //!     System.out.println("VOUS NE POUVEZ PAS VOUS SUPPRIMER!\n");

                //!     blnLoopMenuSupprimerRendezVous = false;
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
                
                int intArrayRendezVousIndex;

                System.out.print("\nChoisissez l'index du rendez vous à supprimer parmi la liste: ");
                String strRendezVousIndex = optionMenuSupprimerRendezVous.nextLine().trim();

                switch(strRendezVousIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuSupprimerRendezVous = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuSupprimerRendezVous = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuSupprimerRendezVous = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                        break;
                }

                try {
                    intArrayRendezVousIndex = Integer.parseInt(strRendezVousIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index du rendez vous doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayRendezVousIndex = -1;
                    continue;
                }
                if (intArrayRendezVousIndex >= 0 && intArrayRendezVousIndex < arrRendezVous.size()) {
                    RendezVous rendezVous = arrRendezVous.get(intArrayRendezVousIndex);

                    System.out.println();
                    System.out.println(strStyleConfirmationDoubleDash + "\n");
                    System.out.println(strStyleConfirmation);
                    System.out.println(strStyleConfirmationDash + "\n");
                    System.out.println(rendezVous);
    
                    System.out.print("\nConfirmer la suppression [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                    String strUserConfirmation = optionMenuSupprimerRendezVous.nextLine().trim();

                    switch(strUserConfirmation) {
                        case "":
                        case Config.strAppAccept:
					    case Config.strAppAcceptAlternative:
                            blnLoopMenuSupprimerRendezVous = false;
                            changeDataListRendezVous(EnumTypeCRUD.DELETE, rendezVous, Config.strDataFileRendezVous, false);
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;
                        
                        case Config.strAppRefuse:
					    case Config.strAppRefuseAlternative:
                            blnLoopMenuSupprimerRendezVous = false;
                            System.out.println("\nSuppression du rendez vous réfusé!");
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;
    
                        case Config.strAppExit:
                            System.out.println("Au Revoir!");
                            System.out.println("=========================================");
                            blnLoopMenuSupprimerRendezVous = false;
                            System.exit(0);
                        break;
    
                        case Config.strAppDisconnect:
                            blnLoopMenuSupprimerRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;
    
                        case Config.strAppMenuBack:
                            blnLoopMenuSupprimerRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;

                        default:
                            System.out.println("Veuillez entrer une option valide!");
                            System.out.println("=========================================");
                            continue;
                    }
                }
                else {
                    System.err.println("\nErreur! L'index du rendez vous doit se situer entre [0] et [" + (arrRendezVous.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        } finally {
            optionMenuSupprimerRendezVous.close();
        }
    }

    // removes rendezVous from data csv file
    private void changeDataListRendezVous(EnumTypeCRUD enumCRUD, RendezVous userInputRendezVous, String strFileName, boolean blnShowVerbose) {
        String tempFile = Config.strResourceFolder + "tempSuppressionEmpoyes.csv";
        File oldFile = new File(strFileName),
                newFile = new File(tempFile);

        // System.out.println("\n\noldFile: " + oldFile.getAbsolutePath() + " \nnewFile: " + newFile.getAbsolutePath() + "\n");

        ArrayList<RendezVous> arrRendezVousChanged = new ArrayList<>();

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

                    RendezVous rendezVous = new RendezVous();

                    String[] arrCurrentLigne = strCurrentLine.split("\\s*" + Config.strCSVMainSeparator + "\\s*");
                    if(arrCurrentLigne.length > 0) {
                        for(int i=0; i<arrCurrentLigne.length; i++) {
                            if(i==0) {
                                int intRendezVous;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    intRendezVous = Integer.parseInt(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intRendezVous' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intRendezVous = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intRendezVous' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    intRendezVous = 0;
                                }
                                rendezVous.setIntRendezVous(intRendezVous);
                            }
                            if(i==1) {
                                int intTypeDose;
                                try {
                                    // System.out.println("\n\n'" + strInfos.split("\\s*" + Miscellaneous.strCSVMainSeparator + "\\s*")[i] + "'\n\n");
                                    intTypeDose = Integer.parseInt(arrCurrentLigne[i]);
                                } 
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intTypeDose' Number Format at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    intTypeDose = 0;
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'intTypeDose' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    intTypeDose = 0;
                                }
                                rendezVous.setIntTypeDose(intTypeDose);
                            }
                            else if (i==2) {
                                String strDate;
                                try {
                                    LocalDate localDate = LocalDate.parse(arrCurrentLigne[i], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    strDate = localDate.toString();

                                    if(strDate.trim().isEmpty()) {
                                        System.out.println("Error: 'strDate' is empty at line " + intCurrentLineNumber);
                                        throw new Exception("'dateRendezVous' est vide!");
                                    }

                                    Date dateRendezVous = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
                                    if(todayDate.compareTo(dateRendezVous) > 0) {
                                        System.out.println("Error: 'dateRendezVous' est plus petit que la date d'aujourd'hui at line " + intCurrentLineNumber);
                                        throw new Exception("'dateRendezVous' est plus petit que la date d'aujourd'hui!");
                                    }
                                }
                                catch (DateTimeParseException dtpe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: La date de rendez vous doit être une date valide (yyyy-mm-dd) et plus grande ou égale à la date d'aujourd'hui, 'strDate' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
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
                                rendezVous.setStrDate(strDate);
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
                                rendezVous.setStrNom(strNom);
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
                                rendezVous.setStrPrenom(strPrenom);
                            }
                            else if(i==5) {
                                String strCourriel;
                                try {
                                    strCourriel = arrCurrentLigne[i];
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strCourriel' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strCourriel = "";
                                }
                                rendezVous.setStrCourriel(strCourriel);
                            }
                        }
                    }

                    if (enumCRUD == EnumTypeCRUD.DELETE) {
                        if (rendezVous.getIntRendezVous()>0 && rendezVous.getIntRendezVous() == userInputRendezVous.getIntRendezVous()) {
                            System.out.println("\nSuccès: Le Rendez Vous '" + Long.toString(rendezVous.getIntRendezVous()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                            arrRendezVousChanged.add(rendezVous);

                            System.out.println("Suppression de ce rendez vous dans le fichier");
                        }
                        else 
                            out.println(strCurrentLine);
                    }
                    else if (enumCRUD == EnumTypeCRUD.UPDATE) {
                        if (rendezVous.getIntRendezVous()>0 && rendezVous.getIntRendezVous() == userInputRendezVous.getIntRendezVous()) {
                            System.out.println("\nSuccès: Le Rendez Vous '" + Long.toString(rendezVous.getIntRendezVous()) + "' est trouvé dans le fichier '" + strFileName + "' à la ligne " + Integer.toString(intCurrentLineNumber));
                            arrRendezVousChanged.add(rendezVous);
                            
                            System.out.println("Modification de ce rendez vous dans le fichier");
                        }
                        else
                            out.println(strCurrentLine);
                    }
                }
                else { // copy empty line & write into new temp file
                    out.println(strCurrentLine);
                }
            }

            // if modifying rendezVous, add rendezVous to end of file
            if (enumCRUD == EnumTypeCRUD.UPDATE)
                out.println(userInputRendezVous.printInfosRendezVous());

            // out.flush();
            oldFile.delete();
            File dump = new File(strFileName);
            newFile.renameTo(dump);

            // System.out.println("\n\nnewFile: " + newFile.getAbsolutePath() + "\ndumpFile: " + dump.getAbsolutePath() + "\n");

            if (enumCRUD == EnumTypeCRUD.DELETE)
                System.out.println("\nSuppression du rendez vous '" + Integer.toString(userInputRendezVous.getIntRendezVous()) + "' réussi!\n");
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.out.println("\nModification du rendez vous '" + Integer.toString(userInputRendezVous.getIntRendezVous()) + "' réussi!\n");

            if (arrRendezVousChanged == null || arrRendezVousChanged.size() <= 0) {
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    System.out.println("LA LISTE DES RENDEZ VOUS SUPPRIMER EST VIDE!");
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    System.out.println("LA LISTE DES RENDEZ VOUS MODIFIER EST VIDE!");
            }
            else {
                String strStyleListe = "";
                if (enumCRUD == EnumTypeCRUD.DELETE)
                    strStyleListe = "Liste des rendez vous supprimer du fichier:";
                else if (enumCRUD == EnumTypeCRUD.UPDATE)
                    strStyleListe = "Liste des rendez vous modifier du fichier:";

                String strStyleListeDash = "", strStyleListeDoubleDash = "";
                for(int j=0; j<strStyleListe.length(); j++) {
                    strStyleListeDash += "-";
                    strStyleListeDoubleDash += "=";
                }
                
                System.out.println(strStyleListeDoubleDash + "\n");
                System.out.println(strStyleListe);
                System.out.println(strStyleListeDash + "\n");
                for(RendezVous e : arrRendezVousChanged)
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
                System.err.println("\nError: Suppression du rendez vous '" + Integer.toString(userInputRendezVous.getIntRendezVous()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
            else if (enumCRUD == EnumTypeCRUD.UPDATE)
                System.err.println("\nError: Modification du rendez vous '" + Integer.toString(userInputRendezVous.getIntRendezVous()) + "' réfusé par l'application!\n\nIO Exception for file '" + strFileName + "'\n" + ioe.toString());
        }
    }

    // Menu Modifier Rendez Vous
    private void menuModifierRendezVous() {
        Scanner optionMenuModifierRendezVous = new Scanner(System.in);

        String strStyleMenuName = "Menu Modifier un RendezVous", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuModifierRendezVous = true;

            while(blnLoopMenuModifierRendezVous) {
                ArrayList<RendezVous> arrRendezVous = readDataListRendezVous(Config.strDataFileRendezVous, false);
                System.out.println("-----------------------------------------");

                if (arrRendezVous == null || arrRendezVous.size() <= 0) {
                    System.out.println("\nLISTE DES RENDEZ VOUS VIDES!");
                    
                    blnLoopMenuModifierRendezVous = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                }
                else {
                    System.out.println("\nListe valide des rendez vous\n");
                    for(int i=0; i<arrRendezVous.size(); i++)
                        System.out.println("["+i+"] " + arrRendezVous.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayRendezVousIndex;

                System.out.print("\nChoisissez l'index du rendez vous à modifier parmi la liste: ");
                String strRendezVousIndex = optionMenuModifierRendezVous.nextLine().trim();

                switch(strRendezVousIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierRendezVous = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierRendezVous = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierRendezVous = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                        break;
                }

                try {
                    intArrayRendezVousIndex = Integer.parseInt(strRendezVousIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index du rendez vous doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayRendezVousIndex = -1;
                    continue;
                }

                if (intArrayRendezVousIndex >= 0 && intArrayRendezVousIndex < arrRendezVous.size()) {
                    RendezVous rendezVous = arrRendezVous.get(intArrayRendezVousIndex);

                    menuModifierDetailsRendezVous(rendezVous);
                }
                else {
                    System.err.println("\nErreur! L'index du rendez vous doit se situer entre [0] et [" + (arrRendezVous.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierRendezVous.close();
        }
    }

    private void menuModifierDetailsRendezVous(RendezVous rendezVous) {
        boolean blnLoopErrorMenuModifierDetailsRendezVous = true;
        Scanner userInputModifierRendezVous = new Scanner(System.in);

        int intTypeDose = 0;
        String strDate = "", strNom = "", strPrenom = "", strCourriel = "";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir modifier le rendez vous qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            System.out.println("=========================================\n");
            while(blnLoopErrorMenuModifierDetailsRendezVous) {
                System.out.println("Modification du Rendez Vous:\n");
                System.out.println(rendezVous + "\n\n");

                System.out.print("Numéro de Rendez Vous (Non Modifiable): " + Integer.toString(rendezVous.getIntRendezVous()) + "\n");

                if(intTypeDose <= 0 || intTypeDose > Config.intFormatVaccinNbDoseMax) {
                    System.out.print("Type de Dose [" + Integer.toString(rendezVous.getIntTypeDose()) + "]: ");
                    String strTypeDose = userInputModifierRendezVous.nextLine().trim();

                    switch(strTypeDose) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;
                    }

                    try {
                        if (strTypeDose.trim().isEmpty())
                            intTypeDose = rendezVous.getIntTypeDose();
                        else
                            intTypeDose = Integer.parseInt(strTypeDose);

                        if(intTypeDose > Config.intFormatVaccinNbDoseMax) {
                            System.err.println("\nLe type de dose doit être un chiffre entier et inférieur au nombre maximal de dose " + Config.intFormatVaccinNbDoseMax + "!\n");
                            System.out.println("=========================================");
                            continue;
                        }

                        rendezVous.setIntTypeDose(intTypeDose);
                    }
                    catch (NumberFormatException nfe) {
                        System.err.println("\nLe type de dose doit être un chiffre entier et d'une longueur exacte de 10 charactère!\n\n" + nfe.toString() + "\n");
                        System.out.println("=========================================");
                        continue;
                    }
                }
                else if (intTypeDose > 0 && intTypeDose <= Config.intFormatVaccinNbDoseMax) {
                    System.out.println("Type de dose: " + String.valueOf(intTypeDose));
                    rendezVous.setIntTypeDose(intTypeDose);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if (strDate == null || strDate.trim().isEmpty()) {
                    System.out.print("Date de naissance [" + rendezVous.getStrDate() + "]: ");
                    strDate = userInputModifierRendezVous.nextLine().trim();

                    switch(strDate) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;
                    }
                    
                    if (strDate.trim().isEmpty())
                        strDate = rendezVous.getStrDate();
                    else {
                        try {
                            LocalDate localDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            // strDate = localDate.toString();
    
                            if(strDate.trim().isEmpty()) {
                                System.out.println("La date de rendez vous ne doit pas être vide!");
                                throw new Exception("'dateRendezVous' vide!");
                            }
    
                            Date dateRendezVous = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
                            if(todayDate.compareTo(dateRendezVous) > 0) {
                                System.out.println("Error: 'dateRendezVous' doit être plus grande ou égale à la date d'aujourd'hui!");
                                throw new Exception("'dateRendezVous' est plus petit que la date d'aujourd'hui!");
                            }
                        }
                        catch (DateTimeParseException dtpe) {
                            System.err.println("Error: La date de rendez vous doit être une date valide (yyyy-mm-dd) et plus grande ou égale à la date d'aujourd'hui, 'strDate' Date Time Parse Exception:\t" + dtpe.toString());
                            System.out.println("=========================================");
                            strDate = "";
                            continue;
                        }
                        catch (IndexOutOfBoundsException ioobe) {
                            System.err.println("Error: 'strDate' Index Out Of Bound:\t" + ioobe.toString());
                            System.out.println("=========================================");
                            strDate = "";
                            continue;
                        }
                        catch (Exception e) {
                            System.err.println("Error: 'strDate' Exception at line:\t" + e.toString());
                            System.out.println("=========================================");
                            strDate = "";
                            continue;
                        }
                    }

                    rendezVous.setStrDate(strDate);
                }
                else if (strDate != null && !strDate.trim().isEmpty()) {
                    System.out.println("Date de Rendez Vous: " + strDate);
                    rendezVous.setStrDate(strDate);
                }
                else {
                    System.out.println("=========================================");
                    continue;
                }

                if(strNom.trim().isEmpty() || strNom.trim().length() > 50) {
                    System.out.print("Nom [" + rendezVous.getStrNom() + "]: ");
                    strNom = userInputModifierRendezVous.nextLine().trim();

                    switch(strNom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;
                    }

                    if (strNom.trim().isEmpty())
                        strNom = rendezVous.getStrNom();
                    else if(strNom.trim().length() > 50) {
                        System.out.println("\nLe nom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    rendezVous.setStrNom(strNom);
                }
                else if(!strNom.trim().isEmpty() && strNom.trim().length() <= 50) {
                    System.out.println("Nom: " + strNom);
                    rendezVous.setStrNom(strNom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }

                if(strPrenom.trim().isEmpty() || strPrenom.trim().length() > 50) {
                    System.out.print("Prénom [" + rendezVous.getStrPrenom() + "]: ");
                    strPrenom = userInputModifierRendezVous.nextLine().trim();

                    switch(strPrenom) {
                        case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopErrorMenuModifierDetailsRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                            break;
                    }

                    if (strPrenom.trim().isEmpty())
                        strPrenom = rendezVous.getStrPrenom();
                    else if(strPrenom.trim().length() > 50) {
                        System.out.println("\nLe prénom ne doit pas être vide et ne pas dépasser 50 charactères!");
                        System.out.println("=========================================");
                        continue;
                    }

                    rendezVous.setStrPrenom(strPrenom);
                }
                else if(!strPrenom.trim().isEmpty() && strPrenom.trim().length() <= 50) {
                    System.out.println("Prénom: " + strPrenom);
                    rendezVous.setStrPrenom(strPrenom);
                }
                else {
                    System.out.println("=========================================");
                    continue; 
                }
                
                if (strCourriel.trim().isEmpty()) {
                    System.out.print("Adresse Courriel [" + rendezVous.getStrCourriel() + "]: ");
                    strCourriel = userInputModifierRendezVous.nextLine().trim();

                    if (strCourriel.trim().isEmpty())
                        strCourriel = rendezVous.getStrCourriel();
                }
                rendezVous.setStrCourriel(strCourriel);

                // if (rendezVous == null)
                //     continue;

                System.out.println();
                System.out.println(strStyleConfirmationDoubleDash + "\n");
                System.out.println(strStyleConfirmation);
                System.out.println(strStyleConfirmationDash + "\n");
                System.out.println(rendezVous);

                System.out.print("\nConfirmer la modification [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                String strUserConfirmation = userInputModifierRendezVous.nextLine().trim(); //optionMenuModifierRendezVous.nextLine().trim();

                switch(strUserConfirmation) {
                    case "":
                    case Config.strAppAccept:
					case Config.strAppAcceptAlternative:
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        changeDataListRendezVous(EnumTypeCRUD.UPDATE, rendezVous, Config.strDataFileRendezVous, false);
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                        break;
                    
                    case Config.strAppRefuse:
					case Config.strAppRefuseAlternative:
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.out.println("\nModification du rendez vous réfusé!");
                        System.out.println("\nRetour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopErrorMenuModifierDetailsRendezVous = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                        break;

                    default:
                        System.out.println("Veuillez entrer une option valide!");
                        System.out.println("=========================================");
                        continue;
                }
            }
        }
        finally {
            userInputModifierRendezVous.close();
        }
    }

    // Menu Confirmer Rendez Vous
    private void menuConfirmerRendezVous(EnumTypeMenuCli enumTypeMenu) {
        Scanner optionMenuModifierRendezVous = new Scanner(System.in);

        String strStyleMenuName = "Menu Confirmer un RendezVous", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        String strStyleConfirmation = "Êtes-vous sûr de vouloir envoyer une confirmation pour le rendez vous qui a comme informations:", strStyleConfirmationDash = "", strStyleConfirmationDoubleDash = "";
        for(int i=0; i<strStyleConfirmation.length(); i++) {
            strStyleConfirmationDash += "-";
            strStyleConfirmationDoubleDash += "=";
        }

        try {
            boolean blnLoopMenuModifierRendezVous = true;

            while(blnLoopMenuModifierRendezVous) {
                ArrayList<RendezVous> arrRendezVous = readDataListRendezVous(Config.strDataFileRendezVous, false);
                System.out.println("-----------------------------------------");

                if (arrRendezVous == null || arrRendezVous.size() <= 0) {
                    System.out.println("\nLISTE DES RENDEZ VOUS VIDES!");
                    
                    blnLoopMenuModifierRendezVous = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                }
                else {
                    System.out.println("\nListe valide des rendez vous\n");
                    for(int i=0; i<arrRendezVous.size(); i++)
                        System.out.println("["+i+"] " + arrRendezVous.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayRendezVousIndex;

                System.out.print("\nChoisissez l'index du rendez vous à modifier parmi la liste: ");
                String strRendezVousIndex = optionMenuModifierRendezVous.nextLine().trim();

                switch(strRendezVousIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierRendezVous = false;
                        System.exit(0);
                    break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierRendezVous = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierRendezVous = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                        break;
                }

                try {
                    intArrayRendezVousIndex = Integer.parseInt(strRendezVousIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index du rendez vous doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayRendezVousIndex = -1;
                    continue;
                }

                if (intArrayRendezVousIndex >= 0 && intArrayRendezVousIndex < arrRendezVous.size()) {
                    RendezVous rendezVous = arrRendezVous.get(intArrayRendezVousIndex);

                    System.out.println();
                    System.out.println(strStyleConfirmationDoubleDash + "\n");
                    System.out.println(strStyleConfirmation);
                    System.out.println(strStyleConfirmationDash + "\n");
                    System.out.println(rendezVous);

                    System.out.print("\nConfirmer la modification [oui par défaut] (" + Config.strAppAcceptAlternative + "|" + Config.strAppRefuse + "): ");
                    String strUserConfirmation = optionMenuModifierRendezVous.nextLine().trim(); //optionMenuModifierRendezVous.nextLine().trim();

                    switch(strUserConfirmation) {
                        case "":
                        case Config.strAppAccept:
                        case Config.strAppAcceptAlternative:
                            blnLoopMenuModifierRendezVous = false;
                            rendezVous.EnvoiConfirmationRendezVous();
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                            break;
                        
                        case Config.strAppRefuse:
                        case Config.strAppRefuseAlternative:
                            blnLoopMenuModifierRendezVous = false;
                            System.out.println("\nConfirmation du rendez vous réfusé!");
                            System.out.println("\nRetour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                            break;

                        case Config.strAppExit:
                            System.out.println("Au Revoir!");
                            System.out.println("=========================================");
                            blnLoopMenuModifierRendezVous = false;
                            System.exit(0);
                        break;

                        case Config.strAppDisconnect:
                            blnLoopMenuModifierRendezVous = false;
                            System.out.println("Déconnexion et Retour au Menu Principal");
                            System.out.println("=========================================");
                            new CliLogin().run();
                            break;

                        case Config.strAppMenuBack:
                            blnLoopMenuModifierRendezVous = false;
                            System.out.println("Retour au Menu Précédent");
                            System.out.println("=========================================");
                            run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, enumTypeMenu);
                            break;

                        default:
                            System.out.println("Veuillez entrer une option valide!");
                            System.out.println("=========================================");
                            continue;
                    }
                }
                else {
                    System.err.println("\nErreur! L'index du rendez vous doit se situer entre [0] et [" + (arrRendezVous.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierRendezVous.close();
        }
    }
}
