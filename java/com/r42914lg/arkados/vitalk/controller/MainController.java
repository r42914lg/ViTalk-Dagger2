package com.r42914lg.arkados.vitalk.controller;

import static com.r42914lg.arkados.vitalk.ViTalkConstants.FILE_NAME;
import static com.r42914lg.arkados.vitalk.ViTalkConstants.LOG;
import static com.r42914lg.arkados.vitalk.ViTalkConstants.URL_RESULT;
import static com.r42914lg.arkados.vitalk.model.ViTalkVM.ASK_RATINGS_ACTION_CODE;
import static com.r42914lg.arkados.vitalk.model.ViTalkVM.AUDIO_ACTION_CODE;
import static com.r42914lg.arkados.vitalk.model.ViTalkVM.GOOGLE_SIGNIN_ACTION_CODE;
import static com.r42914lg.arkados.vitalk.model.ViTalkVM.PREVIEW_ACTION_CODE;
import static com.r42914lg.arkados.vitalk.model.ViTalkVM.SHARE_ACTION_CODE;
import static com.r42914lg.arkados.vitalk.model.ViTalkVM.UPLOAD_LOCAL_VIDEO_CODE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.r42914lg.arkados.vitalk.R;
import com.r42914lg.arkados.vitalk.model.LocalVideo;
import com.r42914lg.arkados.vitalk.model.ViTalkVM;
import com.r42914lg.arkados.vitalk.ui.ICoreFrame;
import com.r42914lg.arkados.vitalk.utils.AudioProvider;

import java.net.URLConnection;
import java.text.MessageFormat;


public class MainController {
    public static final String TAG = "LG> ViTalkPresenterMain";

    private class GoogleSignInHelper {
        private GoogleSignInClient mGoogleSignInClient;
        private ActivityResultLauncher<Long> mAccountPicker;

        public GoogleSignInHelper() {
            init();
        }

        public void init() {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
            mGoogleSignInClient = GoogleSignIn.getClient(appCompatActivity, gso);
            mAccountPicker = appCompatActivity.registerForActivityResult(
                    new ActivityResultContract<Long, Intent>() {
                        @NonNull
                        @Override
                        public Intent createIntent(@NonNull Context context, Long input) {
                            return mGoogleSignInClient.getSignInIntent();
                        }

                        @Override
                        public Intent parseResult(int resultCode, @Nullable Intent intent) {
                            return intent;
                        }
                    },
                    new ActivityResultCallback<Intent>() {
                        @Override
                        public void onActivityResult(Intent intent) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                            GoogleSignInAccount account = null;
                            try {
                                account = task.getResult(ApiException.class);
                                // Signed in successfully, show authenticated UI.
                            } catch (ApiException e) {
                                // The ApiException status code indicates the detailed failure reason.
                                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                                if (LOG) {
                                    Log.d(TAG, ".initMainActivity.GoogleSignIn signInResult:failed code = " + e.getStatusCode());
                                    e.printStackTrace();
                                }
                            }
                            onSignIn(account);
                        }
                    }
            );
        }

