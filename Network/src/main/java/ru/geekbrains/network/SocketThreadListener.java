package ru.geekbrains.network;

import java.net.Socket;

public interface SocketThreadListener {
    void onClientConnected(Socket socket);
    void onSocketReceived(Socket socket);
}
