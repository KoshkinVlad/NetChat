package ru.geekbrains.core;

import ru.geekbrains.data.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthController {

    private HashMap<String, User> users = new HashMap<>();

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
        userArrayList.add(new User("admin", "admin", "sysroot"));
        userArrayList.add(new User("Koshkin", "123", "-=КоШкИн=-"));
        return userArrayList;
    }
}
