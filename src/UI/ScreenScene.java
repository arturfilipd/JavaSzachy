package UI;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

public class ScreenScene {
    public Scene scene;
    public ScreenControler controler;
    public ScreenScene(){}

    protected void newScene(javafx.scene.Parent p){

        double x = controler.stage.getWidth();
        double y = controler.stage.getHeight();
        scene = new Scene(p, x, y);

    }

    public void readResponse(String s){
        return;
    }

}
