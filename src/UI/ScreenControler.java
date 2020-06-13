package UI;

import UI.Scenes.*;
import javafx.stage.Stage;


public class ScreenControler {
    public Stage stage;
    ScreenScene currentScene;

    public ScreenControler(){ }

    public void start(){
        currentScene = new MainMenu(this);
        stage.setScene(currentScene.scene);
        stage.setMinHeight(480);
        stage.setMinWidth(680);
        stage.show();
    }

    public void changeScene(ScreenScene s){
        currentScene = s;
        stage.setScene(currentScene.scene);
    }


}
