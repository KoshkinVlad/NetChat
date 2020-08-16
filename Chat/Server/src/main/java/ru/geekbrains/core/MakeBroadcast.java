package ru.geekbrains.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MakeBroadcast {

    public static final String DELIMITER = "##";
    public static final String TYPE_BROADCAST = "/broadcast";

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF(TYPE_BROADCAST + DELIMITER + System.currentTimeMillis() + DELIMITER + "Test method" + DELIMITER + "Test message");
            System.out.println(in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
