package VaxTodo.Views.CommandLine;

import java.util.Scanner;

import VaxTodo.Configs.Config;
import VaxTodo.Models.EnumTypeMenuCli;

/** Old Menu Employe to choose what to do next after login in with an Employe account
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Old Command Line Interface, not needed since App supports JavaFx views
 */
public class MenuEmploye {
    // private String strCurrentUsername;
    private int currentUserIntCodeIdentification;
    private long currentUserLngNoCompte;

    public void run(long currentUserLngNoCompte, int currentUserIntCodeIdentification) {
        this.currentUserIntCodeIdentification = currentUserIntCodeIdentification;
        this.currentUserLngNoCompte = currentUserLngNoCompte;

        Scanner optionMenuEmploye = new Scanner(System.in);

        String strStyleMenuName = "Menu Employé", strStyleDash = "";
        for(int i=0; i<strStyleMenuName.length(); i++)
            strStyleDash += "-";

        try {
            boolean blnLoopMenu = true;

            while (blnLoopMenu) {
                System.out.println("\n\n" + strStyleMenuName);
                System.out.println(strStyleDash);
                System.out.println("[No Compte: '" + Long.toString(currentUserLngNoCompte) + (currentUserIntCodeIdentification>0 ? "', No Identification: '" + Integer.toString(currentUserIntCodeIdentification) : "'")  + "]\n");
                System.out.println("[" + Config.strAppExit + "] Quitter l'application");
                System.out.println("[" + Config.strAppDisconnect + "] Se Déconnecter\n");
                System.out.println("[1] Gestion des Employés");
                System.out.println("[2] Gestion des Visiteurs");
                System.out.println("[3] Gestion des Bénévoles");
                // System.out.println("[4] Gestion des Vaccinations");
                System.out.println("[4] Gestion des Rendez-Vous");
                System.out.println("[5] Calendrier");
                System.out.print("\nChoisissez une option parmi la liste: ");

                switch (optionMenuEmploye.nextLine()) {
                    // Menu Gestion Employe
                    case "1":
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        new MenuGestionEmployes().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    // Menu Gestion Visiteur
                    case "2":
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        new MenuGestionVisiteurs().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    // Menu Gestion Benevole
                    case "3":
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        new MenuGestionBenevoles().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        break;

                    // Menu Gestion Rendez-vous
                    case "4":
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        new MenuGestionRendezVous().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUGESTIONRENDEZVOUS);
                        break;

                    // Menu Calendrier
                    case "5":
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        // new MenuCalendrier().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification);
                        new MenuGestionRendezVous().run(this.currentUserLngNoCompte, this.currentUserIntCodeIdentification, EnumTypeMenuCli.MENUCALENDRIER);
                        break;

                    // Menu Gestion Vaccination
                    // case "4":
                    //     System.out.println("=========================================");
                    //     // blnLoopMenu = false;
                    //     menuGestionVaccinations();
                    //     break;

                    case Config.strAppExit:
                        System.out.println("Au Revoir!");
                        System.out.println("=========================================");
                        blnLoopMenu = false;
                        System.exit(0);
                        break;

                    case Config.strAppDisconnect:
                        blnLoopMenu = false;
                        System.out.println("Déconnexion et Retour au Menu Principal");
                        System.out.println("=========================================");
                        new CliLogin().run();
                        break;

                    default:
                        System.out.println("Veuillez entrer un menu valide!");
                        System.out.println("=========================================");
                        break;
                }
            }
        } finally {
            optionMenuEmploye.close();
        }
    }

    // ToDo implemente menuGestionVisiteurs()
    private void menuGestionVisiteurs() {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    // ToDo implemente menuGestionBenevoles()
    private void menuGestionBenevoles() {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    // ToDo implemente menuGestionVaccinations()
    private void menuGestionVaccinations() {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    // ToDo implemente menuGestionRendezVous()
    private void menuGestionRendezVous() {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }

    // ToDo implemente menuCalendrier()
    private void menuCalendrier() {
        System.err.println("!!! Not yet implemented !!!");
        System.err.println("=========================================");
    }
}
