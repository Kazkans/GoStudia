package com.example.gostudia.Server.Players;

import com.example.gostudia.Database.Winner;
import com.example.gostudia.Logic.Board;
import com.example.gostudia.Server.InputOperations.InputOperation;
import com.example.gostudia.Server.InputOperations.MoveOperation;
import com.example.gostudia.Server.InputOperations.PassOperation;
import com.example.gostudia.StateField;
import javafx.util.Pair;

import java.util.Random;

@SuppressWarnings("ClassEscapesDefinedScope")
public class BotPlayer implements IPlayer{
    private final StateField color;
    private Board board;
    private boolean beforePassed = false;
    public BotPlayer(StateField color) {
        this.color=color;
    }
    @Override
    public StateField getColor() {
        return color;
    }

    @Override
    public void sendActiveTurn(boolean active) {
        if(!active)
            beforePassed=false;
    }

    @Override
    public void sendBoard(Board board) {
        this.board = board;
    }

    @Override
    public void sendPass() {
        beforePassed=true;
    }

    @Override
    public void sendEnd(Winner w) {}
    @Override
    public void close() {}

    public float eval(Board board) {
        return board.calculatePoints(color) - board.calculatePoints(color.opposite());
    }
    @Override
    public InputOperation getInput() {
        System.out.println("START ENGINE MOVE");

        // run 4000 moves with 8 depth and get the best
        // if we are winning and last was pass we pass again
        // if the best is equal or slightly bad then pass
        // if better then move
        Random random = new Random();

        float threshold = -3; // How bad the position can be before passing
        float numMove = 4000;
        float depth = 8;

        Pair<Integer, Integer> bestMove = null;
        float bestEval= Float.MIN_VALUE;

        for(int i=0;i<numMove;i++) {
            Board tmp = board.copy();

            Pair<Integer, Integer> first = makeRandomMove(tmp, random);
            for(int j=0;j<depth;j++) {
                makeRandomMove(tmp, random);
            }

            float val = eval(tmp);
            if(val>bestEval) {
                bestMove=first;
                bestEval=val;
            }
        }

        if(bestMove==null || bestEval < threshold || (bestEval > 0 && beforePassed)) {
            System.out.println("ENGINE PASSED " + bestEval + " " + threshold + " " + beforePassed + " " + (bestMove==null));
            return new PassOperation();
        } else {
            System.out.println("MADE ENGINE MOVE " + bestMove.getKey() + " " + bestMove.getValue());
            return new MoveOperation(bestMove.getKey(), bestMove.getValue(), this);
        }
    }

    public Pair<Integer, Integer> makeRandomMove(Board board, Random random) {
        int x;
        int y;
        do {
            x = random.nextInt(board.getSize());
            y = random.nextInt(board.getSize());
        } while(!board.place(x, y, color));
        return new Pair<>(x,y);
    }
}
