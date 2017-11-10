package com.giftsearcher.giftsearcherclient.entity;

public class Gift {

    private long id;

    private String nameGift;

    private String description;

    private byte[] image;

    private double price;

    private int appreciated;

    private Shop shop;

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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Gift() {}

    public Gift(long id, String nameGift, String description, byte[] image, double price, int appreciated) {
        this.id = id;
        this.nameGift = nameGift;
        this.description = description;
        this.image = image;
        this.price = price;
        this.appreciated = appreciated;
    }
}