package reminder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import reminder.listeners.SetTimeListener;

import java.time.LocalTime;

public class ChangeStartController {
    @FXML
    private TextField hour;
    @FXML
    private TextField minutes;


    public void initialize(){
        hour.textProperty().addListener(new SetTimeListener(hour));
        minutes.textProperty().addListener(new SetTimeListener(minutes));

    }

    //Returns the hour when is the mold change started
    public int getHour(){
        return (hour.getText().isEmpty())? 0 : Integer.parseInt(hour.getText());
    }

    //Returns the minute when is the mold change started
    public int getMinute(){
        return (minutes.getText().isEmpty())? 0 : Integer.parseInt(minutes.getText());
    }

}
