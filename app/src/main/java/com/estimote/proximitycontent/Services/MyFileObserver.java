package com.estimote.proximitycontent.Services;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.FileObserver;
import android.util.Log;

import com.estimote.proximitycontent.FileUtils;
import com.estimote.proximitycontent.ProximityContentManagerController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;
import static com.estimote.proximitycontent.FileUtils.deleteFile;
import static com.estimote.proximitycontent.FileUtils.getPublicDownloadsStorageFile;
import static com.estimote.proximitycontent.MyApplication.FILE_AUDIO_PLAY;
import static com.estimote.proximitycontent.MyApplication.FILE_START_SCAN;
import static com.estimote.proximitycontent.Services.MyAudioService.FILENAME_EXTRA;


public class MyFileObserver extends FileObserver {
    private final String TAG = MyFileObserver.this.getClass().getSimpleName();

    MyFileObserver(String path, int mask) {
        super(path, mask);

    }

    @Override
    public void onEvent(int event, String path) {
        if (path != null) {
            String filename = path.substring(path.lastIndexOf("/") + 1);
            if (filename.equals(FILE_START_SCAN)) {
                switch (event) {
                    case FileObserver.MODIFY:
                        /*case FileObserver.ACCESS:*/
                    case FileObserver.CREATE:
                        Log.d(TAG, "onEvent: Created/Accessed/Modified: " + path);
                        ProximityContentManagerController.startScan();
                        break;
                    case FileObserver.DELETE:
                        ProximityContentManagerController.stopScan();
                        FileUtils.deleteFile(FILE_START_SCAN);
                        deleteFile(FileUtils.filename);
                        deleteFile(FileUtils.listfilename);
                        Log.d(TAG, "onEvent: Deleted: " + path);
                        break;
                    default:
                        Log.d(TAG, "onEvent() called with: event = [" + event + "], path = [" + path + "]");

                }
            } else if (filename.equals(FILE_AUDIO_PLAY)) {
                switch (event) {
                    case FileObserver.MODIFY:
                        /*case FileObserver.ACCESS:*/
                    case FileObserver.CREATE:
                        Log.d(TAG, "onEvent: Created/Modified: " + path);
                        FileInputStream is = null;
                        BufferedReader reader;

                        //Read file content (contains the filename to be played)
                        final File file = getPublicDownloadsStorageFile(filename);
                        if (file.exists()) {
                            try {
                                is = new FileInputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            reader = new BufferedReader(new InputStreamReader(is));
                            String line = null;
                            try {
                                line = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (line != null) {
                                Log.d(TAG, "onEvent() filename = [" + line + "]");

                                //prepare intent to be sent
                                Intent audioIntent = new Intent(getApplicationContext(), MyAudioService.class);
                                audioIntent.putExtra(FILENAME_EXTRA, line);
                                audioIntent.setAction(MyAudioService.ACTION_PLAY_AUDIO);

                                PendingIntent pendingIntent = PendingIntent
                                        .getService(getApplicationContext(), 0, audioIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                try {
                                    pendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "pending Intent = [" + pendingIntent.toString() + "]");
                            }
                        }
                        break;
                    case FileObserver.DELETE:
                        //Stop playback
                        Intent audioIntent = new Intent(getApplicationContext(), MyAudioService.class);
                        audioIntent.setAction(MyAudioService.ACTION_STOP_AUDIO);
                        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, audioIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }


                        Log.d(TAG, "onEvent: Deleted: " + path);
                        break;
                    default:
                        Log.d(TAG, "onEvent() called with: event = [" + event + "], path = [" + path + "]");
                }
            }
        }
    }
}
