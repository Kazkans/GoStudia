package com.example.gostudia.Server.Players;

import com.example.gostudia.Database.Winner;
import com.example.gostudia.Logic.Board;
import com.example.gostudia.Server.InputOperations.InputOperation;
import com.example.gostudia.StateField;

import java.io.IOException;

@SuppressWarnings("ClassEscapesDefinedScope")
public interface IPlayer {
    StateField getColor();
    void sendActiveTurn(boolean active) throws IOException;
    void sendBoard(Board board) throws IOException;
    void sendPass();
    void sendEnd(Winner w) throws IOException;
    InputOperation getInput() throws IOException, ClassNotFoundException;
    void close() throws IOException;

}
