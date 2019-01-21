package com.estimote.proximitycontent;


import android.os.FileObserver;
import android.util.Log;

import static com.estimote.proximitycontent.FileUtils.deleteFile;
import static com.estimote.proximitycontent.FileUtils.filename;
import static com.estimote.proximitycontent.MyApplication.FILE_START_SCAN;


public class MyFileObserver extends FileObserver {
    private final String TAG = MyFileObserver.this.getClass().getSimpleName();

    MyFileObserver(String path, int mask) {
        super(path, mask);

    }

    @Override
    public void onEvent(int event, String path) {
        if ((path != null) && (path.substring(path.lastIndexOf("/") + 1).equals(FILE_START_SCAN))) {

            switch (event) {
                case FileObserver.MODIFY:
                case FileObserver.ACCESS:
                case FileObserver.CREATE:
                    Log.d(TAG, "onEvent: Created/Modified: " + path);
                    ProximityContentManagerController.startScan();
                    break;
                case FileObserver.DELETE:
                    ProximityContentManagerController.stopScan();
                    FileUtils.deleteFile(FILE_START_SCAN);
                    deleteFile(filename);
                    Log.d(TAG, "onEvent: Deleted: " + path);
                    break;
                default:
                    Log.d(TAG, "onEvent() called with: event = [" + event + "], path = [" + path + "]");

            }
        }
    }
}
