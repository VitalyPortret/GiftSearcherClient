package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

public class GiftDetailActivity extends AppCompatActivity {

    private final String URL_SERVER = "http://192.168.0.103:8080";
    private TextView tvGiftName, tvGiftPrice, tvGiftDescription;
    private ImageView imageGiftDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);

        Intent intent = getIntent();
        long idGift = intent.getLongExtra("id", 0);

        tvGiftName = (TextView) findViewById(R.id.tvGiftName);
        tvGiftPrice = (TextView) findViewById(R.id.tvGiftPrice);
        tvGiftDescription = (TextView) findViewById(R.id.tvGiftDescription);
        imageGiftDetail = (ImageView) findViewById(R.id.imageGiftDetail);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle("List Activity");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                onBackPressed();// возврат на предыдущий activity
//            }
//        });

        String url_detail_gift = URL_SERVER + "/api/gifts/gift/" + idGift;
        new JSONTask().execute(url_detail_gift);

    }

    private class JSONTask extends AsyncTask<String, Void, Gift> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Gift doInBackground(String... params) {
            return JSONUtil.getGiftFromJSON(params[0]);
        }

        @Override
        protected void onPostExecute(Gift gift) {
            super.onPostExecute(gift);

            tvGiftName.setText(gift.getNameGift());
            tvGiftPrice.setText(String.format("%s", gift.getPrice()));
            tvGiftDescription.setText(gift.getDescription());
        }
    }
}