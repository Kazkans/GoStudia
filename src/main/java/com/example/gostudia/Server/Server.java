package com.example.gostudia.Server;

import com.example.gostudia.Logic.Board;
import com.example.gostudia.StateField;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static Board board = new Board(19);

    public static int consecutivePassess=0;
    public static void main(String[] args) {
        SocketStreams blackStreams = null;
        SocketStreams whiteStreams = null;

        try (ServerSocket serverSocket = new ServerSocket(4444)) {

            System.out.println("Server is listening on port 4444");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                if(blackStreams == null) {
                    System.out.println("Black connected");
                    blackStreams = new SocketStreams(socket);
                    blackStreams.out.println("You are black colour");
                } else if(whiteStreams == null) {
                    System.out.println("White connected");
                    whiteStreams = new SocketStreams(socket);
                    whiteStreams.out.println("You are white colour");
                }
                if(blackStreams!=null && whiteStreams!=null)
                    startGame(blackStreams, whiteStreams);
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
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
            mainStreams.oos.writeObject(new Signal(true));

            Scanner scan = new Scanner(mainStreams.input);
            while (true) {
                String inputStr = scan.nextLine().strip();
                System.out.println(inputStr);
                if (inputStr.equals("pass")) {
                    System.out.println("pass: " + color);
                    consecutivePassess+=1;
                    if(consecutivePassess==2) {
                      //  endGame();
                    }
                    return;
                } else {
                    consecutivePassess = 0;
                    String[] inputs = inputStr.split(" ");
                    int x = Integer.parseInt(inputs[0]);
                    int y = Integer.parseInt(inputs[1]);
                    System.out.println("list: " + color + " " + x + " " + y);

                    if (!board.place(x, y, color))
                        continue;

                    Signal s = new Signal(board.getBoard());

                    mainStreams.oos.writeObject(s);
                    sideStreams.oos.writeObject(s);

                    mainStreams.oos.writeObject(new Signal(false));

                    return;
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
