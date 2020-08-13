package edu.hcmus.hw9;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {
    MediaPlayer player;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "MusicPlayerService created", Toast.LENGTH_LONG).show();
        Log.e("MusicPlayerService", "onCreate");
        player = MediaPlayer.create(getApplicationContext(), R.raw.cardigan);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MusicPlayerService stopped", Toast.LENGTH_LONG).show();
        Log.e("MusicPlayerService", "onDestroy");
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(player.isPlaying()){
            Toast.makeText(this, "MusicPlayerService already started" + startId, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "MusicPlayerService started" + startId, Toast.LENGTH_LONG).show();
            Log.e("MusicPlayerService", "onStart");
            player.start();
        }
    }
}
