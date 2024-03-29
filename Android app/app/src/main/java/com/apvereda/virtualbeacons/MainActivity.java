package com.apvereda.virtualbeacons;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {


    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private MenuItem menuResetServerItem, menuChangeTemperatureItem;
    private Menu menuList;
    private VirtualProfile myVirtualProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PREFS_NAME, 0);

        String userName = settings.getString("userName", "felipe");
        String temperature = settings.getString("userTemperature", "20");


        myVirtualProfile = VirtualProfile.getVirtualProfileInstance();
        myVirtualProfile.setUserName(userName);
        myVirtualProfile.setTemperature(Double.parseDouble(temperature));


        Button start = (Button) findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReceiverActivity.class);
                startActivity(intent);
            }
        });
        Button stop = (Button) findViewById(R.id.stopBtn);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SenderActivity.class);
                startActivity(intent);
            }
        });
        Button query = (Button) findViewById(R.id.queryViewbtn);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DBConsulting.class);
                startActivity(intent);
            }
        });

        //request location permissions

        // No explanation needed, we can request the permission.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menuList = menu;
        menuChangeTemperatureItem = menu.findItem(R.id.action_set_temperature);
        menuResetServerItem = menu.findItem(R.id.action_restart_server);
        toggleMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter new name");


// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
            input.setText(myVirtualProfile.getUserName());
            input.selectAll();
            builder.setView(input);



// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setUserName(input.getText().toString());

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();


            return true;
        }else if(item.getItemId() == R.id.action_set_temperature){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter new temperature");


// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            input.setText(Double.toString(myVirtualProfile.getTemperature()));
            input.selectAll();
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setPreferredTemperature(input.getText().toString());

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();


            return true;


        }else if(item.getItemId() == R.id.action_restart_server){
            restartServer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartServer(){
        myVirtualProfile.restartGameVariables();
        new CallAPI().execute(CallAPI.restartGameURL);
    }

    private void setUserName(String newUserName){
        myVirtualProfile.setUserName(newUserName);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userName", myVirtualProfile.getUserName());
        editor.commit();
        toggleMenuItem();

    }

    private void setPreferredTemperature(String newTemperature){

        double temperature = Double.parseDouble(newTemperature);
        myVirtualProfile.setTemperature(temperature);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userTemperature", Double.toString(myVirtualProfile.getTemperature()));
        editor.commit();
        toggleMenuItem();
    }


    private void toggleMenuItem(){
        if(myVirtualProfile.getUserName().equals("root")){
            menuResetServerItem.setVisible(true);
        }else{
            menuResetServerItem.setVisible(false);
        }
    }

}


