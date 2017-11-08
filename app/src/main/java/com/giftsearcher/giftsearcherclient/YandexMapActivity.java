package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
        showObject();


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

    public void showObject() {
        // Load required resources
        Resources res = getResources();
        // Create a layer of objects for the map
        Overlay overlay = new Overlay(mMapController);
        // Create an object for the layer
        final OverlayItem kremlin = new OverlayItem(new GeoPoint(56.128721, 40.402729), res.getDrawable(R.drawable.ic_map_flag));
        // Create a balloon model for the object
        BalloonItem balloonKremlin = new BalloonItem(this,kremlin.getGeoPoint());
        balloonKremlin.setText("Биби");

        // Add the balloon model to the object
        kremlin.setBalloonItem(balloonKremlin);
        // Add the object to the layer
        overlay.addOverlayItem(kremlin);

        mOverlayManager.addOverlay(overlay);
    }
}
