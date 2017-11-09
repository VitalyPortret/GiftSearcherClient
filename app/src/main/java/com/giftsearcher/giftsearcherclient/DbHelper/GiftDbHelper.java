package com.giftsearcher.giftsearcherclient.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.giftsearcher.giftsearcherclient.entity.Gift;

import java.util.ArrayList;
import java.util.List;


public class GiftDbHelper extends SQLiteOpenHelper  {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Gifts.db";
    private SQLiteDatabase db;

    private static final String TABLE_NAME = "gifts";
    private static final String COLUMN_SQLITE_ID = "id_sqlite";
    private static final String COLUMN_GIFT_ID = "id";
    private static final String COLUMN_NAME_GIFT = "name_gift";
    private static final String COLUMN_DESCRIPTION_GIFT = "description";
    private static final String COLUMN_IMAGE_GIFT = "image";
    private static final String COLUMN_PRICE_GIFT = "price";
    private static final String COLUMN_APPRECIATED_GIFT = "appreciated";

    //Запрос создания таблицы
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_SQLITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_GIFT_ID + " INTEGER, " +
                    COLUMN_NAME_GIFT + " TEXT, " +
                    COLUMN_DESCRIPTION_GIFT + " TEXT, " +
                    COLUMN_IMAGE_GIFT + " BLOB, " +
                    COLUMN_PRICE_GIFT + " REAL, " +
                    COLUMN_APPRECIATED_GIFT + " INTEGER);";

    public static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + TABLE_NAME;

    public GiftDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public GiftDbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long addGift(Gift gift) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GIFT_ID, gift.getId());
        values.put(COLUMN_NAME_GIFT, gift.getNameGift());
        values.put(COLUMN_DESCRIPTION_GIFT, gift.getDescription());
        values.put(COLUMN_IMAGE_GIFT, gift.getImage());
        values.put(COLUMN_PRICE_GIFT, gift.getPrice());
        values.put(COLUMN_APPRECIATED_GIFT, gift.getAppreciated());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public int deleteGift(Long giftId) {
        db = this.getWritableDatabase();
        String selection = COLUMN_GIFT_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(giftId) };
        int result = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
        return result;
    }

    public List<Gift> getGifts() {
        List<Gift> gifts = new ArrayList<>();
        db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_GIFT_ID,
                COLUMN_NAME_GIFT,
                COLUMN_DESCRIPTION_GIFT,
                COLUMN_IMAGE_GIFT,
                COLUMN_PRICE_GIFT,
                COLUMN_APPRECIATED_GIFT
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            do {
                gifts.add(getGiftFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return gifts;
    }

    private Gift getGiftFromCursor(Cursor cursor){
        if(cursor != null && cursor.getCount() > 0){
            Long id = cursor.getLong(cursor.getColumnIndex(COLUMN_GIFT_ID));
            String nameGift = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GIFT));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION_GIFT));
            final byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_GIFT));
            double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_GIFT));
            int appreciated = cursor.getInt(cursor.getColumnIndex(COLUMN_APPRECIATED_GIFT));
            return new Gift(id, nameGift, description, image, price, appreciated);
        }
        return null;
    }
}
