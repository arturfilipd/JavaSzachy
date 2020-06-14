package UI.Scenes;

import UI.ScreenControler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SoloGameSettings extends GameScreen{
    public SoloGameSettings(ScreenControler c){
        super(c);

        VBox vb = new VBox();
        vb.setId("pane");
        vb.setAlignment(Pos.CENTER_LEFT);
        vb.setSpacing(20);
        newScene(vb);
        VBox panel = new VBox();
        panel.setSpacing(20);
        panel.setAlignment(Pos.BASELINE_CENTER);
        panel.getStyleClass().add("menuPanel");
        panel.setMaxWidth(300);
        vb.getChildren().add(panel);
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        ComboBox<String> difficultyCB = new ComboBox<>();
        difficultyCB.getItems().addAll("Very Easy", "Easy", "Medium", "Hard");
        difficultyCB.getSelectionModel().selectFirst();
        ComboBox<String> colorCB = new ComboBox<>();
        colorCB.getItems().addAll("Random", "White", "Black");
        colorCB.getSelectionModel().selectFirst();
        Button startButton = new Button("Start");
        startButton.getStyleClass().add("gameButton");
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("gameButton");

        startButton.setOnAction(actionEvent -> {
            int diff = difficultyCB.getSelectionModel().getSelectedIndex();
            int clr = colorCB.getSelectionModel().getSelectedIndex();
            controler.changeScene(new SoloGameScreen(controler, clr, diff));
        });

        cancelButton.setOnAction(actionEvent -> {
            controler.changeScene(new MainMenu(controler));
        });


        HBox h1 = new HBox();
        h1.getChildren().addAll(new Label("Difficulty: "), difficultyCB);
        HBox h2 = new HBox();
        h2.getChildren().addAll(new Label("Play as: "), colorCB);
        HBox h3 = new HBox();
        h3.getChildren().addAll(startButton, cancelButton);

        panel.getChildren().addAll(h1, h2, h3);


    }
}
