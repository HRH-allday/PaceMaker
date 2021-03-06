package com.example.q.pacemaker;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by q on 2017-01-15.
 */

public class PostThread extends Thread {


    private HttpURLConnection huc;
    private URL u;
    private String ec2url = "http://ec2-52-78-200-87.ap-northeast-2.compute.amazonaws.com:3000";
    private String localurl = "http://143.248.49.213:3000";
    private JSONObject data;
    private String route;

    public PostThread(JSONObject data, String rt) {
        this.data = data;
        this.route = rt;

    }
    @Override
    public void run() {
        super.run();
        Log.i("entered","hi");
        String str = "";
        try {
            u = new URL(ec2url + route);
            huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("POST");
            huc.setDoInput(true);
            huc.setDoOutput(true);
            huc.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStream os = huc.getOutputStream();
            Log.i("sended", data.toString());
            os.write((data.toString()).getBytes("utf-8"));
            os.flush();
            os.close();
            InputStream is = huc.getInputStream();
            byte[] arr = new byte[is.available()];
            is.read(arr);
            str = new String(arr, "UTF-8");
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatCreateDialog.rid = str;
        ChatCreateDialog.end_flag = true;

    }


}