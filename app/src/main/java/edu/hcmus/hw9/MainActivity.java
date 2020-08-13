package edu.hcmus.hw9;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView tvMsg;
    private BroadcastReceiver broadcastReceiver;
    private Intent musicPlayerIntent, fibonacciIntent, gpsTrackingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        findViewById(R.id.btnStartMusic).setOnClickListener(this);
        findViewById(R.id.btnStopMusic).setOnClickListener(this);
        findViewById(R.id.btnStartFibo).setOnClickListener(this);
        findViewById(R.id.btnStopFibo).setOnClickListener(this);
        findViewById(R.id.btnStartGPS).setOnClickListener(this);
        findViewById(R.id.btnStopGPS).setOnClickListener(this);

        Log.e("MAIN", "Main started");

        musicPlayerIntent = new Intent(this, MusicPlayerService.class);
        fibonacciIntent = new Intent(this, FibonacciService.class);
        gpsTrackingIntent = new Intent(this, GPSTrackingService.class);

        IntentFilter fiboFilter = new IntentFilter("edu.hcmus.fibo");
        IntentFilter gpsFilter = new IntentFilter("edu.hcmus.gps");
        broadcastReceiver = new EmbeddedBroadcastReceiver();
        registerReceiver(broadcastReceiver, fiboFilter);
        registerReceiver(broadcastReceiver, gpsFilter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnStartMusic) {
            Log.e("MAIN", "onClick: start Music player");
            startService(musicPlayerIntent);
        } else if (view.getId() == R.id.btnStopMusic) {
            Log.e("MAIN", "onClick: stop Music player");
            stopService(musicPlayerIntent);
        }
        if (view.getId() == R.id.btnStartFibo) {
            Log.e("MAIN", "onClick: start Fibo");
            startService(fibonacciIntent);
        } else if (view.getId() == R.id.btnStopFibo) {
            Log.e("MAIN", "onClick: stop Fibo");
            stopService(fibonacciIntent);
        }
        if (view.getId() == R.id.btnStartGPS) {
            Log.e("MAIN", "onClick: start GPS");
            startService(gpsTrackingIntent);
        } else if (view.getId() == R.id.btnStopGPS) {
            Log.e("MAIN", "onClick: stop GPS");
            stopService(gpsTrackingIntent);
        }
    }

    public class EmbeddedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MAIN>>", "ACTION: " + intent.getAction());
            if (intent.getAction().equals("edu.hcmus.fibo")) {
                String fiboData = intent.getStringExtra("fiboData");
                Log.e("MAIN>>", "Data received from FibonacciService: " + fiboData);
                tvMsg.append("\nFibonacciService data: " + fiboData);
            } else if (intent.getAction().equals("edu.hcmus.gps")) {
                double latitude = intent.getDoubleExtra("latitude", -1);
                double longitude = intent.getDoubleExtra("longitude", -1);
                String provider = intent.getStringExtra("provider");
                String gpsData = provider + "lat" + Double.toString(latitude) + "lon" + Double.toString(longitude);
                Log.e("MAIN>>", "Data received from GPSTrackingService: " + gpsData);
                tvMsg.append("\nGPSTrackingService data: " + gpsData);
            }
        }
    }
}