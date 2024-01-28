package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.Server.InternalState;
import com.example.gostudia.Server.Players.IPlayer;

import java.io.IOException;

public class ReplayOperation implements InputOperation{
    private MoveEntity move;

    public ReplayOperation(MoveEntity move) {
        this.move = move;
    }
    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer, Database database) throws IOException {
        if (!internal.board.place(move.getX(), move.getY(), move.getState()))
            throw new IOException("Couldn't do a move saved in the database.");
        internal.moveNumber++;
        sidePlayer.sendBoard(internal.board);
        return false;
    }
}
