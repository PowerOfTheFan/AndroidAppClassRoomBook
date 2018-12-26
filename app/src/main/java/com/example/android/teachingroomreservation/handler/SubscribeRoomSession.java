package com.example.android.teachingroomreservation.handler;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.teachingroomreservation.Search;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

// chua dung duoc
public class SubscribeRoomSession {


    private String httpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
//        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        Log.e(SubscribeRoomSession.class.toString(), conn.toString());


        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();
        conn.getInputStream();


        // 5. return response message
        return conn.getResponseMessage()+"";

    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return httpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            tvResult.setText(result);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + result);
        }
    }


    public void send(String url) {
//        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        // perform HTTP POST request
        new HTTPAsyncTask().execute(url);

    }

    //    public void send(View view) {
//        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//        // perform HTTP POST request
//        if(checkNetworkConnection())
//            new HTTPAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
//        else
//            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
//
//    }

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idRoom", 1);
        jsonObject.put("idSession",  1);
        jsonObject.put("date",  "24-12-2018");
        jsonObject.put("idSubscriber", 2);

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
}
