package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.entity.Shop;
import com.giftsearcher.giftsearcherclient.util.JSONUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by adminvp on 12/8/17.
 */

public class CreateGiftActivity extends AppCompatActivity  {

    private Gift newGift;
    private final String[] genders = { "Мужчина", "Женщина", "Ребенок", "Унисекс" };
    private final String[] usGender = { "MEN", "WOMEN", "CHILD", "UNISEX" };
    private final String[] shops = { "PichShop", "Красный куб", "Другие подарки", "Разверни", "ДАРЮ!", "LeFUTUR", "PresentDream", "Пум-пу-ру", "Podarkoff" };

    private final int PICK_IMAGE = 1;
    private ImageView ivGiftImageLoad;
    private EditText etGiftName, etPrice, etRecommendedAge, etDescription;
    private Spinner spinnerGender, spinnerShop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gift);

        newGift = new Gift();

        etGiftName = (EditText) findViewById(R.id.etGiftName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etRecommendedAge = (EditText) findViewById(R.id.etRecommendedAge);
        etDescription = (EditText) findViewById(R.id.etDescription);
        ivGiftImageLoad = (ImageView) findViewById(R.id.ivGiftImageLoad);

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerShop = (Spinner) findViewById(R.id.spinnerShop);
        setSpinnerAdapter(spinnerGender, "gender", genders);
        setSpinnerAdapter(spinnerShop, "shop", shops);

        findViewById(R.id.btnAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSendGift();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {

            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                ivGiftImageLoad.setImageBitmap(yourSelectedImage);

                //Преобразование картинки из Bitmap в byte[]
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                newGift.setImage(stream.toByteArray());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setSpinnerAdapter(final Spinner spinner, final String component, final String[] array) {

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, array);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (component) {
                    case "shop":
                        Shop s = new Shop();
                        s.setShopName(shops[position]);
                        newGift.setShop(s);
                        break;
                    case "gender":
                        newGift.setGender(usGender[position]);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showToast(String toastString) {
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }

    private void validateAndSendGift(){
        newGift.setNameGift(etGiftName.getText().toString());
        newGift.setDescription(etDescription.getText().toString());
        if (!etPrice.getText().toString().equals("")) {
            newGift.setPrice(Double.valueOf(etPrice.getText().toString()));
        }
        if (!etRecommendedAge.getText().toString().equals("")) {
            newGift.setRecommendedAge(Byte.valueOf(etRecommendedAge.getText().toString()));
        }

        if (newGift.getNameGift() == null || newGift.getNameGift().equals("")) {
            showToast("Название подарка не заполнено");
            return;
        }
        if (newGift.getDescription() == null || newGift.getDescription().equals("")) {
            showToast("Описание подарка не заполнено");
            return;
        }
        if (newGift.getPrice() == 0) {
            showToast("Цена подарка не заполнена");
            return;
        }
        if (newGift.getRecommendedAge() == null) {
            showToast("Рекомендуемый возраст не заполнен");
            return;
        }

        if (newGift.getShop() == null || newGift.getShop().getShopName() == null) {
            showToast("Магазин не выбран");
            return;
        }
        if (newGift.getGender() == null) {
            showToast("Не выбрано кому предназначен подарок");
            return;
        }

        if (newGift.getImage() == null) {
            showToast("картинка подарка не выбрана");
            return;
        }
        new PostTask().execute(newGift);
    }

    private class PostTask extends AsyncTask<Gift, Void, Gift> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Gift doInBackground(Gift... gifts) {
            try {
                return JSONUtil.postGift(gifts);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Gift gift) {
            super.onPostExecute(gift);

        }
    }
}
