package com.r42914lg.arkados.vitalk.graph;

import com.r42914lg.arkados.vitalk.model.DataLoaderListenerImpl;
import com.r42914lg.arkados.vitalk.model.IDataLoaderListener;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface DataLoaderListenerModule {

    @Singleton
    @Binds
    IDataLoaderListener getDataLoaderListener(DataLoaderListenerImpl impl);
}
