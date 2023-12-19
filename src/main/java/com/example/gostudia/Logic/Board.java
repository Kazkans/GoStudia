package com.example.gostudia.Logic;

import com.example.gostudia.StateField;

public class Board {
    private Field[][] board;
    private StateField[][] koBoard;
    private Move koMove;
    private int size;

    public Board(int size) {
        if (size < 3)
            throw new IllegalArgumentException("Board size too small");
        if (size % 2 == 0)
            throw new IllegalArgumentException("Board size has to be even");

        this.size = size;
        koMove = new Move();

        // Initialise fields
        board = new Field[size][size];
        koBoard = new StateField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Field();
                koBoard[i][j] = StateField.EMPTY;
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

                StateField[][] temp = new StateField[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        temp[i][j] = board[i][j].getState();
                    }
                }

                int points = board[x][y].setState(state);
                if (!board[x][y].hasBreaths()) {
                    board[x][y].setState(StateField.EMPTY);
                }

                // Ko rule check
                boolean ko = true;
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (!board[i][j].getState().equals(koBoard[i][j])) {
                            ko = false;
                            break;
                        }
                    }
                    if (!ko)
                        break;
                }

                if (ko) {
                    board[koMove.getX()][koMove.getY()].setState(koMove.getState());
                }
                else {
                    koMove.set(x, y, state);

                    for (int i = 0; i < size; i++) {
                        System.arraycopy(temp[i], 0, koBoard[i], 0, size);
                    }
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

    public int getSize() {
        return size;
    }
}
