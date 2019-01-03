package com.example.android.teachingroomreservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.ResultObject.RoomAvailable;
import com.example.android.teachingroomreservation.handler.HttpPostRoomsession;
import com.example.android.teachingroomreservation.handler.SubscribeRoomSession;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEmptyRoom extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    String TAG = AddEmptyRoom.class.getSimpleName();
    Button btnDatePicker, btnSearchRoom;
    Spinner spnShift;
    TableLayout room_table;

    private int cDate, cMonth, cYear;

    ArrayList<RoomAvailable> roomList;
    String ID_EMP;

    private String url;
    // get id emp
    String getIdEmp(){
        StringBuilder sbId = new StringBuilder();

        try{
            FileInputStream fileInputId = openFileInput(Login.fileId);
            BufferedReader idReader = new BufferedReader(new InputStreamReader(fileInputId));
            String idStr;
            while((idStr = idReader.readLine())!=null){
                sbId.append(idStr).append("");
            }
            return sbId.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addemptyroom);

        // get IdEMP tu file
        ID_EMP = getIdEmp();
        // check id exist
        if(ID_EMP == null){
            Intent intentLogin = new Intent(this, Login.class);
            startActivity(intentLogin);
        }

        btnDatePicker = findViewById(R.id.btn_date_addemptyroom);
        btnSearchRoom = findViewById(R.id.btn_search_addemptyroom); btnSearchRoom.setEnabled(false);
        spnShift = findViewById(R.id.spinner_shift_addemptyroom);
        room_table = findViewById(R.id.room_table_addemptyroom);

        btnSearchRoom.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        spnShift.setOnItemSelectedListener(this);

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

        if(v == btnDatePicker){
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
            btnSearchRoom.setEnabled(true);
        }

        if (v == btnSearchRoom) {

            String shift = spnShift.getSelectedItem().toString();
            String date = btnDatePicker.getText().toString();
            Log.e(TAG, "Date: " + date);
            // Making a request to url and getting response
            url = "https://roomroomroom.herokuapp.com/Room/roomavailble?date=" + date +"&session="+shift;
            Log.e(TAG, "url: " + url);
            new GetRoomAvailable().execute(url);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GetRoomAvailable extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AddEmptyRoom.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... urlStr) {
            HttpHandler sh = new HttpHandler();
            roomList = new ArrayList<>();
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
                        String seatAmount = room.getString(2);


                        RoomAvailable r = new RoomAvailable(id, roomName, seatAmount);

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
        TextView txtIdroom, txtRoom, txtSeatAmount;
        Button btnAddRoom;
        TextView titleId, titleRoom, titleSeatAmount;
        titleId = new TextView(this);
        titleRoom = new TextView(this);
        titleSeatAmount = new TextView(this);

        titleId.setText("Mã phòng");
        titleRoom.setText("Tên phòng");
        titleSeatAmount.setText("Số ghế");

        titleId.setTypeface(null,1); // text BOLD
        titleRoom.setTypeface(null, 1);
        titleSeatAmount.setTypeface(null, 1);

//        titleId.setWidth(10);
//        titleRoom.setWidth(20);
//        titleShift.setWidth(10);
//        titleDate.setWidth(20);

        row = new TableRow(this);
        row.addView(titleId);
        row.addView(titleRoom);
        row.addView(titleSeatAmount);
        room_table.addView(row);



        for (int i=0; i < roomList.size(); i++) {
            row = new TableRow(this);

            txtIdroom = new TextView(this);
            txtRoom = new TextView(this);
            txtSeatAmount = new TextView(this);
            btnAddRoom = new Button(this);

            txtIdroom.setText(list.get(i).getIdRoom());
            txtRoom.setText(list.get(i).getRoomName());
            txtSeatAmount.setText(list.get(i).getSeatAmount());
            btnAddRoom.setText("Thêm");
            final int index = i;

            // set onClick btn
            btnAddRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AddEmptyRoom.this, roomList.get(index).getRoomName(), Toast.LENGTH_SHORT).show();
                    int idRoom = Integer.parseInt(roomList.get(index).getIdRoom());
                    int  shiftSession = Integer.parseInt(spnShift.getSelectedItem().toString());
                    String date = btnDatePicker.getText().toString();
                    int creator = Integer.parseInt(ID_EMP);
                    HttpPostRoomsession s = new HttpPostRoomsession();
                    s.send("https://roomroomroom.herokuapp.com/Roomsession/create?room="+idRoom+"&session="+shiftSession+"&date="+date+"&creator="+creator);
                    new GetRoomAvailable().execute(url);
//                    getAllRoom();
                }
            });

            row.addView(txtIdroom);
            row.addView(txtRoom);
            row.addView(txtSeatAmount);
            row.addView(btnAddRoom);

            room_table.addView(row);
        }
    }
}
