package com.example.android.teachingroomreservation;

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

import com.example.android.teachingroomreservation.handler.FormatStringDate;
import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.handler.RoomSession;
import com.example.android.teachingroomreservation.handler.UpdateRoomSession;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ReservationConfirm extends AppCompatActivity {

    final String TAG = ReservationConfirm.class.getSimpleName();
    String url;
    ArrayList<RoomSession> roomList;
    TableLayout roomNonApproved_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationconfirm);

        roomNonApproved_table = findViewById(R.id.roomNonApproved_table);
        getList();
    }

    private void getList(){
        this.url="http://roomroomroom.herokuapp.com/Roomsession/nonapproved";
        new GetRoomSessionNonApproved().execute(url);
    }
    private class GetRoomSessionNonApproved extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                        String idRoom = room.getString(0);
                        String roomName = room.getString(1);
                        String shiftSession = room.getString(2);
                        String inDate = room.getString(3);
                        String idEmp = room.getString(4);
                        String nameEmp = room.getString(5);


                        RoomSession r = new RoomSession(idRoom, roomName, shiftSession, inDate, idEmp, nameEmp);

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

    public void fillTable(ArrayList<RoomSession> list){
        Log.e("AAAA", list.get(0).getNameSubscriber());
        Log.e("AAAA", list.get(0).getInDate());
        TableRow row;
        roomNonApproved_table.removeAllViewsInLayout();
        TextView txtRoom, txtShift, txtDate, txtSubscriber;
        Button btnConfirm;

        TextView titleRoom, titleShift, titleDate, titleSubscriber;
        titleRoom = new TextView(this);
        titleShift = new TextView(this);
        titleDate = new TextView(this);
        titleSubscriber = new TextView(this);
        titleRoom.setText("Phòng");
        titleShift.setText("Ca");
        titleDate.setText("Ngày");
        titleSubscriber.setText("Booker");

        titleRoom.setTypeface(null, 1);
        titleShift.setTypeface(null, 1);
        titleDate.setTypeface(null, 1);
        titleSubscriber.setTypeface(null, 1);

//        titleId.setWidth(10);
//        titleRoom.setWidth(20);
//        titleShift.setWidth(10);
//        titleDate.setWidth(20);

        row = new TableRow(this);
        row.addView(titleRoom);
        row.addView(titleShift);
        row.addView(titleDate);
        row.addView(titleSubscriber);
        roomNonApproved_table.addView(row);



        for (int i=0; i < roomList.size(); i++) {
            row = new TableRow(this);

            txtRoom = new TextView(this);
            txtShift = new TextView(this);
            txtDate = new TextView(this);
            txtSubscriber = new TextView(this);
            btnConfirm = new Button(this);

            txtRoom.setText(list.get(i).getRoomName());
            txtShift.setText(list.get(i).getShiftSession());
            txtDate.setText(list.get(i).getInDate());
            txtSubscriber.setText(list.get(i).getNameSubscriber());
            btnConfirm.setText("Ok");
            final int index = i;

            // set onClick btn
            final Button finalBtnConfirm = btnConfirm;
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(Search.this, roomList.get(index).getRoomName(), Toast.LENGTH_SHORT).show();
//                    SubscribeRoomSession s = new SubscribeRoomSession();
//                    s.send("https://roomroomroom.herokuapp.com/Roomsession/subscribe");
                    String idr = roomList.get(index).getIdRoom();
                    String ss = roomList.get(index).getShiftSession();
                    FormatStringDate fmd = new FormatStringDate();
                    // conver
                    String d = fmd.dateFormat(roomList.get(index).getInDate());
                    String idApprover = "4";
                    String url = "https://roomroomroom.herokuapp.com/Roomsession/approve?idRoom="+idr+"&idSession="+ss+"&date="+d+"&idApprover="+idApprover;
                    Log.e(TAG, url);
                    UpdateRoomSession updateRoomSession= new UpdateRoomSession();
                    updateRoomSession.execute(url);
                    finalBtnConfirm.setEnabled(false);
//                    triggerNotification();
                }
            });

            row.addView(txtRoom);
            row.addView(txtShift);
            row.addView(txtDate);
            row.addView(txtSubscriber);
            row.addView(btnConfirm);

            roomNonApproved_table.addView(row);
        }
    }
}
