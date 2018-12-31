package com.example.android.teachingroomreservation.handler;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.teachingroomreservation.Search;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

// chua dung duoc
public class HttpPostRoomSession {

    int idRoom;
    int shiftSession;
    String date;
    int creator;


    private class HTTPAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
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
        Log.e(HttpPostRoomSession.class.getSimpleName(), url);
        new HTTPAsyncTask().execute(url);
    }

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("room", idRoom);
        jsonObject.accumulate("session",  shiftSession);
        jsonObject.accumulate("date",  date);
        jsonObject.accumulate("creator", creator);

        return jsonObject;
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(Search.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    public HttpPostRoomSession(int idRoom, int shiftSession, String date, int creator) {
        this.idRoom = idRoom;
        this.shiftSession = shiftSession;
        this.date = date;
        this.creator = creator;
    }
    public HttpPostRoomSession(){}

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public int getShiftSession() {
        return shiftSession;
    }

    public void setShiftSession(int shiftSession) {
        this.shiftSession = shiftSession;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
}
