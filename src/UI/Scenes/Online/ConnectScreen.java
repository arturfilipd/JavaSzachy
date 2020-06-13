package UI.Scenes.Online;

import Server.Response;
import UI.Scenes.OnlineGameScreen;
import UI.ScreenControler;
import UI.ScreenScene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectScreen extends ScreenScene {

    Socket socket;

    public ConnectScreen (ScreenControler c, String address) throws IOException {
        controler = c;
        VBox vb = new VBox();
        newScene(vb);
        Label info = new Label("Contecting...");
        vb.getChildren().add((info));

        socket = new Socket(address, 4999);
        Runnable r = new Response(socket, this);
        Thread responseThread = new Thread(r);
        responseThread.start();
    }


    @Override
    public void readResponse(String msg){
        OnlineGameScreen newGame = null;
        try {
            newGame = new OnlineGameScreen(controler,socket,null, Integer.parseInt(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        controler.changeScene(newGame);
    }
}
