package UI;

import UI.Scenes.MainMenu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class Main extends Application{

    public static void main (String[] args) {launch(args);}

    private ScreenControler controler = new ScreenControler();


    @Override
    public void start(Stage primaryStage) throws  Exception {

        controler.stage = primaryStage;
        controler.start();


        Timeline updater = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Event Handling
            }
        }));
        updater.setCycleCount(Timeline.INDEFINITE);
        updater.play();
    }


}
