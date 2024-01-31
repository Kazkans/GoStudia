package com.example.gostudia.Server;

import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.Database.Winner;
import com.example.gostudia.Logic.Board;
import com.example.gostudia.Logic.GoBoard;

public class InternalState {
    public int consecutivePasses = 0;
    public boolean ended = false;
    public Board board = new GoBoard(19);
    public Winner winner = null;
    public int moveNumber = 0;
    public GameEntity currentGame = null;
}
