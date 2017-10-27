package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class GiftDetailActivity extends AppCompatActivity {

    private final String URL_SERVER = "http://192.168.0.103:8080";
    private String url_detail_gift = URL_SERVER + "/api/gifts/gift/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);

        Intent intent = getIntent();

    }
}
