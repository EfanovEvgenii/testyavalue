package com.jeezsoft.testyavalue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

    final String TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                refreshCurrency();

            }
        });
        t.start();
    }

    private void refreshCurrency() {

        URL url;

        Log.d(TAG, "start");
        try{
            String currencyFeed = getString(R.string.currency_feed);
            url = new URL(currencyFeed);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream in = httpConnection.getInputStream();
                XmlPullParserFactory factory;
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, null);
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT){
                    if (eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("rate")){
                        Log.d(TAG, "rate id =" + xpp.getAttributeValue(0));
                        eventType = xpp.next();
                        while (!(eventType == XmlPullParser.END_TAG  && xpp.getName().equalsIgnoreCase("rate")) && xpp.getName()!=null){
                            if (eventType == XmlPullParser.START_TAG){
                                Log.d(TAG, xpp.getName() + " = " + xpp.nextText());
                            }
                            eventType = xpp.next();
                        }

                    }
                    eventType = xpp.next();
                }
            }


        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException");
            //e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException");
            //e.printStackTrace();
        } catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
