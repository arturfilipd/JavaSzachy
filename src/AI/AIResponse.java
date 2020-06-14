package AI;

import Game.Board;
import Server.Response;
import UI.Scenes.GameScreen;
import javafx.application.Platform;

public class AIResponse implements Runnable {

    Engine engine;
    GameScreen game;
    Board board;
    int depth;

    public AIResponse(GameScreen g, Engine e, Board b, int diff){
        engine = e;
        game = g;
        board = b;
        if(diff == 0 || diff == 1){
            depth = 0;
        }
        else depth = 2;
    }


    public void run(){
        int[] resp = engine.getNextMove(board, depth).bestMove;
        String s = "" + resp[0] + resp[1] + resp[2] + resp[3];
        Platform.runLater(()->game.readResponse(s));
    }
}
