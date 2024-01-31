package com.example.gostudia.Server.Servers;

import com.example.gostudia.Database.Database;
import com.example.gostudia.Database.MariaDB;
import com.example.gostudia.Database.MoveEntity;
import com.example.gostudia.Server.InternalState;
import com.example.gostudia.Server.Players.DBClient;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.OptionalInt;

public class ServerDB implements Runnable {
    // private static Board board = new Board(19);

    private final InternalState internal = new InternalState();
    private final Database database = MariaDB.getInstance();
    private final Socket socket;

    public ServerDB(Socket clientSocket) {
        this.socket=clientSocket;
    }

    @Override
    public void run() {
        DBClient dbClient;

        try {
            System.out.println("SERVEDB: Client connected");
            dbClient = new DBClient(socket);

            int page=0;
            for(;;) {
                dbClient.sendGames(database.read10Games(page));
                System.out.println("SERVEDB: Send games");
                OptionalInt i = dbClient.getPage();

                if(i.isPresent())
                    page = i.getAsInt();
                else
                    break;
            }

            int i = dbClient.getId();
            internal.currentGame = database.readGame(i);

            System.out.println("SERVEDB: REPLAY STARTED");

            startReplay(dbClient);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

      //  database.close();
    }

    public void startReplay(DBClient watch) throws IOException, InterruptedException {
        while (!internal.ended) {
            try {
                MoveEntity m = database.readMove(internal.currentGame, internal.moveNumber);
                internal.board.place(m);
                internal.moveNumber++;

                watch.sendBoard(internal.board);
                Thread.sleep(1000);
            } catch (NoSuchElementException e) {
                internal.ended=true;
            }
        }

        watch.sendEnd(internal.winner);
        watch.close();
    }
}
