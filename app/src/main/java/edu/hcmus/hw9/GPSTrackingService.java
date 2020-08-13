package edu.hcmus.hw9;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class GPSTrackingService extends Service {
    String GPS_FILTER = "edu.hcmus.gps";
    Thread thread;
    LocationManager lm;
    GPSListener gpsListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getGPSFix_Version2();
            }
        });
        thread.start();
    }

    public void getGPSFix_Version2() {
        try {
            Looper.prepare();
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new GPSListener();

            long minTime = 2000;
            float minDistance = 0;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            lm.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Intent response = new Intent(GPS_FILTER);
            response.putExtra("latitude", latitude);
            response.putExtra("longitude", longitude);
            response.putExtra("provider", location.getProvider());
            sendBroadcast(response);
        }
    }
}
