package com.example.android.teachingroomreservation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.teachingroomreservation.handler.Account;
import com.example.android.teachingroomreservation.handler.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText Name, Pass;
    Button buttonPanelID;

    String emailEmp, idEmp, nameEmp, positionEmp; // pos = ADMIN
//    public static String extraIDEmp = "idEmp";
    public static String fileId = "idNote.txt";
    public static String filePos = "idNote.txt";

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
        buttonPanelID.setOnClickListener(this);

        // kiem tra account
        checkAccount();

    }

    void checkAccount(){
        StringBuilder sbId = new StringBuilder();
        StringBuilder sbPos = new StringBuilder();
        try {
//            FileInputStream fileInputId = this.openFileInput(Login.fileId);
            FileInputStream fileInputPos = this.openFileInput(Login.filePos);
//            BufferedReader idReader = new BufferedReader(new InputStreamReader(fileInputId));
            BufferedReader posReader = new BufferedReader(new InputStreamReader(fileInputPos));

//            String idStr;
            String posStr;
//            while ((idStr = idReader.readLine())!= null){
//                sbId.append(idStr).append("\n");
//            }
            while ((posStr = posReader.readLine())!= null){
                sbId.append(posStr).append("\n");
            }
//            System.out.println(sbId.toString());
            System.out.println(sbPos.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(sbPos.toString().equals("ADMIN")){
            Intent intentAddEmptyRoom = new Intent(this, AddEmptyRoom.class);
            startActivity(intentAddEmptyRoom);
        }else {
            Intent intentSearch = new Intent(this, Search.class);
            startActivity(intentSearch);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonPanelID){
            String username = Name.getText().toString();
            String Password = Pass.getText().toString();

            String url = "https://roomroomroom.herokuapp.com/employee/login?email=" + username + "&id=" + Password;
            new GetEmployee().execute(url);
            Toast.makeText(Login.this, emailEmp+"@@@@@@@@@@@@@@@@@@", Toast.LENGTH_SHORT).show();
            if (emailEmp == null) {
                Toast.makeText(getApplicationContext(),"Login fail",Toast.LENGTH_LONG).show();
            }else {
                setEmailEmp(null);
                // ghi file
                try {
                    FileOutputStream osId = this.openFileOutput(fileId, MODE_PRIVATE);
                    osId.write(idEmp.getBytes());
                    osId.close();

                    FileOutputStream osPos = this.openFileOutput(filePos, MODE_PRIVATE);
                    osPos.write(idEmp.getBytes());
                    osPos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(getPositionEmp().equals("ADMIN")){
                    Intent intentAddEmptyRoom = new Intent(this, AddEmptyRoom.class);
//                    intentAddEmptyRoom.putExtra(extraIDEmp, idEmp);
                    startActivity(intentAddEmptyRoom);
                }else{
                    Intent intentSearch = new Intent(this, Search.class);
//                    intentLogin.putExtra(extraIDEmp, idEmp);
                    startActivity(intentSearch);
                }
            }
        }
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

