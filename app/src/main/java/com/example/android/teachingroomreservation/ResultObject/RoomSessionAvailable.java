package com.example.android.teachingroomreservation.ResultObject;

public class RoomSessionAvailable {
    String idRoom;
    String roomName;
    String shiftSession;
    String inDate;

    public RoomSessionAvailable(String idRoom, String roomName, String shiftSession, String inDate) {
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
}
