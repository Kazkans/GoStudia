package com.example.gostudia;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.PrintWriter;

public class BoardField extends Circle {
    StateField stateField;
    public BoardField(int i, int j, PrintWriter out) {
        this.setOnMouseClicked(event ->  {
            // sends signal
            out.println(i +" "+ j);
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
