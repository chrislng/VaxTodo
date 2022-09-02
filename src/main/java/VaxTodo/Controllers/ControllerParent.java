package VaxTodo.Controllers;

import javafx.stage.Stage;

/** Controller of Interface ViewMenuGestion Benevoles.fxml
 * Displays data from csv file to interface and saves data from interface to csv file
 * Let the user add, modify or delete Employe accounts
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 * @deprecated Will soon be deprecated since it is not that really usefull right now ¯\_(ツ)_/¯
 */
public abstract class ControllerParent {
    // public abstract void setStageAndSetupListeners(Stage stage);
    public abstract void setStageAndSetupListeners(Stage stage, String currentUserStrFullName, long currentUserLngNoCompte, int currentUserIntCodeIdentification);
}
