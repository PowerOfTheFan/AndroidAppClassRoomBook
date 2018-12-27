package com.example.android.teachingroomreservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText Name, Pass;
    Button buttonPanelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Name = (EditText)findViewById(R.id.username_edtext);
        Pass = (EditText)findViewById(R.id.passwd_edtext);
        buttonPanelID = (Button)findViewById(R.id.login_button);

        buttonPanelID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "Admin";
                String Password = "Admin";
                if (Name.getText().toString().equals(username) && Pass.getText().toString().equals(Password)){
                    Toast.makeText(getApplicationContext(),R.string.loginsuccess,Toast.LENGTH_LONG).show();
                    Intent mh2 = new Intent(Login.this, Homepage.class);
                    startActivity(mh2);
                }
                else {
                    Toast.makeText(getApplicationContext(),R.string.loginerror,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.login_button:
                break;
//            case R.id.buttonPanelIDout:
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.exit_captione)
//                        .setMessage(R.string.exit_mess)
//                        .setNegativeButton(R.string.Button_yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        })
//                        .setPositiveButton(R.string.Button_no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                return;
//                            }
//                        })
//                        .show();
//                break;
        }
        return true;
    }

}
