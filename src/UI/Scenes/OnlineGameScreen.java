package UI.Scenes;

import Server.Server;
import UI.ScreenControler;
import Server.Response;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OnlineGameScreen extends GameScreen {

    Socket socket;
    boolean isHost = false;
    Server gameserver;

    PrintWriter writer;
    BufferedReader reader;


    //UI elements
    Label colorL;
    Label moveL;

    public OnlineGameScreen(ScreenControler c, Socket sc, Server sv, int color) throws IOException {
        super(c);
        socket = sc;
        playerColor = color;
        if(sv != null){
            isHost = true;
            gameserver = sv;
        }
        controler = c;
        setSideMenu();
        updateSideMenu();
        boardSP.setOnMouseClicked(event ->{
            int x = ((int)event.getX()) / 60;
            int y = 7 - ((int)event.getY()) / 60;
            if (x >= 0 && x < 8 && y >= 0 && y < 8){
                parseClickOnBoard(x, y);
            }
        });



        writer = new PrintWriter(socket.getOutputStream());
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(in);


        if(playerColor != toMove){
            Runnable response = new Response(socket, this);
            Thread thread = new Thread(response);
            thread.start();
        }

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
        exitButton.setOnAction(actionEvent -> {
            if(!finished){
                writer.println("END");
                writer.flush();
            }
            if(isHost) {
                try {
                    gameserver.cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            controler.changeScene(new MainMenu(controler));

        });
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
                if(gameBoard.field[x][y].getColor() == playerColor)
                    setFocus(x, y);
            }
            else{
                if(x == focusX && y == focusY) clearFocus();
                else{
                    if(gameBoard.field[focusX][focusY].getColor() == playerColor && gameBoard.field[focusX][focusY].isMoveLegal(x,y) != 0){
                        //Movement
                        gameBoard.parseMove(focusX, focusY, x, y);
                        gameBoard.nextMove();
                        toMove = -toMove+1;
                        updateSideMenu();
                        clearFocus();
                        String cmd = ("" + focusX + "" + focusY + "" + x + ""  + y);
                        writer.println(cmd);
                        writer.flush();
                        if(gameBoard.detectMate(gameBoard.move)) {
                            showMate(gameBoard.getMatingPieces(-toMove+1));
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText("Checkmate!");
                            alert.showAndWait();
                        }
                        else{
                            Runnable response = new Response(socket, this);
                            Thread thread = new Thread(response);
                            thread.start();
                        }
                    }
                }
            }
        }
    }




    @Override
    public void readResponse(String msg){
        if(msg.equals("END")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Your opponent has left the game.");
            alert.showAndWait();
            finished = true;
        }
        else {
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

}
