package com.example.bharat.joespizzeria.models;

/**
 * Created by bharat on 3/15/18.
 */

public class Reservation {

    public String available;
    public String chairs;
    public String status;
    public String price;
    public String total;

    public Reservation() {
    }

    public Reservation(String available, String chairs, String status, String price, String total) {
        this.available = available;
        this.chairs = chairs;
        this.status = status;
        this.price = price;
        this.total = total;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getChairs() {
        return chairs;
    }

    public void setChairs(String chairs) {
        this.chairs = chairs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
