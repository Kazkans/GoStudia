package com.example.gostudia;

import com.example.gostudia.Logic.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        board.place(3,3, StateField.BLACK);
        board.place(4,3, StateField.BLACK);
        board.place(4,4, StateField.BLACK);
        board.print();

        board.place(2,3, StateField.WHITE);
        board.place(3,2, StateField.WHITE);
        board.place(4,2, StateField.WHITE);
        board.place(5,3, StateField.WHITE);
        board.place(5,4, StateField.WHITE);
        board.place(4,5, StateField.WHITE);
        board.place(3,4, StateField.WHITE);
        board.print();

        assertEquals(StateField.EMPTY, board.getField(3,3).getState());
        assertEquals(StateField.EMPTY, board.getField(4,3).getState());
        assertEquals(StateField.EMPTY, board.getField(4,4).getState());


        assertEquals(StateField.WHITE, board.getField(2,3).getState());
        assertEquals(StateField.WHITE, board.getField(3,2).getState());
        assertEquals(StateField.WHITE, board.getField(4,2).getState());
        assertEquals(StateField.WHITE, board.getField(5,3).getState());
        assertEquals(StateField.WHITE, board.getField(5,4).getState());
        assertEquals(StateField.WHITE, board.getField(4,5).getState());
        assertEquals(StateField.WHITE, board.getField(3,4).getState());
    }
}
