package com.r42914lg.arkados.vitalk.graph;

import android.app.Application;
import android.os.Handler;

import com.r42914lg.arkados.vitalk.model.DataLoaderListenerImpl;
import com.r42914lg.arkados.vitalk.model.FirebaseHelper;
import com.r42914lg.arkados.vitalk.model.IDataLoaderListener;
import com.r42914lg.arkados.vitalk.model.LocalStorageHelper;
import com.r42914lg.arkados.vitalk.model.ViTalkVM;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {ConcurrentModule.class, DataLoaderListenerModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        AppComponent build();

        @BindsInstance
        Builder application(Application application);
    }

    Application exposeApplication();
    ExecutorService exposeExecutorService();
    Handler exposeHandler();
    IDataLoaderListener exposeDataLoaderListener();

    MyViewModelFactory exposeFactory();
    ViTalkVM exposeVM();
    FirebaseHelper exposeFirebase();
    LocalStorageHelper exposeStorage();
}
