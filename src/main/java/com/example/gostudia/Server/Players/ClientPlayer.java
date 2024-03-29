package com.example.gostudia.Server.Players;

import com.example.gostudia.Database.Winner;
import com.example.gostudia.Logic.Board;
import com.example.gostudia.Server.InputOperations.InputOperation;
import com.example.gostudia.Server.InputOperations.MoveOperation;
import com.example.gostudia.Server.InputOperations.PassOperation;
import com.example.gostudia.Server.InputOperations.SurrenderOperation;
import com.example.gostudia.StateField;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class ClientPlayer implements IPlayer{
    private final ObjectOutputStream oos;
    private final InputStream input;
    private final StateField color;
    private final Socket socket;

    public ClientPlayer(Socket socket, StateField color) throws IOException {
        this.socket = socket;
        this.color = color;
        //Wysylanie do socketa
        oos = new ObjectOutputStream(socket.getOutputStream());
        OutputStream output = socket.getOutputStream();
        PrintWriter out = new PrintWriter(output, true);

        switch(color) {
            case BLACK -> out.println("You are black colour");
            case WHITE -> out.println("You are white colour");
        }

        //Odbieranie od socketa
        input = socket.getInputStream();
    }

    @Override
    public StateField getColor() {
        return color;
    }

    @Override
    public void sendActiveTurn(boolean active) throws IOException {
        oos.writeObject(active);
    }

    @Override
    public void sendBoard(Board board) throws IOException {
        oos.writeObject(board.getBoard());
    }

    @Override
    public void sendPass() {
    }

    @Override
    public void sendEnd(Winner w) throws IOException {
        oos.writeObject(w.toString());
    }

    public void waitBot() throws IOException, InterruptedException {
        input.skip(input.available());
        Scanner scan = new Scanner(input);

        while(true) {
            String inputStr = scan.nextLine().strip();
            if (inputStr.equals("bot"))
                return;
            else
                Thread.sleep(20);
        }

    }

    @Override
    public InputOperation getInput() throws IOException {
        input.skip(input.available());

        Scanner scan = new Scanner(input);
        String inputStr = scan.nextLine().strip();

        if (inputStr.equals("surrender")) {
            return new SurrenderOperation();
        } else if (inputStr.equals("pass")) {
            return new PassOperation();
        } else {
            String[] inputs = inputStr.split(" ");
            int x = Integer.parseInt(inputs[0]);
            int y = Integer.parseInt(inputs[1]);

            return new MoveOperation(x, y, this);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
