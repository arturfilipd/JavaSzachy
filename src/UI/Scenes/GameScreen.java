package UI.Scenes;

import Game.Board;
import Game.Piece;
import Game.Players.Player;
import UI.ScreenControler;
import UI.ScreenScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameScreen extends ScreenScene {

    ImageView[][] pieces = new ImageView[8][8];
    ImageView[][] highlights = new ImageView[8][8];
    int toMove = 0;
    int playerColor = 0;
    Player opponent;

    int focusX, focusY;
    boolean focusOn = false;
    boolean finished = false;
    int result = 0;

    StackPane boardSP;
    VBox uiVB;
    SplitPane splitPane;

    Image[] imagePieces = new Image[16];
    {
        imagePieces[0]  = new Image("file:src/resources/pw.png");
        imagePieces[1]  = new Image("file:src/resources/nw.png");
        imagePieces[2]  = new Image("file:src/resources/bw.png");
        imagePieces[3]  = new Image("file:src/resources/rw.png");
        imagePieces[4]  = new Image("file:src/resources/qw.png");
        imagePieces[5]  = new Image("file:src/resources/kw.png");
        imagePieces[6]  = new Image("file:src/resources/pb.png");
        imagePieces[7]  = new Image("file:src/resources/nb.png");
        imagePieces[8]  = new Image("file:src/resources/bb.png");
        imagePieces[9]  = new Image("file:src/resources/rb.png");
        imagePieces[10]  = new Image("file:src/resources/qb.png");
        imagePieces[11]  = new Image("file:src/resources/kb.png");
        imagePieces[12]  = new Image("file:src/resources/blank.png");
        imagePieces[13] = new Image("file:src/resources/focus.png");
        imagePieces[14] = new Image("file:src/resources/enemy.png");
        imagePieces[15] = new Image("file:src/resources/tip.png");
    }
    Image chessBoardImage = new Image("file:src/resources/chessboard.png");

    Board gameBoard = new Board();

    void setPieceImages(ImageView[][] iv, Image[] img) {
        for (int x = 0; x < 8; x++)
            for(int y = 0; y < 8; y++) {
                if(!gameBoard.isNull(x,7-y)) {
                    Piece p = gameBoard.getPiece(x, 7-y);
                    iv[x][y].setImage(img[p.getType() + (6 * p.getColor())]);
                    iv[x][y].setVisible(true);
                }
                else iv[x][y].setImage(img[12]);
            }
    }

    void setEnemyHighlight(int x, int y) {

        highlights[x][7-y].setImage(imagePieces[14]);

    }


    void setTip(int x, int y){
        highlights[x][7-y].setImage(imagePieces[15]);
    }

    void clearTips(){
        for(int x = 0; x < 8; x++)
            for(int y = 0; y < 8; y++)
               if(highlights[x][y].getImage() == imagePieces[15]) highlights[x][y].setImage(imagePieces[12]);
    }


    void setFocus(int x, int y){
        focusOn = true;
        highlights[x][7-y].setImage(imagePieces[13]);
        focusY = y;
        focusX = x;
        for (int a = 0; a < 8; a++)
            for(int b = 0; b < 8; b++)
                if(gameBoard.field[x][y].isMoveLegal(a, b) != 0) setTip(a, b);
    }

    void showMate(int [] pieces){
        focusOn = false;
        for (int i = 0 ; i < pieces.length/2; i ++){
            highlights[pieces[i*2]][7-pieces[i*2+1]].setImage(imagePieces[13]);
        }
    }

    void clearHighlights(){
        focusOn = false;
        for(int x = 0; x < 8; x++)
            for(int y = 0; y < 8; y++)
                highlights[x][y].setImage(imagePieces[12]);
    }

    void clearFocus(){
        focusOn = false;
        highlights[focusX][7-focusY].setImage(imagePieces[12]);
        clearTips();
    }


    public GameScreen(ScreenControler c) {
        controler = c;


        //////////////////////////UI
        VBox mainVB = new VBox();
        newScene(mainVB);


        boardSP = new StackPane();
        uiVB = new VBox();
        boardSP.setId("pane");
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        splitPane = new SplitPane();
        splitPane.getItems().addAll(boardSP, uiVB);
        mainVB.getChildren().add(splitPane);
        splitPane.prefHeightProperty().bind(mainVB.heightProperty());
        GridPane highlightsGrid = new GridPane();
        GridPane piecesGrid = new GridPane();
        ImageView chessBoard = new ImageView(chessBoardImage);
        chessBoard.setPreserveRatio(true);
        chessBoard.setFitWidth(boardSP.getWidth());
        boardSP.getChildren().addAll(chessBoard, highlightsGrid, piecesGrid);

        boardSP.setAlignment(Pos.TOP_LEFT);
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++) {
                highlights[x][y] = new ImageView();
                pieces[x][y] = new ImageView();
                GridPane.setConstraints(highlights[x][y], x, y, 1, 1);
                piecesGrid.getChildren().add(highlights[x][y]);
                GridPane.setConstraints(pieces[x][y], x, y, 1, 1);
                piecesGrid.getChildren().add(pieces[x][y]);
            }
        }





        //Gameplay
        gameBoard.setUp();
        setPieceImages(pieces, imagePieces);
        Timeline updater = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setPieceImages(pieces, imagePieces);
            }
        }));
        updater.setCycleCount(Timeline.INDEFINITE);
        updater.play();

    }

    void updateSideMenu(){

    }

    Button addUIButton(String label){
        Button b = new Button(label);
        b.getStyleClass().add("gameButton");
        b.prefWidthProperty().bind(uiVB.widthProperty());
        return b;
    }



}
