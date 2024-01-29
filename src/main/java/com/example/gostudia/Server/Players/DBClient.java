package com.example.gostudia.Server.Players;

import com.example.gostudia.Database.GameEntity;
import com.example.gostudia.Logic.Board;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Scanner;

public class DBClient {
    protected InputStream input;
    private final ObjectOutputStream oos;
    private final Socket socket;
    public DBClient(Socket socket) throws IOException {
        this.socket=socket;
        //Wysylanie do socketa
        oos = new ObjectOutputStream(socket.getOutputStream());
        //Odbieranie od socketa
        input = new BufferedInputStream(socket.getInputStream());
    }

    public void sendGames(List<GameEntity> list) throws IOException {
        oos.writeObject(list);
    }

    public void sendEnd() throws IOException {
        oos.writeObject("Game Ended");
    }

    public OptionalInt getPage() throws IOException {
        try {
            input.mark(100);
            Scanner scan = new Scanner(input);
            String inputStr = scan.nextLine().strip();
            System.out.println("getPage: " + inputStr);
            String[] inputs = inputStr.split(" ");
            if(Objects.equals(inputs[0], "page")) {
                int n = Integer.parseInt(inputs[1]);
                return OptionalInt.of(n);
            }
        } catch(Exception ignored) {
        }
        input.reset();
        return OptionalInt.empty();
    }

    public void sendBoard(Board board) throws IOException {
        oos.writeObject(board.getBoard());
    }

    public void close() throws IOException {
        socket.close();
    }
    public int getId() {
        Scanner scan = new Scanner(input);
        return scan.nextInt();
    }
}
