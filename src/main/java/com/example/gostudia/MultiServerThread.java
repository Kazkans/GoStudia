package com.example.gostudia;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiServerThread {
    public enum Color {WHITE, BLACK}
    private static StateField[][] board = new StateField[19][19];

    public static void main(String[] args) {
        Socket black_socket = null;
        SocketStreams blackStreams = null;
        SocketStreams whiteStreams = null;

        Socket white_socket = null;
        initBoard();

        try (ServerSocket serverSocket = new ServerSocket(4444)) {

            System.out.println("Server is listening on port 4444");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                if(blackStreams == null) {
                    System.out.println("Black connected");
                    blackStreams = new SocketStreams(socket);
                } else if(whiteStreams == null) {
                    System.out.println("White connected");
                    whiteStreams = new SocketStreams(socket);
                }
                if(blackStreams!=null && whiteStreams!=null)
                    startGame(blackStreams, whiteStreams);
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void initBoard() {
        for(int i=0;i<19;i++) {
            for(int j=0;j<19;j++) {
                board[i][j] = StateField.EMPTY;
            }
        }
    }

    public static void startGame(SocketStreams blackStreams, SocketStreams whiteStreams) {
        while(true) {
            move(blackStreams, whiteStreams, StateField.BLACK);
            move(whiteStreams, blackStreams, StateField.WHITE);
        }
    }
    public static void move(SocketStreams mainStreams, SocketStreams sideStreams, StateField color) {
        try {
            // skips bytes that were sent before turn
            mainStreams.input.skip(mainStreams.input.available());

            Scanner scan = new Scanner(mainStreams.input);
            while (true) {
                String inputStr = scan.nextLine().strip();
                System.out.println(inputStr);
                if (inputStr.equals("pass")) {
                    System.out.println("pass");
                } else {
                    String[] inputs = inputStr.split(" ");
                    int x = Integer.parseInt(inputs[0]);
                    int y = Integer.parseInt(inputs[1]);
                    System.out.println("list: " + color + " " + x + " " + y);
                    board[x][y] = color;
                    mainStreams.oos.writeObject(board);
                    sideStreams.oos.writeObject(board);
                    mainStreams.oos.reset();
                    sideStreams.oos.reset();

                    return;
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
