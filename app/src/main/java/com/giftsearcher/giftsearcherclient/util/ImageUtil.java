package com.giftsearcher.giftsearcherclient.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {

    public static Bitmap createBitmapFromByteArray(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }
}
