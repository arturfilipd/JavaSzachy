package UI.Scenes.Online;

import Server.Response;
import UI.Scenes.MainMenu;
import UI.Scenes.OnlineGameScreen;
import UI.ScreenControler;
import UI.ScreenScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.Socket;

public class ConnectScreen extends ScreenScene {

    Socket socket;

    Thread response;

    public ConnectScreen (ScreenControler c, String address) throws IOException {
        controler = c;

        //UI
        VBox vb = new VBox();
        newScene(vb);
        vb.setId("pane");
        scene.getStylesheets().addAll(this.getClass().getResource("../style.css").toExternalForm());
        Label info = new Label("Contecting...");

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("gameButton");
        vb.getChildren().addAll(info, cancelButton);

        cancelButton.setOnAction(actionEvent -> {
            response.interrupt();
            controler.changeScene(new MainMenu(controler));
        });

        socket = new Socket(address, 4999);
        Runnable r = new Response(socket, this);
        response = new Thread(r);
        response.start();
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
