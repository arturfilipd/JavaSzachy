package AI;

public class EngineResponse {
    public int[] bestMove;
    public float[] value;

    public EngineResponse(int[] m, float[] v){
        bestMove = m;
        value = v;
    }

}
