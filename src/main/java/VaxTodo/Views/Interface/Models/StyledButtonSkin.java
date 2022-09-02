package VaxTodo.Views.Interface.Models;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.util.Duration;

public class StyledButtonSkin extends ButtonSkin {

    public StyledButtonSkin(Button btnControl) {
        super(btnControl);

        // final PauseTransition delayTransition = new PauseTransition(Duration.millis(2000));
        // btnControl.setOnMouseEntered(e -> {
        //     // delayTransition.setOnFinished(event -> doSomething(newValue));
        //     delayTransition.playFromStart();
        // });

        
        // btnControl.hoverProperty().addListener((observable, oldValue, newValue) -> {
        //     // delayTransition.setOnFinished(event -> doSomething(newValue));
        //     delayTransition.playFromStart();
        // });

        // delayTransition.setDelay(Duration.millis(2000));
        // btnControl.hoverProperty().addListener(e -> delayTransition.playFromStart());
        // btnControl.setOnMouseEntered(e -> delayTransition.playFromStart());

        // final FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
        // fadeIn.setNode(btnControl);
        // fadeIn.setToValue(1);
        // btnControl.setOnMouseEntered(e -> fadeIn.playFromStart());

        // final FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
        // fadeOut.setNode(btnControl);
        // fadeOut.setToValue(0.5);
        // btnControl.setOnMouseExited(e -> fadeOut.playFromStart());

        // btnControl.setOpacity(0.1);
    }
}
