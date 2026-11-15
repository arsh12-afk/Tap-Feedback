package com.example.tapfeedback;

import android.app.Service;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class OverlayService extends Service {
    static OverlayService instance;
    WindowManager wm;
    IndicatorView indicator;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        indicator = new IndicatorView();
        int type = Build.VERSION.SDK_INT >= 26 ?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
            type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        wm.addView(indicator, lp);
    }

    void showAt(float x, float y) { if (indicator != null) indicator.showAt(x,y); }
    @Override public int onStartCommand(Intent i,int f,int id) { return START_STICKY; }
    @Override public IBinder onBind(Intent i) { return null; }
    @Override public void onDestroy() {
        instance = null;
        if (indicator != null) wm.removeView(indicator);
        indicator = null;
        super.onDestroy();
    }

    class IndicatorView extends View {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float x=-1000, y=-1000;
        Handler handler = new Handler();
        IndicatorView() { super(OverlayService.this); paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(4f); }
        void showAt(float px,float py) {
            x=px; y=py; invalidate();
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> { x=-1000; invalidate(); }, 320);
        }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            if (x < 0) return;
            int diameter = getSharedPreferences("prefs",0).getInt("size",80);
            c.drawCircle(x,y,diameter/2f,paint);
        }
    }
}
