package com.giftsearcher.giftsearcherclient.entity;

import java.util.Date;

public class Gift {

    private long id;

    private String nameGift;

    private String imagePath;

    private String description;

    private byte[] image;

    private double price;

    private int appreciated;

    private Date dateAdding;

    private String gender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameGift() {
        return nameGift;
    }

    public void setNameGift(String nameGift) {
        this.nameGift = nameGift;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAppreciated() {
        return appreciated;
    }

    public void setAppreciated(int appreciated) {
        this.appreciated = appreciated;
    }

    public Date getDateAdding() {
        return dateAdding;
    }

    public void setDateAdding(Date dateAdding) {
        this.dateAdding = dateAdding;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "nameGift='" + nameGift + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", price=" + price +
                ", appreciated=" + appreciated +
                ", dateAdding=" + dateAdding +
                ", gender='" + gender + '\'' +
                '}';
    }
}