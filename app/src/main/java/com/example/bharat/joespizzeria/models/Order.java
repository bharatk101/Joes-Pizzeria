package com.example.bharat.joespizzeria.models;

/**
 * Created by bharat on 1/30/18.
 */

public class Order {

    private int ID;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;
    private String Size;
    private String Crust;
    private String Image;
    private String Pstatus;


    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price, String discount, String size, String crust, String image,String pstatus ) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Size = size;
        Crust = crust;
        Image = image;
        this.Pstatus = "Available";
    }



    public Order(int ID, String productId, String productName, String quantity, String price, String discount, String size, String crust, String image,String pstatus) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Size = size;
        Crust = crust;
        Image = image;
        this.Pstatus = "Available";
    }

    public String getPstatus() {
        return Pstatus;
    }

    public void setPstatus(String pstatus) {
        Pstatus = pstatus;
    }

    public int getID() {
        return ID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCrust() {
        return Crust;
    }

    public void setCrust(String crust) {
        Crust = crust;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }


}
