package reminder;

import enums.ChangeSide;
import enums.ChangeType;
import enums.MoldType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import reminder.listeners.SetTimeListener;

public class ChangeController {
    @FXML
    private TextField pressNo;
    @FXML
    private ToggleGroup radio;
    @FXML
    private CheckBox coldPlate;
    @FXML
    private ChoiceBox<String> moldType, changeType;

    public void initialize() {

        //listener which allows to insert only numbers
        pressNo.textProperty().addListener(new SetTimeListener(pressNo));

        changeType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue!= null && newValue.equals("Mold Fix")){
                    coldPlate.setDisable(true);
                }
                else
                    coldPlate.setDisable(false);
            }
        });
        radio.getSelectedToggle();
    }

    //Returns selected mold type
    public String getMoldType() {
        int selected = moldType.getSelectionModel().getSelectedIndex();
        if (selected == -1)
            selected = 2;
        switch (selected) {
            default:
                return null;
            case 0:
                return MoldType.TWO_PART.toString();
            case 1:
                return MoldType.KL5.toString();
            case 2:
                return MoldType.CONTAINER8.toString();
            case 3:
                return MoldType.CONTAINER10.toString();
        }
    }

    //Return selected mold change's type
    public String getChangeType(){
        int selectedIndex= changeType.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1){
            selectedIndex= 0;
        }
        switch(selectedIndex){
            default: return null;
            case 0: return ChangeType.NEW_DIMENSION.toString();
            case 1: return ChangeType.MOLD_CHANGE.toString();
            case 2: return ChangeType.FLANK_CHANGE.toString();
            case 3: return ChangeType.TILE_CHANGE.toString();
            case 4: return ChangeType.MOLD_FIX.toString();
            case 5: return ChangeType.SANDBLASTING.toString();
        }
    }

    public boolean coldPlate() {
        return coldPlate.selectedProperty().getValue();
    }


    //Returns a side where is the mold change
    public String getSide() {
        RadioButton toggleValue = (RadioButton) radio.getSelectedToggle();
        switch (toggleValue.getId()) {
            default:
                return null;
            case "left":
                return ChangeSide.LEFT.toString();
            case "right":
                return ChangeSide.RIGHT.toString();
            case "both":
                return ChangeSide.BOTH.toString();
        }
    }

    //Returns the press id
    public String getPressNo() {
        return pressNo.textProperty().getValue();
    }

}