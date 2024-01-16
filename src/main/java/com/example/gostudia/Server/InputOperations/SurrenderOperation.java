package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.Server.InternalState;

public class SurrenderOperation implements InputOperation {
    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer) {
        // TODO: calculating who won;
        internal.ended=true;
        return false;
    }
}
