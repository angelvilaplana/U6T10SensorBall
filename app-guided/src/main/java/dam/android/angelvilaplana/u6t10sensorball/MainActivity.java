package dam.android.angelvilaplana.u6t10sensorball;

import android.content.Context;
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

        ballView = new BallView(this);
        setContentView(ballView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(ballView);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(ballView,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }
}