package com.todo.Controllers;

        import com.todo.Models.IUsers;
        import com.todo.Models.User;
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

    public void setUsers(IUsers users) {
        this.users = users;
    }

    private Optional<ButtonType> Confirm(String title, String text, String header){

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        return alert.showAndWait();
    }
    @FXML
    public void LogoutButton(ActionEvent event) throws JAXBException, IOException {
        save();
        Optional<ButtonType> result = Confirm("Logout","Are you ok with this?", "Logout process");
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

    public void save() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(XmlUsers.class);

        File xml = new File("src/input.xml");

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        List<String> l = (List<String>)genericLinkedList;

        for (User u :((XmlUsers)users).getPersons()) {
            if(u.getLogin().equals(this.Name.getText())){
                u.setTasks(l);
            }
        }
//        ((XmlUsers)users).getPersons().get(0).setTasks(l);

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
            Optional<ButtonType> result = Confirm("Creating new task","Are you ok with this?", "Add the task: " + input + " ?");
            if(result.get() == ButtonType.OK){
                genericLinkedList.add(input);
            }
            inpuTextField.clear();
            OnShow(null);
        } catch (Exception e) {
            e.printStackTrace();
            this.showInvalidDataAlert(e);
        }
    }

//    @FXML
//    public void OnDelete(ActionEvent event) {
//        String input = inpuTextField.getText();
//        try {
//            if(input.equals(""))
//                throw new Exception();
//            genericLinkedList.remove(input);
//            inpuTextField.clear();
//            OnShow(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            this.showInvalidDataAlert(e);
//        }
//    }

    private void OnShow(ActionEvent event) {
        listView.setItems(genericLinkedList);

        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                XCell x = new XCell(genericLinkedList);
                return x;
            }
        });

    }

    class XCell extends ListCell<String> {
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

                    Optional<ButtonType> result = Confirm("Removing","Are you ok with this?", "Remove the " + label.getText());
                    if(result.get() == ButtonType.OK){
                        for(int i = 0; i < listView.size(); i++)
                        {
                            String removableText = listView.get(i);
                            if(removableText == label.getText()){
                                listView.remove(i);
                            }
                        }
                    }
                }
            });
            //Strategy!
            buttonDone.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    for(int i = 0; i < listView.size(); i++)
                    {
                        String labelText = listView.get(i);
                        if(labelText.equals(label.getText())){
                            if(labelText.contains("Is done")){
                                String s = String.format("%s",label.getText().replace("Is done",""));
                                listView.set(i, s);
                                break;
                            }

                            String s = String.format("%s %s",label.getText(), "Is done");
                            listView.set(i, s);
                        }
                    }
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

