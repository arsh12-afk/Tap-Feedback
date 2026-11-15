package com.example.tapfeedback;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        SeekBar sizeSeek = findViewById(R.id.sizeSeek);
        TextView sizeLabel = findViewById(R.id.sizeLabel);
        int saved = getSharedPreferences("prefs",0).getInt("size",80);
        sizeSeek.setProgress(saved - 20);
        sizeLabel.setText("Circle size: " + saved + " px");
        sizeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s,int p,boolean user) {
                int size = p + 20;
                sizeLabel.setText("Circle size: " + size + " px");
                getSharedPreferences("prefs",0).edit().putInt("size",size).apply();
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
        findViewById(R.id.startButton).setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            } else {
                startService(new Intent(this, OverlayService.class));
                startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
            }
        });
    }
}
