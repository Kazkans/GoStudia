package com.example.gostudia.Server.Players;

import com.example.gostudia.Logic.Board;
import com.example.gostudia.Server.InputOperations.InputOperation;
import com.example.gostudia.StateField;

import java.io.IOException;

public interface IPlayer {
    public StateField getColor();
    public void sendActiveTurn(boolean active) throws IOException;
    public void sendBoard(Board board) throws IOException;
    public void sendPass();
    public void sendEnd() throws IOException;
    public InputOperation getInput() throws IOException, ClassNotFoundException;

}
