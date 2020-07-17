package ru.geekbrains.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends Thread {
    private final int port;
    private final SocketThreadListener socketThreadListener;

    public ServerSocketThread(SocketThreadListener socketThreadListener, String name, int port) {
        super(name);
        this.port = port;
        this.socketThreadListener = socketThreadListener;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!isInterrupted()) {
                System.out.println(getClass().getCanonicalName() + " cервер запущен, слушаю подключение в отдельном потоке...");
                Socket socket = serverSocket.accept();
//                как только кто-то подключился, надо сокет отдать чат серверу
                socketThreadListener.onSocketReceived(socket);
                socketThreadListener.onClientConnected(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
