package ru.geekbrains.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.chat.common.MessageLibrary;
import ru.geekbrains.network.MessageSocketThreadListener;
import ru.geekbrains.network.ServerSocketThread;
import ru.geekbrains.network.SocketThreadListener;

import java.net.Socket;
import java.util.Vector;

public class ChatServer implements SocketThreadListener, MessageSocketThreadListener {

    private ServerSocketThread serverSocketThread;
    private ClientSessionThread clientSession;
    private ChatServerListener listener;
    private AuthController authController;
    private Vector<ClientSessionThread> clients = new Vector<>();
    private static final Logger logger = LogManager.getLogger(ChatServer.class);

    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
    }

    public void start(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            return;
        }
        serverSocketThread = new ServerSocketThread(this, "Server Socket Thread", port, 3000);
        logMessage("Запускаю сервер на порту " + port);
        serverSocketThread.start();
        authController = new AuthController();
        authController.init();
    }

    public void stop() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            return;
        }
        logMessage("Останавливаю чат сервер");
        serverSocketThread.interrupt();
    }

    @Override
    public void onClientConnected(Socket socket) {
    }

    public void onSocketReceived(Socket socket) {
        this.clientSession = new ClientSessionThread(this, "ClientSessionThread", socket);
        clients.add(this.clientSession);
    }

    @Override
    public void onMessageReceived(String message) {
        if (clientSession.isAuthorized()) {
            processAuthorizedUserMessage(message);
        } else {
            processUnAuthorizedUserMessage(message);
        }
    }

    private void processAuthorizedUserMessage(String message) {
        logMessage(message);
        if (message.matches(MessageLibrary.ALTER_NICKNAME + MessageLibrary.DELIMITER + ".*")) {
            String[] splited = message.split(MessageLibrary.DELIMITER);
            authController.alterNickName(splited[1], splited[2]);
            clientSession.sendMessage("служебное сообщение: пользователь " + splited[1] + " сменил свой никнейм. Теперь он " + splited[2]);
        } else {
            clientSession.sendMessage("echo: " + message);
        }

    }

    private void processUnAuthorizedUserMessage(String message) {
        String[] arr = message.split(MessageLibrary.DELIMITER);
        if (arr[0].equals(MessageLibrary.TYPE_BROADCAST) && arr.length == 4) {
            String broadMessage = arr[2] + " говорит всем: \"" + arr[3] + "\"";
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).sendMessage(broadMessage);
            }
        } else {
            if (arr.length < 4 ||
                    !arr[0].equals(MessageLibrary.AUTH_METHOD)
                    || !arr[1].equals(MessageLibrary.AUTH_REQUEST)) {

                //clientSession.authError("Bad request " + message);
                clientSession.authError("Ошибка при входе в чат");
                return;
            }
            String login = arr[2];
            String password = arr[3];
            String nickname = authController.getNickname(login, password);
            if (nickname == null) {
                clientSession.authDeny();
                return;
            }
            // авторизиван
            clientSession.authAccept(nickname);
        }

    }

    @Override
    public void onException(Throwable throwable) {
        logMessage(throwable.getMessage());
    }

    @Override
    public void onSocketReady() {
        logMessage(clientSession.getSocket().getRemoteSocketAddress() + " подключился!");
    }

    @Override
    public void onSocketClosed() {
        logMessage(clientSession.getSocket().getRemoteSocketAddress() + " отключился!");
    }

    public void disconnectAll() {
    }

    private void logMessage(String message) {
        listener.onChatServerMessage(message);
        logger.info(message);   // а можно в метод передавать уровень логирования? я нашёл такое перечисление:
//        Level level=Level.INFO;
//        logger.atLevel(level);
//        но не пойму, как работать с этим
    }
}