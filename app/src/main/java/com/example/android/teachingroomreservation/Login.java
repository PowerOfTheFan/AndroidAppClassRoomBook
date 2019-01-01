package com.example.android.teachingroomreservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.teachingroomreservation.handler.HttpHandler;
import com.example.android.teachingroomreservation.handler.RoomAvailable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText Name, Pass;
    Button buttonPanelID;

    String emailEmp, idEmp, nameEmp, positionEmp; // pos = ADMIN

    String TAG = Login.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Name = (EditText) findViewById(R.id.username_edtext);
        Pass = (EditText) findViewById(R.id.passwd_edtext);
        buttonPanelID = (Button) findViewById(R.id.login_button);
        Name = (EditText) findViewById(R.id.username_edtext);
        Pass = (EditText) findViewById(R.id.passwd_edtext);
        buttonPanelID = (Button) findViewById(R.id.login_button);
//        Name.setText("Admin");
//        Pass.setText("Admin");

        buttonPanelID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Name.getText().toString();
                String Password = Pass.getText().toString();

                String url = "https://roomroomroom.herokuapp.com/employee/login?email=" + username + "&id=" + Password;
                new GetEmployee().execute(url);
                Toast.makeText(Login.this, emailEmp+"@@@@@@@@@@@@@@@@@@", Toast.LENGTH_SHORT).show();
                // sau khi GetEmployee().execute(url) duoc thuc hien, neu emailEmp hoac idEmp... co gia tri thi login thanh cong

                if (getEmailEmp() == null) {

                    Toast.makeText(getApplicationContext(),"Login fail",Toast.LENGTH_LONG).show();
                }
                else {
                    setEmailEmp(null);
                    if(getPositionEmp() == "ADMIN"){
                        // goi acctivity cua admin, chuyen idEMP sang
                        Toast.makeText(getApplicationContext(), R.string.loginsuccess, Toast.LENGTH_LONG).show();
                        Intent mh2 = new Intent(Login.this, AddEmptyRoom.class);
                        startActivity(mh2);
                    }else{
                        //goiacctivity teacher
                        Toast.makeText(getApplicationContext(), R.string.loginsuccess, Toast.LENGTH_LONG).show();
                        Intent mh2 = new Intent(Login.this, Search.class);
                        startActivity(mh2);
                    }
                    // kiem pos = admin. neu true thi goi activity mac dinh cuar admin, false goi acctivity cuar teacher
                    // chuyen idEmp sang acctivity duoc goi

                }
            }
        });
    }

    private class GetEmployee extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(Login.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... urlStr) {
            HttpHandler sh = new HttpHandler();
//            roomList = new ArrayList<>();
            String url = urlStr[0];
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);

                    // Getting JSON Array node
                        JSONArray emp = jsonArr.getJSONArray(0);
                        Login.this.setIdEmp(emp.getString(0));
                        Login.this.setEmailEmp(emp.getString(1));
                        Login.this.setNameEmp(emp.getString(2));
                        System.out.println((emp.getString(2).toString()));
                        Login.this.setPositionEmp(emp.getString(3));


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
//            fillTable(roomList);
        }
    }

    public String getEmailEmp() {
        return emailEmp;
    }

    public void setEmailEmp(String emailEmp) {
        this.emailEmp = emailEmp;
    }

    public String getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(String idEmp) {
        this.idEmp = idEmp;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }

    public String getPositionEmp() {
        return positionEmp;
    }

    public void setPositionEmp(String positionEmp) {
        this.positionEmp = positionEmp;
    }
}

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.login_button:
//                break;
//        }
//        return true;
//    }

