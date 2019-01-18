package com.estimote.proximitycontent;


import android.os.FileObserver;
import android.util.Log;


public class MyFileObserver extends FileObserver {
    private final String TAG = MyFileObserver.this.getClass().getSimpleName();
    private String absPath /*= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)*/;

    MyFileObserver(String path, int mask) {
        super(path, mask);
        absPath = path;

    }

    @Override
    public void onEvent(int event, String path) {
        if (path != null) {
            Log.d(TAG, "onEvent() called with: event = [" + event + "], path = [" + path + "]");
        }
    }
}
