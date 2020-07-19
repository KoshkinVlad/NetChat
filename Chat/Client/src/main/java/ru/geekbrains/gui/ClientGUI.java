package ru.geekbrains.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.geekbrains.chat.common.MessageLibrary;
import ru.geekbrains.network.*;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, MessageSocketThreadListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final int X_SPAWN = 800;
    public static final int Y_SPAWN = 450;

    private final JTextArea chatArea = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField ipAddressField = new JTextField("127.0.0.1");
    private final JTextField portField = new JTextField("8080");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top", true);
    private final JTextField loginField = new JTextField("admin");
    private final JPasswordField passwordField = new JPasswordField("admin");
    private final JButton buttonLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton buttonDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField messageField = new JTextField();
    private final JButton buttonSend = new JButton("Send");

    private final JList<String> listUsers = new JList<String>();

    private MessageSocketThread messageSocketThread;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });

    }


    ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat");
        setLocation(X_SPAWN, Y_SPAWN);
        setSize(WIDTH, HEIGHT);
        setAlwaysOnTop(true);

        listUsers.setListData(new String[]{"user1", "user2", "user3", "user4",
                "user5", "user6", "user7", "user8", "user9", "user-with-too-long-name-in-this-chat"});
        JScrollPane scrollPaneUsers = new JScrollPane(listUsers);
        JScrollPane scrollPaneChatArea = new JScrollPane(chatArea);
        scrollPaneUsers.setPreferredSize(new Dimension(100, 0));

        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);

        panelTop.add(ipAddressField);
        panelTop.add(portField);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(loginField);
        panelTop.add(passwordField);
        panelTop.add(buttonLogin);
        panelBottom.add(buttonDisconnect, BorderLayout.WEST);
        panelBottom.add(messageField, BorderLayout.CENTER);
        panelBottom.add(buttonSend, BorderLayout.EAST);

        add(scrollPaneChatArea, BorderLayout.CENTER);
        add(scrollPaneUsers, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        panelBottom.setVisible(false);
        panelTop.setVisible(true);

        cbAlwaysOnTop.addActionListener(this);
        buttonSend.addActionListener(this);
        messageField.addActionListener(this);
        buttonLogin.addActionListener(this);
        buttonDisconnect.addActionListener(this);

        setVisible(true);
    }

    private void sendMessage(String name, String message) {
        if (!message.isEmpty()) {
            putMessage(name, message);
            messageField.setText("");
            messageField.grabFocus();
            messageSocketThread.sendMessage(message);
        }
    }

    private void putMessage(String name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy H:m:s");
        String messageFormatted = String.format("[%s] %s: %s\n", dateFormat.format(new Date()), name, message);
        chatArea.append(messageFormatted);
        writeLog(messageFormatted);
    }

    private void writeLog(String message) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("messages.log", true))) {
            pw.print(message);
        } catch (FileNotFoundException e) {
            showError(message);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == buttonSend || src == messageField) {
            sendMessage(loginField.getText(), messageField.getText());
        } else if (src == buttonLogin) {
            try {
                Socket socket = new Socket(ipAddressField.getText(), Integer.parseInt(portField.getText()));
                messageSocketThread = new MessageSocketThread(this, loginField.getText(), socket);
            } catch (UnknownHostException unknownHostException) {
                showError(unknownHostException.getMessage());
            } catch (IOException ioException) {
                showError(ioException.getMessage());
            }
        } else if (src == buttonDisconnect) {
            messageSocketThread.close();

        } else {
            throw new RuntimeException("Unsupported action: " + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        StackTraceElement[] ste = e.getStackTrace();
        String msg = String.format("Exception in \"%s\": %s %s%n\t %s",
                t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
        showError(msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Exception!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onMessageReceived(String msg) {
        putMessage("Server", msg);
    }

    @Override
    public void onException(Throwable throwable) {
        showError(throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onSocketReady() {
        panelTop.setVisible(false);
        panelBottom.setVisible(true);
        messageSocketThread.sendMessage(MessageLibrary.getAuthRequestMessage(loginField.getText(), new String(passwordField.getPassword())));
    }

    @Override
    public void onSocketClosed() {
        panelTop.setVisible(true);
        panelBottom.setVisible(false);
    }
}
