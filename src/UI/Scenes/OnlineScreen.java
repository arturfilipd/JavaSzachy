package UI.Scenes;

import UI.Scenes.Online.ConnectScreen;
import UI.Scenes.Online.HostScreen;
import UI.ScreenControler;
import UI.ScreenScene;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class OnlineScreen extends ScreenScene {
    public OnlineScreen(ScreenControler c){
        controler = c;
        VBox vb = new VBox();
        vb.setAlignment(Pos.BASELINE_LEFT);
        this.scene = new Scene(vb, 680, 480);


        SplitPane sp = new SplitPane();

        VBox leftSide = new VBox();
        VBox rightSide = new VBox();
        sp.getItems().addAll(leftSide, rightSide);
        vb.getChildren().add(sp);

        leftSide.setAlignment(Pos.BASELINE_CENTER);
        leftSide.setSpacing(25);
        leftSide.setPadding(new Insets(25, 25, 25, 25));
        Label leftLabel = new Label("Host a new game");
        leftSide.getChildren().add(leftLabel);
        Button hostButton = new Button("Host");
        leftSide.getChildren().addAll(hostButton);

        rightSide.setAlignment(Pos.BASELINE_CENTER);
        rightSide.setSpacing(25);
        rightSide.setPadding(new Insets(25, 25, 25, 25));
        Label r_label = new Label("Join game");
        rightSide.getChildren().add(r_label);
        HBox hb = new HBox();
        Label ipLabel = new Label ("IP Address: ");
        TextField ipField = new TextField();
        hb.getChildren().addAll(ipLabel, ipField);
        Button connectButton = new Button("Connect");
        rightSide.getChildren().addAll(hb, connectButton);


        hostButton.setOnAction(actionEvent -> {
            try {
                controler.changeScene(new HostScreen(controler));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        connectButton.setOnAction(actionEvent -> {
            if(ipField.getText() != null){
                try {
                    controler.changeScene(new ConnectScreen(controler, ipField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}