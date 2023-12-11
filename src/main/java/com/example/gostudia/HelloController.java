package com.example.gostudia;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class HelloController implements Initializable{
    @FXML
    private Pane pane;
    /**
     * Function called on initialization
     *
     * Function on init, reads log config and sets pane clip(so shapes don't clip to menu)
     * @return void
     */
    float marginY=40f;
    float marginX=200f;

    float cellSize=30f;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectInputStream ois;

    public void createHLine(float y) {
        Line line = new Line();
        line.setStartX(marginX);
        line.setStartY(y);
        line.setEndX(cellSize*18+marginX);
        line.setEndY(y);

        pane.getChildren().addAll(line);
    }
    public void createVLine(float x) {
        Line line = new Line();
        line.setStartX(x);
        line.setStartY(marginY);
        line.setEndX(x);
        line.setEndY(cellSize*18+marginY);

        pane.getChildren().addAll(line);
    }

    BoardField[][] board = new BoardField[19][19];
    public void createButton(float x, float y, int i, int j) {
        BoardField circle = new BoardField(i, j, out);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(cellSize/1.41/2);
        circle.setFill(Color.TRANSPARENT);
        board[i][j]=circle;

        pane.getChildren().addAll(circle);
    }
    public void createBoard() {
        for(int i=0;i<19;i++) {
            createVLine(marginX+i*cellSize);
            createHLine(marginY+i*cellSize);
        }
        for(int i=0;i<19;i++) {
            for (int j = 0; j < 19; j++) {
                createButton(marginX + i * cellSize, marginY + j * cellSize, i, j);
            }
        }

    }

    public void connect() {
        try  {
            Socket socket = new Socket("localhost", 4444);
            // Wysylanie do serwera
            out = new PrintWriter(socket.getOutputStream(), true);
            // Odbieranie z serwera
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ois = new ObjectInputStream(socket.getInputStream());

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            System.exit(-1);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Updates the board upon input form server
     */
    public class UpdateServerThread extends Thread {
        public void run() {
            while(true) {
                try {
                    StateField[][] stateBoard = (StateField[][]) ois.readObject();
                    for(int i=0;i<19;i++) {
                        for(int j=0;j<19;j++) {
                            board[i][j].update(stateBoard[i][j]);
                            if(stateBoard[i][j]==StateField.BLACK)
                                System.out.println("Black:"+i+" "+j);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Got new!!");
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        connect();
        (new UpdateServerThread()).start();
        createBoard();
    }


}