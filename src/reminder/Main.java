package reminder;

import dao.DataDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main-window.fxml"));
        primaryStage.setTitle("Reminder");
        primaryStage.setScene(new Scene(root, 900, 475));
        primaryStage.show();
    }

    //When the program is closing than this method will save all data on a file
    @Override
    public void stop() throws Exception {
        DataDao.saveData();
    }

    //When the program is starts, that method load all data from a file
    @Override
    public void init() throws Exception {
        DataDao.loadData();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
