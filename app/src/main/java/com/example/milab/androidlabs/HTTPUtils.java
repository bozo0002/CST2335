package com.example.milab.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by milab on 2017-10-26.
 */

public class HTTPUtils {

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        Bitmap temp = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                temp = BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {connection.disconnect();}
        }
        return temp;
    }

    public static Bitmap getImage(String urlString) {
        Bitmap temp = null;
        try {
            URL url = new URL(urlString);
            temp =  getImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return temp;
    }
    }

