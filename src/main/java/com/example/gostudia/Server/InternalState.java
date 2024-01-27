package com.example.gostudia.Server;

import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.Database.Winner;
import com.example.gostudia.Logic.Board;

public class InternalState {
    public int consecutivePasses = 0;
    public boolean ended = false;
    public Board board = new Board(19);
    public Winner winner = null;
    public int moveNumber = 0;
    public GameEntity currentGame = null;
}
