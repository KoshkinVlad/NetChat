package ru.geekbrains.core;

import ru.geekbrains.data.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class AuthController {

    private HashMap<String, User> users = new HashMap<>();
    private Connection conn;

    public void init() {
        for (User user : receiveUsers()) {
            users.put(user.getLogin(), user);// структура типа "логин" (ключ) - "пользователь"(значение)
        }

    }

    public String getNickname(String login, String password) {
        User user = users.get(login);
        if (user != null && user.isPasswordCorrect(password)) {
            return user.getNickname();
        }
        return null;
    }

    private ArrayList<User> receiveUsers() {
        ArrayList<User> userArrayList = new ArrayList<>();
        try {
            Statement st = dbConnect();
            ResultSet rs = st.executeQuery("select * from connectioninfo");

            while (rs.next()) {
                String login = rs.getString("Login");
                String password = rs.getString("Password");
                String nickname = rs.getString("Nickname");
                userArrayList.add(new User(login, password, nickname));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userArrayList;
    }

    private Statement dbConnect() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
//        Properties prop = new Properties();
//        prop.setProperty("useSSL", "false");
//        prop.setProperty("serverTimezone", "Europe/Moscow");
        // The newInstance() call is a work around for some
        // broken Java implementations
//        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/messanger?user=root&password=123456", prop);

        Class.forName("org.sqlite.JDBC").newInstance();
        conn = DriverManager.getConnection("jdbc:sqlite:main.db");
        return conn.createStatement();
    }

    public void alterNickName(String login, String newNickName) {
        try {
            Statement st = dbConnect();
            st.executeUpdate("UPDATE connectioninfo SET Nickname='" + newNickName + "' WHERE login='" + login + "';");
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

