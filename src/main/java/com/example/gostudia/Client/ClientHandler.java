package com.example.gostudia.Client;

import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.StateField;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Manages server and handles all actions
 */
public abstract class ClientHandler extends Thread {
    private final PrintWriter out;
    private final BufferedReader in;
    private final ObjectInputStream ois;
    private final int size;
    private String color;

    public ClientHandler(Socket socket, int size, boolean gameMode) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.size=size;
        if(gameMode)
            readColor();
    }

    private void readColor() {
        Scanner scan = new Scanner(in);
        color = scan.nextLine().strip();
    }

    public List<GameEntity> readGames() {
        try {
            return (List<GameEntity>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String getColor() {
        return color;
    }

    public void run() {
        while(true) {
            try {
                Object obj = ois.readObject();
                switch(obj) {
                    case String s -> updateLabel(s);
                    case Boolean b -> updateTurn(b);
                    case StateField[][] b -> updateBoard(b);
                    default -> throw new ClassNotFoundException();
                }
            } catch (IOException e){
                detach();
                return;
                // TODO: safe return
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void sendMove(int i, int j) {
        out.println(i +" "+ j);
        try {  // sometimes server doesnt catch for the first time
            out.wait(100);
        } catch (Exception ignored) {}
        out.println(i +" "+ j);
    }
    public void sendBot() {
        out.println("bot");
    }
    public void sendPass() {
        out.println("pass");
    }
    public void sendSurrender() {
        out.println("surrender");
    }
    public void sendPage(int page) {
        out.println("page " + page);
    }
    public void sendId(long id) {
        out.println(id);
    }
    private void updateBoard(StateField[][] stateBoard) {
        System.out.println("Got a new board!!");
        for(int i=0;i<size;i++) {
            for(int j=0;j<size;j++){
                updateField(i, j, stateBoard[i][j]);
            }
        }
    }

    public abstract void updateLabel(String s);
    public abstract void updateTurn(boolean mine);
    public abstract void updateField(int i, int j, StateField state);
    public abstract void detach();
}
