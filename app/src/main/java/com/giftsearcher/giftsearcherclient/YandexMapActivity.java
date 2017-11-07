package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.giftsearcher.giftsearcherclient.util.GlobalUrls;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by adminvp on 11/7/17.
 */

public class YandexMapActivity extends AppCompatActivity {

    MapController mMapController;
    OverlayManager mOverlayManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yandex_map);
//        Intent intent = getIntent();
//        long idShop = intent.getLongExtra("idShop", 0);

        final MapView mapView = (MapView) findViewById(R.id.map);

        mMapController = mapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        // Disable determining the user's location
        mOverlayManager.getMyLocation().setEnabled(false);

        // A simple implementation of map objects
        showObject();
    }

    public void showObject(){
        // Load required resources
        Resources res = getResources();
        // Create a layer of objects for the map
        Overlay overlay = new Overlay(mMapController);
        // Create an object for the layer
        final OverlayItem kremlin = new OverlayItem(new GeoPoint(55.752004, 37.617017), res.getDrawable(R.drawable.ic_heart2));
        // Create a balloon model for the object
        BalloonItem balloonKremlin = new BalloonItem(this,kremlin.getGeoPoint());
        balloonKremlin.setText("Биби");
        // Add the balloon model to the object
        kremlin.setBalloonItem(balloonKremlin);
        // Add the object to the layer
        overlay.addOverlayItem(kremlin);

        // Create an object for the layer
        final OverlayItem yandex = new OverlayItem(new GeoPoint(55.734182, 37.588142), res.getDrawable(R.drawable.ic_heart1));
        // Create the balloon model for the object
        BalloonItem balloonYandex = new BalloonItem(this,yandex.getGeoPoint());
        balloonYandex.setText("KJK");
        // Add the balloon model to the object
        yandex.setBalloonItem(balloonYandex);
        // Add the object to the layer
        overlay.addOverlayItem(yandex);
        // Add the layer to the map
        mOverlayManager.addOverlay(overlay);
    }
}
