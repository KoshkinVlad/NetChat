package ru.geekbrains.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MessageSocketThread extends Thread {
    private Socket socket;
    private MessageSocketThreadListener listener;
    private DataInputStream in;
    private DataOutputStream out;

    public MessageSocketThread(MessageSocketThreadListener listener, String name, Socket socket) {
        super(name);
        this.socket = socket;
        this.listener = listener;
        start();
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener.onSocketReady();
            while (!isInterrupted()) {
                String msg = in.readUTF();
                listener.onMessageReceived(msg);
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            close();
        }
    }

    public void sendMessage(String msg) {
        try {
            if(!socket.isConnected()||socket.isClosed()){
                listener.onException(new RuntimeException("Сокет закрыт или не инициализирован"));
            }
            out.writeUTF(msg);
        } catch (IOException e) {
            close();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void close() {
        listener.onSocketClosed();
        interrupt();
        try {
            socket.close();
            if (out != null){
                out.close();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
