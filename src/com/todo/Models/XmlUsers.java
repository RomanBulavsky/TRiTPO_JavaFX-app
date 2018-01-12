package com.todo.Models;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "XmlSource")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUsers implements IUsers{
    @XmlElement(name = "user")
    private List<User> persons;

    public List<User> getPersons() {
        return this.persons;
    }

    public void setPersons(List<User> p) {
        this.persons = p;
    }

    public IUsers CreateUsers(){
        return  new XmlUsers();
    }
}

