package com.r42914lg.arkados.vitalk.model;

import static com.r42914lg.arkados.vitalk.ViTalkConstants.LOG;

import android.graphics.Bitmap;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;


@Singleton
public class DataLoaderListenerImpl implements IDataLoaderListener {
    public static final String TAG = "LG> DataLoaderListenerImpl";

    private final Provider<ViTalkVM> viTalkVM;

    @Inject
    public DataLoaderListenerImpl(Provider<ViTalkVM> viTalkVM) {
        if (LOG) {
            Log.d(TAG, " instance created " +  this);
        }

        this.viTalkVM = viTalkVM;
    }

    @Override
    public void callbackLoadImageFromURL(Bitmap youTubeImage, String youTubeId) {
        if (LOG) {
            Log.d(TAG, ".callbackLoadImageFromURL ID --> " + youTubeId);
        }

        viTalkVM.get().getImagesMap().put(youTubeId, youTubeImage);
        int positionInAdapter = viTalkVM.get().lookUpForPositionInAdapter(youTubeId);
        if (positionInAdapter >= 0) {
            viTalkVM.get().getInvalidateItemAtPositionLiveData().setValue(positionInAdapter);
        }
    }

    @Override
    public void callbackVideoTileReceived(String title, String youTubeId) {
        if (LOG) {
            Log.d(TAG, ".callbackVideoTileReceived ID --> " + youTubeId);
        }

        int index = viTalkVM.get().lookUpForIndexInList(youTubeId);
        if (index != -1) {
            viTalkVM.get().getWorkItemVideoList().get(index).title = title;

            int positionInAdapter = viTalkVM.get().lookUpForPositionInAdapter(youTubeId);
            if (positionInAdapter >= 0) {
                viTalkVM.get().getInvalidateItemAtPositionLiveData().setValue(positionInAdapter);
            }
        }
    }

    @Override
    public void callbackFirebaseAuthenticated() {
        if (LOG) {
            Log.d(TAG, ".callbackFirebaseAuthenticated");
        }

        viTalkVM.get().setFirebaseAuthenticated(true);
    }

    @Override
    public void onFirebaseUploadFailed(String fullPath) {
        if (LOG) {
            Log.d(TAG, ".onFirebaseUploadFailed to FULL PATH --> " + fullPath);
        }

        viTalkVM.get().getProgressBarFlagLiveData().setValue(false);
        viTalkVM.get().retryDialog();
    }

    @Override
    public void onFirebaseUploadFinished(String youTubeId) {
        if (LOG) {
            Log.d(TAG, ".onFirebaseUploadFinished ID --> " +  youTubeId);
        }

        viTalkVM.get().setRecordExistFlag(youTubeId, true);
        viTalkVM.get().storeWorkItems();
        viTalkVM.get().getProgressBarFlagLiveData().setValue(false);
        viTalkVM.get().notifyUIShowToastOnUploadFinished();
        viTalkVM.get().getFirebaseUploadFinishedLiveData().setValue(true);
    }
}
