package com.example.android.teachingroomreservation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.teachingroomreservation.ResultObject.BookingHistory;
import com.example.android.teachingroomreservation.handler.CompareDay;
import com.example.android.teachingroomreservation.handler.FormatStringDate;
import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.handler.UpdateRoomSession;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReservationHistory extends AppCompatActivity {

    final String TAG = ReservationHistory.class.getSimpleName();
    String url = null;
    ArrayList<BookingHistory> bookingList;

    TableLayout bookingHistory_table;
    String ID_EMP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationhistory);

        bookingHistory_table = findViewById(R.id.bookingHistory_table);

        ID_EMP = getIdEmp();
        if(ID_EMP == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        url = "https://roomroomroom.herokuapp.com/Roomsession/history/"+2;
        new GetRoomBookingHistory().execute(url);
    }

    private class GetRoomBookingHistory extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(ReservationHistory.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... urlStr) {
            HttpHandler sh = new HttpHandler();
            bookingList = new ArrayList<>();
            String url = urlStr[0];
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for(int i=0; i<jsonArr.length(); i++){
                        JSONArray list = jsonArr.getJSONArray(i);
                        String idRoom = list.getString(0);
                        String roomName = list.getString(1);
                        String shiftSession = list.getString(2);
                        String inDate = list.getString(3);
                        String subscriber = list.getString(4);


                        BookingHistory r = new BookingHistory(idRoom, roomName, shiftSession, inDate, subscriber);

                        bookingList.add(r);
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
            fillTable(bookingList);
        }
    }

    public void fillTable(final ArrayList<BookingHistory> list){
        TableRow row;
        bookingHistory_table.removeAllViewsInLayout();
        TextView txtIdroom, txtRoom, txtShift, txtDate;
        Button btnCancel;
        TextView titleIdRoom, titleRoom, titleShift, titleDate;
        titleIdRoom = new TextView(this);
        titleRoom = new TextView(this);
        titleShift = new TextView(this);
        titleDate = new TextView(this);
        titleIdRoom.setText("Mã phòng");
        titleRoom.setText("Tên phòng");
        titleShift.setText("Ca");
        titleDate.setText("Ngày");

        titleIdRoom.setTypeface(null,1); // text BOLD
        titleRoom.setTypeface(null, 1);
        titleShift.setTypeface(null, 1);
        titleDate.setTypeface(null, 1);

//        titleId.setWidth(10);
//        titleRoom.setWidth(20);
//        titleShift.setWidth(10);
//        titleDate.setWidth(20);

        row = new TableRow(this);
//        row.addView(titleIdRoom);
        row.addView(titleRoom);
        row.addView(titleShift);
        row.addView(titleDate);
        bookingHistory_table.addView(row);


        final FormatStringDate fmd = new FormatStringDate();
        for (int i=0; i < list.size(); i++) {
            row = new TableRow(this);

            txtIdroom = new TextView(this);
            txtRoom = new TextView(this);
            txtShift = new TextView(this);
            txtDate = new TextView(this);
            btnCancel = new Button(this);

            txtIdroom.setText(list.get(i).getIdRoom());
            txtRoom.setText(list.get(i).getRoomName());
            txtShift.setText(list.get(i).getShiftSession());
            txtDate.setText(fmd.dateFormat(list.get(i).getDate()));
            btnCancel.setText("Xóa");
            final int index = i;

            // kiem tra ngay bat dau > 2, cho phep xoa
            CompareDay cpd = new CompareDay();
            if(cpd.differanceDay(fmd.dateFormat(list.get(i).getDate())) >= 2){
                // set onClick btn
                final Button finalBtnCancel = btnCancel;
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Toast.makeText(Search.this, roomList.get(index).getRoomName(), Toast.LENGTH_SHORT).show();
////                    SubscribeRoomSession s = new SubscribeRoomSession();
////                    s.send("https://roomroomroom.herokuapp.com/Roomsession/subscribe");
                        String idr = list.get(index).getIdRoom();
                        String ss = list.get(index).getShiftSession();
//                    FormatStringDate fmd = new FormatStringDate();
                        // conver
                        String d = fmd.dateFormat(list.get(index).getDate());
//                    String idSubscriber = bookingList.get(index).getSubscriber();
                        String url = "https://roomroomroom.herokuapp.com/Roomsession/subscribe/delete?idRoom="+idr+"&idSession="+ss+"&date="+d+"&idSubscriber="+2;
                        Log.e(TAG, url);
                        UpdateRoomSession updateRoomSession= new UpdateRoomSession();
                        updateRoomSession.execute(url);
                        finalBtnCancel.setEnabled(false);
//                    getAllRoom();
                    }
                });
            }else{
                btnCancel.setEnabled(false);
            }


//            row.addView(txtIdroom);
            row.addView(txtRoom);
            row.addView(txtShift);
            row.addView(txtDate);
            row.addView(btnCancel);

            bookingHistory_table.addView(row);
        }
    }

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
}
