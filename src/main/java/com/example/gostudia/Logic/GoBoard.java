package com.example.gostudia.Logic;

import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.StateField;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GoBoard implements Board{
    private final GoField[][] board;
    private final StateField[][] koBoard;
    private final Move koMove;
    private final int size;
    private int blackPoints = 0;
    private int whitePoints = 0;

    public GoBoard(int size) {
        if (size < 3)
            throw new IllegalArgumentException("Board size too small");
        if (size % 2 == 0)
            throw new IllegalArgumentException("Board size has to be even");

        this.size = size;
        koMove = new Move();

        // Initialise fields
        board = new GoField[size][size];
        koBoard = new StateField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new GoField();
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

    @Override
    public Board copy() {
        return new GoBoard(this);
    }

    private GoBoard(GoBoard b) {
        this.size = b.size;
        this.koMove = new Move(b.koMove);
        this.blackPoints = b.blackPoints;
        this.whitePoints = b.whitePoints;

        board = new GoField[size][size];
        koBoard = new StateField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new GoField(b.board[i][j].getState());
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
    @Override
    public boolean place(MoveEntity m) {
        return place(m.getX(), m.getY(), m.getState());
    }

    @Override
    public boolean place(int x, int y, StateField state) {
        return place(x, y, state, false);
    }

    public boolean place(int x, int y, StateField state, boolean disableKo) {
        int points;
        if (state != StateField.EMPTY) {
            if (board[x][y].getState() == StateField.EMPTY) {

                StateField[][] temp = getBoard();

                points = board[x][y].setState(state);

                // Suicide prevention
                if (!board[x][y].hasBreaths()) {
                    board[x][y].setState(StateField.EMPTY);
                    return false;
                }

                // Ko rule check
                boolean ko = true;
                if (!disableKo) {
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
                }
                else {
                    ko = false;
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

        switch (state){
            case BLACK -> blackPoints += points;
            case WHITE -> whitePoints += points;
        }
        return true;
    }

    @Override
    public StateField getField(int x, int y) {
        try {
            return board[x][y].getState();
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public StateField[][] getBoard() {
        StateField[][] temp = new StateField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp[i][j] = board[i][j].getState();
            }
        }
        return temp;
    }

    public int calculatePoints(StateField color) {
        if (color != StateField.EMPTY) {
            checkAlive(color.opposite());
            checkTerritory();

            int sum = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j].getTerritory() == color) {
                        sum++;

                    } else if (board[i][j].getState() == color.opposite() &&
                            board[i][j].getNeighbours().stream().anyMatch(f -> f.getTerritory() == color)) {
                        sum++;
                    }
                }
            }

            switch (color){
                case BLACK -> sum += blackPoints;
                case WHITE -> sum += whitePoints;
            }
            return sum;
        }
        else {
            throw new IllegalArgumentException("Cannot calculate points for empty fields.");
        }
    }

    private void killAll(StateField color) {
        if (color != StateField.EMPTY) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j].getState() == color) {
                        for (int k = -1; k < 2; k+=2) {
                            try {
                                if (board[i + k][j].getState() == StateField.EMPTY)
                                    place(i + k, j, color.opposite(), true);
                            } catch (ArrayIndexOutOfBoundsException ignore) {}
                        }
                        for (int k = -1; k < 2; k+=2) {
                            try {
                                if (board[i][j + k].getState() == StateField.EMPTY)
                                    place(i, j + k, color.opposite(), true);
                            } catch (ArrayIndexOutOfBoundsException ignore) {}
                        }
                    }
                }
            }
        }
        else {
            throw new IllegalArgumentException("Cannot kill empty fields.");
        }
    }

    private void checkAlive(StateField color) {
        if (color != StateField.EMPTY) {
            GoBoard copy = (GoBoard) this.copy();

            copy.killAll(color);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (copy.getField(i, j) == color)
                        board[i][j].setAlive(true);
                }
            }
        }
        else {
            throw new IllegalArgumentException("Cannot check life for empty field.");
        }
    }

    private void checkTerritory() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                GoField field = board[i][j];
                StateField terrOwner;
                if (field.getState() == StateField.EMPTY && field.getTerritory() == null) {
                    Set<GoField> territory = field.checkTerritory();
                    Set<GoField> boarders = new HashSet<>();

                    // looking for boarders of a territory
                    for (GoField f : territory) {
                        int sum = 0;
                        for (GoField neighbour : f.getNeighbours()) {
                            if (territory.contains(neighbour))
                                sum++;
                        }
                        if (sum < 4)
                            boarders.add(f);
                    }

                    // filtering stones that lay on boarders
                    Set<GoField> stones = boarders.stream()
                            .filter(f -> f.getState() != StateField.EMPTY)
                            .collect(Collectors.toSet());

                    boolean whitesOnBoarders = stones.stream().anyMatch(f -> f.getState() == StateField.WHITE);
                    boolean blacksOnBoarders = stones.stream().anyMatch(f -> f.getState() == StateField.BLACK);

                    if (whitesOnBoarders && !blacksOnBoarders) {
                        terrOwner = StateField.WHITE;
                    }
                    else if (!whitesOnBoarders && blacksOnBoarders) {
                        terrOwner = StateField.BLACK;
                    }
                    else {
                        terrOwner = StateField.EMPTY;
                    }

                    // setting owner to each field of a current territory
                    if (terrOwner != StateField.EMPTY) {
                        if (territory.stream().noneMatch(f -> f.getState() == terrOwner.opposite() && f.isAlive())) {
                            territory.forEach(f -> f.setTerritory(terrOwner));
                        }
                    } else {
                        territory.forEach(f -> f.setTerritory(StateField.EMPTY));
                    }
                }
            }
        }
    }
}
