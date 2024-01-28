package com.example.gostudia.Server.Players;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.Logic.Board;
import com.example.gostudia.Server.InputOperations.InputOperation;
import com.example.gostudia.Server.InputOperations.ReplayOperation;
import com.example.gostudia.Server.InputOperations.SurrenderOperation;
import com.example.gostudia.Server.InternalState;
import com.example.gostudia.StateField;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.NoSuchElementException;

public class ReplayPlayer implements IPlayer{
    private Database database;
    private InternalState internal;

    public ReplayPlayer(Database database, InternalState internal) {
        this.database = database;
        this.internal = internal;
    }

    @Override
    public StateField getColor() {
        return StateField.WHITE;
    }

    @Override
    public void sendActiveTurn(boolean active) throws IOException {

    }

    @Override
    public void sendBoard(Board board) throws IOException {

    }

    @Override
    public void sendPass() {

    }

    @Override
    public void sendEnd() throws IOException {

    }

    @Override
    public InputOperation getInput() throws IOException, ClassNotFoundException {
        try {
            MoveEntity move = database.readMove(internal.currentGame, internal.moveNumber);
            return new ReplayOperation(move);
        } catch (NoSuchElementException e) {
            return new SurrenderOperation();
        }
    }
}
