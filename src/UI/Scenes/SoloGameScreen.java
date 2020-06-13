package UI.Scenes;

import AI.AIResponse;
import AI.Engine;
import Server.Response;
import UI.ScreenControler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class SoloGameScreen extends GameScreen {

    Engine engine;

    //UI elements
    Label colorL;
    Label moveL;

    SoloGameScreen(ScreenControler c) {
        super(c);
        init();
        if (playerColor != toMove) {
            Runnable response = new AIResponse(this, engine, gameBoard);
            Thread thread = new Thread(response);
            thread.start();
        }
    }



    void init(){
        toMove = 0;
        int r = ThreadLocalRandom.current().nextInt(0, 1000);
        playerColor = (r>500)?1:0;
        engine = new Engine(-playerColor+1);
        setSideMenu();
        boardSP.setOnMouseClicked(event ->{
            int x = ((int)event.getX()) / 60;
            int y = 7 - ((int)event.getY()) / 60;
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
        uiVB.getChildren().add(colorL);

        moveL = new Label();
        moveL.prefWidthProperty().bind(uiVB.widthProperty());
        moveL.setAlignment(Pos.BASELINE_CENTER);
        moveL.setPadding(new Insets(10, 10, 10, 10));
        moveL.setStyle("-fx-border-width: 1; -fx-border-style: solid;");
        uiVB.getChildren().add(moveL);

        Button exitButton = new Button("Quit match");
        uiVB.getChildren().add(exitButton);
        updateSideMenu();
    }

    void updateSideMenu(){
        if(playerColor == 0){
            colorL.setText("Playing as White");
            colorL.setTextFill(Paint.valueOf("BLACK"));
            colorL.setStyle("-fx-background-color: #FFFFF0; -fx-border-width: 1; -fx-border-style: solid;");
        }
        else{
            colorL.setText("Playing as Black");
            colorL.setTextFill(Paint.valueOf("WHITE"));
            colorL.setStyle("-fx-background-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        }
        if(toMove == playerColor){moveL.setText("Your move"); }
        else{moveL.setText("Waiting for the opponent...");}
    }


    void parseClickOnBoard(int x, int y){
        if(finished) return;
        if(toMove == playerColor){
            if(!focusOn){
                if(gameBoard.field[x][y] != null)
                    if(gameBoard.field[x][y].getColor() == playerColor && playerColor == toMove)
                        setFocus(x, y);
            }
            else{
                if(x == focusX && y == focusY) clearFocus();
                else{
                    if(gameBoard.field[focusX][focusY].isMoveLegal(x,y) != 0){
                        //Movement
                        gameBoard.parseMove(focusX, focusY, x, y);
                        gameBoard.nextMove();
                        toMove = -toMove+1;
                        updateSideMenu();
                        clearFocus();
                        if(gameBoard.detectMate(gameBoard.move)) {
                            showMate(gameBoard.getMatingPieces(-toMove+1));
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText("Checkmate!");
                            alert.showAndWait();
                        }
                        else{
                            Runnable ai = new AIResponse(this, engine, gameBoard);
                            Thread thread = new Thread(ai);
                            thread.start();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readResponse(String msg){
        int sx = msg.charAt(0) - '0';
        int sy = msg.charAt(1) - '0';
        int dx = msg.charAt(2) - '0';
        int dy = msg.charAt(3) - '0';
        System.out.println("Response received: " + sx + "-" + sy + "->" + dx + "-" + dy);
        gameBoard.parseMove(sx, sy, dx, dy);
        gameBoard.nextMove();
        toMove = -toMove + 1;
        updateSideMenu();
        clearFocus();
        clearHighlights();
        setEnemyHighlight(sx, sy);
        setEnemyHighlight(dx, dy);
    }
}
