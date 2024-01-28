package com.example.gostudia.Server;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.Database.MariaDB;
import com.example.gostudia.Server.Players.*;
import com.example.gostudia.StateField;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
   // private static Board board = new Board(19);

    public static InternalState internal = new InternalState();
    public static Database database = MariaDB.getInstance();

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
            clientPlayer.waitFor("bot");
            return new BotPlayer(StateField.WHITE);
        }
    }

    static class ReplayWait implements Callable<IPlayer> {
        private final ClientPlayer clientPlayer;

        public ReplayWait(ClientPlayer clientPlayer) {
            this.clientPlayer = clientPlayer;
        }
        @Override
        public IPlayer call() throws Exception {
            clientPlayer.waitFor("rep");
            return new ReplayPlayer(database, internal);
        }
    }
    public static void main(String[] args) {
        ClientPlayer blackPlayer = null;
        IPlayer whitePlayer = null;

        try (ServerSocket serverSocket = new ServerSocket(4444)) {

            System.out.println("Server is listening on port 4444");

            Socket socket = serverSocket.accept();
            System.out.println("Black connected");
            blackPlayer = new ClientPlayer(socket, StateField.BLACK);

            // waiting for engine input
            // waiting for second player

            List<Callable<IPlayer>> taskList = new ArrayList<>();
            taskList.add(new BotWait(blackPlayer));
            taskList.add(new ClientWait(serverSocket));
            taskList.add(new ReplayWait(blackPlayer));
            ExecutorService executorService = Executors.newFixedThreadPool(taskList.size());
            whitePlayer = executorService.invokeAny(taskList);
            executorService.shutdown();

            if (whitePlayer instanceof ReplayPlayer) {
                String inputStr = "p0";
                int page;
                do {
                    page = Integer.parseInt(inputStr.substring(1));
                    blackPlayer.sendGames(database.read10Games(page));
                    System.out.println("Send games");
                    inputStr = blackPlayer.readLine();
                    System.out.println("Received: " + inputStr);
                }while(inputStr.charAt(0) < 48 || inputStr.charAt(0) > 57);

                internal.currentGame = database.readGame(Integer.parseInt(inputStr));

                System.out.println("REPLAY STARTED");

                startReplay(blackPlayer, whitePlayer);
            }
            else {
                System.out.println("GAME STARTED");

                startGame(blackPlayer, whitePlayer);
            }

            database.close();

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startGame(IPlayer blackStreams, IPlayer whiteStreams) throws IOException {
        try {
            database.beginTransaction();
            internal.currentGame = new GameEntity();

            database.saveGame(internal.currentGame);

            while (!internal.ended) {
                move(blackStreams, whiteStreams);
                move(whiteStreams, blackStreams);
            }

            internal.currentGame.setEndTime(LocalDateTime.now());
            internal.currentGame.setWinner(internal.winner);
            database.updateGame(internal.currentGame);
            database.commit();

        } catch (Exception e) {
            database.rollback();
            throw e;
        }

        blackStreams.sendEnd();
        whiteStreams.sendEnd();
    }

    public static void startReplay(IPlayer watch, IPlayer replay) throws IOException, InterruptedException {
        while (!internal.ended) {
            move(replay, watch);
            Thread.sleep(1000);
        }

        watch.sendEnd();
        replay.sendEnd();
    }
    public static void move(IPlayer mainPlayer, IPlayer sidePlayer) {
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
}
