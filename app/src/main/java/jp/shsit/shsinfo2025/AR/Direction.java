package jp.shsit.shsinfo2025.AR;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class Direction extends AsyncTask<Void, Void, Void> implements SensorEventListener {
    protected final static double RAD2DEG = 180/ Math.PI;

    SensorManager sensorManager;

    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];
    private Context ContextDire;
    TextView text;


    public Direction(Context context1) {

        this.ContextDire = context1;
        initSensor();

        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        //磁気センサー
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }


    protected void initSensor(){
        sensorManager = (SensorManager)ContextDire.getSystemService(SENSOR_SERVICE);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
        }

        if(geomagnetic != null && gravity != null) {

            SensorManager.getRotationMatrix(
                    rotationMatrix, null,
                    gravity, geomagnetic);

            SensorManager.getOrientation(
                    rotationMatrix,
                    attitude);
            //方角　０度北　90度東　180度南　-90度西
           // Log.i("test", Integer.toString(
                  //  (int) (attitude[0] * RAD2DEG)));


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void sendBroadcast(String message) {
      //  Log.i("test",message);
        // IntentをブロードキャストすることでMainActivity2へデータを送信
        Intent intent = new Intent();
        intent.setAction("direction");
        intent.putExtra("message", message);
        ContextDire.sendBroadcast(intent);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        boolean loop = true;

        // 繰り返し処理
        while (loop) {
            //方角　０度北　90度東　180度南　-90度西
            String direc = Integer.toString((int) (attitude[0] * RAD2DEG));
            sendBroadcast(direc);

            SystemClock.sleep(100);  // 1000ミリ秒の時間稼ぎ

            if (this.isCancelled()) {
                loop = false;
            }
        }

        return null;
    }

}
