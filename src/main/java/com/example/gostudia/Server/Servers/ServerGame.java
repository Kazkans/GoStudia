package com.example.gostudia.Server.Servers;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.Database.MariaDB;
import com.example.gostudia.Server.InternalState;
import com.example.gostudia.Server.Players.BotPlayer;
import com.example.gostudia.Server.Players.ClientPlayer;
import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.StateField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerGame {
   // private static Board board = new Board(19);

    private final InternalState internal = new InternalState();
    private static final Database database = MariaDB.getInstance();

    static class ClientWait implements Callable<IPlayer> {
        private final ServerSocket serverSocket;
        public ClientWait(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }
        @Override
        public IPlayer call() throws Exception {
            Socket socket = serverSocket.accept();
            System.out.println("White connected");
            return new ClientPlayer(socket, StateField.WHITE);
        }
    }
    static class BotWait implements Callable<IPlayer> {
        private final ClientPlayer clientPlayer;
        public BotWait(ClientPlayer clientPlayer) {
            this.clientPlayer = clientPlayer;
        }
        @Override
        public IPlayer call() throws Exception {
            clientPlayer.waitBot();
            return new BotPlayer(StateField.WHITE);
        }
    }

    public void run() {
        ClientPlayer blackPlayer;
        IPlayer whitePlayer;

        try (ServerSocket serverSocket = new ServerSocket(4444)) {

            System.out.println("ServerGame: Server is listening on port 4444");

            Socket socket = serverSocket.accept();
            System.out.println("Black connected");
            blackPlayer = new ClientPlayer(socket, StateField.BLACK);

            // waiting for engine input
            // waiting for second player
            List<Callable<IPlayer>> taskList = new ArrayList<>();
            taskList.add(new BotWait(blackPlayer));
            taskList.add(new ClientWait(serverSocket));
            ExecutorService executorService = Executors.newFixedThreadPool(taskList.size());
            whitePlayer = executorService.invokeAny(taskList);
            executorService.shutdown();

            System.out.println("GAME STARTED");

            startGame(blackPlayer, whitePlayer);

            whitePlayer.close();
            blackPlayer.close();
          //  database.close();

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startGame(IPlayer blackStreams, IPlayer whiteStreams) throws IOException {
        try {
            addGame();

            while (!internal.ended) {
                move(blackStreams, whiteStreams);
                move(whiteStreams, blackStreams);
            }

            commitGame();
        } catch (Exception e) {
            System.out.println("GOT THIS ERROR:" + e);
            database.rollback();
            throw e;
        }

        blackStreams.sendEnd(internal.winner);
        whiteStreams.sendEnd(internal.winner);
    }

    private void move(IPlayer mainPlayer, IPlayer sidePlayer) {
        if(internal.ended)
            return;

        try {
            // skips bytes that were sent before turn
            mainPlayer.sendActiveTurn(true);

            while(mainPlayer.getInput().execute(internal, sidePlayer, database));

            mainPlayer.sendActiveTurn(false);

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addGame() {
        database.beginTransaction();
        internal.currentGame = new GameEntity();

        database.saveGame(internal.currentGame);
    }

    private void commitGame() {
        internal.currentGame.setEndTime(LocalDateTime.now());
        internal.currentGame.setWinner(internal.winner);
        database.updateGame(internal.currentGame);
        database.commit();
    }

}
