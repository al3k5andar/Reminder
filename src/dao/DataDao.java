package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class DataDao
{
    // File's location
    private static String location= "AllChanges.dat";
    //List where are all changes
    private static ObservableList<Change> masterList= FXCollections.observableArrayList();

    public static void addChange(Change change){
        masterList.add(change);
    }
    public static ObservableList<Change> getMasterList(){
        return masterList;
    }

    //Method which saving all data on a file
    public static void saveData(){
        try(ObjectOutputStream output= new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(location)))){
            for(Change change: ChangeFactory.getChangeList()){
                output.writeObject(change);
            }
        }
        catch (IOException ex){}
    }

    //Method for reading all data from a file
    public static void loadData(){
        try(ObjectInputStream input= new ObjectInputStream(new BufferedInputStream(new FileInputStream(location)))){
            boolean eof= false;
            while(!eof){
                try{
                    Change change= (Change)input.readObject();
                    addChange(change);
                }
                catch (EOFException ex){
                    eof= true;
                }
                catch (ClassNotFoundException ex){
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
