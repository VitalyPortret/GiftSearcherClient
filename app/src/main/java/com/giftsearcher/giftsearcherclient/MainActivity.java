package com.giftsearcher.giftsearcherclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.giftsearcher.giftsearcherclient.util.GlobalUrls;
import com.giftsearcher.giftsearcherclient.util.ImageUtil;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GiftListAdapter giftsAdapter;

    private ListView giftsListView;
    private Spinner spinnerGiftsSort;

    //Предназначена для того чтобы
    //определить нужно ли, очищать список в адапторе
    private boolean cleanAdapterList;
    private String currentUrl = GlobalUrls.URL_BEST_RATING_GIFTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        giftsListView = (ListView) findViewById(R.id.giftsListView);
        giftsAdapter = new GiftListAdapter(MainActivity.this, R.layout.gift_list_item, new ArrayList<Gift>());
        giftsListView.setAdapter(giftsAdapter);
        giftsListView.setOnItemClickListener(this);

        giftsListView.setOnScrollListener(new LessScrollListener<Gift>(giftsListView, giftsAdapter, 33) {
            @Override
            public void onLoadMore(int page) {
                new JSONTask().execute(currentUrl + "?page=" + page);
                cleanAdapterList = false;
            }

            @Override
            public boolean onUpdatePage() {
                return cleanAdapterList;
            }
        });

        spinnerGiftsSort = (Spinner) findViewById(R.id.spinnerGiftsSort);
        setSpinnerAdapter(spinnerGiftsSort);
    }

    //Создание выподающего Спинера-ДропДаунЛиста(для выбора списка подарков)
    private void setSpinnerAdapter(Spinner spinner) {
        //Категории
        final String[] spinnerItems = {
                "Хиты продаж",
                "Новинки",
                "Цена по возрастанию",
                "Цена по убыванию" };

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        currentUrl = GlobalUrls.URL_POPULAR_GIFTS;
                        break;
                    case 1:
                        currentUrl = GlobalUrls.URL_NEW_GIFTS;
                        break;
                    case 2:
                        currentUrl = GlobalUrls.URL_CHEAP_GIFTS;
                        break;
                    case 3:
                        currentUrl = GlobalUrls.URL_EXPENSIVE_GIFTS;
                        break;
                }

                new JSONTask().execute(currentUrl);
                cleanAdapterList = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        switch (id) {
            case R.id.action_wish_gifts:
                intent = new Intent(this, WishGiftsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_advanced_search:
                intent = new Intent(this, AdvancedSearchActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Клик по любому из списка подарков
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Gift gift = giftsAdapter.getItem(position);
        if (gift == null) {
            return;
        }

        Intent intent = new Intent(this, GiftDetailActivity.class);
        intent.putExtra("id", gift.getId());
        startActivity(intent);
    }

    //Обработка запросов в фоновом потоке
    private class JSONTask extends AsyncTask<String, Void, List<Gift>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Gift> doInBackground(String... params) {
            try {
                return JSONUtil.getGiftListFromJSON(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Gift> result) {
            super.onPostExecute(result);
            if (result != null) {
                if (cleanAdapterList) {
                    //Скролл к первому элементу
                    giftsListView.smoothScrollToPosition(0);
                    giftsAdapter.clear();
                }
                giftsAdapter.addAll(result);
                giftsAdapter.notifyDataSetChanged();
            }
        }
    }

    //Адаптер для вывода СПИСКА ПОДАРКОВ!
    private class GiftListAdapter extends ArrayAdapter<Gift> {

        private List<Gift> gifts;
        private int resource;
        private LayoutInflater inflater;

        public List<Gift> getGifts() {
            return gifts;
        }

        public GiftListAdapter(Context context, int resource, List<Gift> gifts) {
            super(context, resource, gifts);
            this.gifts = gifts;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }

            ImageView ivGiftList;
            TextView tvGiftName, tvGiftPrice, tvGiftAppreciated;

            ivGiftList = convertView.findViewById(R.id.ivGiftList);
            tvGiftName = convertView.findViewById(R.id.tvGiftName);
            tvGiftPrice = convertView.findViewById(R.id.tvGiftPrice);
            tvGiftAppreciated = convertView.findViewById(R.id.tvGiftAppreciated);

            Gift gift = gifts.get(position);
            tvGiftName.setText(gift.getNameGift());
            tvGiftPrice.setText(String.format("%s", gift.getPrice() + " ₽"));
            tvGiftAppreciated.setText(String.format("%s", gift.getAppreciated()));

            if (gift.getImage() != null) {
                Bitmap bitmap = ImageUtil.createBitmapFromByteArray(gift.getImage());
                ivGiftList.setImageBitmap(bitmap);
            }
            return convertView;
        }
    }

}



