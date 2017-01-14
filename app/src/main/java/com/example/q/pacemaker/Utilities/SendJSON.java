package com.example.q.pacemaker.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
    USAGE

    import com.example.q.pacemaker.App;

    try {
        // GENERATE REQUEST
        JSONObject req = new JSONObject();
        req.put("<field_name>", "<field_value>");

        // GET RESPONSE AND CHECK VALIDITY
        JSONObject res = new sendJSON(App.server_url, req.toString(), App.JSONcontentsType).execute().get();
        if (res == null) {
            Log.e("NULL RESPONSE", "res = null");
            return -1;
        }

        // PARSE RESPONSE
        if (res.has("<field_name>")) {
            String field_value = res.getString("<field_name>");
        }
    } catch (Exception e) {
        e.printStackTrace();
        return -1;
    }
 */

public class SendJSON extends AsyncTask<Void, Void, JSONObject> {
    private String urlStr;
    private String data;
    private String contentType;

    public SendJSON(String urlStr, String data, String contentType) {
        this.urlStr = urlStr;
        this.data = data;
        this.contentType = contentType;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        HttpURLConnection conn;
        OutputStream os;
        InputStream is;

        BufferedReader reader;
        JSONObject json;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(data.getBytes());
            os.flush();
            os.close();

            int statusCode = conn.getResponseCode();
            Log.e("STATUS CODE", String.valueOf(statusCode));

            is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }

            reader.close();
            json = new JSONObject(response.toString());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}