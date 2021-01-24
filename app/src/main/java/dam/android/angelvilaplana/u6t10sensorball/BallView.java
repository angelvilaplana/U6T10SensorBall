package dam.android.angelvilaplana.u6t10sensorball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;

public class BallView extends View implements SensorEventListener {

    private final Ball ball;
    private final Paint paint;
    private final Coin[] coins;

    private int width, height;
    private int points;
    private int parallaxBgY;

    private boolean lastCollisionX, lastCollisionY;

    SoundManager soundManager;

    Bitmap bitmapBall;
    Bitmap bitmapBG;

    public BallView(Context context) {
        super(context);
        ball = new Ball();
        paint = new Paint();
        soundManager = new SoundManager();
        soundManager.initSoundManager(context);

        // TODO - Activity 4 - Load sounds
        soundManager.addSound(1, R.raw.ball_bounce);
        soundManager.addSound(2, R.raw.football_kick);
        soundManager.addSound(3, R.raw.kick);
        soundManager.addSound(4, R.raw.kick_ball);
        soundManager.addSound(5, R.raw.soccer_kick);
        soundManager.addSound(6, R.raw.coin);

        bitmapBall = BitmapFactory.decodeResource(getResources(), R.mipmap.ball);
        bitmapBall = Bitmap.createScaledBitmap(bitmapBall, ball.SIZE, ball.SIZE, true);
        bitmapBG = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);

        // TODO - EXTRA
        points = 0;
        coins = new Coin[5];
        lastCollisionX = false;
        lastCollisionY = false;
        parallaxBgY = 0;
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
        ball.y = 100;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        paint.setTextSize(40);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // TODO - Activity 2 - Add a Background Image
        parallaxBgY -= 2;
        if (parallaxBgY <= -1300) {
            parallaxBgY = 0;
        }
        canvas.drawBitmap(bitmapBG, (float) parallaxBgY, 0, null);

        // TODO - EXTRA - COINS
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] == null) {
                coins[i] = new Coin(width, height, 50, 50);
            }
            paint.setColor(coins[i].color);
            canvas.drawCircle(coins[i].x, coins[i].y, coins[i].diameter, paint);
        }

        // TODO - Activity 3 - Draw a Ball with image
        canvas.drawBitmap(bitmapBall, (float) ball.x, (float) ball.y, null);

        // EXTRA AGAIN - TITLE
        paint.setColor(getContext().getColor(R.color.colorPrimary));
        canvas.drawRect(width - 300, 0, width, 80, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("Points: " + points, width- 280, 50, paint);
    }

    protected void updatePhysics() {
        // TODO - Activity 3 - Change values Physics
        boolean collisionX = false;
        boolean collisionY = false;

        if (ball.x + ball.SIZE > width) {
            ball.vx = -ball.vx / 2;
            ball.x = width - 1 - ball.SIZE;
            collisionX = true;
        }
        if (ball.x < 0) {
            ball.vx = -ball.vx / 2;
            ball.x = 1 + ball.x / 2;
            collisionX = true;
        }
        if (ball.y + ball.SIZE > height) {
            ball.vy *= -1;
            ball.y = height - 1 - ball.SIZE;
            collisionY = true;
        }
        if (ball.y < 0) {
            ball.vy *= -1;
            ball.y = ball.SIZE / 2f;
            collisionY = true;
        }

        if (collisionX && !lastCollisionX) {
            soundManager.playCollisionSound();
            lastCollisionX = true;
        } else if (collisionY && !lastCollisionY) {
            soundManager.playCollisionSound();
            lastCollisionY = true;
        }

        if (ball.x > 10 && ball.x + ball.SIZE < width - 10) {
            lastCollisionX = false;
        }
        if (ball.y > 10 && ball.y + ball.SIZE < height - 10) {
            lastCollisionY = false;
        }

        ball.vy += ball.GRAVITY;
        ball.vx += ball.ax;

        ball.x += ball.vx;
        ball.y += ball.vy;
    }

    private void triggerCoin() {
        for (Coin coin : coins) {
            if (coin != null && coin.isCollision(ball)) {
                points += coin.points;
                soundManager.playSound(6);
                coin.generatePosition(width, height, 50, 50);
            }
        }
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
            triggerCoin();
            invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

}
