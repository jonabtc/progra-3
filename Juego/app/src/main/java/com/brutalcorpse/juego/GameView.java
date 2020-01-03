package com.brutalcorpse.juego;

import android.content.Context;
import android.view.SurfaceView;

/**
 * Created by brutalcorpse on 24/04/17.
 */

public class GameView extends SurfaceView {

    public GameView(Context baseContext) {
        super();
    }

}

class GameThread extends Thread{
    static final long FPS = 30;


    private GameView view;
    private boolean running = false;


    public GameThread(GameView view) {
        this.view = view;
    }

}