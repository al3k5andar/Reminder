package reminder.customrendering;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CustomListCellFactory implements Callback<ListView<String>, ListCell<String>>
{
    private ContextMenu contextMenu;

    private CustomListCellFactory(ContextMenu contextMenu){
        this.contextMenu= contextMenu;
    }

    public static CustomListCellFactory getCustomCellFactory(ContextMenu contextMenu){
        return new CustomListCellFactory(contextMenu);
    }

    @Override
    public ListCell<String> call(ListView<String> stringListView) {
        ListCell<String> cell= new ListCell<String>(){
            @Override
            protected void updateItem(String line, boolean empty) {
                super.updateItem(line, empty);
                if(empty){
                    setText(null);
                }
                else
                {
                    setTextFill(Color.DARKRED);
                    setText(line);
                }
            }
        };
        cell.emptyProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if(newValue)
                    cell.setContextMenu(null);
                else
                    cell.setContextMenu(contextMenu);
            }
        });
        return cell;
    }
}
