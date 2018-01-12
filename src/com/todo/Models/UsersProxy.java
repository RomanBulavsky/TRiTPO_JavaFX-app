package com.todo.Models;

import java.util.Date;

public class UsersProxy implements IUsers {
    IUsers users;

    public UsersProxy(IUsers users) {
        this.users = users;
    }

    public void setUsers(IUsers users) {
        this.users = users;
    }

    public IUsers getUsers() {
        return users;
    }

    @Override
    public IUsers CreateUsers() {
        System.out.println("CreateUsers is called " + new Date().toString());
        return users.CreateUsers();
    }
}
