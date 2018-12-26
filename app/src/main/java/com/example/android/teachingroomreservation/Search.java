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

import com.example.android.teachingroomreservation.handler.FormatStringDate;
import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.handler.JSONParser;
import com.example.android.teachingroomreservation.handler.RoomAvailable;
import com.example.android.teachingroomreservation.handler.SubscribeRoomSession;
import com.example.android.teachingroomreservation.handler.UpdateRoomSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String TAG = Search.class.getSimpleName();
    Button btnDatePicker, btnSearch;
    Spinner spnShift;
    TableLayout room_table;
    private int cDate, cMonth, cYear;
    EditText editText;


    ArrayList<RoomAvailable> roomList;

    ListView lv;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        listArr = findViewById(R.id.listview1);
//        lv = findViewById(R.id.listview1);
        room_table = findViewById(R.id.room_table);
        btnDatePicker = findViewById(R.id.btn_date);
        btnSearch = findViewById(R.id.btn_search); btnSearch.setEnabled(false);
        spnShift = findViewById(R.id.spinner_shift);
        spnShift.setOnItemSelectedListener(this);
        btnSearch.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        getAllRoom();
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

    private void getAllRoom(){
        this.url="https://roomroomroom.herokuapp.com/Roomsession/available-teacher";
        new GetRoomAvailable().execute(url);
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
                        }
                    }, cYear, cMonth, cDate);
            datePickerDialog.show();
            btnSearch.setEnabled(true);
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
            String shift = spnShift.getSelectedItem().toString();
            String date = btnDatePicker.getText().toString();
            Log.e(TAG, "Date: " + date);
            // Making a request to url and getting response
            url = "https://roomroomroom.herokuapp.com/Roomsession/search/"+date+"/"+shift;
            Log.e(TAG, "url: " + url);
            new GetRoomAvailable().execute(url);
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

    private class GetRoomAvailable extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Search.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... urlStr) {
            HttpHandler sh = new HttpHandler();
            roomList = new ArrayList<>();
//            String shift = spnShift.getSelectedItem().toString();
//            String date = btnDatePicker.getText().toString();
//            Log.e(TAG, "Date: " + date);
//            // Making a request to url and getting response
//            String url = "https://roomroomroom.herokuapp.com/Roomsession/search/"+date+"/"+shift;
//            Log.e(TAG, "url: " + url);

            String url = urlStr[0];

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

//                        Log.e(TAG, "room info: " + id + roomName + shift + inDate);

                        RoomAvailable r = new RoomAvailable(id, roomName, shiftSession, inDate);

                        roomList.add(r);
                    }
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
            fillTable(roomList);
        }
    }

    public void fillTable(ArrayList<RoomAvailable> list){
        TableRow row;
        room_table.removeAllViewsInLayout();
        TextView txtIdroom, txtRoom, txtShift, txtDate;
        Button btnBook;
        TextView titleId, titleRoom, titleShift, titleDate;
        titleId = new TextView(this);
        titleRoom = new TextView(this);
        titleShift = new TextView(this);
        titleDate = new TextView(this);
        titleId.setText("Mã phòng");
        titleRoom.setText("Tên phòng");
        titleShift.setText("Ca");
        titleDate.setText("Ngày");

        titleId.setTypeface(null,1); // text BOLD
        titleRoom.setTypeface(null, 1);
        titleShift.setTypeface(null, 1);
        titleDate.setTypeface(null, 1);

//        titleId.setWidth(10);
//        titleRoom.setWidth(20);
//        titleShift.setWidth(10);
//        titleDate.setWidth(20);

        row = new TableRow(this);
        row.addView(titleId);
        row.addView(titleRoom);
        row.addView(titleShift);
        row.addView(titleDate);
        room_table.addView(row);



        for (int i=0; i < roomList.size(); i++) {
            row = new TableRow(this);

            txtIdroom = new TextView(this);
            txtRoom = new TextView(this);
            txtShift = new TextView(this);
            txtDate = new TextView(this);
            btnBook = new Button(this);

            txtIdroom.setText(list.get(i).getIdRoom());
            txtRoom.setText(list.get(i).getRoomName());
            txtShift.setText(list.get(i).getShiftSession());
            txtDate.setText(list.get(i).getInDate());
            btnBook.setText("Book");
            final int index = i;

            // set onClick btn
            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Search.this, roomList.get(index).getRoomName(), Toast.LENGTH_SHORT).show();
//                    SubscribeRoomSession s = new SubscribeRoomSession();
//                    s.send("https://roomroomroom.herokuapp.com/Roomsession/subscribe");
                    String idr = roomList.get(index).getIdRoom();
                    String ss = roomList.get(index).getShiftSession();
                    FormatStringDate fmd = new FormatStringDate();
                    // conver
                    String d = fmd.dateFormat(roomList.get(index).getInDate());
                    String url = "https://roomroomroom.herokuapp.com/Roomsession/subscribe?idRoom="+idr+"&idSession="+ss+"&date="+d+"&idSubscriber="+2;
                    Log.e(TAG, url);
                    UpdateRoomSession updateRoomSession= new UpdateRoomSession();
                    updateRoomSession.execute(url);
                    getAllRoom();
                }
            });

            row.addView(txtIdroom);
            row.addView(txtRoom);
            row.addView(txtShift);
            row.addView(txtDate);
            row.addView(btnBook);

            room_table.addView(row);
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
