package dao;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Change implements Serializable
{
    private int pressId;
    private String moldType;
    private String pressSide;
    private String changeType;
    private boolean coldPlate;
    private List<String> additionalInformationList;
    private LocalTime changeStart, changeFinish;
    private StringProperty pressIdProperty;
    private StringProperty changeTypeProperty;
    private StringProperty changeSideProperty;
    private boolean complete;
    private long serialVersionUID= 1L;

    public Change() {
    }

    public Change(int pressId, String moldType, String pressSide, String changeType, boolean coldPlate) {
        this.pressId = pressId;
        this.moldType = moldType;
        this.pressSide = pressSide;
        this.changeType = changeType;
        this.coldPlate = coldPlate;
        additionalInformationList = new LinkedList<>();
    }

    public void addAdditionalInformation(String information){
        this.additionalInformationList.add(information);
    }

    public StringProperty getPressIdProperty(){
        if(pressIdProperty == null)
            return new SimpleStringProperty(String.valueOf(this.getPressId()));
        return pressIdProperty;
    }

    public StringProperty getChangeTypeProperty(){
        if(changeTypeProperty == null)
            return new SimpleStringProperty(this.getChangeType());
        return changeTypeProperty;
    }

    public StringProperty getChangeSideProperty(){
        if(changeSideProperty == null)
            return new SimpleStringProperty(this.getPressSide());
        return changeSideProperty;
    }

    public int getPressId() {
        return pressId;
    }

    public String getMoldType() {
        return moldType;
    }

    public String getPressSide() {
        return pressSide;
    }

    public String getChangeType() {
        return changeType;
    }


    public boolean isColdPlate() {
        return coldPlate;
    }

    public List<String> getAdditionalInformationList() {
        return additionalInformationList;
    }

    public LocalTime getChangeStart() {
        return changeStart;
    }

    public LocalTime getChangeFinish() {
        return changeFinish;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setPressId(int pressId) {
        this.pressId = pressId;
    }

    public void setMoldType(String moldType) {
        this.moldType = moldType;
    }

    public void setPressSide(String pressSide) {
        this.pressSide = pressSide;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }


    public void setColdPlate(boolean coldPlate) {
        this.coldPlate = coldPlate;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setChangeStart(int hour, int minute){
        this.changeStart= LocalTime.of(hour, minute);
    }

    public void setChangeFinish(int hour, int minute){
        this.changeFinish= LocalTime.of(hour, minute);
    }

    @Override
    public String toString() {
        return "ChangeDao{" +
                "pressId=" + pressId +
                ", moldType='" + moldType + '\'' +
                ", pressSide='" + pressSide + '\'' +
                ", changeType='" + changeType + '\'' +
                ", coldPlate=" + coldPlate +
                '}';
    }
}
