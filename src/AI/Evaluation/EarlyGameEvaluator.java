package AI.Evaluation;
import Game.Board;
import Game.Piece;

public class EarlyGameEvaluator extends AI.Evaluation.Evaluator {
    @Override
    public float[] evaluate(Board b){
        float[] score = {0, 0};
        int[] bDeveloped = {0,0};
        int[] nDeveloped = {0,0};
        int[] rDeveloped = {0,0};
        int[] qDeveloped = {0,0};

        //Criteria
        float[] pts = {0.f, 0.f};

        float[] pieceValues = {1.f, 3.f, 3.f, 4.5f, 9.f, 0.f};

        ////Occupation of Center
        for(int x =2; x<=5; x++)
            for(int y = 2; y <=5; y++){
                if(b.field[x][y] != null){
                    if((x<5&&x>2) && (y<5&&y>2)) {//Focal Center
                        if (b.field[x][y].getType() == 0) score[b.field[x][y].getColor()] += 0.4f;
                        else if (b.field[x][y].getType() == 4) score[b.field[x][y].getColor()] += 0.3f;
                        else score[b.field[x][y].getColor()] += 0.2f;
                    }
                    else//Wider Center
                        score[b.field[x][y].getColor()] += 0.1f;
                }
            }


        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if(b.field[x][y]!=null){
                    Piece p = b.field[x][y];
                    int c =  p.getColor();
                    //Control of focal center
                    if ( p.isControlling(3, 4)) score[c] += 0.1f;
                    if ( p.isControlling(3, 3)) score[c] += 0.1f;
                    if ( p.isControlling(4, 4)) score[c] += 0.1f;
                    if ( p.isControlling(4, 3)) score[c] += 0.1f;
                    //Multiple movement
                    if ( p.getMoves() > 1) score[c] -= (0.35 * ( p.getMoves() - 1));
                    //Points
                    pts[c] +=  pieceValues[p.getType()];
                    //Development
                    if ( p.getType() == 1 &&  p.getMoves() != 0) nDeveloped[c]++;
                    if ( p.getType() == 2 &&  p.getMoves() != 0) bDeveloped[c]++;
                    if ( p.getType() == 3 &&  p.getMoves() != 0) rDeveloped[c]++;
                    if ( p.getType() == 4 &&  p.getMoves() != 0) qDeveloped[c]++;
                    //Mobility (Midgame)
                    if (p.getType() == 0)  score[c] += 0.5f * p.countPossibleMoves();
                    else if(p.getType()!= 5) score[c] += 0.1f * p.countPossibleMovesWithoutCaptures();
                }
            }
        }

        //Order of Development
        int[] minorsDeveloped = {bDeveloped[0]+ nDeveloped[0], bDeveloped[1]+ nDeveloped[1]};
        for( int i = 0; i <2; i++) {
            if (bDeveloped[i] > 0 && nDeveloped[i] == 0) score[i] -= 0.2;
            if (qDeveloped[i] > 0  && minorsDeveloped[i] < 2)  score[i] -= 0.3f;
            if (rDeveloped[i] > 0 && minorsDeveloped[i] < 2)  score[i] -= 0.5f;
        }
        //Underdevelopment
        if (minorsDeveloped[0] - minorsDeveloped[1] > 1) score [0] += 1.75f;
        if (minorsDeveloped[1] - minorsDeveloped[0] > 1) score [1] += 1.75f;


        score[0] += pts[0];
        score[1] += pts[1];
        return score;
    }
}
