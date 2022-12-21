package model;

import java.util.Objects;

public class Room implements  IRoom{

    private String roomNumber;
    private Double price;
    private RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }
    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.enumeration;
    }

    @Override
    public boolean isFree() {
        return this.price != null && this.price.equals(0.0);
    }

    @Override
    public String toString(){
        return "Room Number: " + this.getRoomNumber() + "\nRoom Price: " +
                this.getRoomPrice() + "\nRoom Type: " + this.getRoomType();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(!(obj instanceof Room)) {
            return false;
        }

        final Room room = (Room) obj;
        return Objects.equals(this.roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }


}
