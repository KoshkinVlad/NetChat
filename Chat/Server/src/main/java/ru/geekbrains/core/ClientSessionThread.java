package ru.geekbrains.core;

import ru.geekbrains.chat.common.MessageLibrary;
import ru.geekbrains.network.MessageSocketThread;
import ru.geekbrains.network.MessageSocketThreadListener;

import java.net.Socket;
import java.util.Random;

public class ClientSessionThread extends MessageSocketThread {

    private boolean isAuthorized = false;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public ClientSessionThread(MessageSocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public void authAccept(String nickname) {
        this.nickname = nickname;
        this.isAuthorized = true;
        //sendMessage(MessageLibrary.getAuthAcceptMessage(nickname));
        sendMessage(makeWelcomeMessage(nickname));
    }

    private String makeWelcomeMessage(String nickname) {
        final String placeHolder = "\uD83D\uDDFF";
        final String[] possibleMessages = {
                placeHolder + " ворвался в этот чат!",
                "Вечеринка окончилась, " + placeHolder + " здесь...",
                "Welcome to the chat, " + placeHolder,
                "Рады видеть тебя, " + placeHolder,
                placeHolder + " уже здесь"
        };
        Random random=new Random();
        String welcomeMessage=possibleMessages[random.nextInt(possibleMessages.length)];
        String finalWelcomeMessage = welcomeMessage.replaceAll(placeHolder,nickname);
        return finalWelcomeMessage;
    }

    public void authDeny() {
        //sendMessage(MessageLibrary.getAuthDeniedMessage());
        sendMessage("Указаны неверные логин или пароль");
        close();
    }

    public void authError(String message) {
        sendMessage(MessageLibrary.getMsgFormatErrorMessage(message));
        close();
    }

}
