package com.example.android.teachingroomreservation.handler;

public class RoomAvailable {
    String idRoom;
    String roomName;
    String seatAmount;

    public RoomAvailable(String idRoom, String roomName, String seatAmount) {
        this.idRoom = idRoom;
        this.roomName = roomName;
        this.seatAmount = seatAmount;
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

    public String getSeatAmount() {
        return seatAmount;
    }

    public void setSeatAmount(String seatAmount) {
        this.seatAmount = seatAmount;
    }
}
