package com.example.gostudia;

public enum StateField {
    WHITE, BLACK, EMPTY;

    public StateField opposite() {
        return switch (this){
            case BLACK -> WHITE;
            case WHITE -> BLACK;
            case EMPTY -> EMPTY;
        };
    }
}
