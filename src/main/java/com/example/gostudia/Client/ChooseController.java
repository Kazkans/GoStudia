package com.example.gostudia.Client;

import com.example.gostudia.Database.GameEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ChooseController{

    @FXML
    private VBox contentBox;

    @FXML
    private Button nextButton;

    @FXML
    private Label pageLabel;

    @FXML
    private Button prevButton;

    @FXML
    private ScrollPane scrollPane;

    private ClientHandler cm;
    private int pageNumber = 0;

    private void updateLabel() {
        pageLabel.setText(String.valueOf(pageNumber+1));
    }

    @FXML
    public void handleNextAction(ActionEvent event) {
        pageNumber++;
        updateLabel();
        cm.sendPage(pageNumber);
        updateGames(cm.readGames());
    }

    @FXML
    public void handlePrevAction(ActionEvent event) {
        if (pageNumber > 0) {
            pageNumber--;
            updateLabel();
            cm.sendPage(pageNumber);
            updateGames(cm.readGames());
        }
    }

    public void updateGames(List<GameEntity> list) {
        contentBox.getChildren().clear();

        for (GameEntity game : list) {
            Button button = new Button();
            button.setText(game.getStartTime().toString());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(actionEvent -> {
                cm.sendId(game.getId());
                cm.start();
                ((Stage) scrollPane.getScene().getWindow()).close();
            });
            contentBox.getChildren().add(button);
        }
    }

    public void setClientHandler(ClientHandler cm) {
        this.cm = cm;
        updateGames(cm.readGames());
    }
}
