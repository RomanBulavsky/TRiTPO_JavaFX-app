package com.todo.Controllers;

import com.todo.Models.User;
import com.todo.Models.IUsers;
import com.todo.Models.XmlUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.bind.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LoginController {
    @FXML
    private TextField inputName;
    @FXML
    private PasswordField inputPassword;

    private XmlUsers users;

    /*public void test() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(XmlUsers.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File("src/input.xml");
        xml.createNewFile();
        //XmlConf xmlConf =(XmlConf) unmarshaller.unmarshal(xml);

        XmlUsers xmlConf = new XmlUsers();
        xmlConf.setAge(12);
        List l = new LinkedList();
        User u = new User();
        u.setLogin("Vasa");
        u.setPassword("123");
        List<String> tasks = new LinkedList<String>();
        tasks.add("Lol Is done");
        tasks.add("Lol2 Is done");
        tasks.add("Lasd");
        tasks.add("Lxc");
        List<String> tasks2 = new LinkedList<String>();
        tasks2.add("2Lol Is done");
        tasks2.add("2Lol2 Is done");
        tasks2.add("2Lasd");
        tasks2.add("2Lxc");
        u.setTasks(tasks);
        User u2 = new User();
        u2.setTasks(tasks2);
        u2.setLogin("zasasd");
        u2.setPassword("1xxx1x1");
        l.add(u);
        l.add(u2);
        xmlConf.setPersons(l);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(xmlConf, xml);

    }*/

    public IUsers test2() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(XmlUsers.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File("src/input.xml");
        xml.createNewFile();
        XmlUsers xmlConf =(XmlUsers) unmarshaller.unmarshal(xml);

        List<User> list  = (List<User>) xmlConf.getPersons();
        //Marshaller marshaller = jc.createMarshaller();
        //marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //marshaller.marshal(xmlConf, xml);
        return xmlConf;
    }
    @FXML
    public void onLogin(ActionEvent event) throws IOException, JAXBException {
        //test();
        users = (XmlUsers)test2();
        //savePersonDataToFile(getPersonFilePath());

        if(checkUser()) {
            FXMLLoader Loader = new FXMLLoader();
            Parent root = null;
            Loader.setLocation(getClass().getResource("../Views/main.fxml"));
            Loader.load();

            ListController dc =  Loader.getController();
            dc.setName(users.getPersons().get(0).getLogin() + " " + users.getPersons().get(0).getPassword());
            dc.setGenericLinkedList(users.getPersons().get(0).getTasks());
            dc.setUsers(users);
            root = Loader.getRoot();
            Scene rootScene = new Scene(root);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //rootScene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            appStage.setScene(rootScene);
            appStage.show();
        }
        else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Login or Password");
            alert.setHeaderText("Please write down the right credentials.");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    private boolean checkUser(){
        String userName = inputName.getText();
        String userPassword = inputPassword.getText();

        List<User> uuu = ((XmlUsers)users).getPersons();
        for (User user : uuu) {

            if(user.getLogin().equals(userName))
                if(user.getPassword().equals(userPassword)){
                    return true;
                }
        }

        return false;
    }
}