        public void launchSignIn() {
            mAccountPicker.launch(999L);
        }
    }

    private final ViTalkVM viTalkVM;
    private final AppCompatActivity appCompatActivity;
    private GoogleSignInHelper googleSignInHelper;


    public MainController(AppCompatActivity appCompatActivity, ViTalkVM viTalkVM) {
        this.viTalkVM = viTalkVM;
        this.appCompatActivity = appCompatActivity;

        if (LOG) {
            Log.d(TAG, ".ViTalkPresenterMain instance created");
        }
    }

    public void initMainActivity(ICoreFrame iCoreFrame) {

        viTalkVM.getFavoritesLiveData().observe(appCompatActivity, event -> iCoreFrame.renderMenuItems());
        viTalkVM.getToastLiveData().observe(appCompatActivity, s -> showToast(s, Toast.LENGTH_LONG));
        viTalkVM.getTerminateDialogEventMutableLiveData().observe(appCompatActivity, terminateDialogEvent -> showTerminateDialog(terminateDialogEvent.getTitle(), terminateDialogEvent.getText()));
        viTalkVM.getGoogleSignInLiveData().observe(appCompatActivity, iCoreFrame::updateUI);
        viTalkVM.getLiveToolBarTitle().observe(appCompatActivity, s -> appCompatActivity.getSupportActionBar().setTitle(s));
        viTalkVM.getShowFabLiveData().observe(appCompatActivity, iCoreFrame::showFab);
        viTalkVM.getShowTabOneMenuItems().observe(appCompatActivity, iCoreFrame::showTabOneMenuItems);

        viTalkVM.getProgressBarFlagLiveData().observe(appCompatActivity, aBoolean -> {
            if (aBoolean) {
                iCoreFrame.startProgressOverlay();
            } else {
                iCoreFrame.stopProgressOverlay();
            }
        });

        viTalkVM.getUiActionMutableLiveData().observe(appCompatActivity, viTalkUIAction -> {
            switch (viTalkUIAction) {
                case UPLOAD_LOCAL_VIDEO_CODE:
                    startVideoUpload(viTalkVM.getLocalVideo());
                    break;
                case SHARE_ACTION_CODE:
                    processShareRequest(viTalkVM.getYoutubeVideoIdToShareOrPreview());
                    break;
                case PREVIEW_ACTION_CODE:
                    processPreviewRequest(viTalkVM.getYoutubeVideoIdToShareOrPreview());
                    break;
                case ASK_RATINGS_ACTION_CODE:
                    ((ICoreFrame) appCompatActivity).askRatings();
                    break;
                case GOOGLE_SIGNIN_ACTION_CODE:
                    doGoogleSignIn();
                    break;
                case AUDIO_ACTION_CODE:
                    Intent playAudioIntent = new Intent();
                    Uri uri = Uri.parse(AudioProvider.CONTENT_URI + "/" + FILE_NAME);

                    playAudioIntent.setAction(Intent.ACTION_SEND);
                    playAudioIntent.setType(URLConnection.guessContentTypeFromName(uri.toString()));
                    playAudioIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    appCompatActivity.startActivity(playAudioIntent);
                    break;
                default:
                    throw new IllegalStateException("Wrong UI Action received in observer");
            }
        });

        googleSignInHelper = new GoogleSignInHelper();

        if (LOG) {
            Log.d(TAG, ".initMainActivity");
        }
    }

    public void handleIntent(Intent newIntent) {
        String type = newIntent.getType();

        if (type != null) {
            if (type.equals("YOUTUBE_LINK")) {
                Bundle extras = newIntent.getExtras();
                if (extras != null) {
                    String youTubeId = parseYouTubeId(extras.getString(Intent.EXTRA_TEXT));
                    viTalkVM.addYouTubeIdToWorkItems(youTubeId);
                }
            }
            if (type.equals("VIDEO_URI")) {
                Uri videoUri = (Uri) newIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (videoUri != null) {
                    startVideoUpload(videoUri);
                }
            }
            newIntent.setType("CONSUMED");
        }

        checkGoogleSignInUser();

        if (LOG) {
            Log.d(TAG, ".handleIntent");
        }
    }

    public void checkGoogleSignInUser() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(appCompatActivity);
        onSignIn(account);
    }

    public void doGoogleSignIn() {
        if (LOG) {
            Log.d(TAG, ".doGoogleSignIn --> launching account picker");
        }
        googleSignInHelper.launchSignIn();
    }

    public boolean noGoogleSignIn() {
        return viTalkVM.getGoogleSignInLiveData().getValue() ==  null;
    }

    public void onSignIn(GoogleSignInAccount credential) {
        if (credential != null)  {
            viTalkVM.setGoogleAccount(credential);
        }
    }

    public void showToast(String text, int duration) {
        Toast.makeText(appCompatActivity, text, duration).show();
    }

    public void processPreviewRequest(String youTubeId) {
        if (LOG) {
            Log.d(TAG, ".processPreviewRequest");
        }

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(MessageFormat.format(URL_RESULT, youTubeId, viTalkVM.getGoogleAccId())));
        appCompatActivity.startActivity(i);
    }

    public void processShareRequest(String youTubeId) {
        if (LOG) {
            Log.d(TAG, ".processShareRequest");
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, MessageFormat.format(URL_RESULT, youTubeId, viTalkVM.getGoogleAccId()));
        sendIntent.setType("text/plain");
        appCompatActivity.startActivity(sendIntent);
    }

    public void startVideoUpload(LocalVideo localVideoSelected) {
        startVideoUpload(localVideoSelected.uri);
    }

    public void startVideoUpload(Uri videoUri) {
        if (LOG) {
            Log.d(TAG, ".startVideoUpload --> " + videoUri.getEncodedPath());
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("video/3gpp");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Title_text");
        intent.putExtra(Intent.EXTRA_STREAM,videoUri);
        appCompatActivity.startActivity(Intent.createChooser(intent,appCompatActivity.getString(R.string.chooser_text)));
    }

    private void showTerminateDialog(String title, String text) {
        AlertDialog dialog = new AlertDialog.Builder(appCompatActivity).create();
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appCompatActivity.finish();
                dialog.cancel();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                appCompatActivity.finish();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private String parseYouTubeId(String url) {
        String afterSlash = url.substring(url.lastIndexOf("/") + 1);
        int index = afterSlash.indexOf("?");

        return index == -1 ? afterSlash : afterSlash.substring(0, index);
    }
}

