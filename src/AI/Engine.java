package AI;
import Game.Board;

import java.util.LinkedList;

public class Engine {
    AI.Evaluation.Evaluator eval;
    int color;
    public EngineResponse getNextMove(Board b, int depth) {



        LinkedList<int[]> possibleMoves = new LinkedList<>();
        for (int sx = 0; sx < 8; sx++) {
            for (int sy = 0; sy < 8; sy++) {
                if (b.field[sx][sy] != null && b.field[sx][sy].getColor() == color)
                    for (int dx = 0; dx < 8; dx++) {
                        for (int dy = 0; dy < 8; dy++) {
                            if (b.field[sx][sy].isMoveLegal(dx, dy) != 0)
                                possibleMoves.add(new int[]{sx, sy, dx, dy});
                        }
                    }
            }
        }
        int i = 0;
        float bestR = -999999;
        float[] bestVal = {0,0};
        int bestM = 0;
        if(possibleMoves.size() == 0) return new EngineResponse(new int[]{1,1,1,1}, (color == 0)?new float[]{1.f,999999.f}:new float[]{999999.f,1.f});
        for (int[] m: possibleMoves) {
            Board tmp = new Board(b);
            tmp.move = color;
            float val;
            EngineResponse response = null;
            float[] r = (color == 0)?new float[]{1.f,999999.f}:new float[]{999999.f,1.f};
            if (tmp.parseMove(m) != 1 ) {
                val = - 99999999;
            }
            else{
                if( tmp.detectCheck(-color+1) == -1) val = - 99999999;
                else if(tmp.detectMate(1-color)) val =  99999999;

                else{

                    if(depth > 0){
                        Engine opponent = new Engine(-color+1);
                        response = opponent.getNextMove(tmp, depth-1);
                        r = response.value;
                    }
                    else {
                        r = eval.evaluate(tmp);
                    }
                    val = r[color] - r[-color + 1];
                }
            }
            if (val > bestR) {bestR = val; bestM = i; bestVal = r;}
            i++;
            if(depth == 3) System.out.println(i + "/" + possibleMoves.size());
        }
        int []bestMove = possibleMoves.get(bestM);
        /*
        if(depth ==2 ){
            System.out.println("Best move value is " + bestR + " #" + bestM);
            System.out.println("Anticipated response: " + bestMove[0] + "-" + bestMove[1] + " -> " + bestMove[2] + "-" + bestMove[3]);
        }

         */

        return new EngineResponse(bestMove, bestVal);
    }
    public Engine (int c){
        color = c;
        eval = new AI.Evaluation.EarlyGameEvaluator();
    }

}

