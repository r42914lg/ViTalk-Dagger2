package com.r42914lg.arkados.vitalk.ui;

import static com.r42914lg.arkados.vitalk.ViTalkConstants.LOG;
import static com.r42914lg.arkados.vitalk.ViTalkConstants.VIDEO_GALLERY_ADAPTER_COLUMNS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.r42914lg.arkados.vitalk.R;
import com.r42914lg.arkados.vitalk.databinding.FragmentThirdBinding;
import com.r42914lg.arkados.vitalk.graph.MyViewModelFactory;
import com.r42914lg.arkados.vitalk.model.LocalVideo;
import com.r42914lg.arkados.vitalk.model.ViTalkVM;
import com.r42914lg.arkados.vitalk.model.VideoGalleryScanner;
import com.r42914lg.arkados.vitalk.controller.ThirdFragmentController;

import javax.inject.Inject;

public class ThirdFragment extends Fragment {
    public static final String TAG = "LG> ThirdFragment";

    private FragmentThirdBinding binding;

    @Inject
    MyViewModelFactory myViewModelFactory;

    private ViTalkVM viTalkVM;

    ThirdFragmentController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).getActivityComponent()
                .inject(this);

        viTalkVM = new ViewModelProvider(getActivity(), myViewModelFactory).get(ViTalkVM.class);
        controller =  new ThirdFragmentController(this, viTalkVM);

        binding = FragmentThirdBinding.inflate(inflater, container, false);

        binding.videoGalleryRecycler.setLayoutManager(new GridLayoutManager(container.getContext(), VIDEO_GALLERY_ADAPTER_COLUMNS));
        VideoGalleryScanner videoGalleryScanner = new VideoGalleryScanner(getActivity());
        VideoGalleryAdapter videoGalleryAdapter = new VideoGalleryAdapter(videoGalleryScanner, this);
        binding.videoGalleryRecycler.setAdapter(videoGalleryAdapter);

        if (LOG) {
            Log.d(TAG, ".onCreateView");
        }

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller.initGalleryChooserFragment();

        if (LOG) {
            Log.d(TAG, ".onViewCreated");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        if (LOG) {
            Log.d(TAG, ".onDestroyView");
        }
    }

    public void startVideoUploadNavigateToFirst(LocalVideo localVideoSelected) {
        NavHostFragment.findNavController(ThirdFragment.this).navigate(R.id.action_ThirdFragment_to_FirstFragment);
        viTalkVM.setLocalVideo(localVideoSelected);
        viTalkVM.getUiActionMutableLiveData().setValue(ViTalkVM.UPLOAD_LOCAL_VIDEO_CODE);

        if (LOG) {
            Log.d(TAG, ".startVideoUploadNavigateToFirst");
        }
    }
}