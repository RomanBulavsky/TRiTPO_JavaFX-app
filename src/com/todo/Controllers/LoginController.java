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

    public IUsers getUsers() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(XmlUsers.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File("src/input.xml");
        xml.createNewFile();
        XmlUsers xmlConf =(XmlUsers) unmarshaller.unmarshal(xml);

        List<User> list  = (List<User>) xmlConf.getPersons();
        return xmlConf;
    }
    @FXML
    public void onLogin(ActionEvent event) throws IOException, JAXBException {
        //save();
        users = (XmlUsers)getUsers();
        //savePersonDataToFile(getPersonFilePath());

        if(checkUser()) {
            FXMLLoader Loader = new FXMLLoader();
            Parent root = null;
            Loader.setLocation(getClass().getResource("../Views/main.fxml"));
            Loader.load();

            ListController dc =  Loader.getController();
            //dc.setName(users.getPersons().get(0).getLogin());
            for (User u :((XmlUsers)users).getPersons()) {
                if(u.getLogin().equals(this.inputName.getText())){
                    dc.setName(u.getLogin());
                    dc.setGenericLinkedList(u.getTasks());
                }
            }
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
