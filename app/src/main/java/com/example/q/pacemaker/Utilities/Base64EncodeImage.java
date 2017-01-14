package com.example.q.pacemaker.Utilities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/*
    USAGE

    Bitmap bmp = <bitmap_image>;
    String encoded = new Base64EncodeImage().execute(bmp).get();
 */

public class Base64EncodeImage extends AsyncTask <Bitmap, Void, String> {
    private static final int compressPercentage = 20;

    public String doInBackground(Bitmap... bmps) {
        Bitmap bmp = bmps[0];
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, compressPercentage, ByteStream);
        byte[] b = ByteStream.toByteArray();

        String encoded = Base64.encodeToString(b, Base64.NO_WRAP);
        encoded = "data:image/jpeg;base64," + encoded;

        return encoded;
    }
}