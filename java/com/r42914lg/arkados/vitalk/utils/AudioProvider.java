package com.r42914lg.arkados.vitalk.utils;

import static com.r42914lg.arkados.vitalk.ViTalkConstants.FILE_NAME;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class AudioProvider extends ContentProvider {
    public static final Uri CONTENT_URI= Uri.parse("content://com.r42914lg.arkados.vitalk_result");

    @Override
    public boolean onCreate() {
        File f = new File(getContext().getExternalCacheDir(), FILE_NAME);
        boolean b = f.exists();
        return f.exists();
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {

        File f = new File(getContext().getExternalCacheDir(), uri.getPath());
        if(!f.exists())
            throw new FileNotFoundException();

        ParcelFileDescriptor afd = null;
        try {
            afd = ParcelFileDescriptor.open(f, ParcelFileDescriptor.parseMode(mode));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return afd;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return URLConnection.guessContentTypeFromName(uri.toString());
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        throw new RuntimeException("Operation not supported");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("Operation not supported");
    }
}
