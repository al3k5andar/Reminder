package reminder.customrendering;

import dao.Change;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CustomRowFactory implements Callback<TableView<Change>, TableRow<Change>>
{
    private ContextMenu contextMenu;

    private CustomRowFactory(ContextMenu contextMenu){
        this.contextMenu= contextMenu;
    }
    public static CustomRowFactory getCustomRowFactory(ContextMenu contextMenu){
        return new CustomRowFactory(contextMenu);
    }

        @Override
        public TableRow<Change> call(TableView<Change> changeTableView) {
            TableRow<Change> row= new TableRow<Change>(){
                @Override
                protected void updateItem(Change change, boolean empty) {
                    super.updateItem(change, empty);
                    if(empty)
                        setText(null);
                    else
                    {
                        if(change.isComplete()){
                            for (int i = 0; i < this.getChildren().size() ; i++) {
                                ((Labeled)this.getChildren().get(i)).setTextFill(Color.GREEN);
                            }
                        }
                        else
                        {
                            for (int i = 0; i < this.getChildren().size(); i++) {
                                ((Labeled)this.getChildren().get(i)).setTextFill(Color.DARKRED);
                            }
                        }
                    }
                }
            };
            row.emptyProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                    if(newValue)
                        row.setContextMenu(null);
                    else
                        row.setContextMenu(contextMenu);
                }
            });

            return row;

        }
}
