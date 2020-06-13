package UI.Scenes.Online;

import Server.Server;
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

    boolean readyToStart = false;
    int playerColor;


    public HostScreen(ScreenControler c) throws java.io.IOException {
        controler = c;

        //UI
        VBox vb = new VBox();
        vb.setAlignment(Pos.BASELINE_CENTER);
        newScene(vb);
        Label l = new Label("Waiting for opponent");
        Button cancelButton = new Button("Cancel");
        vb.getChildren().addAll(l, cancelButton);


        //NETWORKING
        System.out.println("[Kient] Stratowanie serwera");
        gameServer = new Server();
        serverThread= new Thread(gameServer);
        serverThread.start();

        System.out.println("[Kient] Łączenie z serwerem");
        hostSocket = new Socket("localhost", 4999);
        System.out.println("[Kient] Połączono");




        Runnable r = new Response(hostSocket, this);
        Thread responseThread = new Thread(r);
        System.out.println("[Kient] Nasłuchiwanie serwera");
        responseThread.start();


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
