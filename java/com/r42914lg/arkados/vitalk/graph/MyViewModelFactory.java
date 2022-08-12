package com.r42914lg.arkados.vitalk.graph;

import static com.r42914lg.arkados.vitalk.ViTalkConstants.LOG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.r42914lg.arkados.vitalk.model.ViTalkVM;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class MyViewModelFactory implements ViewModelProvider.Factory {
    public static final String TAG = "LG> MyViewModelFactory";

    private final Provider<ViTalkVM> p_viTalkVM;

    @Inject
    public MyViewModelFactory(Provider<ViTalkVM> p_viTalkVM) {
        this.p_viTalkVM = p_viTalkVM;

        if (LOG) {
            Log.d(TAG, " instance created " + this);
        }
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewModel viewModel;
        if (modelClass == ViTalkVM.class) {
            viewModel = p_viTalkVM.get();
        } else{
            throw new RuntimeException("unsupported view model class: " + modelClass);
        }
        return (T) viewModel;
    }
}
