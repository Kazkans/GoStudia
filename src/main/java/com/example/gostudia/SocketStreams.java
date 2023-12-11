package com.example.gostudia;

import java.io.*;
import java.net.Socket;

public class SocketStreams {
    public ObjectOutputStream oos;
    public PrintWriter out;
    public InputStream input;
    public BufferedReader in;
    public SocketStreams(Socket socket) throws IOException {
        //Wysylanie do socketa
        oos = new ObjectOutputStream(socket.getOutputStream());
        OutputStream output = socket.getOutputStream();
        out = new PrintWriter(output, true);

        //Odbieranie od socketa
        input = socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(input));

    }

}