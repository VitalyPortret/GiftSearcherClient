package com.giftsearcher.giftsearcherclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.giftsearcher.giftsearcherclient.DbHelper.GiftDbHelper;
import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.util.ImageUtil;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.io.IOException;
import java.util.List;

public class AdvancedSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView giftsListView;
    private GiftListAdapter giftsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_gifts);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);

        giftsListView = (ListView) findViewById(R.id.wishGiftsListView);
        giftsAdapter = new GiftListAdapter(AdvancedSearchActivity.this, R.layout.gift_list_item, );
        giftsListView.setAdapter(giftsAdapter);
        giftsListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wish_gifts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
