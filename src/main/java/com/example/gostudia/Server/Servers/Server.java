package com.example.gostudia.Server.Servers;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static class handlerDB implements Runnable{
        public void run() {
            try {
                ServerSocket serverSocketDB = new ServerSocket(4445);
                System.out.println("HandlerDB: Server is listening on port 4444");
                while(true) {
                    Socket socketDB = serverSocketDB.accept();
                    Thread serverDB = new Thread(new ServerDB(socketDB));
                    serverDB.start();
                }

            } catch (Exception e) {
                System.out.println("handlerDB: Got " + e);
                System.out.println("Closiing handlerDB");
            }
        }

    }
    private static class handlerClient implements Runnable {
        public void run() {
            while (true) {
                ServerGame serverGame = new ServerGame();
                serverGame.run();
            }
        }
    }
    public static void main(String[] args) {
        (new Thread(new handlerDB())).start();
        (new Thread(new handlerClient())).start();
       // MariaDB.getInstance().close();
    }
}
