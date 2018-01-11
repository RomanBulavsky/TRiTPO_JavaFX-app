package com.todo;

import com.todo.Models.IUsers;
import com.todo.Models.UsersProxy;
import com.todo.Models.XmlUsers;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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

        //primaryStage.setTitle("Hello World");
        Scene rootScene = new Scene(root);
        //rootScene.getStylesheets().add("stylesheet.css");
        //rootScene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        primaryStage.setScene(rootScene);
        primaryStage.show();


        //StackPane pane = new StackPane();
        //Scene scene = new Scene(pane, 300, 150);
        //primaryStage.setScene(scene);


        //ObservableList<String>
//        list = FXCollections.observableArrayList(
//                "Item 1", "Item 2", "Item 3", "Item 4");
//        ListView<String> lv = new ListView<>(list);
//        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
//            @Override
//            public ListCell<String> call(ListView<String> param) {
//                XCell x = new XCell();
//                x.setListView(list);
//                return x;
//            }
//        });
//        primaryStage.show();
//        ObservableList<String> olist = FXCollections.observableArrayList("Item 1", "Item 2", "Item 3", "Item 4");
//        list = new ListView<String>();
//        list.setItems(olist);
//        list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
//            @Override
//            public ListCell<String> call(ListView<String> param) {
//                XCell x = new XCell();
//                x.setListView(olist);
//                return x;
//            }
//        });
//        //list.setItems(FX);
//        pane.getChildren().add(list);

    }


    public static void main(String[] args) {
        launch(args);
    }

    static class XCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("(>)");
        Button buttonDone = new Button("@");
        String lastItem;
        private ObservableList<String> listView;

        public XCell() {
            super();
            hbox.getChildren().addAll(buttonDone, label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    for(int i = 0; i < listView.size(); i++)
                    {
                        String x = listView.get(i);
                        if(x == label.getText()){
                            listView.remove(i);
                        }
                    }

                    //System.out.println(x + " : " + event);
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item!=null ? item : "<null>");
                setGraphic(hbox);
            }
        }

        public void setListView(ObservableList<String> listView) {
            this.listView = listView;
        }
    }
}