package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.Server.InternalState;

public class PassOperation implements InputOperation {
    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer) {
        internal.consecutivePassess++;
        sidePlayer.sendPass();
        if(internal.consecutivePassess==2) {
            internal.ended=true;
        }

        return false;
    }
}
