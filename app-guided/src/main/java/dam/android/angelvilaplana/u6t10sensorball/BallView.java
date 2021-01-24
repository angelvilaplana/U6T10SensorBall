package dam.android.angelvilaplana.u6t10sensorball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;

public class BallView extends View implements SensorEventListener {

    private Ball ball;
    private Paint pen;

    private int width, height;

    SoundManager soundManager;

    Bitmap bitmapBall;

    public BallView(Context context) {
        super(context);
        ball = new Ball();
        pen = new Paint();
        soundManager = new SoundManager();
        soundManager.initSoundManager(context);
        soundManager.addSound(1, R.raw.ball_bounce);
    }

    private double map(double value, double o1, double o2, double d1, double d2) {
        double scale = (d2 - d1) / (o2 - o1);
        return (value - o1) * scale + d1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        ball.x = w / 2f;
        ball.y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pen.setColor(Color.BLUE);
        pen.setStrokeWidth(1);
        pen.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float) ball.x, (float) ball.y, ball.DIAMETER, pen);
    }

    protected void updatePhysics() {
        boolean collision = false;

        if (ball.x + ball.DIAMETER / 2f > width - 1) {
            ball.vx = -ball.vx / 2;
            ball.x = width - 1 - ball.DIAMETER / 2f;
            collision = true;
        }
        if (ball.x - ball.DIAMETER / 2f < 0) {
            ball.vx = -ball.vx / 2f;
            ball.x = ball.DIAMETER / 2f;
            collision = true;
        }
        if (ball.y + ball.DIAMETER / 2f > height - 1) {
            ball.vy *= -1;
            ball.y = height - 1 - ball.DIAMETER / 2f;
            collision = true;
        }
        if (ball.y - ball.DIAMETER / 2f < 0) {
            ball.vy *= -1;
            ball.y = ball.DIAMETER / 2f;
            collision = true;
        }

        if (collision) soundManager.playSound(1);

        ball.vy += ball.GRAVITY;
        ball.vx += ball.ax;

        ball.x += ball.vx;
        ball.y += ball.vy;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double value = event.values[0];

            if (value > 5)  value = 5;
            if (value < -5) value = -5;

            if (value < 0.1 && value > -0.1) {
                ball.ax = 0;
            } else {
                ball.ax = map(Math.abs(value), 0.1, 5, 0.05, 0.5);
                if (value < 0) ball.ax *= -1;
            }

            ball.ax *= -1;

            updatePhysics();
            invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
