package Game;

public class Piece {
    int color; //0 - White, 1 - Black
    int moves;
    int type;
    /*
     * 0 - Pawn
     * 1 - Knight
     * 2 - Bishop
     * 3 - Rook
     * 4 - Queen
     * 5 - King
     */
    int pos_x, pos_y;
    Board board;

    public int getType() {
        return type;
    }

    public int getColor(){
        return color;
    }

    public int getMoves(){
        return moves;
    }

    public int getX(){
        return pos_x;
    }

    public int getY(){
        return pos_y;
    }

    boolean IsFriendly(Piece  p) {
        return (p.color == this.color);
    }

    public void promote(){
        if(type == 0){
            board.field[pos_x][pos_y] = new Queen(pos_x, pos_y, color, board);
        }
    }


    public int isMoveLegal(int dest_x, int dest_y) {
        /*
         * 0 if not possible
         * 1 if possible
         * -1 if would cause a capture
         * 2 white O-O-O
         * 3 white O-O / Pawn Promotion
         * 4 black O-O-O
         * 5 black O-O-O
         */
        return 0;
    }

    public boolean isControlling (int dest_x, int dest_y){
        if (pos_x == dest_x && pos_y == dest_y) return false;
        Board tmp = new Board(board);
        tmp.field[dest_x][dest_y] = null;
        if(tmp.field[pos_x][pos_y] == null) return false;
        if(tmp.field[pos_x][pos_y].isMoveLegal(dest_x, dest_y) != 0) return true;
        return false;
    }

    public int countPossibleMoves(){
        int n = 0;
        for(int x = 0; x < 8; x++)
            for(int y = 0; y < 8; y++)
                if (isMoveLegal(x,y) != 0) n++;
        return n;
    }

    public int countPossibleMovesWithoutCaptures(){
        int n = 0;
        for(int x = 0; x < 8; x++)
            for(int y = 0; y < 8; y++)
                if (isMoveLegal(x,y) == 1) n++;
        return n;
    }

    Piece(){}
}

class Pawn extends Piece {
    Pawn(int pos_x, int pos_y, int color, Board b){
        type = 0;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        board = b;
        moves = 0;
    }

    Pawn(Piece p){
        color = p.color;
        moves = p.moves;
        type = p.type;
        pos_x = p.pos_x;
        pos_y = p.pos_y;
        board = p.board;
    }


    public int isMoveLegal(int dest_x, int dest_y){
        if(dest_x == pos_x) {
            //White move
            if(color == 0 && dest_y - pos_y == 1 && board.field[dest_x][dest_y] == null)
                return 1;
            //White double move
            if(color == 0 && dest_y - pos_y == 2 && board.field[dest_x][dest_y] == null && board.field[dest_x][dest_y-1] == null && moves == 0)
                return 1;
            //Black move
            if(color == 1 && pos_y - dest_y == 1 && board.field[dest_x][dest_y] == null)
                return 1;
            //Black double move
            if(color == 1 && pos_y - dest_y == 2 && board.field[dest_x][dest_y] == null &&  board.field[dest_x][dest_y+1] == null &&moves == 0)
                return 1;

        }
        if(Math.pow((pos_x - dest_x), 2) == 1) {
            //White Capture
            if(color == 0 && dest_y - pos_y == 1 && board.field[dest_x][dest_y] != null && !board.field[dest_x][dest_y].IsFriendly(this))
                return -1;
            //Black Capture
            if(color == 1 && pos_y - dest_y == 1 && board.field[dest_x][dest_y] != null && !board.field[dest_x][dest_y].IsFriendly(this))
                return -1;
        }
        return 0;
    }

    @Override
    public int countPossibleMoves(){
        int n = 0;
        int mod = (color==0)?1:-1;
        if (color == 0){
            if(board.field[pos_x][pos_y+mod] == null || board.field[pos_x][pos_y+mod].color != color) n++;
            if((pos_y<6 && color == 0) ||(pos_y>1 && color == 1))
                if(board.field[pos_x][pos_y+mod] == null || board.field[pos_x][pos_y+mod].color != color) n++;
        }
        return n;
    }

}

class Rook extends Piece {
    Rook(int pos_x, int pos_y, int color, Board b) {
        type = 3;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        board = b;
        moves = 0;
    }

    Rook(Piece p) {
        color = p.color;
        moves = p.moves;
        type = p.type;
        pos_x = p.pos_x;
        pos_y = p.pos_y;
        board = p.board;
    }


    public int isMoveLegal(int dest_x, int dest_y) {
        if (dest_x == pos_x ^ dest_y == pos_y) {
            int dist = dest_x + dest_y - pos_x - pos_y;
            int dir = 1;
            if (dist < 0) {
                dir = -1;
                dist = -dist;
            }

            //Horizontal
            if (dest_y == pos_y) {
                for (int i = 1; i <= dist - 1; i++) {
                    if (board.field[pos_x + (i * dir)][pos_y] != null)
                        return 0;
                }
            }
            //Vertical
            if (dest_x == pos_x) {
                for (int i = 1; i < dist; i++) {
                    if (board.field[pos_x][pos_y + (i * dir)] != null)
                        return 0;
                }
            }

            //Checking Capture
            if (board.field[dest_x][dest_y] != null) {
                if (board.field[dest_x][dest_y].IsFriendly(this))
                    return 0;
                return -1;
            }
            return 1;
        }
        return 0;
    }

