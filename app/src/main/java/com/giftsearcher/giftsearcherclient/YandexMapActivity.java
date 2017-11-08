package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.giftsearcher.giftsearcherclient.entity.Address;
import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.entity.Shop;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yandex_map);
//        Intent intent = getIntent();
//        long idShop = intent.getLongExtra("idShop", 0);
        final MapView mapView = (MapView) findViewById(R.id.map);

        mMapController = mapView.getMapController();
        mMapController.showFindMeButton(true);
        mMapController.setZoomCurrent(15);
        mMapController.setPositionAnimationTo(new GeoPoint(56.128721, 40.402729));
        mOverlayManager = mMapController.getOverlayManager();
        // Disable determining the user's location
        mOverlayManager.getMyLocation().setEnabled(true);
        // A simple implementation of map objects
        new JsonYandexMapTask().execute("");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        // Load required resources
        Resources res = getResources();
        // Create a layer of objects for the map
        Overlay overlay = new Overlay(mMapController);
        // Create an object for the layer
        setTitle(shop.getShopName());
        for (Address address : shop.getAddressList()) {
            OverlayItem overlayItem = new OverlayItem(
                    new GeoPoint(address.getGeoData().getLatitude(), address.getGeoData().getLongitude()),
                    res.getDrawable(R.drawable.ic_map_flag));
            // Create a balloon model for the object
            BalloonItem balloonItem = new BalloonItem(this,overlayItem.getGeoPoint());
            balloonItem.setText(address.getAddress());
            // Add the balloon model to the object
            overlayItem.setBalloonItem(balloonItem);
            // Add the object to the layer
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

            showObject(shop);
        }
    }
}
