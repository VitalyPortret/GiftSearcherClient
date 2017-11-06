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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.GlobalUrls;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.io.IOException;
import java.io.InputStream;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class GiftDetailActivity extends AppCompatActivity {

    private TextView tvGiftName,tvGiftAppreciated, tvGiftPrice, tvGiftDescription;
    private ImageView imageGiftDetail;
    private Toolbar toolbar;

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
        imageGiftDetail = (ImageView) findViewById(R.id.imageGiftDetail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setToolbar(toolbar);

        WebView myWebView = (WebView) findViewById(R.id.map);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        try {
            InputStream is = getAssets().open("index.html");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String htmlText = new String(buffer);
            myWebView.loadDataWithBaseURL(
                    "http://com.yandex.browser.ymapapp",
                    htmlText,
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url_detail_gift = GlobalUrls.URL_DETAIL_GIFT + idGift;
        new JSONTask().execute(url_detail_gift);
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

        switch (id) {
            case R.id.action_account:
                Toast.makeText(this,"Аккаунт", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_favorite:
                Toast.makeText(this,"Мне нравится", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setGiftDetailField(Gift gift) {
        tvGiftName.setText(gift.getNameGift());
        tvGiftPrice.setText(String.format("%s", gift.getPrice() + " ₽"));
        tvGiftDescription.setText(gift.getDescription());
        tvGiftAppreciated.setText(String.format("%d", gift.getAppreciated()));

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
