package AI;
import Game.Board;
import UI.ScreenControler;

import java.util.LinkedList;
import java.util.Random;

public class Engine {
    AI.Evaluation.Evaluator eval;
    int color;

    static int maxDepth = -1;

    boolean USING_RANDOM_MOVES = false;

    public EngineResponse getNextMove(Board b, int depth) {
        if(maxDepth == -1) maxDepth = depth;
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
        float[] best3R = {-999999,-999999,-999999};
        float[] bestVal = {0,0};
        float[][] best3Val = {{0,0}, {0,0}, {0,0}};
        int[] best3M = {0, 0, 0};
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
                        Engine opponent = new Engine(-color+1, maxDepth);
                        response = opponent.getNextMove(tmp, depth-1);
                        r = response.value;
                    }
                    else {
                        r = eval.evaluate(tmp);
                    }
                    val = r[color] - r[-color + 1];
                }
            }
            if (val > bestR) {
                bestR = val; bestM = i; bestVal = r;
            }
            if (depth == maxDepth && USING_RANDOM_MOVES ){
                if(val > best3R[0]) {
                    best3R[2] = best3R[1]; best3R[1] = best3R[0];
                    best3R[0] = val; best3Val[2] = best3Val[1];
                    best3Val[1] = best3Val[0]; best3Val[0] = r;
                    best3M[2] = best3M[1]; best3M[1] = best3M[0]; best3M[0] = i;}
                    if(val > best3R[1]) {
                        best3R[2] = best3R[1]; best3R[1] =val;
                        best3Val[2] = best3Val[1]; best3Val[1] =r;
                        best3M[2] = best3M[1]; best3M[1] = i;}
                    else if(val > best3R[2]) {
                        best3R[2] = val; best3Val[2] = r;
                        best3M[2] = i;}

            }
            i++;
        }
        int []bestMove = possibleMoves.get(bestM);
        if(depth == maxDepth && USING_RANDOM_MOVES){
            Random rand = new Random();
            int chosenM = rand.nextInt(100);
            if(chosenM > 50){
                chosenM = rand.nextInt(3);
                return new EngineResponse(possibleMoves.get(best3M[chosenM]), best3Val[chosenM]);
            }
        }

        return new EngineResponse(bestMove, bestVal);
    }
    public Engine (int c, int diff){
        color = c;
        if(diff == 0 || diff == 1){
            maxDepth = 1;
        }
        else maxDepth = 2;
        if(diff == 0 || diff == 2) USING_RANDOM_MOVES = true;

        eval = new AI.Evaluation.EarlyGameEvaluator();
    }

}

