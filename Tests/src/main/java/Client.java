import ru.geekbrains.network.MessageSocketThread;
import ru.geekbrains.network.MessageSocketThreadListener;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements MessageSocketThreadListener {
    Socket socket;

    public void start() {
        try {
            socket = new Socket("localhost", 8080);
            MessageSocketThread messageSocketThread = new MessageSocketThread(this, "Client", socket);
            //--2 hours later...
            messageSocketThread.sendMessage("улыбок тёбе дед мокар");
            messageSocketThread.sendMessage("улыбок тёбе дед мокар");
            messageSocketThread.sendMessage("улыбок тёбе дед мокар");
            messageSocketThread.sendMessage("улыбок тёбе дед мокар");
            messageSocketThread.sendMessage("улыбок тёбе дед мокар");
            messageSocketThread.sendMessage("улыбок тёбе дед мокар");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onMessageReceived(String msg) {
        System.out.println(msg);
    }
}
