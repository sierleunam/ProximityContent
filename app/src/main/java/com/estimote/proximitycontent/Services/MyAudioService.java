package com.estimote.proximitycontent.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.estimote.proximitycontent.FileUtils;

import java.io.IOException;

public class MyAudioService extends Service {

    public final static String ACTION_PLAY_AUDIO = "com.estimote.proximitycontent.PLAY_AUDIO";
    public final static String ACTION_STOP_AUDIO = "com.estimote.proximitycontent.STOP_AUDIO";
    public final static String FILENAME_EXTRA = "filename";
    private static String AUDIO_FILE = "";
    private static final String TAG = "MyAudioService";
    MediaPlayer mp = new MediaPlayer();

    public MyAudioService() {
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filename = "";
        if (ACTION_PLAY_AUDIO.equals(intent.getAction()) /*&& startId <2*/) {
            filename = intent.getStringExtra(FILENAME_EXTRA);
            filename = FileUtils.getPublicDownloadsStorageFile(filename).toString();
            try {

                if (AUDIO_FILE.isEmpty() || !AUDIO_FILE.equals(filename)) {
                    AUDIO_FILE = filename;

                    mp.reset();
                    mp.setDataSource(filename);
                    mp.prepare();
                    mp.start();
                    Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (ACTION_STOP_AUDIO.equals(intent.getAction())) {
            if (mp != null && mp.isPlaying()) {
                MediaPlayer.TrackInfo[] ti = mp.getTrackInfo();
                Log.d(TAG, "onStartCommand: ACTION_STOP_AUDIO");
                AUDIO_FILE = "";
                mp.stop();
            }
        }
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "], Extra = [" + filename + "]");

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion() called with: mp = [" + mp + "]");
                AUDIO_FILE = "";
                mp.reset();
                mp.release();

                stopSelf();
            }
        });

        return START_NOT_STICKY;
    }


    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // ODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
