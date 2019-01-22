package com.estimote.proximitycontent;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyBeaconService extends Service {
    private static final String TAG = "MyBeaconService";

    static final String ACTION_STOP_SERVICE = "com.estimote.proximitycontent.STOP_SERVICE";
    public Notification notification;
    private MyFileObserver myFileObserver;

    public MyBeaconService() {
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");

        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            Log.d(TAG, "onStartCommand: Stop Service");
            stopSelf();
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Intent stopSelf = new Intent(this, MyBeaconService.class).setAction(ACTION_STOP_SERVICE);
        PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle("Beacon Service")
                .setContentText("Scanning for Beacons...")
                .setSmallIcon(R.drawable.ic_stat_beacon_custom)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_stop_black_24dp, "Stop", pStopSelf)
                /*.setContentIntent(pendingIntent)*/
                .build();
        startForeground(1, notification);

        myFileObserver = new MyFileObserver(MyApplication.DOWNLOADS_FOLDER, FileObserver.CREATE | FileObserver.DELETE);
        FileUtils.deleteFile(MyApplication.FILE_START_SCAN);
        FileUtils.deleteFile(FileUtils.filename);
        myFileObserver.startWatching();
        return START_STICKY;
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
        myFileObserver.stopWatching();
        FileUtils.deleteFile(MyApplication.FILE_START_SCAN);
        FileUtils.deleteFile(FileUtils.filename);
        ProximityContentManagerController.stopScan();
//        ProximityContentManagerController.destroyScan();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
