import UI.ScreenControler;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{

    public static void main (String[] args) {launch(args);}

    private ScreenControler controler = new ScreenControler();


    @Override
    public void start(Stage primaryStage) {

        controler.stage = primaryStage;
        controler.start();

    }
}
