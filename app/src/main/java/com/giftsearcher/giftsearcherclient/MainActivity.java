package com.giftsearcher.giftsearcherclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<Gift> adapter;
    private ListView giftsListView;
    private Spinner spinnerGiftsSort;

    private final String URL_SERVER = "http://192.168.0.103:8080";
    private final String URL_BEST_RATING_GIFTS = URL_SERVER + "/api/gifts";
    private final String URL_POPULAR_GIFTS = URL_SERVER + "/api/gifts/popular";
    private final String URL_NEW_GIFTS = URL_SERVER + "/api/gifts/new";
    private final String URL_CHEAP_GIFTS = URL_SERVER + "/api/gifts/cheap";
    private final String URL_EXPENSIVE_GIFTS = URL_SERVER + "/api/gifts/expensive";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        giftsListView = (ListView) findViewById(R.id.giftsListView);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        giftsListView.setAdapter(adapter);
        giftsListView.setOnItemClickListener(this);

        spinnerGiftsSort = (Spinner) findViewById(R.id.spinnerGiftsSort);
        setSpinnerAdapter(spinnerGiftsSort);
    }

    private void setSpinnerAdapter(Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item);
        final String[] spinnerItems = { "Наилучший рейтинг", "Хиты продаж", "Новинки", "Цена по возрастанию", "Цена по убыванию" };
        spinnerAdapter.addAll(spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new JSONTask().execute(URL_BEST_RATING_GIFTS);
                        break;
                    case 1: new JSONTask().execute(URL_POPULAR_GIFTS);
                        break;
                    case 2: new JSONTask().execute(URL_NEW_GIFTS);
                        break;
                    case 3: new JSONTask().execute(URL_CHEAP_GIFTS);
                        break;
                    case 4: new JSONTask().execute(URL_EXPENSIVE_GIFTS);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Gift gift = adapter.getItem(position);
        if (gift == null) {
            return;
        }

        Intent intent = new Intent(this, GiftDetailActivity.class);
        intent.putExtra("id", gift.getId());
        startActivity(intent);
    }

    private class JSONTask extends AsyncTask<String, Void, List<Gift>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Gift> doInBackground(String... params) {
            return JSONUtil.getGiftListFromJSON(params[0]);
        }

        @Override
        protected void onPostExecute(List<Gift> result) {
            super.onPostExecute(result);
            adapter.clear();
            adapter.addAll(result);
            adapter.notifyDataSetChanged();
        }
    }

    private class GiftListAdapter extends ArrayAdapter {

        private List<Gift> giftList;
        private int resource;
        private LayoutInflater inflater;

        public GiftListAdapter(Context context, int resource, List<Gift> giftList) {
            super(context, resource, giftList);
            this.giftList = giftList;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gift_list_item, null);
            }

            ImageView ivGiftList;
            TextView tvGiftName, tvGiftPrice, tvGiftAppreciated;

            ivGiftList = (ImageView) convertView.findViewById(R.id.ivGiftList);
            tvGiftName = (TextView) convertView.findViewById(R.id.tvGiftName);
            tvGiftPrice = (TextView) convertView.findViewById(R.id.tvGiftPrice);
            tvGiftAppreciated = (TextView) convertView.findViewById(R.id.tvGiftAppreciated);

            Gift gift = giftList.get(position);
            tvGiftName.setText(gift.getNameGift());
            tvGiftPrice.setText(String.format("%s", gift.getPrice()));
            tvGiftAppreciated.setText(gift.getAppreciated());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(gift.getImage(), 0, gift.getImage().length, options);
            ivGiftList.setImageBitmap(bmp);


            return convertView;
        }
    }

}