    @Override
    public int countPossibleMoves() {
        int n = 0;
        boolean a = false;
        boolean b = false;
        for (int i = 1; i <= pos_x;  i++) {
            if(!a && i+pos_x < 8) if(board.field[pos_x+i][pos_y] == null) n++; else a = true;
            if(!b && i-pos_x >= 0) if(board.field[pos_x-i][pos_y] == null) n++; else b = true;
        }
        a = false;
        b = false;
        for (int i = 1; i <= pos_y;  i++) {
            if(!a && i+pos_y < 8) if(board.field[pos_x][pos_y+i] == null) n++; else a = true;
            if(!b && i-pos_y >= 0) if(board.field[pos_x-i][pos_y] == null) n++; else b = true;
        }
        return n;
    }
}


class Knight extends Piece{
    Knight(int pos_x, int pos_y, int color, Board b){
        type = 1;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        board = b;
        moves = 0;
    }

    Knight(Piece p){
        color = p.color;
        moves = p.moves;
        type = p.type;
        pos_x = p.pos_x;
        pos_y = p.pos_y;
        board = p.board;
    }


    public int isMoveLegal(int dest_x, int dest_y) {
        if	(((Math.pow((dest_x - pos_x), 2) == 1)&&Math.pow((dest_y - pos_y), 2) == 4)
                ^ ((Math.pow((dest_x - pos_x), 2) == 4)&&Math.pow((dest_y - pos_y), 2) == 1)) {
            if(board.field[dest_x][dest_y] != null) {
                if(board.field[dest_x][dest_y].IsFriendly(this))
                    return 0;
                else
                    return -1;
            }
            return 1;
        }
        return 0;
    }
}


class Bishop extends Piece{

    Bishop(int pos_x, int pos_y, int color, Board b){
        type = 2;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        moves = 0;
        board = b;
    }

    Bishop(Piece p){
        color = p.color;
        moves = p.moves;
        type = p.type;
        pos_x = p.pos_x;
        pos_y = p.pos_y;
        board = p.board;
    }


    public int isMoveLegal(int dest_x, int dest_y) {
        if(dest_x != pos_x && Math.pow(dest_x - pos_x, 2) == Math.pow(dest_y - pos_y, 2)) {
            int dir_x = (dest_x > pos_x)?(1):(-1);
            int dir_y = (dest_y > pos_y)?(1):(-1);
            int dist = pos_x - dest_x;
            if(dist<0) dist *=-1;
            for(int i = 1; i <= dist-1; i++) {
                if(board.field[pos_x + (i*dir_x)][pos_y + (i * dir_y)] != null)
                    return 0;

            }
            if(board.field[dest_x][dest_y] != null) {
                if(board.field[dest_x][dest_y].IsFriendly(this))
                    return 0;
                else
                    return -1;
            }
            return 1;
        }
        return 0;
    }
}

class Queen extends Piece{
    Queen(int pos_x, int pos_y, int color, Board b){
        type = 4;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        moves = 0;
        board = b;
    }

    Queen(Piece p){
        color = p.color;
        moves = p.moves;
        type = p.type;
        pos_x = p.pos_x;
        pos_y = p.pos_y;
        board = p.board;
    }


    public int isMoveLegal(int dest_x, int dest_y) {
        Rook r =  new Rook(pos_x,pos_y,color,board);
        int ret = r.isMoveLegal(dest_x, dest_y);
        if(ret != 0) return ret;
        Bishop b = new Bishop(pos_x,pos_y,color,board);
        ret = (b.isMoveLegal(dest_x, dest_y));
        return ret;
    }
}

class King extends Piece{
    King(int pos_x, int pos_y, int color, Board b){
        type = 5;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        moves = 0;
        board = b;
    }

    King(Piece p){
        color = p.color;
        moves = p.moves;
        type = p.type;
        pos_x = p.pos_x;
        pos_y = p.pos_y;
        board = p.board;
    }

    public int isMoveLegal(int dest_x, int dest_y) {

        if(dest_x == pos_x && dest_y == pos_y) return 0;

        //Castle
        if(moves == 0) {
            //White Long
            if(color == 0 && dest_x == 2 && dest_y == 0 && board.field[0][0] != null && board.field[1][0] == null && board.field[2][0] == null && board.field[3][0]==null)
                if(board.field[0][0].moves == 0 && !board.isAttacked(2, 0, 1) && !board.isAttacked(3, 0, 1) && !board.isAttacked(4, 0, 1))
                    return 2;
            //White Short
            if(color == 0 && dest_x == 6 && dest_y == 0 && board.field[7][0] != null && board.field[5][0] == null && board.field[6][0] == null)
                if(board.field[7][0].moves == 0 && !board.isAttacked(6, 0, 1) && !board.isAttacked(5, 0, 1) && !board.isAttacked(4, 0, 1))
                    return 3;

            //Black Long
            if(color == 1 && dest_x == 2 && dest_y == 7 && board.field[0][7] != null && board.field[1][7] == null && board.field[2][7] == null && board.field[3][7]==null)
                if(board.field[0][7].moves == 0 && !board.isAttacked(2, 7, 0) && !board.isAttacked(3, 7, 0) && !board.isAttacked(4, 7, 0))
                    return 4;
            //Black Short
            if(color == 1 && dest_x == 6 && dest_y == 7 && board.field[7][7] != null && board.field[5][7] == null && board.field[6][7] == null)
                if(board.field[7][7].moves == 0 && !board.isAttacked(6, 7, 0) && !board.isAttacked(5, 7, 0) && !board.isAttacked(4, 7, 0))
                    return 5;
        }

        if(Math.pow((dest_x - pos_x), 2) < 2 && Math.pow((dest_y - pos_y), 2) < 2) {
            if(board.field[dest_x][dest_y] == null)
                return 1;
            else
            if(!board.field[dest_x][dest_y].IsFriendly(this))
                return -1;
        }



        return 0;
    }
}
