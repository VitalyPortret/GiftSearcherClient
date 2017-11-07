package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.GlobalUrls;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;
import java.io.IOException;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.OnOverlayItemListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class GiftDetailActivity extends AppCompatActivity {

    private TextView tvGiftName,tvGiftAppreciated, tvGiftPrice, tvGiftDescription;
    private ImageView imageGiftDetail;
    private Toolbar toolbar;
    MapController mMapController;
    OverlayManager mOverlayManager;
    MapView mapView;

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

        String url_detail_gift = GlobalUrls.URL_DETAIL_GIFT + idGift;
        new JSONTask().execute(url_detail_gift);
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
        final OverlayItem kremlin = new OverlayItem(new GeoPoint(55.752004, 37.617017), res.getDrawable(R.drawable.ic_users));
        // Create a balloon model for the object
        BalloonItem balloonKremlin = new BalloonItem(this,kremlin.getGeoPoint());
        balloonKremlin.setText("Биби");
//        // Add the balloon model to the object
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


        final OverlayItem parkKultury = new OverlayItem(new GeoPoint(55.735562 , 37.594215), res.getDrawable(R.drawable.ic_heart2));
        parkKultury.setOverlayItemListener(new OnOverlayItemListener(){
            public void onClick(OverlayItem clickItem){
                Log.w("DefaultOverlayActivity", "Park Kultury");
            }
        });
        overlay.addOverlayItem(parkKultury);

        final OverlayItem parkPobedy = new OverlayItem(new GeoPoint(55.736238, 37.516079), res.getDrawable(R.drawable.ic_not_like));
        parkPobedy.setOverlayItemListener(new OnOverlayItemListener(){
            public void onClick(OverlayItem clickItem){
                Log.w("DefaultOverlayActivity", "Park Pobedy");
            }
        });
        overlay.addOverlayItem(parkPobedy);

        // Add the layer to the map
        mOverlayManager.addOverlay(overlay);

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
