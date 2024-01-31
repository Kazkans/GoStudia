package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.Winner;
import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.Server.InternalState;
import com.example.gostudia.StateField;

public class SurrenderOperation implements InputOperation {
    @Override
    public boolean execute(InternalState internal, IPlayer sidePlayer, Database database) {
        internal.ended = true;
        Winner winner = Winner.D;
        switch (sidePlayer.getColor()) {
            case BLACK -> winner = Winner.B;
            case WHITE -> winner = Winner.W;
        }
        internal.winner = winner;
        return false;
    }
}
