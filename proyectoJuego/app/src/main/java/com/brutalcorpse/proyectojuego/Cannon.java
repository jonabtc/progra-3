package com.brutalcorpse.proyectojuego;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by brutalcorpse on 24/04/17.
 */

public class Cannon {
    private int baseRadius;
    private int barrelLength;
    private Point barrelEnd = new Point();
    private double barrelAngle;
    private Cannonball cannonball;
    private Paint paint = new Paint();
    private CannonView view;

    public Cannon(CannonView view, int baseRadius, int barrelLength,
                  int barrelWidth) {
        this.view = view;
        this.baseRadius = baseRadius;
        this.barrelLength = barrelLength;
        paint.setStrokeWidth(barrelWidth); // set width of barrel
        paint.setColor(Color.BLACK); // Cannon's color is Black
        align(Math.PI / 2); // Cannon barrel facing straight right
    }

    private void align(double v) {
    }
}
