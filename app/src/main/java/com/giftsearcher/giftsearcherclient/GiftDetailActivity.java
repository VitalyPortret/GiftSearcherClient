package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giftsearcher.giftsearcherclient.DbHelper.GiftDbHelper;
import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.GlobalUrls;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;
import java.io.IOException;

public class GiftDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvGiftName,tvGiftAppreciated, tvGiftPrice, tvGiftDescription, tvShopName, tvShopAddress;
    private ImageView imageGiftDetail;
    private Gift gift;
    private GiftDbHelper giftDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);

        Intent intent = getIntent();
        long idGift = intent.getLongExtra("id", 0);

        tvGiftName = (TextView) findViewById(R.id.tvGiftName);
        tvGiftPrice = (TextView) findViewById(R.id.tvGiftPrice);
        tvGiftDescription = (TextView) findViewById(R.id.tvGiftDescription);
        tvGiftAppreciated = (TextView) findViewById(R.id.tvGiftAppreciated);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        imageGiftDetail = (ImageView) findViewById(R.id.imageGiftDetail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setToolbar(toolbar);

        final String url_detail_gift = GlobalUrls.URL_DETAIL_GIFT + idGift;
        new JSONTask().execute(url_detail_gift);

        Button buttonOnMap = (Button) findViewById(R.id.buttonOnMap);
        buttonOnMap.setOnClickListener(this);

        giftDbHelper = new GiftDbHelper(this);
    }

    private void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Добавляет выбраное меню в Toolbar
        getMenuInflater().inflate(R.menu.menu_gift_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        switch (id) {
            case R.id.action_advanced_search:
                intent = new Intent(this, AdvancedSearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_wish_gifts:
                intent = new Intent(this, WishGiftsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_favorite:
                if (gift != null) {
                    giftDbHelper.addGift(gift);
                }
                Toast.makeText(this,"Добавлено в понравившиеся", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setGiftDetailField(Gift gift) {
        tvGiftName.setText(gift.getNameGift());
        tvGiftPrice.setText(String.format("%s", gift.getPrice() + " ₽"));
        tvGiftDescription.setText(gift.getDescription());
        tvShopName.setText(gift.getShop().getShopName());
        if (!gift.getShop().getAddressList().isEmpty()) {
            tvShopAddress.setText(gift.getShop().getAddressList().get(0).getAddress());
        }
        tvGiftAppreciated.setText(String.format("%d", gift.getAppreciated()));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(gift.getImage(), 0, gift.getImage().length, options);
        imageGiftDetail.setImageBitmap(bmp);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonOnMap) {
            Intent intent = new Intent(this, YandexMapActivity.class);
            if (gift != null) {
                intent.putExtra("idShop", gift.getShop().getId());
            }
            startActivity(intent);
        }
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
        protected void onPostExecute(Gift outputGift) {
            super.onPostExecute(outputGift);

            if (outputGift == null) {
                return;
            }
            gift = outputGift;
            setGiftDetailField(outputGift);
        }
    }
}
