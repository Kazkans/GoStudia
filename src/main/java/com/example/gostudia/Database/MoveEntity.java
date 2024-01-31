package com.example.gostudia.Database;

import com.example.gostudia.StateField;
import jakarta.persistence.*;

@Entity
@Table(name = "moves")
public class MoveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "move_number")
    private int moveNumber;
    private int x;
    private int y;
    @ManyToOne
    @JoinColumn(name = "game")
    private GameEntity game;
    @Enumerated(EnumType.STRING)
    @Column(name = "player")
    private StateField state;

    public MoveEntity() {}

    public MoveEntity(long id, int x, int y, int moveNumber, GameEntity game, StateField state) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.moveNumber = moveNumber;
        this.game = game;
        this.state = state;
    }

    public MoveEntity(GameEntity game) {
        this.game = game;
    }

    public void set(int moveNumber, int x, int y, StateField player) {
        setMoveNumber(moveNumber);
        setX(x);
        setY(y);
        setState(player);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public StateField getState() {
        return state;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setState(StateField state) {
        this.state = state;
    }
}
