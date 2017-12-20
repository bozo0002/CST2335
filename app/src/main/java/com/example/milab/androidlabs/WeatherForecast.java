package com.example.milab.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {
    protected static final String ACTIVITY_NAME = "WeatherForecast";
    ProgressBar progressBar;
    TextView currentTemp;
    TextView textMinTemperature;
    TextView textMaxTemperature;
    ImageView imageView;
    //    String minT, maxT, currentT;
    private static final String ns = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        currentTemp = (TextView) findViewById(R.id.curentT);
        textMinTemperature = (TextView) findViewById(R.id.minT);
        textMaxTemperature = (TextView) findViewById(R.id.maxT);
        imageView = (ImageView) findViewById(R.id.imageView3);
        progressBar.setVisibility(View.VISIBLE);
        ForecastQuery app = new ForecastQuery();
        app.execute();
    }

    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    //Private class
    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        String minT, maxT, currentT, iconName;
        Bitmap bm;

        @Override protected void onPreExecute() { }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int x= values[0];
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(x);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentTemp.setText(getString(R.string.ct)+" "+ currentT+"C"); //Current temp message
            textMinTemperature.setText(getString(R.string.mint)+ " "+ minT+"C");
            textMaxTemperature.setText(getString(R.string.mxt)+ " "+ maxT+"C");
            imageView.setImageBitmap(bm);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }

        @Override
        protected String doInBackground(String... args) {

            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                InputStream temp = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(temp, null);
                parser.nextTag();

                parser.require(XmlPullParser.START_TAG, ns, "current");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    // Starts by looking for the entry tag
                    if (name.equals("temperature")) {
                        currentT = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        minT = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        maxT = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    } if (name.equals("weather")){
                        iconName  = parser.getAttributeValue(null, "icon");
                        publishProgress(100);
                    }else {
                        skip(parser);
                    }
                }
//Picture bitmap loader
                Bitmap image;
                // Uncomment for default icon
                //               iconName = "11d";
                String imageFile = iconName + ".png";
                Log.i(ACTIVITY_NAME, "Proposed weather picture file - "+imageFile);
                if (!fileExistance(imageFile)){
                    Log.i(ACTIVITY_NAME, "is NOT in local directory, doing save - ");
                    String imageURL = "http://openweathermap.org/img/w/" + imageFile;
                    image = HTTPUtils.getImage(imageURL);
                    FileOutputStream outputStream = openFileOutput(imageFile, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    bm = image;
                } else {
                    Log.i(ACTIVITY_NAME, "HAS BEEN found in local directory, doing load - ");
                    FileInputStream fis = null;
                    fis = openFileInput(imageFile);
                    bm = BitmapFactory.decodeStream(fis);
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally { conn.disconnect(); }

            return "good";
        }
        //Helper skip method from https://developer.android.com/training/basics/network-ops/xml.html#parse
        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

    }
}

