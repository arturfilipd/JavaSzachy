package UI.Scenes;

import UI.ScreenControler;
import UI.ScreenScene;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class MainMenu extends ScreenScene{

    public MainMenu(ScreenControler c){
        controler = c;
        VBox vb = new VBox();
        vb.setAlignment(Pos.BASELINE_LEFT);
        this.scene = new Scene(vb, 680, 480);


        Button soloGameButton = new Button("Solo game");
        soloGameButton.setPrefWidth(300);
        Button localGameButton = new Button("Local game");
        localGameButton.setPrefWidth(300);
        Button onlineGameButton = new Button("Online game");
        onlineGameButton.setPrefWidth(300);
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(300);


        VBox menuBox = new VBox(soloGameButton, localGameButton, onlineGameButton, exitButton);
        vb.setId("pane");
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setSpacing(20);
        menuBox.setFillWidth(true);

        vb.getChildren().add(menuBox);


        ////////BUTTONS
        soloGameButton.setOnAction(actionEvent ->   controler.changeScene(new SoloGameScreen(controler)));
        localGameButton.setOnAction(actionEvent ->  controler.changeScene(new LocalGameScreen(controler)));
        onlineGameButton.setOnAction(actionEvent -> controler.changeScene(new OnlineScreen(controler)));
        exitButton.setOnAction(actionEvent ->{
            Platform.exit();
            System.exit(0);
        });



    }
}
