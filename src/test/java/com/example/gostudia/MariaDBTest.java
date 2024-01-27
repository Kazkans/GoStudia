package com.example.gostudia;

import com.example.gostudia.Database.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class MariaDBTest {
    @Test
    public void testMariaDB() {
        Database db = MariaDB.getInstance();
        db.beginTransaction();

        GameEntity game = new GameEntity();
        db.saveGame(game);

        MoveEntity move1 = new MoveEntity(game);
        move1.set(0,2, 3, StateField.BLACK);
        db.saveMove(move1);

        MoveEntity move2 = new MoveEntity(game);
        move2.set(1,5, 7, StateField.WHITE);
        db.saveMove(move2);

        MoveEntity move3 = new MoveEntity(game);
        move3.set(2,7, 10, StateField.BLACK);
        db.saveMove(move3);

        MoveEntity move4 = new MoveEntity(game);
        move4.set(3,2, 7, StateField.WHITE);
        db.saveMove(move4);

        game.setEndTime(LocalDateTime.now());
        game.setWinner(Winner.D);
        db.updateGame(game);
        db.commit();

        assertTrue(game.equals(db.readGame(game.getId())));
        assertEquals(move1, db.readMove(game, 0));
        assertEquals(move2, db.readMove(game, 1));
        assertEquals(move3, db.readMove(game, 2));
        assertEquals(move4, db.readMove(game, 3));
    }
}
