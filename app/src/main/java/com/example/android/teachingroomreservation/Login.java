package com.example.android.teachingroomreservation;

import android.content.DialogInterface;
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
        Name = (EditText)findViewById(R.id.Name);
        Pass = (EditText)findViewById(R.id.Pass);
        buttonPanelID = (Button)findViewById(R.id.buttonPanelID);
        Name.setText("Admin");
        Pass.setText("Admin");

        buttonPanelID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "Admin";
                String Password = "Admin";

                if (username.compareTo(Name.getText().toString()) == 0){
                    Toast.makeText(getApplicationContext(),R.string.loginsuccess,Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),Name.getText(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.buttonPanelID:
                break;
            case R.id.buttonPanelIDout:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.exit_captione)
                        .setMessage(R.string.exit_mess)
                        .setNegativeButton(R.string.Button_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setPositiveButton(R.string.Button_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();
                break;
        }
        return true;
    }

}
