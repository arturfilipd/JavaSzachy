package UI.Scenes.Online;

import Server.Response;
import UI.Scenes.MainMenu;
import UI.Scenes.OnlineGameScreen;
import UI.Scenes.OnlineScreen;
import UI.ScreenControler;
import UI.ScreenScene;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class ConnectScreen extends ScreenScene {

    Socket socket;

    Thread response;
    boolean connectionFailed = false;

    public ConnectScreen (ScreenControler c, String address) throws IOException {
        controler = c;

        //UI
        VBox vb = new VBox();
        newScene(vb);
        vb.setId("pane");
        vb.setAlignment(Pos.BASELINE_CENTER);
        scene.getStylesheets().addAll(this.getClass().getResource("../style.css").toExternalForm());
        Label info = new Label("Contecting...");
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("gameButton");
        vb.getChildren().addAll(info, cancelButton);

        cancelButton.setOnAction(actionEvent -> {
            if(!connectionFailed){
                response.interrupt();
            }
            controler.changeScene(new MainMenu(controler));
        });


        try{
            socket = new Socket(address, 4999);
        }
        catch(ConnectException exception){
            connectionFailed = true;
            info.setText("No server is running on provided address");
            cancelButton.setText("Ok");
        }
        if(!connectionFailed) {
            Runnable r = new Response(socket, this);
            response = new Thread(r);
            response.start();
        }
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
