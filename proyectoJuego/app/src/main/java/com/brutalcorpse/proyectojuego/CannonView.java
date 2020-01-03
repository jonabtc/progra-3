package com.brutalcorpse.proyectojuego;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by brutalcorpse on 24/04/17.
 */

class CannonView extends SurfaceView implements SurfaceHolder.Callback{

    public CannonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void stopGame() {
    }

    public void releaseResources() {
    }
}
