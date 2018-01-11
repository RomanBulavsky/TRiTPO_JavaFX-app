package com.todo.Controllers;

        import com.todo.Models.IUsers;
        import com.todo.Models.XmlUsers;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.*;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.control.Alert.AlertType;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.Pane;
        import javafx.scene.layout.Priority;
        import javafx.stage.Stage;
        import javafx.util.Callback;

        import javax.xml.bind.JAXBContext;
        import javax.xml.bind.JAXBException;
        import javax.xml.bind.Marshaller;
        import java.io.File;
        import java.io.IOException;
        import java.util.List;
        import java.util.Optional;

public class ListController {
    @FXML
    private Label Name;

    public void setName(String name) {
        Name.setText(name);
    }

    @FXML
    private ListView<String> listView;
    @FXML
    private TextField inpuTextField;

    private ObservableList<String> genericLinkedList;

    private IUsers users;
    //private ObservableList<String> observablelist;

    public void setUsers(IUsers users) {
        this.users = users;
    }
    @FXML
    public void LogoutButton(ActionEvent event) throws JAXBException, IOException {
        test();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Title");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            FXMLLoader Loader = new FXMLLoader();
            Parent root = null;
            Loader.setLocation(getClass().getResource("../Views/login.fxml"));
            Loader.load();

            root = Loader.getRoot();
            Scene rootScene = new Scene(root);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //rootScene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            appStage.setScene(rootScene);
            appStage.show();
        } else {
            // ... user chose CANCEL or closed the dialog
        }


    }

    public void setGenericLinkedList(List<String> genericLinkedList) {
        ObservableList<String> o = FXCollections.observableArrayList(genericLinkedList);
        this.genericLinkedList = o;
        OnShow(null);
    }

    public void test() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(XmlUsers.class);

        File xml = new File("src/input.xml");

        //xml.delete();
        //xml.createNewFile();
        //XmlConf xmlConf =(XmlConf) unmarshaller.unmarshal(xml);

//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//
//        Users xmlConf =(Users) unmarshaller.unmarshal(xml);
//
//        List<User> list  = (List<User>) xmlConf.getPersons();


        //List<User> list  = (List<User>) users.getPersons();
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        List<String> l = (List<String>)genericLinkedList;
        ((XmlUsers)users).getPersons().get(0).setTasks(l);
        //marshaller.marshal(null,xml);
        try{

            marshaller.marshal(users, xml);
        }catch (Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }




    }

    public ListController(){
        genericLinkedList = FXCollections.observableArrayList();
        //observablelist = FXCollections.observableArrayList();
    }
    private void showInvalidDataAlert(Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid input data");
        alert.setHeaderText("Please fill the input");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }

    @FXML
    public void AddButton(ActionEvent event) {
        String input = inpuTextField.getText();
        try {
            if(input.equals(""))
                throw new Exception();
            genericLinkedList.add(input);
            inpuTextField.clear();
            OnShow(null);
        } catch (Exception e) {
            e.printStackTrace();
            this.showInvalidDataAlert(e);
        }
    }


    @FXML
    public void OnDelete(ActionEvent event) {
        String input = inpuTextField.getText();
        try {
            if(input.equals(""))
                throw new Exception();
            genericLinkedList.remove(input);
            inpuTextField.clear();
            OnShow(null);
        } catch (Exception e) {
            e.printStackTrace();
            this.showInvalidDataAlert(e);
        }
    }

    private void OnShow(ActionEvent event) {
        //observablelist = FXCollections.observableArrayList(genericLinkedList);
        listView.setItems(genericLinkedList);

        //Factory
        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                XCell x = new XCell(genericLinkedList);
                //x.setListView();
                return x;
            }
        });

    }

    static class XCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("X");
        CheckBox buttonDone = new CheckBox();
        String lastItem;
        private ObservableList<String> listView;

        public XCell(ObservableList<String> listView) {
            super();
            this.listView = listView;
            hbox.getChildren().addAll(buttonDone, label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);

            //Strategy!
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
            //Strategy!
            buttonDone.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    for(int i = 0; i < listView.size(); i++)
                    {
                        String x = listView.get(i);
                        if(x == label.getText()){
                            if(x.contains("Is done")){
                                String s = String.format("%s",label.getText().replace("Is done",""));
                                listView.set(i, s);
                                break;
                            }

                            String s = String.format("%s %s",label.getText(), "Is done");
                            listView.set(i, s);
                            //listView.set(i, "xxx");
                            //genericLinkedList.set(i, label.getText() + " is DONE!");
                        }
                    }
                    //label = new Label("done");

                    System.out.println(label + " : " + event);
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
            }
//            } else if(item.contains("DONE")) {
//                lastItem = item;
//                label.setText(item!=null ? item : "<null>");
//                //getStylesheets().add("/stylesheet.css");
//                //this.setTextFill(Paint.valueOf("green"));
//                //setStyle("xxx");
//                getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
//                //getStyleClass().add("xxx");
//                PseudoClass inactive = PseudoClass.getPseudoClass("xxx");
//                pseudoClassStateChanged(inactive, item != null && item.endsWith(" - not active"));
//            }
            else{
                lastItem = item;
                if(item.contains("Is done"))
                    buttonDone.setSelected(true);
                else
                    buttonDone.setSelected(false);
                label.setText(item!=null ? item : "<null>");
                setGraphic(hbox);
            }
        }

        public void setListView(ObservableList<String> listView) {
            this.listView = listView;
        }
    }
}

