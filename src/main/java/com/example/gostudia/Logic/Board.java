package com.example.gostudia.Logic;

import com.example.gostudia.StateField;

public class Board {
    private Field[][] board;
    private int size;

    public Board(int size) {
        if (size < 3)
            throw new IllegalArgumentException("Board size too small");
        if (size % 2 == 0)
            throw new IllegalArgumentException("Board size has to be even");

        this.size = size;

        // Initialise fields
        board = new Field[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Field();
            }
        }

        // Set neighbours for each field
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < 4; k++) {
                    try {
                        board[i][j].getNeighbours().add(board[i+((1-k%2)*(k-1))][j+((k%2)*(k-2))]);
                    } catch (IndexOutOfBoundsException e) {}
                }
            }
        }
    }

    public void place(int x, int y, StateField state) {
        if (state != StateField.EMPTY) {
            if (board[x][y].getState() == StateField.EMPTY) {
                int points = board[x][y].setState(state);
                if (!board[x][y].hasBreaths()) {
                    board[x][y].setState(StateField.EMPTY);
                }
            }
            else
                throw new IllegalStateException("Cannot replace stones");
        }
        else
            throw new IllegalStateException("Cannot remove stones from board");
    }

    public Field getField(int x, int y) {
        try {
            return board[x][y];
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                switch (board[j][i].getState()) {
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
