package com.example.gostudia.Database;

public enum Winner {
    B, W, D;
    public String toString() {
        switch(this) {
            case Winner.W -> {
                return ("WHITE WON");
            }
            case Winner.B -> {
                return( "BlACK WON");
            }
            case Winner.D -> {
                return("DRAW");
            }
        }
        return "DEFAULT";
    }
}
