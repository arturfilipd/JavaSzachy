package UI.Scenes.Online;

import Server.Server;
import UI.Scenes.MainMenu;
import UI.Scenes.OnlineGameScreen;
import UI.ScreenControler;
import UI.ScreenScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import Server.Response;
import javafx.util.Duration;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HostScreen extends ScreenScene {


    Server gameServer;
    Thread serverThread;
    Socket hostSocket;

    Thread listener;


    public HostScreen(ScreenControler c) throws java.io.IOException {
        controler = c;

        //UI
        VBox vb = new VBox();
        newScene(vb);
        vb.setId("pane");
        scene.getStylesheets().addAll(this.getClass().getResource("../style.css").toExternalForm());
        vb.setAlignment(Pos.BASELINE_CENTER);
        Label l = new Label("Waiting for opponent");
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("gameButton");
        vb.getChildren().addAll(l, cancelButton);

        cancelButton.setOnAction(actionEvent -> {
            listener.interrupt();

            try {
                hostSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hostSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controler.changeScene(new MainMenu(controler));
        });


        //NETWORKING
        gameServer = new Server();
        serverThread= new Thread(gameServer);
        serverThread.start();
        hostSocket = new Socket("localhost", 4999);
        Runnable r = new Response(hostSocket, this);
        listener = new Thread(r);
        listener.start();


    }

    @Override
    public void readResponse(String msg){
        OnlineGameScreen newGame = null;
        try {
            newGame = new OnlineGameScreen(controler,hostSocket,null, Integer.parseInt(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        controler.changeScene(newGame);
    }
}
