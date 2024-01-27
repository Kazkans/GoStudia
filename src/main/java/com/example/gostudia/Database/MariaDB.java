package com.example.gostudia.Database;

import com.example.gostudia.Logic.Move;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;


public class MariaDB implements Database{
    private static final Database db = new MariaDB();
    private final EntityManager entityManager;

    private MariaDB() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static Database getInstance() {
        return db;
    }

    @Override
    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    @Override
    public void commit() {
        entityManager.getTransaction().commit();
    }

    public void rollback() {
        entityManager.getTransaction().rollback();
    }

    @Override
    public void saveGame(GameEntity gameEntity) {
        entityManager.persist(gameEntity);
    }

    @Override
    public void saveMove(MoveEntity move) {
        entityManager.persist(move);
    }

    @Override
    public MoveEntity readMove(GameEntity game, int number) {
        Query query = entityManager.createQuery(
                "from MoveEntity where game= :game_entity and moveNumber= :number");
        query.setParameter("game_entity", game);
        query.setParameter("number", number);
        return (MoveEntity) query.getResultList().getFirst();
    }

    @Override
    public List<GameEntity> readGames() {
        return entityManager.createQuery("from GameEntity").getResultList();
    }

    @Override
    public GameEntity readGame(long id) {
        return entityManager.getReference(GameEntity.class, id);
    }

    @Override
    public void updateGame(GameEntity game) {
        entityManager.merge(game);
    }
}
