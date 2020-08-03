package ru.geekbrains.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.geekbrains.core.ChatServer;
import ru.geekbrains.core.ChatServerListener;

public class Controller implements ChatServerListener {
    @FXML
    public TextArea logArea;

    private final int PORT = 8080;
    private ChatServer chatServer = new ChatServer(this);

    public void start(ActionEvent actionEvent) {
        chatServer.start(PORT);
    }

    public void stop(ActionEvent actionEvent) {
        chatServer.stop();
    }

    public void disconnectAll(ActionEvent actionEvent) {
        chatServer.disconnectAll();
    }

    @Override
    public void onChatServerMessage(String message) {
        logArea.appendText(message + System.lineSeparator());
    }
}
