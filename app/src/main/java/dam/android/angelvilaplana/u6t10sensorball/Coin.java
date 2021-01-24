package dam.android.angelvilaplana.u6t10sensorball;

import android.graphics.Color;

public class Coin {

    public float x;
    public float y;
    public final float diameter;
    public final int color;
    public final int points;

    public Coin(float x, float y, float diameter, int color, int points) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = color;
        this.points = points;
    }

    public Coin(float x, float y) {
        this.x = x;
        this.y = y;
        this.diameter = 20;
        this.color = Color.YELLOW;
        this.points = 5;
    }

    public Coin(int width, int height, int marginWidth, int marginHeight) {
        generatePosition(width, height, marginWidth, marginHeight);
        this.diameter = 20;
        this.color = Color.YELLOW;
        this.points = 5;
    }

    public boolean isCollision(Ball ball) {
        if (x + diameter / 2 > ball.x && x - diameter / 2 < ball.x + ball.SIZE) {
            return y + diameter / 2 > ball.y && y - diameter / 2 < ball.y + ball.SIZE;
        }
        return false;
    }

    public void  generatePosition(int width, int height, int marginWidth, int marginHeight) {
        x = (float) (Math.random()*((width-marginWidth) - marginWidth + 1) + marginWidth);
        y = (float) (Math.random()*((height-marginHeight) - marginHeight + 1) + marginHeight);
    }

}
