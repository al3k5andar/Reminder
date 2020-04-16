package reminder.customrendering;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CustomTableCellFactory<P,R> implements Callback<TableColumn<P,R>, TableCell<P, R>>
{
    @Override
    public TableCell<P, R> call(TableColumn<P, R> prTableColumn) {
        TableCell<P,R> tableCell= new TableCell<P,R>(){
            @Override
            protected void updateItem(R r, boolean empty) {
                super.updateItem(r, empty);
                String cellContent= (String)r;
                if(empty)
                    setText(null);
                else
                {
                    StringBuffer sb= new StringBuffer();

                    if(cellContent.contains("_")){
                        String[] contentArray= cellContent.split("_");

                        for(String content: contentArray){
                            makeString(sb,content);
                        }
                        setText(sb.toString());
                        sb.delete(0,sb.length());
                    }
                    else
                    {
                        setText(makeString(sb,cellContent).toString());
                        sb.delete(0,sb.length());
                    }

                }
            }
        };
        return tableCell;
    }

    private StringBuffer makeString(StringBuffer sb, String input){
        sb.append(input.substring(0,1).toUpperCase());
        sb.append(input.substring(1).toLowerCase());
        sb.append(" ");
        return sb;
    }
}
