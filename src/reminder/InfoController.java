package reminder;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class InfoController
{
    @FXML
    private TextArea info;


    //Returns a text from the text area
    public String getInfo(){
        return info.getText().trim();
    }
    //Insert text in text area
    public void setInfo(String info){
        this.info.setText(info.trim());
    }
    public TextArea getTextAreaContent(){
        return info;
    }
}
