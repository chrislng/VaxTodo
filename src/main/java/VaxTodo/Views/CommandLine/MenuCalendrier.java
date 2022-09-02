package VaxTodo.Views.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import VaxTodo.Configs.Config;
import VaxTodo.Models.Calendrier;

/** Old Calendar to add, modify or delete Visites Planifiées
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class MenuCalendrier {
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    // Menu Gestion Calendrier
    public void run(long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;
        this.currentUserLngNoCompte = currentUserLngNoCompte;
        
        Scanner optionMenuGestionCalendriers = new Scanner(System.in);

        String strStyleMenuName = "Menu Gestion des Calendriers", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuGestionCalendriers = true;
            while (blnLoopMenuGestionCalendriers) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                // System.out.println("[1] Lister tous les Calendriers");
                // System.out.println("[2] Séléctionner un Calendrier");
                System.out.println("[1] Lister tous les RendezVous");
                System.out.println("[2] Ajouter un RendezVous");
                System.out.println("[3] Envoyer Confirmation d'un RendezVous");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuGestionCalendriers.nextLine()) {
                    // Menu Afficher Liste Calendriers
                    case "1":
                        System.out.println("=========================================");
                        ArrayList<Calendrier> arrCalendriers = readDataListCalendriers(Config.strDataFileCalendriers, true);
                        System.out.println("-----------------------------------------");
                                                
                        if (arrCalendriers == null || arrCalendriers.size() <= 0) {
                            System.out.println("\nLISTE DES CALENDRIERS VIDES!");
                            System.out.println("=========================================");
                        }
                        else {
                            System.out.println("\nListe valide des calendriers\n");
                            for(int i=0; i<arrCalendriers.size(); i++)
                                System.out.println("["+i+"] " + arrCalendriers.get(i).toString() + "\n");
                            System.out.println("=========================================");
                        }
                        // System.out.println("=========================================");
                        // ArrayList<RendezVous> arrRendezVous = MenuGestionRendezVous.readDataListRendezVous(Config.strDataFileRendezVous, true);
                        // System.out.println("-----------------------------------------");
                                                
                        // if (arrRendezVous == null || arrRendezVous.size() <= 0) {
                        //     System.out.println("\nLISTE DES RENDEZ VOUS VIDES!");
                        //     System.out.println("=========================================");
                        // }
                        // else {
                        //     System.out.println("\nListe valide des Rendez Vous\n");
                        //     for(int i=0; i<arrRendezVous.size(); i++)
                        //         System.out.println("["+i+"] " + arrRendezVous.get(i).toString() + "\n");
                        //     System.out.println("=========================================");
                        // }
                        break;
                    
                    // // Menu Ajouter Rendez Vous
                    // case "2":
                    //     System.out.println("=========================================");
                    //     blnLoopMenuGestionCalendriers = false;
                    //     MenuGestionRendezVous.menuAjouterRendezVous(EnumTypeMenu.MENUCALENDRIER, this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                    //     break;

                    // // Menu Confirmer Rendez Vous
                    // case "3":
                    //     System.out.println("=========================================");
                    //     blnLoopMenuGestionCalendriers = false;
                    //     MenuGestionRendezVous.menuConfirmerRendezVous(EnumTypeMenu.MENUCALENDRIER, this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                    //     break;

                    // Menu Lister Calendrier
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenuGestionCalendriers = false;
                        menuSelectionnerCalendrier();
                        break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuGestionCalendriers = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuGestionCalendriers = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuGestionCalendriers = false;
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
            optionMenuGestionCalendriers.close();
        }
    }

    private ArrayList<Calendrier> readDataListCalendriers(String strFileName, boolean blnShowVerbose) {
        ArrayList<Calendrier> arrCalendriers = new ArrayList<>();

        int intCurrentLineNumber = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(strFileName)))) {
            // read & ignore first line since it contains comments on how to structure csv file
            reader.readLine();
            intCurrentLineNumber++;

            String strCurrentLine = null;
            while((strCurrentLine = reader.readLine()) != null) {
                intCurrentLineNumber++;

                // ignore empty lines in csv file
                if (strCurrentLine.trim().length() > 0) {
                    Calendrier calendrier = new Calendrier();

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
                                calendrier.setIntRendezVous(intRendezVous);
                            }
                            else if(i==1) {
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
                                        System.err.println("Error: 'strDate' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
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
                                calendrier.setStrDate(strDate);
                            }
                            else if(i==2) {
                                String strHeure;
                                try {
                                    int intHeure, intMinutes;

                                    String[] arrHeure = arrCurrentLigne[i].trim().split("\\s*" + Config.strCSVHoursSeparator + "\\s*");

                                    if (arrHeure.length <= 0 && arrHeure.length >= 3)
                                        throw new NumberFormatException();

                                    intHeure = Integer.parseInt(arrHeure[0]);
                                    intMinutes = Integer.parseInt(arrHeure[1]);

                                    if(intHeure < 0 || intHeure > 24 || intMinutes < 0 || intMinutes > 60)
                                        throw new NumberFormatException();

                                    strHeure = Integer.toString(intHeure) + ":" + Integer.toString(intMinutes);

                                    if(strHeure.trim().isEmpty())
                                        System.out.println("Error: 'strHeure' is empty at line " + intCurrentLineNumber);
                                }
                                catch (NumberFormatException nfe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strHeure' Number Format (Not 'HH:MM' format) at line " + intCurrentLineNumber + ":\t" + nfe.toString());
                                    strHeure = "";
                                }
                                catch (DateTimeParseException dtpe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strHeure' Date Time Parse Exception at line " + intCurrentLineNumber + ":\t" + dtpe.toString());
                                    strHeure = "";
                                }
                                catch (IndexOutOfBoundsException ioobe) {
                                    if (blnShowVerbose)
                                        System.err.println("Error: 'strHeure' Index Out Of Bound at line " + intCurrentLineNumber + ":\t" + ioobe.toString());
                                    strHeure = "";
                                }
                                calendrier.setStrHeure(strHeure);
                            }
                        }
                    }

                    // System.out.println("\n" + calendrier);

                    // if all fields are present, add into array to be displayed later
                    if (calendrier.getIntRendezVous()>0 && !calendrier.getStrDate().trim().isEmpty() && !calendrier.getStrHeure().trim().isEmpty())
                        arrCalendriers.add(calendrier);
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

        return arrCalendriers;
    }

    // Menu Imprimer Calendrier
    private void menuSelectionnerCalendrier() {
        Scanner optionMenuModifierCalendriers = new Scanner(System.in);

        String strStyleMenuName = "Menu Modifier un Calendrier", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenuModifierCalendriers = true;

            while(blnLoopMenuModifierCalendriers) {
                ArrayList<Calendrier> arrCalendriers = readDataListCalendriers(Config.strDataFileCalendriers, false);
                System.out.println("-----------------------------------------");

                if (arrCalendriers == null || arrCalendriers.size() <= 0) {
                    System.out.println("\nLISTE DES CALENDRIERS VIDES!");
                    
                    blnLoopMenuModifierCalendriers = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.out.println("\nListe valide des calendriers\n");
                    for(int i=0; i<arrCalendriers.size(); i++)
                        System.out.println("["+i+"] " + arrCalendriers.get(i).toString() + "\n");
                    System.out.println("=========================================");
                }

                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter");
                System.out.println("[" + Config.strAppMenuBack + "] Retourner au Menu Précédent\n");
                
                int intArrayCalendrierIndex;

                System.out.print("\nChoisissez l'index de le calendrier à modifier parmi la liste: ");
                String strCalendrierIndex = optionMenuModifierCalendriers.nextLine().trim();

                switch(strCalendrierIndex) {
                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenuModifierCalendriers = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenuModifierCalendriers = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    case Config.strAppMenuBack:
                        blnLoopMenuModifierCalendriers = false;
                        System.out.println("Retour au Menu Précédent");
                        System.out.println("=========================================");
                        run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;
                }

                try {
                    intArrayCalendrierIndex = Integer.parseInt(strCalendrierIndex);
                }
                catch (NumberFormatException nfe) {
                    System.err.println("\nL'index de le calendrier doit être un chiffre entier valide! (ex. [0])\n\n" + nfe.toString() + "\n");
                    intArrayCalendrierIndex = -1;
                    continue;
                }

                if (intArrayCalendrierIndex >= 0 && intArrayCalendrierIndex < arrCalendriers.size()) {
                    Calendrier calendrier = arrCalendriers.get(intArrayCalendrierIndex);

                    menuModifierDetailsCalendrier(calendrier);
                    
                    blnLoopMenuModifierCalendriers = false;
                    System.out.println("Retour au Menu Précédent");
                    System.out.println("=========================================");
                    run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                }
                else {
                    System.err.println("\nErreur! L'index de le calendrier doit se situer entre [0] et [" + (arrCalendriers.size()-1) + "] (ex. [0])\n");
                    continue;
                }
            }
        }
        finally {
            optionMenuModifierCalendriers.close();
        }
    }

    private void menuModifierDetailsCalendrier(Calendrier calendrier) {
        System.out.println("\n\nImpression du Calendrier");
        System.out.println("-------------------------");
        System.out.println(calendrier);
        System.out.println();
    }
}
