package Game;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Board {

    public int move = 0;

    public Piece[][] field = new Piece[8][8];
    //public LinkedList<Piece> whitePieces = new LinkedList<>();
    //public LinkedList<Piece> blackPieces = new LinkedList<>();
   // public LinkedList<Piece> pieces = new LinkedList<>();




    public float[] eval;
    public void setUp() {
        move = 0;
        for(int i = 0; i < 8; i++) {
            addPiece( new Pawn(i,1,0,this));
            addPiece(new Pawn(i,6,1,this));
        }
        addPiece( new Rook(0, 0,0,this));
        addPiece( new Rook(7,0,0,this));
        addPiece(new Rook(0,7,1,this));
        addPiece(new Rook(7,7,1,this));

        addPiece( new Knight(1,0,0,this));
        addPiece( new Knight(6,0,0,this));
        addPiece( new Knight(1,7,1,this));
        addPiece( new Knight(6,7,1,this));

        addPiece( new Bishop(2,0,0,this));
        addPiece( new Bishop(5,0,0,this));
        addPiece( new Bishop(2,7,1,this));
        addPiece( new Bishop(5,7,1,this));

        addPiece(new Queen(3,0,0,this));
        addPiece(new Queen(3,7,1,this));

        addPiece( new King(4,0,0,this));
        addPiece( new King(4,7,1,this));


    }

    public void nextMove() {
        move = (move == 1)?(0):(1);
    }

    public boolean isAttacked(int x, int y, int color) {
        for(int i = 0; i < 8; i ++)
            for(int j = 0; j < 8; j ++) {
                if(field[i][j] != null)
                    if(field[i][j].getColor() == color && field[i][j].isMoveLegal(x, y) != 0)
                        return true;
            }
        return false;
    }


    Piece addPiece(Piece p){

        Piece pc = new Piece();

        if (p.type == 0)  pc = new Pawn(p);
        if (p.type == 1)  pc = new Knight(p);
        if (p.type == 2)  pc = new Bishop(p);
        if (p.type == 3)  pc = new Rook(p);
        if (p.type == 4)  pc = new Queen(p);
        if (p.type == 5)  pc = new King(p);
        pc.board = this;

        field[p.getX()][p.getY()] = pc;
        //pieces.add(pc);
        return pc;
    }

    public int detectCheck(int move) {
        //  0  no check
        //  1  checking move
        // -1  illegal game state
        Piece whiteKing = new Piece();
        Piece blackKing = new Piece();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(field[i][j] != null) {
                    if(field[i][j].type == 5 && field[i][j].color == 0)
                        whiteKing = field[i][j];
                    if(field[i][j].type == 5 && field[i][j].color == 1)
                        blackKing = field[i][j];
                }
            }
        }

        boolean bcheck = false;
        boolean wcheck = false;
        boolean illegal = false;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(field[i][j] != null) {
                    if(field[i][j].color == 0) {
                        if(field[i][j].isMoveLegal(blackKing.pos_x, blackKing.pos_y) != 0) {
                            if(move == 0)
                                illegal = true;
                            else
                                bcheck = true;
                        }
                    }
                    else {
                        if(field[i][j].isMoveLegal(whiteKing.pos_x, whiteKing.pos_y) != 0) {
                            if(move == 0)
                                wcheck = true;
                            else
                                illegal = true;
                        }
                    }
                }
            }
        }
        if(illegal) return -1;
        if(wcheck) return 1;
        if(bcheck) return 2;

        return 0;
    }

    public int parseMove(int [] a){
        return parseMove(a[0], a[1], a[2], a[3]);
    }

    public int parseMove(int pos_x, int pos_y, int dest_x, int dest_y) {
        if((pos_x >= 0 && pos_x < 8)
                &&(pos_y >= 0 && pos_y < 8)
                &&(dest_x >= 0 && dest_x < 8)
                &&(dest_y >= 0 && dest_x < 8)) {
            if(field[pos_x][pos_y] != null) {
                if(field[pos_x][pos_y].color == move) {
                    int ret = field[pos_x][pos_y].isMoveLegal(dest_x, dest_y);
                    if(ret != 0) {
                        field[dest_x][dest_y] =  field[pos_x][pos_y];
                        field[dest_x][dest_y].pos_x = dest_x;
                        field[dest_x][dest_y].pos_y = dest_y;
                        field[dest_x][dest_y].moves++;
                        field[pos_x][pos_y] = null;
                        if(ret == 2 && field[dest_x][dest_y].getType() == 5) {
                            field[3][0] = field[0][0];
                            field[3][0].pos_x = 3;
                            field[3][0].pos_y = 0;
                            field[0][0] = null;
                        }
                        if(ret == 3 && field[dest_x][dest_y].getType() == 5) {
                            field[5][0] = field[7][0];
                            field[5][0].pos_x = 5;
                            field[5][0].pos_y = 0;
                            field[7][0] = null;
                        }
                        if(ret == 4 && field[dest_x][dest_y].getType() == 5) {
                            field[3][7] = field[0][7];
                            field[3][7].pos_x = 3;
                            field[3][7].pos_y = 7;
                            field[0][7] = null;
                        }
                        if(ret == 5 && field[dest_x][dest_y].getType() == 5) {
                            field[5][7] = field[7][7];
                            field[5][7].pos_x = 5;
                            field[5][7].pos_y = 7;
                            field[7][7] = null;
                        }

                        if(field[dest_x][dest_y].getType() == 0 && field[dest_x][dest_y].getColor() == 0 && dest_y == 7)
                            field[dest_x][dest_y].promote();
                        if(field[dest_x][dest_y].getType() == 0 && field[dest_x][dest_y].getColor() == 1 && dest_y == 0)
                            field[dest_x][dest_y].promote();

                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
            return 0;
        }
        return -1;
    }

    public boolean detectMate(int move) {
        boolean moveb = (move == 1);
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                for(int destx = 0; destx < 8; destx++)
                    for(int desty = 0; desty < 8; desty++) {
                        if(field[i][j] != null) {
                            if (field[i][j].color == move && field[i][j].isMoveLegal(destx, desty) != 0) {
                                Board temp = new Board(this);
                                temp.move =  move;
                                temp.parseMove(i, j, destx, desty);
                                int chk = temp.detectCheck(move);
                                if(chk !=  ((moveb)?(2):(1)) && chk != -1) {
                                    return false;
                                }
                            }
                        }
                    }
        return true;
    }

    public boolean isNull(int x, int y) {
        return (field[x][y] == null);
    }

    public Piece getPiece(int x, int y) {
        if(isNull(x,y))
            return null;
        return field[x][y];
    }

    public Board(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j ++)
                field[i][j] = null;
    }



    public Board(Board b){
        move = b.move;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b.field[i][j] != null) {
                    if (b.field[i][j].type == 0) field[i][j] = new Pawn(b.field[i][j]);
                    if (b.field[i][j].type == 1) field[i][j] = new Knight(b.field[i][j]);
                    if (b.field[i][j].type == 2) field[i][j] = new Bishop(b.field[i][j]);
                    if (b.field[i][j].type == 3) field[i][j] = new Rook(b.field[i][j]);
                    if (b.field[i][j].type == 4) field[i][j] = new Queen(b.field[i][j]);
                    if (b.field[i][j].type == 5) field[i][j] = new King(b.field[i][j]);
                    field[i][j].board = this;
                } else field[i][j] = null;
            }
        }





    }

    public int[] getMatingPieces(int color){
       LinkedList<Piece> mating = new LinkedList<Piece>();
        Piece king = null;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(field[i][j] != null) {
                    if(field[i][j].type == 5 && field[i][j].color == (-color+1))
                        king = field[i][j];
                }
            }
        }
        for (int x = 0;x < 8; x++){
            for (int y = 0; y < 8; y++){
                if(field[x][y] != null){
                    if(field[x][y].color == color && field[x][y].isMoveLegal(king.pos_x, king.pos_y) != 0){
                        mating.add(field[x][y]);
                    }
                }
            }
        }
        int [] pieces = new int[mating.size()*2];
        for(int i = 0; i < mating.size(); i++){
            pieces[i*2] = mating.get(i).getX();
            pieces[i*2+1] = mating.get(i).getY();

        }
        return pieces;
    }



}
