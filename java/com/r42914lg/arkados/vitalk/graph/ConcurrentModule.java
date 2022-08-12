package com.r42914lg.arkados.vitalk.graph;

import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConcurrentModule {

    @Singleton
    @Provides
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(4);
    }

    @Singleton
    @Provides
    public Handler mainThreadHandler() {
        return HandlerCompat.createAsync(Looper.getMainLooper());
    }
}
