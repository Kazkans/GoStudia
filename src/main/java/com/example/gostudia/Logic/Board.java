package com.example.gostudia.Logic;

import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.StateField;

public interface Board {
    boolean place(int x, int y, StateField state);
    boolean place(MoveEntity m);
    StateField getField(int x, int y);
    StateField[][] getBoard();
    int getSize();
    Board copy();
    int calculatePoints(StateField color);
}
