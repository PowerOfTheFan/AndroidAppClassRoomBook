package com.example.android.teachingroomreservation;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.handler.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String TAG = Search.class.getSimpleName();
    Button btnDatePicker, btnSearch;
    Spinner spnShift;
    private int cDate, cMonth, cYear;
    EditText editText;

    ArrayList<HashMap<String, String>> roomList;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        listArr = findViewById(R.id.listview1);
        lv = findViewById(R.id.listview1);
        btnDatePicker = findViewById(R.id.btn_date);
        btnSearch = findViewById(R.id.btn_search);
        spnShift = findViewById(R.id.spinner_shift);
        spnShift.setOnItemSelectedListener(this);
        btnSearch.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);

        roomList = new ArrayList<>();
        // Spinner Drop down elements
        List<String> shift = new ArrayList<String>();
        shift.add("1");
        shift.add("2");
        shift.add("3");
        shift.add("4");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shift);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnShift.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get current date
            final Calendar c = Calendar.getInstance();
            cDate = c.get(Calendar.DAY_OF_MONTH);
            cMonth = c.get(Calendar.MONTH);
            cYear = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            int month12 = month +1;
                            String frmDate;
                            String frmMonth;
                            // format dd-mm-yyyy
                            if(month12 < 10){
                                frmMonth = "0" + month12;
                            }else frmMonth = "" + month12;

                            if(dayOfMonth < 10){
                                frmDate = "0" + dayOfMonth;
                            }else frmDate = "" + dayOfMonth;
                            btnDatePicker.setText(frmDate + "-" + frmMonth + "-" + year);
//                            btnDatePicker.setText(dayOfMonth + "-" + month + "-" + year);
                        }
                    }, cYear, cMonth, cDate);
            datePickerDialog.show();
        }

        if (v == btnSearch) {

//            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
//
//            TableLayout table = new TableLayout(this);
//
//            for (int i = 0; i < 10; i++) {
//
//                TableRow row = new TableRow(this);
//
//                for (int j = 0; j < 10; j++) {
//
//                    TextView cell = new android.support.v7.widget.AppCompatTextView(this) {
//                        @Override
//                        protected void onDraw(Canvas canvas) {
//                            super.onDraw(canvas);
//                            Rect rect = new Rect();
//                            Paint paint = new Paint();
//                            paint.setStyle(Paint.Style.STROKE);
//                            paint.setColor(Color.BLUE);
//                            paint.setStrokeWidth(20);
//                            getLocalVisibleRect(rect);
//                            canvas.drawRect(rect, paint);
//                        }
//
//                    };
//                    cell.setText(i + ", " + j);
//                    cell.setPadding(6, 4, 6, 4);
//                    row.addView(cell);
//
//                }
//
//                table.addView(row);
//            }
//
//            layout.addView(table);
            new GetContacts().execute();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Search.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String shift = spnShift.getSelectedItem().toString();
            String date = btnDatePicker.getText().toString();
            Log.e(TAG, "Date: " + date);
            // Making a request to url and getting response
            String url = "https://roomroomroom.herokuapp.com/Roomsession/search/"+date+"/"+shift;
            Log.e(TAG, "url: " + url);

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for(int i=0; i<jsonArr.length(); i++){
                        JSONArray room = jsonArr.getJSONArray(i);
                        String id = room.getString(0);
                        String roomName = room.getString(1);
                        String shiftSession = room.getString(2);
                        String inDate = room.getString(3);

                        Log.e(TAG, "room info: " + id + roomName + shift + inDate);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", id);
                        map.put("roomName", roomName);
                        map.put("shiftSession", shiftSession);
                        map.put("inDate", inDate);

                        roomList.add(map);
                    }

                    // looping through All Contacts
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//                        String id = c.getString("id");
//                        String name = c.getString("name");
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");
//
//                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");
//
//                        // tmp hash map for single contact
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
//                        contact.put("name", name);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);
//
//                        // adding contact to contact list
//                        contactList.add(contact);
//                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Search.this, roomList,
                    R.layout.list_item, new String[]{"id", "roomname", "shift", "date"},
                    new int[]{R.id.textview_idSession, R.id.textview_roomname, R.id.textview_shift, R.id.textview_date});
            lv.setAdapter(adapter);
        }
    }
}

//if(v == btnShift){
//        String url = "https://roomroomroom.herokuapp.com/Roomsession/available";
//final ProgressDialog dialog;
//        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
////            EditText edtResult = new EditText(this);
//        //---------------------------------------------------
//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading....");
//        dialog.show();
//        //-----------------------------------------------------
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//@Override
//public void onResponse(String string) {
//        System.out.println(string);
//        parseJsonData(string);
//        }
//        }, new Response.ErrorListener() {
//@Override
//public void onErrorResponse(VolleyError volleyError) {
//        Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
//        dialog.dismiss();
//        }
//        });
//
//        RequestQueue rQueue = Volley.newRequestQueue(Search.this);
//        rQueue.add(request);
//        }

//    void parseJsonData(String jsonString) {
//        try {
////            JSONObject object = new JSONObject(jsonString);
//            JSONArray fruitsArray =  new JSONArray(jsonString);
////            JSONArray fruitsArray = object.getJSONArray("");
//            ArrayList al = new ArrayList();
//
//            for(int i = 0; i < fruitsArray.length(); ++i) {
//                al.add(fruitsArray.getString(i));
//            }
//
//            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
//            listArr.setAdapter(adapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
