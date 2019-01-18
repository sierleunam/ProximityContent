package com.estimote.proximitycontent;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetails;
import com.estimote.proximitycontent.estimote.ProximityContentManager;

import static com.estimote.proximitycontent.FileUtils.DeleteFile;
import static com.estimote.proximitycontent.MyApplication.beaconName;
import static com.estimote.proximitycontent.MyApplication.proximityContentManager;


public class MyProximityService extends Service {
    private static final String TAG = "MyProxmityService";
    private static final String FILE_STOP_SCAN = "stop.scan";
    private static final String FILE_START_SCAN = "start.scan";
    private static final String ACTION_START = "com.estimote.proximitycontent.action.START";
    private static final String ACTION_STOP = "com.estimote.proximitycontent.action.STOP";
    private BroadcastReceiver yourReceiver;

    public MyProximityService() {
    }

    /**
     * Starts this service to perform action Scan with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context
     * @see
     */

    public static void startActionScan(Context context) {
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;

                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
//                    text = "You're in " + beaconDetails + "'s range!";
                    beaconName = beaconDetails.getBeaconName();
                } else {
                    text = "No beacons in range.";
                    beaconName = "UNKNOWN";
                    Log.d(TAG, "onContentChanged() returned: " + text);
                }

            }
        });

        proximityContentManager.startContentUpdates();
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see
     */
    public static void stopActionScan(Context context) {
        proximityContentManager.stopContentUpdates();

        DeleteFile(FileUtils.filename);
        DeleteFile(FILE_START_SCAN);
        DeleteFile(FILE_STOP_SCAN);

        Log.d(TAG, "stopActionScan() called with: context = [" + context + "]");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION_START);
        theFilter.addAction(ACTION_STOP);
        this.yourReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_START))
                    startActionScan(context);
                else if (intent.getAction().equals(ACTION_STOP))
                    stopActionScan(context);

                Log.d(TAG, "onReceive() from service called with: context = [" + context + "], intent = [" + intent + "]");
            }
        };
        this.registerReceiver(this.yourReceiver, theFilter);
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

        this.unregisterReceiver(this.yourReceiver);
    }

}

