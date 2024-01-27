package com.example.gostudia.Database;

import com.example.gostudia.Logic.Move;

import java.time.LocalDateTime;
import java.util.List;

public interface Database {
    void beginTransaction();
    void commit();
    void rollback();
    void saveGame(GameEntity gameEntity);
    void saveMove(MoveEntity move);
    MoveEntity readMove(GameEntity game, int number);
    List<GameEntity> readGames();
    GameEntity readGame(long id);
    void updateGame(GameEntity game);
}
