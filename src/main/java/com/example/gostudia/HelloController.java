package com.example.gostudia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable{
    @FXML
    private Pane pane;
    @FXML
    private Label colorLabel;

    float marginY=40f;
    float marginX=200f;

    float cellSize=30f;
    private PrintWriter out = null;
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
    }

    public void createFields() {
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

    public void handlePassAction(ActionEvent actionEvent) {
        if(out!=null)
            out.println("pass");
    }

    /**
     * Waits for input from server and sets label to color which got
     */
    public void setColorLabel() {
        Scanner scan = new Scanner(in);
        String inputStr = scan.nextLine().strip();
        colorLabel.setText(inputStr);
    }
    public void handelConnectAction(ActionEvent actionEvent) {
        if(out==null) {
            connect();
            setColorLabel();
            createFields();
            (new UpdateServerThread()).start();
        }
    }

    /**
     * Thread to updates the board upon input form server
     */
    public class UpdateServerThread extends Thread {
        public void run() {
            while(true) {
                try {
                    StateField[][] stateBoard = (StateField[][]) ois.readObject();
                    for(int i=0;i<19;i++) {
                        for(int j=0;j<19;j++) {
                            board[i][j].update(stateBoard[i][j]);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Function called on initialization
     *
     * Function on init creates empty board
     * @return void
     */
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        createBoard();
    }


}