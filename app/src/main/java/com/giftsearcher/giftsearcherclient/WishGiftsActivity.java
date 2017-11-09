package com.giftsearcher.giftsearcherclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

import java.util.List;

public class WishGiftsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView giftsListView;
    private GiftListAdapter giftsAdapter;
    private List<Gift> wishGiftList;
    private GiftDbHelper giftDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_gifts);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);

        giftDbHelper = new GiftDbHelper(this);

        giftsListView = (ListView) findViewById(R.id.wishGiftsListView);
        giftsAdapter = new GiftListAdapter(WishGiftsActivity.this, R.layout.gift_list_item, giftDbHelper.getGifts());
        giftsListView.setAdapter(giftsAdapter);
        giftsListView.setOnItemClickListener(this);
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
