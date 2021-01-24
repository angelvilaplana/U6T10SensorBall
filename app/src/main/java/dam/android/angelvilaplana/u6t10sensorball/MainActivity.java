package dam.android.angelvilaplana.u6t10sensorball;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private BallView ballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO - Activity 1 - Locks orientation in code & manifest
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ballView = new BallView(this);
        setContentView(ballView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(ballView);
        // TODO - Activity 4 - Release the SoundPool
        ballView.getSoundManager().releaseSoundPool();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(ballView,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        // TODO - Activity 4 - Load the SoundPool
        ballView.getSoundManager().loadSoundPool();
    }

}