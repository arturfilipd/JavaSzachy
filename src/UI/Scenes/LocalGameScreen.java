package UI.Scenes;



import Game.Board;
import UI.ScreenControler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

public class LocalGameScreen extends GameScreen{

    Label colorL;
    Label wPts;
    Label bPts;

    LocalGameScreen(ScreenControler c){
       super(c);
       init();
    }

    public void init(){
        toMove = 0;
        playerColor = 0;
        setSideMenu();
        updateSideMenu();
        boardSP.setOnMouseClicked(event ->{
            int x = ((int)event.getX()) / (int) pieceSize;
            int y = 7 - ((int)event.getY()) / (int) pieceSize;
            if (x >= 0 && x < 8 && y >= 0 && y < 8){
                parseClickOnBoard(x, y);
            }
        });
    }

    void setSideMenu(){
        uiVB.setFillWidth(true);
        colorL = new Label();
        colorL.setAlignment(Pos.BASELINE_CENTER);
        colorL.prefWidthProperty().bind(uiVB.widthProperty());
        colorL.setPadding(new Insets(10, 10, 10, 10));

        HBox scoreBox = new HBox();
        scoreBox.setAlignment(Pos.CENTER);
        score = gameBoard.getPoints();
        wPts = new Label(""+score[0]);
        wPts.setStyle("-fx-font: 32 arial;");
        bPts = new Label(""+score[1]);
        bPts.setStyle("-fx-font: 32 arial;");
        ImageView wIcon = new ImageView(imagePieces[5]);
        ImageView bIcon = new ImageView(imagePieces[11]);
        scoreBox.getChildren().addAll(wIcon, wPts, bIcon, bPts);


        Separator s = new Separator(Orientation.HORIZONTAL);
        Button restartButton = addUIButton("Restart");
        restartButton.setOnAction(actionEvent -> {
            gameBoard.setUp();
            toMove = 0;
            result = 0;
            finished = false;
            playerColor = 0;
            clearHighlights();
            updateSideMenu();
        });
        Button exitButton = addUIButton("Exit");
        exitButton.setOnAction(actionEvent -> controler.changeScene(new MainMenu(controler)));
        uiVB.getChildren().addAll(colorL, scoreBox, s, restartButton, exitButton);
        wIcon.setFitHeight(60);
        bIcon.setFitHeight(60);
        wIcon.setFitWidth(60);
        bIcon.setFitWidth(60);

    }
    void updateSideMenu(){
        score = gameBoard.getPoints();
        wPts.setText(""+score[0]);
        bPts.setText(""+score[1]);
        if(toMove == 0){
            colorL.setText("White to play");
            colorL.setTextFill(Paint.valueOf("BLACK"));
            colorL.setStyle("-fx-background-color: #FFFFF0; -fx-border-width: 1; -fx-border-style: solid;");
        }
        else{
            colorL.setText("Black to play");
            colorL.setTextFill(Paint.valueOf("WHITE"));
            colorL.setStyle("-fx-background-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        }
        int chk = gameBoard.detectCheck(toMove);
        if(chk != 0) {
            if( chk == 1){
                colorL.setText("White wins!");
                colorL.setTextFill(Paint.valueOf("BLACK"));
                colorL.setStyle("-fx-background-color: #FFFFF0; -fx-border-width: 1; -fx-border-style: solid;");
            }
            else{
                colorL.setText("Black wins!");
                colorL.setTextFill(Paint.valueOf("WHITE"));
                colorL.setStyle("-fx-background-color: black; -fx-border-width: 1; -fx-border-style: solid;");
            }

        }
    }


    void parseClickOnBoard(int x, int y){
        if(finished) return;
        if(toMove == playerColor){
            if(!focusOn){
                if(gameBoard.field[x][y] != null)
                    if(gameBoard.field[x][y].getColor() == toMove)
                        setFocus(x, y);
            }
            else{
                if(x == focusX && y == focusY) clearFocus();
                else{
                    if(gameBoard.field[focusX][focusY].isMoveLegal(x,y) != 0){
                        //Movement
                        Board tmp = new Board(gameBoard);
                        if(tmp.parseMove(focusX, focusY, x, y) == 1){
                            gameBoard.parseMove(focusX, focusY, x, y);
                            gameBoard.nextMove();
                            toMove = -toMove+1;
                            playerColor = toMove;
                            updateSideMenu();
                            clearFocus();
                            if(gameBoard.detectMate(gameBoard.move)) {
                                showMate(gameBoard.getMatingPieces(-toMove+1));
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText("Checkmate!");
                                alert.showAndWait();
                            }
                        }

                    }
                }
            }
        }
    }


}
