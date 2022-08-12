package com.r42914lg.arkados.vitalk.controller;

import static com.r42914lg.arkados.vitalk.ViTalkConstants.LOG;

import android.util.Log;

import androidx.fragment.app.Fragment;
import com.r42914lg.arkados.vitalk.model.ViTalkVM;


public class ThirdFragmentController {
    public static final String TAG = "LG> ViTalkPresenterThirdFragment";


    private final ViTalkVM viTalkVM;

    public ThirdFragmentController(Fragment fragment, ViTalkVM viTalkVM) {

        this.viTalkVM = viTalkVM;

        if (LOG) {
            Log.d(TAG, ".ViTalkPresenterThirdFragment instance created");
        }
    }

    public void initGalleryChooserFragment() {
        viTalkVM.getShowFabLiveData().setValue(false);
        viTalkVM.getShowTabOneMenuItems().setValue(false);

        if (LOG) {
            Log.d(TAG, ".initGalleryChooserFragment");
        }
    }
}
