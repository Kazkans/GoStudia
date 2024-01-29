package com.example.gostudia.Client;

import com.example.gostudia.StateField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class represents one board field. It handles sending info upon clicking and drawing it.
 */
public class BoardField extends Circle {
    private final int i;
    private final int j;
    public BoardField(int i, int j) {
        this.i=i;
        this.j=j;
    }

    public void setHandler(ClientHandler cm) {
        this.setOnMouseClicked(event ->  {
            // sends signal
            cm.sendMove(i,j);
        });
    }
    public void update(StateField s) {
        switch(s) {
            case EMPTY:
                this.setFill(Color.TRANSPARENT);
                this.setStroke(Color.TRANSPARENT);
                break;
            case BLACK:
                this.setFill(Color.BLACK);
                this.setStroke(Color.BLACK);
                break;
            case WHITE:
                this.setFill(Color.WHITE);
                this.setStroke(Color.BLACK);
                break;
        }
    }
}
