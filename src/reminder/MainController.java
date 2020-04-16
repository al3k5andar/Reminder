package reminder;

import dao.Change;
import dao.ChangeFactory;
import enums.ChangeType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import reminder.customrendering.CustomListCellFactory;
import reminder.customrendering.CustomRowFactory;
import reminder.customrendering.CustomTableCellFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class MainController
{
    @FXML
    private TableView<Change> changeTable;
    @FXML
    private ListView<String> otherInformation;
    @FXML
    private TextArea changeInformation;
    @FXML
    private BorderPane mainWindow;
    @FXML
    private ImageView finishIndicator;
    @FXML
    private TableColumn<Change,String> pressId, changeType, changeSide;
    @FXML
    private Button completeButton;
    @FXML
    private ToggleButton filterButton;
    private ContextMenu tableContext, infoMenu;
    private MenuItem changeStartItem, changeFinishItem, otherInformationItem, deletePressItem, editItem, deleteItem;
    private Predicate<Change> allChanges, unfinished;
    private FilteredList<Change> filteredList;


    public void initialize(){

        completeButton.setDisable(true);

        //Context menu for table view
        tableContext= new ContextMenu();
        changeStartItem= new MenuItem("Change Start");
        changeFinishItem= new MenuItem("Change Finish");
        otherInformationItem= new MenuItem("Other Information");
        deletePressItem= new MenuItem("Delete");

        tableContext.getItems().addAll(changeStartItem, changeFinishItem, otherInformationItem, deletePressItem);

        changeStartItem.setOnAction(new StartChangeListener());
        changeFinishItem.setOnAction(new FinishChangeListener());
        otherInformationItem.setOnAction(new OtherInformationListener());
        deletePressItem.setOnAction(new DeletePressListener());


        //Context menu for other information in list view
        infoMenu= new ContextMenu();
        editItem= new MenuItem("Edit");
        deleteItem= new MenuItem("Delete");
        infoMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(new UpdateInfoListener());
        deleteItem.setOnAction(new DeleteInfoListener());

        //this part of program sets the table view width which is relative to main window
        changeTable.prefWidthProperty().bind(mainWindow.widthProperty().multiply(0.35));

        //next three callback calls links the table columns with change object
        pressId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Change, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Change, String> data) {
                return data.getValue().getPressIdProperty();
            }
        });
        changeType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Change, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Change, String> data) {
                return data.getValue().getChangeTypeProperty();
            }
        });

        changeSide.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Change, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Change, String> data) {
                return data.getValue().getChangeSideProperty();
            }
        });

        //Callback calls for cell rendering in columns  for mold change type and side where is the mold change
        changeType.setCellFactory(new CustomTableCellFactory<Change,String>());
        changeSide.setCellFactory(new CustomTableCellFactory<Change,String>());
        
        changeTable.setRowFactory(CustomRowFactory.getCustomRowFactory(tableContext));
        
        //This predicate shows all mold changes
        allChanges= new Predicate<Change>() {
            @Override
            public boolean test(Change change) {
                return true;
            }
        };
        //This predicate shows only unfinished mold changes
        unfinished= new Predicate<Change>() {
            @Override
            public boolean test(Change change) {
                return !change.isComplete();
            }
        };

        //A filtered list contains all mold changes who is depending to the predicate which shows only the unfinished mold changes or all mold changes
        filteredList= new FilteredList<>(ChangeFactory.getChangeList(), allChanges);

        //This part of the program sorts the presses according to their number
        SortedList<Change> sortedList= new SortedList<>(filteredList, new Comparator<Change>() {
            @Override
            public int compare(Change o1, Change o2) {
                return Integer.compare(o1.getPressId(), o2.getPressId());
            }
        });

        //this program's part sets table view items
        changeTable.setItems(sortedList);
        changeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        otherInformation.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //change information is a Text Area object, and he must be not editable
        changeInformation.setEditable(false);

        //Listener for selecting change in table view
        changeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Change>() {
            @Override
            public void changed(ObservableValue<? extends Change> observableValue, Change oldValue, Change newValue) {
                if (newValue != null)
                {
                    otherInformation.setItems(ChangeFactory.getAdditionalChangeInformation(newValue));
                    changeInformation.setText(ChangeFactory.changeInformation(newValue));

                    if(newValue.getChangeFinish() != null)
                        completeButton.setDisable(false);
                    else
                        completeButton.setDisable(true);


                    otherInformation.setCellFactory(CustomListCellFactory.getCustomCellFactory(infoMenu));
                    changeTable.setRowFactory(CustomRowFactory.getCustomRowFactory(tableContext));

                    //This part of the code regulates the table's drop-down menu
//                    if(!newValue.getChangeType().equals(ChangeType.TILE_CHANGE.toString())) {
//                        if (newValue.getChangeStart() == null && newValue.getChangeFinish() == null) {
//                            System.out.println(1);
//                            setMenuItemsState(false,true);
//                        } else if (newValue.getChangeStart() != null && newValue.getChangeFinish() == null) {
//                            System.out.println(2);
//                            setMenuItemsState(true,false);
//                        } else {
//                            System.out.println(3);
//                            setMenuItemsState(true,true);
//                        }
//                    }
//                    else if(newValue.getChangeFinish() != null) {
//                        setMenuItemsState(false, false);
//                    }

                    //Shows a small icon if the mold change is completed
                    if (newValue.isComplete()) {
                        finishIndicator.setImage(new Image("Images/checkmark_30px.png"));
                    } else {
                        finishIndicator.setImage(null);
                    }
                }
            }
        });
    }

    //Set MenuItems state for 'changeStartItem' and 'changeFinishItem'
    private void setMenuItemsState(boolean changeStart, boolean changeFinish){
        changeStartItem.setDisable(changeStart);
        changeFinishItem.setDisable(changeFinish);
    }

    //Sets the appropriate predicate if the button is pressed
    @FXML
    public void filterButtonClick(){
        if(filterButton.isSelected())
            filteredList.setPredicate(unfinished);
        else
            filteredList.setPredicate(allChanges);
    }

    //Method which creates a new change object
    @FXML
    public void newChange(){
        FXMLLoader loader= new FXMLLoader();
        Dialog<ButtonType> dialog= this.getDialog(loader, "change-window.fxml", "New Change");
        dialog.setHeaderText("Create a new mold change:");

        Optional<ButtonType> result= dialog.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)){
            ChangeController controller = loader.getController();
            String pressId= pressNoAlert(controller.getPressNo());
            if(pressId != null)
            {
                Change change= ChangeFactory.createChange(Integer.parseInt(pressId), controller.getMoldType(), controller.getSide(), controller.getChangeType(),
                        controller.coldPlate());
                ChangeFactory.addChange(change);

                changeTable.getSelectionModel().select(change);
                changeTable.setRowFactory(CustomRowFactory.getCustomRowFactory(tableContext));
            }
        }
    }

    // This method proclaims the change is completed
    @FXML
    public void setChangeComplete(){
        Change change= changeTable.getSelectionModel().getSelectedItem();
        if (change != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Completeness Confirmation");
            alert.setHeaderText("Are you sure the change is completed ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                change.setComplete(true);
                finishIndicator.setImage(new Image("Images/checkmark_30px.png"));
                changeTable.getSelectionModel().clearSelection();
                changeTable.setRowFactory(CustomRowFactory.getCustomRowFactory(tableContext));
            }
        }
    }

    //Clears all reminders data
    @FXML
    public void newRemember(){
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Remember");
        alert.setHeaderText("Do you want to create a new remember?");
        alert.setContentText("All previous data will delete if you press OK button.");
        Optional<ButtonType> result= alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)){
            ChangeFactory.getChangeList().clear();
            otherInformation.getItems().clear();
            changeInformation.setText("");
        }
    }

    //A method for closing the program
    @FXML
    private void exitProgram(){
        Platform.exit();
    }

    // Private method which creates a new dialog object
    private Dialog<ButtonType> getDialog(FXMLLoader loader, String location, String windowName){
        Dialog<ButtonType> dialog= new Dialog<>();
        dialog.getDialogPane().getScene().getWindow();
        dialog.setTitle(windowName);
        loader.setLocation(getClass().getResource(location));
        try{
            dialog.getDialogPane().setContent(loader.load());
        }
        catch(IOException ex){
            ex.getStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
        return dialog;
    }

    //Method which creates a alert dialog if the press id is not inserted
    private String pressNoAlert(String pressNo){
        if(pressNo == null || pressNo.isEmpty()){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Press number isn't entered !!!");
            alert.setContentText("Try again.");
            alert.showAndWait();
            return null;
        }
        return pressNo;
    }

    private void accessDenied(String description){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Access Denied!");
        alert.setHeaderText(description);
        alert.showAndWait();
    }

    // Listener for editing press information
    public class UpdateInfoListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent)
        {
            FXMLLoader loader= new FXMLLoader();
            Dialog<ButtonType> dialog= getDialog(loader,"set-info-window.fxml","Update Press Info");
            dialog.initOwner(mainWindow.getScene().getWindow());

            Change change= changeTable.getSelectionModel().getSelectedItem();

            //Selected item value
            String selectedItem= otherInformation.getSelectionModel().getSelectedItem();
            InfoController info= loader.getController();
            //insert value of the selected item in TextArea
            info.setInfo(selectedItem);

            Optional<ButtonType> result= dialog.showAndWait();
            if(result.isPresent() && result.get().equals(ButtonType.OK)){
                //Selected item index in list
                int indexInList= otherInformation.getSelectionModel().getSelectedIndex();
                //Remove item in list which is on selected index
                otherInformation.getItems().remove(indexInList);
                //Set new item in list on selected index
                otherInformation.getItems().add(indexInList, info.getInfo());
                change.getAdditionalInformationList().remove(indexInList);
                change.getAdditionalInformationList().add(indexInList, info.getInfo());
                otherInformation.getSelectionModel().select(indexInList);

            }
        }
    }
    //Listener for deleting press information
    public class DeleteInfoListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            //Get selected press
            Change change= changeTable.getSelectionModel().getSelectedItem();
            //Get selected index of information in list
            int indexInList= otherInformation.getSelectionModel().getSelectedIndex();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete info");
            alert.setHeaderText("Are you sure to want to delete this information ?");
            alert.setContentText(change.getAdditionalInformationList().get(indexInList));

            Optional<ButtonType> result= alert.showAndWait();
            if(result.isPresent() && result.get().equals(ButtonType.OK)){
                change.getAdditionalInformationList().remove(indexInList);
                otherInformation.setItems(FXCollections.observableArrayList(change.getAdditionalInformationList()));
            }
        }
    }

    //Listener witch activate dialog for input the time when is the change started
    public class StartChangeListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            FXMLLoader loader = new FXMLLoader();
            Change change = changeTable.getSelectionModel().getSelectedItem();
            if (change.getChangeType().equals(ChangeType.TILE_CHANGE.toString()))
            {
                Alert alert= new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("If the tile is changed" +
                        ", only enter the time when the change is completed.");
                alert.showAndWait();
            }
            else
            {
                if(change.getChangeStart()!= null){
                    accessDenied("The time when the mold change is started is already set");
                }
                else {
                    Dialog<ButtonType> dialog = getDialog(loader, "set-start-window.fxml", "Change Start");
                    dialog.setHeaderText("Please sets the time when the mold change is started:");
                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        ChangeStartController start = loader.getController();
                        if (start.getHour() > 23 || start.getMinute() > 59) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information");
                            alert.setHeaderText("Wrong time input, try again.");
                            alert.showAndWait();
                        } else {
                            change.setChangeStart(start.getHour(), start.getMinute());
                            changeInformation.setText(ChangeFactory.changeInformation(change));
                            changeTable.getSelectionModel().clearSelection();
                        }
                    }
                }
            }
        }
    }

    //Listener which activate dialog for input the time when is the change completed
    private class FinishChangeListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            FXMLLoader loader= new FXMLLoader();
            Change change= changeTable.getSelectionModel().getSelectedItem();

            Dialog<ButtonType> dialog= getDialog(loader, "set-finish-window.fxml", "Change finished");
            dialog.setHeaderText("Please sets the time when the mold change is completed:");

            if(change.getChangeStart() == null && !change.getChangeType().equals(ChangeType.TILE_CHANGE.toString()))
                accessDenied("Please set the time when the mold change is started.");
            else if(change.getChangeFinish()!= null){
                accessDenied("The time when the mold change finished is already set.");
            }
            else {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    ChangeFinishedController finish = loader.getController();
                    if (finish.getHour() > 23 || finish.getMinute() > 59) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Wrong time input, try again.");
                        alert.showAndWait();
                    } else {
                        change.setChangeFinish(finish.getHour(), finish.getMinute());
                        changeInformation.setText(ChangeFactory.changeInformation(change));
                        changeTable.getSelectionModel().clearSelection();
                    }

                }
            }
        }
    }

    //Listener for adding additional information for selected press
    private class OtherInformationListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            FXMLLoader loader= new FXMLLoader();
            Dialog<ButtonType> dialog= getDialog(loader,"set-info-window.fxml","Additional information");
            dialog.setHeaderText("Additional press information:");

            Optional<ButtonType> result= dialog.showAndWait();
            if(result.isPresent() && result.get().equals(ButtonType.OK)){
                InfoController info= loader.getController();
                Change change= changeTable.getSelectionModel().getSelectedItem();
                change.addAdditionalInformation(info.getInfo());
                otherInformation.setItems(ChangeFactory.getAdditionalChangeInformation(change));
                otherInformation.getSelectionModel().select(info.getInfo());
            }
        }
    }

    //Listener for deleting selected press
    private class DeletePressListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            Change change= changeTable.getSelectionModel().getSelectedItem();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Press");
            alert.setHeaderText("Are u sure to want to delete press ?");
            alert.setContentText("Press No: "+ change.getPressId());

            Optional<ButtonType> result= alert.showAndWait();
            if(result.isPresent() && result.get().equals(ButtonType.OK)){
                change.getAdditionalInformationList().clear();
                changeInformation.setText("");
                ChangeFactory.removeChange(change);
                if(ChangeFactory.getChangeList().isEmpty())
                    finishIndicator.setImage(null);
            }
        }
    }

}
