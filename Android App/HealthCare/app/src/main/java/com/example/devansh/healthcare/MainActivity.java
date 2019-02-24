package com.example.devansh.healthcare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button heart = (Button)findViewById(R.id.heart);
        final Button bc = (Button)findViewById(R.id.bc);


        heart.setEnabled(false);
        bc.setEnabled(false);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            heart.setEnabled(true);
            bc.setEnabled(true);
            Toast.makeText(getBaseContext(), "Internet connectivity achieved!", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getBaseContext(), "Please connect to Internet!", Toast.LENGTH_LONG).show();


        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "In heart disease prediction", Toast.LENGTH_SHORT).show();

                Intent in = new Intent(MainActivity.this, Heart.class);
                startActivity(in);
            }
        });

        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "In Breast Cancer Detection", Toast.LENGTH_SHORT).show();

                Intent in = new Intent(MainActivity.this, BreastCancer.class);
                startActivity(in);
            }
        });
    }
}

