package ru.geekbrains.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class ServerSocketThread extends Thread {
    private final int port;
    private final SocketThreadListener socketThreadListener;
    private final int timeout;

    public ServerSocketThread(SocketThreadListener socketThreadListener, String name, int port, int timeout) {
        super(name);
        this.port = port;
        this.socketThreadListener = socketThreadListener;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(timeout);
            while (!isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
//                как только кто-то подключился, надо сокет отдать чат серверу
                    socketThreadListener.onSocketReceived(socket);
                    socketThreadListener.onClientConnected(socket);
                } catch (SocketTimeoutException ex) {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
