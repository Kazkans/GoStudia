package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.Server.InternalState;
import com.example.gostudia.Server.Players.IPlayer;

import java.io.IOException;

public class MoveOperation implements InputOperation {
    IPlayer mainPlayer;
    public final int x;
    public final int y;
    public MoveOperation(int x, int y, IPlayer player) {
        this.mainPlayer = player;
        this.x=x;
        this.y=y;
    }
    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer, Database database) throws IOException {
        if (!internal.board.place(x, y, mainPlayer.getColor()))
            return true;
        internal.consecutivePasses = 0;

        mainPlayer.sendBoard(internal.board);
        sidePlayer.sendBoard(internal.board);

        MoveEntity move = new MoveEntity(internal.currentGame);
        move.set(internal.moveNumber, x, y, mainPlayer.getColor());
        database.saveMove(move);

        internal.moveNumber++;

        return false;
    }
}
