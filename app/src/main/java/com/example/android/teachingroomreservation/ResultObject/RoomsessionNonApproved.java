package com.example.android.teachingroomreservation.ResultObject;

public class RoomsessionNonApproved {
    String idRoom;
    String roomName;
    String shift;
    String date;
    String idEmp;
    String nameEmp;

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

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(String idEmp) {
        this.idEmp = idEmp;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }

    public RoomsessionNonApproved() {
    }

    public RoomsessionNonApproved(String idRoom, String shift, String date, String idEmp) {
        this.idRoom = idRoom;
        this.shift = shift;
        this.date = date;
        this.idEmp = idEmp;
    }

    public RoomsessionNonApproved(String idRoom, String roomName, String shift, String date, String idEmp, String nameEmp) {
        this.idRoom = idRoom;
        this.roomName = roomName;
        this.shift = shift;
        this.date = date;
        this.idEmp = idEmp;
        this.nameEmp = nameEmp;
    }
}
