package com.example.gostudia;

import com.example.gostudia.Logic.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.gostudia.TestUtils.*;

public class FieldTest {

    @Test
    public void testPlace() {
        Board board = new Board(9);
        board.place(3,3, StateField.BLACK);
        board.place(4,3, StateField.WHITE);

        assertEquals(StateField.BLACK, board.getField(3,3).getState());
        assertEquals(StateField.WHITE, board.getField(4,3).getState());

        assertThrows(IllegalStateException.class,
                () -> board.place(3,3, StateField.BLACK));
        assertThrows(IllegalStateException.class,
                () -> board.place(3,3, StateField.EMPTY));
    }
    @Test
    public void testChain() {
        Board board = new Board(9);
        multiplePlace(board, StateField.BLACK,
                3, 4, 4, 3, 4, 4);
        printBoard(board);
        
        multiplePlace(board, StateField.WHITE,
                4, 2, 5, 3, 5, 4, 4, 5, 3, 5, 2, 4, 3, 3);
        printBoard(board);

        multipleAssert(board, StateField.EMPTY,
                3, 4, 4, 3, 4, 4);
        multipleAssert(board, StateField.WHITE,
                4, 2, 5, 3, 5, 4, 4, 5, 3, 5, 2, 4, 3, 3);
    }

    @Test
    public void testSuicide() {
        Board board = new Board(9);
        multiplePlace(board, StateField.BLACK,
                3, 3, 4, 4, 3, 5, 2, 4);
        printBoard(board);

        board.place(3,4, StateField.WHITE);
        assertEquals(StateField.EMPTY, board.getField(3,4).getState());

        board.place(3, 4, StateField.BLACK);
        assertEquals(StateField.BLACK, board.getField(3,4).getState());
    }

    @Test
    public void testKillAndSurvive() {
        Board board = new Board(9);
        multiplePlace(board, StateField.BLACK,
                0, 1, 1, 1, 1, 0);
        multiplePlace(board, StateField.WHITE,
                2, 0, 2, 1, 0, 2, 1, 2);
        printBoard(board);

        board.place(0,0, StateField.WHITE);
        multipleAssert(board, StateField.EMPTY,
                0, 1, 1, 1, 1, 0);
        assertEquals(StateField.WHITE, board.getField(0,0).getState());
    }

    @Test
    public void testKo() {
        Board board = new Board(9);
        multiplePlace(board, StateField.BLACK,
                0, 1, 1, 0, 2, 1, 1, 2);
        multiplePlace(board, StateField.WHITE,
                2, 0, 3, 1, 2, 2);
        printBoard(board);

        board.place(1, 1, StateField.WHITE);
        board.place(2,1, StateField.BLACK);
        printBoard(board);

        multipleAssert(board, StateField.BLACK,
                0, 1, 1, 0, 1, 2);
        multipleAssert(board, StateField.WHITE,
                2, 0, 3, 1, 2, 2, 1, 1);
    }
}
