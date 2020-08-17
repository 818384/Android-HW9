package edu.hcmus.hw9;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FibonacciService extends Service {
    private ComputeFibonacciRecursivelyTask asyncTask;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.e("FibonacciServiceHandler", "Handler got from FibonacciService" + (String)msg.obj);
        }


    };
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
        asyncTask = new ComputeFibonacciRecursivelyTask();
        asyncTask.execute(20, 50);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(true);
        Log.e("FibonacciService", "onDestroy");
        Log.e("FibonacciService", "AsyncTask stopped");
    }

    public Integer fibonacci(Integer n){
        if (n == 0 || n == 1){
            return 1;

        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    public class ComputeFibonacciRecursivelyTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            for(int i = integers[0]; i < integers[1]; i++){
                Integer fibo = fibonacci(i);
                publishProgress(i, fibo);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Intent fibonacciIntent = new Intent("edu.hcmus.fibo");
            String data = "dataItem-5-fibonacci-AsyncTask" + values[0] + ": " + values[1];
            fibonacciIntent.putExtra("fiboData", data);
            sendBroadcast(fibonacciIntent);
            Message msg = handler.obtainMessage(5, data);
            handler.sendMessage(msg);
        }

        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);
        }
    }
}
