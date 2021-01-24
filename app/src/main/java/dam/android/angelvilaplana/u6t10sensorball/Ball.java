package dam.android.angelvilaplana.u6t10sensorball;

public class Ball {

    public final int SIZE = 150;
    public final  double GRAVITY = 0.8;
    public double ax;
    public double vx, vy;
    public double x, y;

    public Ball() {
        vx = 0;
        vy = 1;
        x = y = 0;
        ax = 0;
    }

}
