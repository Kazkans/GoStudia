package com.example.gostudia.Logic;

import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.StateField;

public class Board {
    private final Field[][] board;
    private final StateField[][] koBoard;
    private final Move koMove;
    private final int size;

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
                    } catch (IndexOutOfBoundsException ignored) {}
                }
            }
        }
    }

    public Board(Board b) {
        this.size = b.size;
        this.koMove = new Move(b.koMove);

        board = new Field[size][size];
        koBoard = new StateField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Field(b.board[i][j].getState());
                koBoard[i][j] = b.koBoard[i][j];
            }
        }

        // Set neighbours for each field
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < 4; k++) {
                    try {
                        board[i][j].getNeighbours().add(board[i+((1-k%2)*(k-1))][j+((k%2)*(k-2))]);
                    } catch (IndexOutOfBoundsException ignored) {}
                }
            }
        }

    }

    public boolean place(MoveEntity m) {
        return place(m.getX(), m.getY(), m.getState());
    }
    public boolean place(int x, int y, StateField state) {
        if (state != StateField.EMPTY) {
            if (board[x][y].getState() == StateField.EMPTY) {

                StateField[][] temp = getBoard();

                int points = board[x][y].setState(state);

                // Suicide prevention
                if (!board[x][y].hasBreaths()) {
                    board[x][y].setState(StateField.EMPTY);
                    return false;
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
                    // Ko prevention
                    board[koMove.getX()][koMove.getY()].setState(koMove.getState());
                    return false;
                }
                else {
                    koMove.set(x, y, state);

                    for (int i = 0; i < size; i++) {
                        System.arraycopy(temp[i], 0, koBoard[i], 0, size);
                    }
                }
            }
            else
                return false;
        }
        else
            throw new IllegalStateException("Cannot remove stones from board");

        return true;
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

    public StateField[][] getBoard() {
        StateField[][] temp = new StateField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp[i][j] = board[i][j].getState();
            }
        }
        return temp;
    }
}
