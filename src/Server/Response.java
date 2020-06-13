package Server;

import UI.ScreenScene;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Response implements Runnable {

    ScreenScene scene;
    Socket socket;

    @Override
    public void run(){

        InputStreamReader in = null;
        try {
            in = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(in);
        String s = null;
        try {
            s = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String msg = s;
        Platform.runLater(()->scene.readResponse(msg));

    }



    public Response(Socket sk, ScreenScene scn){
        scene = scn;
        socket = sk;
    }



}
