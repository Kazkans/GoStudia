package com.example.gostudia;

import com.example.gostudia.Logic.Board;
import com.example.gostudia.Logic.GoBoard;
import com.example.gostudia.Logic.Move;
import com.example.gostudia.Server.InputOperations.InputOperation;
import com.example.gostudia.Server.InputOperations.MoveOperation;
import com.example.gostudia.Server.Players.BotPlayer;
import org.junit.jupiter.api.Test;

import static com.example.gostudia.TestUtils.multiplePlace;
import static com.example.gostudia.TestUtils.printBoard;
import static org.junit.jupiter.api.Assertions.*;

class BotPlayerTest {

    @Test
    void testBot() {
        BotPlayer b = new BotPlayer(StateField.WHITE);
        GoBoard board = new GoBoard(9);
        multiplePlace(board, StateField.BLACK,
                0, 0, 0, 1, 1, 0, 1, 1);
        multiplePlace(board, StateField.WHITE,
                0, 2, 1, 2, 2, 2, 2, 1);
        printBoard(board);

        b.sendBoard(board);
        InputOperation i = b.getInput();

        assertEquals(MoveOperation.class, i.getClass());

        MoveOperation m = (MoveOperation) i;
        board.place(m.x, m.y, StateField.WHITE);
        printBoard(board);
    }
}