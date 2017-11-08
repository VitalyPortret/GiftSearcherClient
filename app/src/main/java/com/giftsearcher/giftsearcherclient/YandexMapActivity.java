package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.giftsearcher.giftsearcherclient.entity.Address;
import com.giftsearcher.giftsearcherclient.entity.Shop;
import com.giftsearcher.giftsearcherclient.util.GlobalUrls;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.io.IOException;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class YandexMapActivity extends AppCompatActivity {

    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private TextView tvShopName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yandex_map);

        Intent intent = getIntent();
        final long idShop = intent.getLongExtra("idShop", 0);
        final String url_shop = GlobalUrls.URL_SHOP + idShop;
        new JsonYandexMapTask().execute(url_shop);

        tvShopName = (TextView) findViewById(R.id.tvShopName);

        final MapView mapView = (MapView) findViewById(R.id.map);
        mapView.showFindMeButton(true);
        mMapController = mapView.getMapController();
        mMapController.showFindMeButton(true);
        mMapController.setZoomCurrent(15);
        mOverlayManager = mMapController.getOverlayManager();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);
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

    public void showObject(Shop shop) {
        //Загрузка ресурсов
        Resources res = getResources();
        // Создание слоя для добавления обектов на карту
        Overlay overlay = new Overlay(mMapController);
        tvShopName.setText(shop.getShopName());
        Address firstAddress = shop.getAddressList().get(0);
        double latitude = firstAddress.getGeoData().getLatitude();
        double longitude = firstAddress.getGeoData().getLongitude();

        mMapController.setPositionAnimationTo(new GeoPoint(latitude, longitude));

        for (Address address : shop.getAddressList()) {
            // Создание объекта для этого слоя
            OverlayItem overlayItem = new OverlayItem(
                    new GeoPoint(address.getGeoData().getLatitude(), address.getGeoData().getLongitude()),
                    res.getDrawable(R.drawable.ic_map_flag)
            );
            // Создание bolloon для объекта
            BalloonItem balloonItem = new BalloonItem(this,overlayItem.getGeoPoint());
            balloonItem.setText(address.getAddress());
            // Добавление bolloon в объект
            overlayItem.setBalloonItem(balloonItem);
            // Добавление объекта на слой
            overlay.addOverlayItem(overlayItem);
        }
        mOverlayManager.addOverlay(overlay);
    }

    private class JsonYandexMapTask extends AsyncTask<String, Void, Shop> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Shop doInBackground(String... params) {
            try {
                return JSONUtil.getShopFromJSON(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Shop shop) {
            super.onPostExecute(shop);
            if (shop != null){
                showObject(shop);
            }
        }
    }
}
