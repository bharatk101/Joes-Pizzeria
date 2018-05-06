package com.example.bharat.joespizzeria.models;

import java.net.PortUnreachableException;

/**
 * Created by Bharat on 06-03-2018.
 */

public class Banner {
    private String id;
    String name;
    String image;

    public Banner(){

    }
    public Banner(String id,String name, String image){
        this.id=id;
        this.name=name;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
