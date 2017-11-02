package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.GlobalConstants;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.io.IOException;

public class GiftDetailActivity extends AppCompatActivity {

    private final String URL_SERVER = GlobalConstants.URL_SERVER;;
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

    private void setGiftDetailField(Gift gift) {
        tvGiftName.setText(gift.getNameGift());
        tvGiftPrice.setText(String.format("%s", gift.getPrice()));
        tvGiftDescription.setText(gift.getDescription());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(gift.getImage(), 0, gift.getImage().length, options);
        imageGiftDetail.setImageBitmap(bmp);
    }

    private class JSONTask extends AsyncTask<String, Void, Gift> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Gift doInBackground(String... params) {
            try {
                return JSONUtil.getGiftFromJSON(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Gift gift) {
            super.onPostExecute(gift);

            setGiftDetailField(gift);
        }
    }
}
