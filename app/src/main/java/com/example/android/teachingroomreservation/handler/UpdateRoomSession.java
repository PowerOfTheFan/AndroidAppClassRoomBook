package com.example.android.teachingroomreservation.handler;

import android.os.AsyncTask;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateRoomSession extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... urlStr) {
        try{
            URL url = new URL(urlStr[0]);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("Resource content");
            out.close();
            httpCon.getInputStream();
        }catch (Exception e){
            System.out.println(UpdateRoomSession.class.getClass().toString()+"@@@@@@@@@"+e);
        }
        return null;
    }
}
