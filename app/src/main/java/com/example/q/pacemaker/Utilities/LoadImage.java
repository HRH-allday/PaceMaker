package com.example.q.pacemaker.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/*
    USAGE
    try {
        String url = "<image_url>";
        Bitmap bmp = new LoadImage().execute(url).get();
        <ImageViewName>.setImageBitmap(bmp);
    } catch (Exception e) {
        e.printStackTrace();
        return -1;
    }
 */

public class LoadImage extends AsyncTask <String, Void, Bitmap> {
    public Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap image = null;
        try {
            InputStream in = new URL(urlDisplay).openStream();
            image = BitmapFactory.decodeStream(in);

            // If you want a rounded image, uncomment the line below
            // image = RoundedImageView.getCroppedBitmap(image, image.getWidth());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }
}