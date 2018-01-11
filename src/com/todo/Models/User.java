package com.todo.Models;

import java.util.List;

public class User implements IUser{

    private String Login;
    private List<String> Tasks;

    public void setTasks(List<String> tasks) {
        Tasks = tasks;
    }

    public List<String> getTasks() {
        return Tasks;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Password;

    public String getLogin() {
        return Login;
    }

    public String getPassword() {
        return Password;
    }
}
