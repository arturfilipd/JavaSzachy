package AI;

import Game.Board;
import Server.Response;
import UI.Scenes.GameScreen;
import javafx.application.Platform;

public class AIResponse implements Runnable {

    Engine engine;
    GameScreen game;
    Board board;

    public AIResponse(GameScreen g, Engine e, Board b){
        engine = e;
        game = g;
        board = b;
    }


    public void run(){
        int[] resp = engine.getNextMove(board, 2).bestMove;
        String s = "" + resp[0] + resp[1] + resp[2] + resp[3];
        Platform.runLater(()->game.readResponse(s));
    }


}
