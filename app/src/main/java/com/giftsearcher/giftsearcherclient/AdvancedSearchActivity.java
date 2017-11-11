package com.giftsearcher.giftsearcherclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AdvancedSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView giftsListView;
    private GiftListAdapter giftsAdapter;
    private EditText etPriceFrom, etPriceTo, etAgeFrom, etAgeTo;
    private Spinner spinnerHoliday, spinnerHobby, spinnerGender;
    private Button btnSearchGift;
    private String holiday, hobby, gender;

    private final String[] holidays = { "День рождения", "Новый год", "23 февраля",
            "8 марта", "День всех влюбленных", "Другие" };

    private final String[] hobbies = { "Спорт", "Фильмы", "Автомобили", "Мода", "Музыка", "Путешествия",
            "Книги", "Исскуство", "Настольные игры", "Компьютерные игры", "Программирование",
            "Реклама", "Дизайн", "Бизнес", "Еда", "Рукоделие", "Коллекционирование", "Йога",
            "Иностранные языки", "Тренинги", "Охота и рыбалка", "Другие" };

    private final String[] genders = { "Мужчина", "Женщина", "Ребенок", "Унисекс" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_advansed_search);
        setToolbar(toolbar);

        giftsListView = (ListView) findViewById(R.id.searchGiftsListView);
        giftsAdapter = new GiftListAdapter(AdvancedSearchActivity.this, R.layout.gift_list_item, new ArrayList<Gift>());
        giftsListView.setAdapter(giftsAdapter);
        giftsListView.setOnItemClickListener(this);

        etPriceFrom = (EditText) findViewById(R.id.etPriceFrom);
        etPriceTo = (EditText) findViewById(R.id.etPriceTo);
        etAgeFrom = (EditText) findViewById(R.id.etAgeFrom);
        etAgeTo = (EditText) findViewById(R.id.etAgeTo);

        btnSearchGift = (Button) findViewById(R.id.btnSearchGift);
        btnSearchGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createParams();
            }
        });

        spinnerHoliday = (Spinner) findViewById(R.id.spinnerHoliday);
        spinnerHobby = (Spinner) findViewById(R.id.spinnerHobby);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        setSpinnerAdapter(spinnerHoliday,"holiday", holidays);
        setSpinnerAdapter(spinnerHobby, "hobby", hobbies);
        setSpinnerAdapter(spinnerGender, "gender", genders);
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

    private void createParams() {
        String priceFrom = etPriceFrom.getText().toString();
        String priceTo = etPriceTo.getText().toString();
        String ageFrom = etAgeFrom.getText().toString();
        String ageTo = etAgeTo.getText().toString();
        String url = GlobalUrls.URL_ADVANCED_SEARCH;

        new AdvancedSearchPostTask().execute(url, priceFrom, priceTo, ageFrom, ageTo, gender, hobby, holiday);
    }

    private void setSpinnerAdapter(final Spinner spinner, final String component, String[] array) {

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, array);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (component) {
                    case "holiday":
                        holiday = holidays[position];
                        break;
                    case "hobby":
                        hobby = hobbies[position];
                        break;
                    case "gender":
                        String[] usGender = {"MEN", "WOMEN", "CHILD", "UNISEX"};
                        gender = usGender[position];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_advanced_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_wish_gifts) {
            Intent intent = new Intent(this, WishGiftsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
    private class AdvancedSearchPostTask extends AsyncTask<String, Void, List<Gift>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Gift> doInBackground(String... params) {
            try {
                return JSONUtil.getGiftsFromAdvancedSearch(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Gift> result) {
            super.onPostExecute(result);
            if (giftsAdapter.getCount() > 0) {
                giftsAdapter.clear();
            }
            if (result != null) {
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
