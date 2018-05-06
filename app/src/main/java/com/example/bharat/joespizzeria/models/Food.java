package com.example.bharat.joespizzeria.models;


/**
 * Created by Someone on 02-01-2018.
 */

public class Food {
    private String Name, Image, Description, Price, Discount, MenuId, Crust,Size;
    private String Pstatus;

    public Food() {
    }

    public Food(String name, String image, String description, String price, String discount, String menuId, String crust, String size,String pstatus) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
        Crust = crust;
        Size = size;
        this.Pstatus = "Available";
    }

    public String getPstatus() {
        return Pstatus;
    }

    public void setPstatus(String pstatus) {
        Pstatus = pstatus;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }


}
