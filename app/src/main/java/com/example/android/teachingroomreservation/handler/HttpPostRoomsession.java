package com.example.android.teachingroomreservation.handler;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpPostRoomsession {
    private class HttpPostAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0]; // URL to call
//            String data = params[1]; //data to post

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
//                urlConnection.getResponseCode();
                urlConnection.getResponseMessage();
//                out = new BufferedOutputStream(urlConnection.getOutputStream());
//                Log.e("ReponseCode: ",String.valueOf(urlConnection.getResponseCode()));
//                urlConnection.connect();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void send(String url) {
        Log.e(HttpPostRoomsession.class.getSimpleName(), url);
        new HttpPostAsyncTask().execute(url);
    }
}
