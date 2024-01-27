package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.Winner;
import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.Server.InternalState;

public class PassOperation implements InputOperation {
    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer, Database database) {
        internal.consecutivePasses++;
        sidePlayer.sendPass();
        if(internal.consecutivePasses ==2) {
            internal.ended=true;
            internal.winner = Winner.D;
        }

        return false;
    }
}
