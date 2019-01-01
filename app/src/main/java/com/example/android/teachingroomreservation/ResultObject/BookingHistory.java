package com.example.android.teachingroomreservation.ResultObject;

public class BookingHistory {
    String idRoom;
    String roomName;
    String shiftSession;
    String date;
    String subscriber;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public BookingHistory() {

    }

    public BookingHistory(String idRoom, String roomName, String shiftSession, String date, String subscriber) {

        this.idRoom = idRoom;
        this.roomName = roomName;
        this.shiftSession = shiftSession;
        this.date = date;
        this.subscriber = subscriber;
    }
}
