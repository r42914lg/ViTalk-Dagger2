package com.r42914lg.arkados.vitalk;

import android.app.Application;
import com.r42914lg.arkados.vitalk.graph.AppComponent;
import com.r42914lg.arkados.vitalk.graph.DaggerAppComponent;

public class ViTalkApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
