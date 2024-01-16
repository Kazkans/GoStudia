package com.example.gostudia.Server.InputOperations;

import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.Server.InternalState;

import java.io.IOException;

public interface InputOperation {
    public boolean execute(InternalState internal, IPlayer sidePlayer) throws IOException;
}
