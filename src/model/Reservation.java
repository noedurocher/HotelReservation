package model;

import java.util.Date;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer c, IRoom r, Date checkIn, Date checkOut){
        this.customer=c;
        this.room=r;
        this.checkInDate=checkIn;
        this.checkOutDate=checkOut;
    }

    public IRoom getRoom() {
        return this.room;
    }

    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

    @Override
    public String toString(){
        return "Customer: " + this.customer.toString() +
                "\nRoom : " + this.room.toString() +
                "\nCheck-In Date: " + this.checkInDate +
                "\nCheck-Out Date: " + this.checkOutDate;
    }

}
