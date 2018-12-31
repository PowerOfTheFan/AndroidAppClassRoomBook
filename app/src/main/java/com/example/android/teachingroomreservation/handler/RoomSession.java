package com.example.android.teachingroomreservation.handler;

public class RoomSession {
    String idRoom;
    String roomName;
    String shiftSession;
    String inDate;
    String idCreator, nameCreator;
    String idSubscriber, nameSubscriber;
    String idApprover, nameApprover;

    public RoomSession(String idRoom, String roomName, String shiftSession, String inDate, String idSubscriber, String nameSubscriber) {
        this.idRoom = idRoom;
        this.roomName = roomName;
        this.shiftSession = shiftSession;
        this.inDate = inDate;
        this.idSubscriber = idSubscriber;
        this.nameSubscriber = nameSubscriber;
    }

//    public RoomSession(String idRoom, String shiftSession, String inDate, String idCreator, String idSubscriber, String idApprover) {
//        this.idRoom = idRoom;
//        this.shiftSession = shiftSession;
//        this.inDate = inDate;
//        this.idCreator = idCreator;
//        this.idSubscriber = idSubscriber;
//        this.idApprover = idApprover;
//    }

    public RoomSession(String idRoom, String roomName, String shiftSession, String inDate) {
        this.idRoom = idRoom;
        this.roomName = roomName;
        this.shiftSession = shiftSession;
        this.inDate = inDate;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getShiftSession() {
        return shiftSession;
    }

    public void setShiftSession(String shiftSession) {
        this.shiftSession = shiftSession;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }
    public String getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(String idCreator) {
        this.idCreator = idCreator;
    }

    public String getNameCreator() {
        return nameCreator;
    }

    public void setNameCreator(String nameCreator) {
        this.nameCreator = nameCreator;
    }

    public String getIdSubscriber() {
        return idSubscriber;
    }

    public void setIdSubscriber(String idSubscriber) {
        this.idSubscriber = idSubscriber;
    }

    public String getNameSubscriber() {
        return nameSubscriber;
    }

    public void setNameSubscriber(String nameSubscriber) {
        this.nameSubscriber = nameSubscriber;
    }

    public String getIdApprover() {
        return idApprover;
    }

    public void setIdApprover(String idApprover) {
        this.idApprover = idApprover;
    }

    public String getNameApprover() {
        return nameApprover;
    }

    public void setNameApprover(String nameApprover) {
        this.nameApprover = nameApprover;
    }
}
