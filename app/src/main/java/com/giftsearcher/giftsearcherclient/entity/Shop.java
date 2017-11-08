package com.giftsearcher.giftsearcherclient.entity;

import java.util.List;

public class Shop {

    private String shopName;

    private List<Address> addressList;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
