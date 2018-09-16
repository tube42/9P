package se.tube42.p9.android;

import android.app.Activity;
import android.os.Bundle;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


import se.tube42.p9.*;
import se.tube42.p9.logic.*;

public class MainActivity extends AndroidApplication {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        cfg.useWakelock = true;
        initialize(new P9(), cfg);
    }

    public void onPause()
    {
        super.onPause();
        ServiceProvider.saveAll();
        ServiceProvider.finish();
    }

    public void onDestroy()
    {
        super.onDestroy();
    }
}
