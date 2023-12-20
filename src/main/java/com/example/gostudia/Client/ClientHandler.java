package com.example.gostudia.Client;

import com.example.gostudia.Server.Signal;
import com.example.gostudia.StateField;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Manages server and handles all actions
 */
public abstract class ClientHandler extends Thread {
    private PrintWriter out = null;
    private BufferedReader in;
    private ObjectInputStream ois;
    private int size;
    private String color;

    public ClientHandler(Socket socket, int size) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.size=size;
        readColor();
    }

    private void readColor() {
        Scanner scan = new Scanner(in);
        color = scan.nextLine().strip();
    }
    public String getColor() {
        return color;
    }

    public void run() {
        while(true) {
            try {
                Signal s = (Signal) ois.readObject();
                switch(s.getType()) {
                    case BOARD:
                        updateBoard((StateField[][]) s.getObject());
                        break;
                    case TURN:
                        updateTurn((Boolean) s.getObject());
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void sendMove(int i, int j) {
        out.println(i +" "+ j);
    }

    public void sendPass() {
        out.println("pass");
    }

    private void updateBoard(StateField[][] stateBoard) {
        for(int i=0;i<size;i++) {
            for(int j=0;j<size;j++){
                updateField(i, j, stateBoard[i][j]);
            }
        }
    }

    public abstract void updateTurn(boolean mine);
    public abstract void updateField(int i, int j, StateField state);
}
