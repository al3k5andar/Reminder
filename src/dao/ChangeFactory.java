package dao;

import enums.ChangeType;
import enums.MoldType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Duration;
import java.time.LocalTime;

public class ChangeFactory
{
    private static ObservableList<Change> changeList= DataDao.getMasterList();

    //That method returns new mold change object
    public static Change createChange(int pressId, String moldType, String pressSide, String changeType, boolean coldPlate){
        return new Change(pressId,moldType,pressSide,changeType,coldPlate);
    }

    //Add's mold change in the list
    public static void addChange(Change change){
        changeList.add(change);
    }

    //Removes mold change from the list
    public static void removeChange(Change change){
        changeList.remove(change);
    }

    public static ObservableList<String> getAdditionalChangeInformation(Change change){
        return FXCollections.observableArrayList(change.getAdditionalInformationList());
    }


    //A method that governs the mold heating duration
    public static Duration moldHeating(Change change){

        String currentType= MoldType.valueOf(change.getMoldType()).toString();
        Duration heating= Duration.ofMinutes(20);

        if(change.getChangeType().equals(ChangeType.MOLD_FIX.toString())){
            // Get difference between start and finish
            long difference= changeDuration(change.getChangeStart(), change.getChangeFinish()).toMinutes();
            if((difference < 60) && (difference > 0)){
                return changeDuration(change.getChangeStart(), change.getChangeFinish());
            }
        }


        if(!(change.getChangeType().equals(ChangeType.TILE_CHANGE.toString()))) {
            switch (currentType) {
                case "TWO_PART":
                    if (!change.isColdPlate()) {
                        heating = Duration.ofMinutes(60);
                    } else {
                        heating = Duration.ofMinutes(90);
                    }
                    break;
                case "KL5":
                case "CONTAINER8":
                    heating = Duration.ofMinutes(120);
                    break;
                case "CONTAINER10":
                    heating = Duration.ofMinutes(180);
                    break;
            }
        }
        //If change's type is new dimension than mold heating is 45 minutes longer
        return (change.getChangeType().equals(ChangeType.NEW_DIMENSION.toString()))? heating.plusMinutes(45) : heating;
    }

    //Additional changes status information
    public static String changeInformation(Change change){
        String output= "Press: "+ change.getPressId()+"\n";
        output += "Mold Type: "+ change.getMoldType()+"\n";
        if(change.getChangeStart() != null)
            output += "Mold work is started at: "+ change.getChangeStart()+"\n";
        if(change.getChangeFinish() != null) {
            output += "Mold work is finished at: " + change.getChangeFinish() + "\n";
            output += "Required time for mold heating are: "+ moldHeating(change).toMinutes()+ " minutes." +"\n";

            //If the change type is ZPL then the change start time isn't required and it's dont shows
            if(change.getChangeType().equals(ChangeType.TILE_CHANGE.toString()))
                output+= "The press should starts production at: "+ change.getChangeFinish().plus(moldHeating(change));
        }
        if(change.getChangeStart() != null && change.getChangeFinish() != null)
            output+= "The press should starts production at: "+ productionStartAt(change);
        return output;
    }

    //Returns the time when the production starts
    public static LocalTime productionStartAt(Change change){
        return change.getChangeFinish().plus(moldHeating(change));
    }

    //Returns the time difference between the mold change is started and when it's completed
    private static Duration changeDuration(LocalTime start, LocalTime finish){
        return Duration.between(start, finish);
    }

    //Returns mold change's list
    public static ObservableList<Change> getChangeList() {
        return changeList;
    }

}
