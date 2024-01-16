package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.Server.InternalState;

import java.io.IOException;

public class MoveOperation implements InputOperation {
    IPlayer mainPlayer;
    int x;
    int y;
    public MoveOperation(int x, int y, IPlayer player) {
        this.mainPlayer = player;
        this.x=x;
        this.y=y;
    }

    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer) throws IOException {
        if (!internal.board.place(x, y, mainPlayer.getColor()))
            return true;
        internal.consecutivePassess=0;

        mainPlayer.sendBoard(internal.board);
        sidePlayer.sendBoard(internal.board);

        return false;
    }
}
