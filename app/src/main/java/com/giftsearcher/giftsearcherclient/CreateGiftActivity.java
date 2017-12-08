package com.giftsearcher.giftsearcherclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by adminvp on 12/8/17.
 */

public class CreateGiftActivity extends AppCompatActivity {

    private final String[] holidays = { "День рождения", "Новый год", "23 февраля",
            "8 марта", "День всех влюбленных", "Другие" };

    private final String[] hobbies = { "Спорт", "Фильмы", "Автомобили", "Мода", "Музыка", "Путешествия",
            "Книги", "Исскуство", "Настольные игры", "Компьютерные игры", "Программирование",
            "Реклама", "Дизайн", "Бизнес", "Еда", "Рукоделие", "Коллекционирование", "Йога",
            "Иностранные языки", "Тренинги", "Охота и рыбалка", "Другие" };

    private final String[] genders = { "Мужчина", "Женщина", "Ребенок", "Унисекс" };
    private final String[] shops = { "PichShop", "Красный куб", "Другие подарки", "Разверни", "ДАРЮ!", "LeFUTUR", "PresentDream", "Пум-пу-ру", "Podarkoff"  };

    private final int PICK_IMAGE = 1;
    private ImageView ivGiftImageLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gift);

        ivGiftImageLoad = (ImageView) findViewById(R.id.ivGiftImageLoad);

        findViewById(R.id.btnAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Here we need to check if the activity that was triggers was the Image Gallery.
            // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
            // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
//            if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
//                // Let's read picked image data - its URI
//                Uri pickedImage = data.getData();
//                // Let's read picked image path using content resolver
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
//                cursor.moveToFirst();
//                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
//
//                // Do something with the bitmap
//
//
//                // At the end remember to close the cursor or you will end with the RuntimeException!
//                cursor.close();
//            if (data == null) {
//                //Display an error
//                return;
//            }
//            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
        }
    }
}
