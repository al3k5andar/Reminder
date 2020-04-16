package reminder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import reminder.listeners.SetTimeListener;

public class ChangeFinishedController
{
    @FXML
    private TextField hour;
    @FXML
    private TextField minutes;

    public void initialize()
    {
        hour.textProperty().addListener(new SetTimeListener(hour));
        minutes.textProperty().addListener(new SetTimeListener(minutes));
    }

    //Returns the hour when is the mold change is finished
    public int getHour(){
        return (hour.getText().isEmpty())? 0 : Integer.parseInt(hour.getText());
    }

    //Returns the minute is when the mold change is finished
    public int getMinute(){
        return (minutes.getText().isEmpty())? 0 : Integer.parseInt(minutes.getText());
    }
}
