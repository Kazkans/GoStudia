package com.example.gostudia.Client;

import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.StateField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable{
    @FXML
    public Button surrenderButton;
    @FXML
    public Button passButton;
    @FXML
    public Button connectButton;
    @FXML
    public Button botButton;
    @FXML
    public Button replayButton;
    @FXML
    private Pane pane;
    @FXML
    private Label colorLabel;
    @FXML
    public Label turnLabel;

    float marginY=40f;
    float marginX=200f;
    float cellSize=30f;
    int size=19;

    private ClientHandler cm;

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
        BoardField circle = new BoardField(i, j, cm);
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
    public boolean isConnected() {
        return cm != null;
    }
    public void connect() {
        try {
            cm = new ClientHandler(new Socket("localhost", 4444), size) {
                @Override
                public void updateTurn(boolean mine) {
                    setTurnLabel(mine);
                }
                @Override
                public void updateLabel(String s) {
                    setLabel(s);
                }
                @Override
                public void updateField(int i, int j, StateField state) {
                    board[i][j].update(state);
                }
            };
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            System.exit(-1);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            System.exit(-1);
        }
    }

    public void handlePassAction() {
        cm.sendPass();
    }
    public void handleSurrenderAction() {
        cm.sendSurrender();
    }
    public void setTurnLabel(boolean mine) {
        Platform.runLater(() -> turnLabel.setText(mine ? "Your turn" : ""));
    }
    public void setLabel(String s) {
        Platform.runLater(() -> turnLabel.setText(s));
    }

    public void setColorLabel() {
        if(isConnected())
            colorLabel.setText(cm.getColor());
    }

    public void setButtonsVisibility(boolean visibility) {
        surrenderButton.setVisible(visibility);
        passButton.setVisible(visibility);

        botButton.setVisible(!visibility);
        connectButton.setVisible(!visibility);
        replayButton.setVisible(!visibility);
    }

    private void connectAndSetup() {
        connect(); // creates cm
        setColorLabel();
        createFields();
        setButtonsVisibility(true);
    }

    public void handleConnectionAction() {
        if(!isConnected()) {
            connectAndSetup();
            cm.start();
        }
    }

    /**
     * Function called on initialization
     *
     * Function on init creates empty board
     * @return void
     **/
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        createBoard();
        setButtonsVisibility(false);
    }


    public void handleConnectionBotAction() {
        handleConnectionAction();
        cm.sendBot();
    }

    public void handleReplayAction() throws IOException{
        connectAndSetup();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("choose-game-view.fxml"));
        Scene scene = new Scene(loader.load());

        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        dialog.show();

        cm.sendReplay();

        ((ChooseController) loader.getController()).setClientHandler(cm);
    }
}