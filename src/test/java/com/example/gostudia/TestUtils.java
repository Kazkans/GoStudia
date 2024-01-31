package com.example.gostudia;

import com.example.gostudia.Logic.Board;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public static void multiplePlace(Board board, StateField state, int... points) {
        if (points.length % 2 == 1)
            throw new IllegalArgumentException("There has to be even");

        for (int i = 0; i < points.length; i+=2) {
            board.place(points[i], points[i+1], state);
        }
    }

    public static void multipleAssert(Board board, StateField state, int... points) {
        if (points.length % 2 == 1)
            throw new IllegalArgumentException("There has to be even");

        for (int i = 0; i < points.length; i+=2) {
            assertEquals(state, board.getField(points[i], points[i+1]));
        }
    }

    public static void printBoard(Board board) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                switch (board.getField(j, i)) {
                    case BLACK -> System.out.print("X");
                    case WHITE -> System.out.print("O");
                    case EMPTY -> System.out.print("_");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
