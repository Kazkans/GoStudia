package com.example.gostudia.Logic;

import com.example.gostudia.StateField;

public class Move {
    protected int x;
    protected int y;
    protected StateField state;

    public Move() {
        x = 0;
        y = 0;
        state = StateField.EMPTY;
    }

    public Move(Move m) {
        this.x=m.x;
        this.y=m.y;
        this.state=m.state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public StateField getState() {
        return state;
    }

    public void set(int x, int y, StateField state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }
}
