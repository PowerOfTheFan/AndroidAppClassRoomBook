package com.example.android.teachingroomreservation;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import com.example.android.teachingroomreservation.handler.FormatStringDate;
import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.ResultObject.RoomSessionAvailable;
import com.example.android.teachingroomreservation.handler.UpdateRoomSession;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Search extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int NOTIFY_ME_ID=1337;
    private int count=0;
    Notification myNotication;
    private NotificationManager notifyMgr=null;

    String TAG = Search.class.getSimpleName();
    Button btnDatePicker, btnSearch;
    Spinner spnShift;
    TableLayout room_table;
    private int cDate, cMonth, cYear;

    String ID_EMP = null;


    ArrayList<RoomSessionAvailable> roomList;

    private String url;

    String getIdEmp(){
        StringBuilder sbId = new StringBuilder();

        try{
            FileInputStream fileInputId = this.openFileInput(Login.fileId);
            BufferedReader idReader = new BufferedReader(new InputStreamReader(fileInputId));
            String idStr;
            while((idStr = idReader.readLine())!=null){
                sbId.append(idStr).append("");
            }
            System.out.println("@@@@@@@@@@@@@@@@ Seach getIdEmp: "+sbId.toString());
            return sbId.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ID_EMP = getIdEmp();
        System.out.println("@@@@@@@@@@@@@@@ Search :"+ID_EMP);
        if(ID_EMP == null){
            Intent intentLogin = new Intent(this, Login.class);
            startActivity(intentLogin);
        }


        notifyMgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

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
        new GetRoomSessionAvailable().execute(url);
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

            String shift = spnShift.getSelectedItem().toString();
            String date = btnDatePicker.getText().toString();
            Log.e(TAG, "Date: " + date);
            // Making a request to url and getting response
            url = "https://roomroomroom.herokuapp.com/Roomsession/search/"+date+"/"+shift;
            Log.e(TAG, "url: " + url);
            new GetRoomSessionAvailable().execute(url);
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

    private class GetRoomSessionAvailable extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Search.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

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
                        String shiftSession = room.getString(2);
                        String inDate = room.getString(3);


                        RoomSessionAvailable r = new RoomSessionAvailable(id, roomName, shiftSession, inDate);

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

    public void fillTable(ArrayList<RoomSessionAvailable> list){
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
                    String url = "https://roomroomroom.herokuapp.com/Roomsession/subscribe?idRoom="+idr+"&idSession="+ss+"&date="+d+"&idSubscriber="+ ID_EMP;
                    Log.e(TAG, url);
                    UpdateRoomSession updateRoomSession= new UpdateRoomSession();
                    updateRoomSession.execute(url);
                    getAllRoom();
                    triggerNotification();
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

    /////////////////////// NOTIFICATION
    public void triggerNotification() {

        Intent intent = new Intent("com.example.android.teachingroomreservation.ReservationHistory");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, ReservationHistory.class), 0);

        NotificationCompat.Action declineAction = new NotificationCompat.Action
                .Builder(R.drawable.ic_launcher_background, "Decline Request", pendingIntent).build();

        Notification.Builder builder = new Notification.Builder(Search.this);
//        builder.setAutoCancel(false);
//        builder.setTicker("this is ticker text");
        builder.setContentTitle("Class Room Booking");
        builder.setContentText("Bạn đã đặt phòng thành công");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
//        builder.setContentIntent(pendingIntent);
//        builder.setOngoing(true);
//        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.addAction(R.drawable.ic_launcher_background, "AAAAAAAAAAAA", pendingIntent);
        builder.build();

        myNotication = builder.getNotification();
        notifyMgr.notify(NOTIFY_ME_ID, myNotication);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {

        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_seach:
                Intent intentSearch = new Intent(this, Search.class);
                startActivity(intentSearch);
                break;
            case R.id.menu_viewHistory:
                Intent intentHis = new Intent(this, ReservationHistory.class);
                startActivity(intentHis);
                break;
            case R.id.menu_deleteSubs:
                Intent intentDelete = new Intent(this, RoomsessionRemove.class);
                startActivity(intentDelete);
                break;
            case R.id.menu_logout:
                File dir = getFilesDir();
                File idFile = new File(dir, Login.fileId);
                File posFile = new File(dir, Login.filePos);
                idFile.delete();
                posFile.delete();

                Intent intentLogin = new Intent(this, Login.class);
                startActivity(intentLogin);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public void clearNotification() {
        notifyMgr.cancel(NOTIFY_ME_ID);
    }

}
