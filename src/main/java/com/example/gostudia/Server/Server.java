package com.example.gostudia.Server;

import com.example.gostudia.Server.Players.BotPlayer;
import com.example.gostudia.Server.Players.ClientPlayer;
import com.example.gostudia.Server.Players.IPlayer;
import com.example.gostudia.StateField;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
   // private static Board board = new Board(19);

    public static InternalState internal = new InternalState();

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
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            List<Callable<IPlayer>> taskList = new ArrayList<>();
            taskList.add(new BotWait(blackPlayer));
            taskList.add(new ClientWait(serverSocket));
            whitePlayer = executorService.invokeAny(taskList);
            executorService.shutdown();

            System.out.println("GAME STARTED");

            startGame(blackPlayer, whitePlayer);

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
        while(!internal.ended) {
            move(blackStreams, whiteStreams);
            move(whiteStreams, blackStreams);
        }

        blackStreams.sendEnd();
        whiteStreams.sendEnd();
    }
    public static void move(IPlayer mainPlayer, IPlayer sidePlayer) {
        if(internal.ended)
            return;

        try {
            // skips bytes that were sent before turn
            mainPlayer.sendActiveTurn(true);

            while(mainPlayer.getInput().execute(internal, sidePlayer));

            mainPlayer.sendActiveTurn(false);

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
