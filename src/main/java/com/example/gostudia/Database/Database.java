package com.example.gostudia.Database;

import java.util.List;

public interface Database {
    void beginTransaction();
    void commit();
    void rollback();
    void saveGame(GameEntity gameEntity);
    void saveMove(MoveEntity move);
    MoveEntity readMove(GameEntity game, int number);
    List<GameEntity> read10Games(int page);
    GameEntity readGame(long id);
    void updateGame(GameEntity game);
    void close();
}
