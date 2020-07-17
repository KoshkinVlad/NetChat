import ru.geekbrains.network.MessageSocketThread;
import ru.geekbrains.network.MessageSocketThreadListener;
import ru.geekbrains.network.ServerSocketThread;
import ru.geekbrains.network.SocketThreadListener;

import java.net.Socket;

public class Server implements MessageSocketThreadListener, SocketThreadListener {
    MessageSocketThread socketThread;
    ServerSocketThread serverSocketThread;

    public void start() {
        serverSocketThread=new ServerSocketThread(this, "Test", 8080);
        serverSocketThread.start();
    }

    private String reversed(String init) {
        StringBuilder sb=new StringBuilder();
        for(int i=init.length()-1;i>=0;i--){
            sb.append(init.charAt(i));
        }
        return sb.toString();
    }

    public void onMessageReceived(String msg) {
        System.out.println(msg);
        socketThread.sendMessage(reversed(msg));
    }

    public void onClientConnected(Socket socket) {
        System.out.println(socket.getRemoteSocketAddress()+" подключился!");
    }

    public void onSocketReceived(Socket socket) {
        this.socketThread=new MessageSocketThread(this, socket.getRemoteSocketAddress().toString(), socket);
    }
}
