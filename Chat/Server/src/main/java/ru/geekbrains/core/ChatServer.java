package ru.geekbrains.core;

import ru.geekbrains.network.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatServer implements SocketThreadListener, MessageSocketThreadListener {

    private ServerSocketThread serverSocketThread;
    private MessageSocketThread socket;

    public void start(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            return;
        }
        serverSocketThread = new ServerSocketThread(this, "Server Socket Thread", port);
        serverSocketThread.start();
    }

    public void stop() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            return;
        }
        System.out.println("Останавливаю чат сервер");
        serverSocketThread.interrupt();
    }

    @Override
    public void onClientConnected(Socket socket) {
        this.socket.sendMessage(socket.getRemoteSocketAddress() + " подключился!");
    }

    public void onSocketReceived(Socket socket) {
        this.socket = new MessageSocketThread(this, "ServerSocket", socket);
    }

    @Override
    public void onMessageReceived(String msg) {
        System.out.println(msg);
        socket.sendMessage("echo: " + msg);
    }
}