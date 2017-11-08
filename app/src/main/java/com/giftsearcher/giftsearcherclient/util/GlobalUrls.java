package com.giftsearcher.giftsearcherclient.util;

public class GlobalUrls {

    private static final String URL_SERVER = "http://10.0.2.2:8080";

    //MainActivity
    public static final String URL_BEST_RATING_GIFTS = URL_SERVER + "/api/gifts";
    public static final String URL_POPULAR_GIFTS = URL_SERVER + "/api/gifts/popular";
    public static final String URL_NEW_GIFTS = URL_SERVER + "/api/gifts/new";
    public static final String URL_CHEAP_GIFTS = URL_SERVER + "/api/gifts/cheap";
    public static final String URL_EXPENSIVE_GIFTS = URL_SERVER + "/api/gifts/expensive";

    //GiftDetailActivity
    public static final String URL_DETAIL_GIFT = URL_SERVER + "/api/gifts/gift/";

    //YandexMapActivity
    public static final String URL_SHOP = URL_SERVER + "/api/shop/";
}