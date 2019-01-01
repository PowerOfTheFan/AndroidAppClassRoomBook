package com.example.android.teachingroomreservation;

import android.content.pm.ActivityInfo;
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

import com.example.android.teachingroomreservation.ResultObject.RoomsessionNonApproved;
import com.example.android.teachingroomreservation.handler.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ReservationConfirm extends AppCompatActivity {

    final String TAG = ReservationConfirm.class.getSimpleName();
    String url = "http://roomroomroom.herokuapp.com/Roomsession/nonapproved";
    ArrayList<RoomsessionNonApproved> nonApproveds;

    TableLayout nonApprovedTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationconfirm);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        nonApprovedTable = findViewById(R.id.room_table_nonApproved);

        new GetRoomsessionNonApproved().execute(url);
    }

    private class GetRoomsessionNonApproved extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urlStr) {
            HttpHandler sh = new HttpHandler();
            nonApproveds = new ArrayList<>();
            String url = urlStr[0];
            String jsonObject = sh.makeServiceCall(url);

            if(jsonObject != null){
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonObject);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONArray obj = jsonArray.getJSONArray(i);
                        String idRoom = obj.getString(0);
                        String roomNmae = obj.getString(1);
                        String shiftSession = obj.getString(2);
                        String date = obj.getString(3);
                        String idEmp = obj.getString(4);
                        String nameEmp = obj.getString(5);

                        RoomsessionNonApproved r = new RoomsessionNonApproved(idRoom, roomNmae, shiftSession, date, idEmp, nameEmp);
                        nonApproveds.add(r);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fillTable(nonApproveds);
        }
    }

    private void fillTable(ArrayList<RoomsessionNonApproved> nonApproveds) {
        TableRow row;
        nonApprovedTable.removeAllViewsInLayout();
        TextView txtRoom, txtShift, txtDate, txtSubscriber;
        Button btnOk, btnDelete;
        TextView titleRoom, titleShift, titleDate, titleSubscriber;

        titleRoom = new TextView(this);
        titleShift = new TextView(this);
        titleDate = new TextView(this);
        titleSubscriber = new TextView(this);

        titleRoom.setText("Phòng");
        titleShift.setText("Ca");
        titleDate.setText("Ngày");
        titleSubscriber.setText("Subscriber");

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
        nonApprovedTable.addView(row);



        for (int i=0; i < nonApproveds.size(); i++) {
            row = new TableRow(this);

            txtRoom = new TextView(this);
            txtShift = new TextView(this);
            txtDate = new TextView(this);
            txtSubscriber = new TextView(this);
            btnOk = new Button(this);
            btnDelete= new Button(this);

            txtRoom.setText(nonApproveds.get(i).getRoomName());
            txtShift.setText(nonApproveds.get(i).getShift());
            txtDate.setText(nonApproveds.get(i).getDate());
            txtSubscriber.setText(nonApproveds.get(i).getNameEmp());
            btnOk.setText("Ok");
//            btnOk.setWidth(0);
            btnDelete.setText("Delete");
            final int index = i;

            // set onClick btn
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(Search.this, roomList.get(index).getRoomName(), Toast.LENGTH_SHORT).show();
////                    SubscribeRoomSession s = new SubscribeRoomSession();
////                    s.send("https://roomroomroom.herokuapp.com/Roomsession/subscribe");
//                    String idr = roomList.get(index).getIdRoom();
//                    String ss = roomList.get(index).getShiftSession();
//                    FormatStringDate fmd = new FormatStringDate();
//                    // conver
//                    String d = fmd.dateFormat(roomList.get(index).getInDate());
//                    String url = "https://roomroomroom.herokuapp.com/Roomsession/subscribe?idRoom="+idr+"&idSession="+ss+"&date="+d+"&idSubscriber="+2;
//                    Log.e(TAG, url);
//                    UpdateRoomSession updateRoomSession= new UpdateRoomSession();
//                    updateRoomSession.execute(url);
//                    getAllRoom();
                }
            });

            row.addView(txtRoom);
            row.addView(txtShift);
            row.addView(txtDate);
            row.addView(txtSubscriber);
            row.addView(btnOk);
            row.addView(btnDelete);

            nonApprovedTable.addView(row);
        }
    }
}
