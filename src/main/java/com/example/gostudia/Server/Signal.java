package com.example.gostudia.Server;

import com.example.gostudia.StateField;

import java.io.Serializable;

public class Signal implements Serializable {
    public enum Type {
        BOARD,
        TURN,
    }
    private final Type type;
    private final Object o;

    public Signal(StateField[][] o) {
        this.o=o;
        this.type= Type.BOARD;
    }
    public Signal(Boolean mine) {
        this.o=mine;
        this.type= Type.TURN;
    }
    public Object getObject() {
        return o;
    }
    public Type getType() {
        return type;
    }
}
