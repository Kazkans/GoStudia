package com.example.gostudia;

import com.example.gostudia.Logic.GoBoard;
import org.junit.jupiter.api.Test;

import static com.example.gostudia.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
public class TerritoryTest {
    @Test
    public void test1() {
        GoBoard board = new GoBoard(9);
        multiplePlace(board, StateField.BLACK,
                3,0, 3,1, 3,2, 3,3, 3,4, 3,5, 3,6, 3,7, 3,8);
        multiplePlace(board, StateField.WHITE,
                5,0, 5,1, 5,2, 5,3, 5,4, 5,5, 5,6, 5,7, 5,8);
        printBoard(board);

        assertEquals(27, board.calculatePoints(StateField.BLACK));
        assertEquals(27, board.calculatePoints(StateField.WHITE));
    }

    @Test
    public void test2() {
        GoBoard board = new GoBoard(9);
        multiplePlace(board, StateField.BLACK,
                3,0, 3,1, 3,2, 0,2, 1,2, 2,2);
        multiplePlace(board, StateField.WHITE,
                5,0, 5,1, 5,2, 5,3, 5,4, 5,5, 0,5, 1,5, 2,5, 3,5, 4,5);
        printBoard(board);

        assertEquals(6, board.calculatePoints(StateField.BLACK));
        assertEquals(45, board.calculatePoints(StateField.WHITE));
    }

    @Test
    public void test3() {
        GoBoard board = new GoBoard(7);
        multiplePlace(board, StateField.BLACK,
                5,0, 5,1, 2,2, 3,2, 4,2, 4,3, 3,4, 5,4, 3,5, 4,5, 2,6, 3,6);
        multiplePlace(board, StateField.WHITE,
                1,0, 4,0, 0,1, 2,1, 3,1, 4,1, 1,2, 2,3, 3,3, 1,4, 2,4, 0,5, 2,5, 1,6);
        printBoard(board);

        assertEquals(13, board.calculatePoints(StateField.BLACK));
        assertEquals(10, board.calculatePoints(StateField.WHITE));
    }
}
