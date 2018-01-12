package com.todo;

import com.todo.Models.IUsers;
import com.todo.Models.UsersProxy;
import com.todo.Models.XmlUsers;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class Main extends Application {


    @FXML
    private  ListView<String> list;

    @Override
    public void start(Stage primaryStage) throws Exception{
        IUsers u = new XmlUsers();
        UsersProxy upXML = new UsersProxy(u);
        XmlUsers xmlU = (XmlUsers) upXML.CreateUsers();
        Parent root = FXMLLoader.load(getClass().getResource("Views/login.fxml"));
        Scene rootScene = new Scene(root);
        primaryStage.setScene(rootScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}