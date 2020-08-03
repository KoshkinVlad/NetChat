package ru.geekbrains.network;

public interface MessageSocketThreadListener {
    void onMessageReceived(String msg);
    void onException(Throwable throwable);
    void onSocketReady();
    void onSocketClosed();
}
