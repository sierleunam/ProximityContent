package com.estimote.proximitycontent;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetails;
import com.estimote.proximitycontent.estimote.ProximityContentManager;

import java.util.Objects;

import static com.estimote.proximitycontent.FileUtils.DeleteFile;
import static com.estimote.proximitycontent.MyApplication.beaconName;
import static com.estimote.proximitycontent.MyApplication.proximityContentManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * ODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyProximityIntentService extends IntentService {

    private static final String TAG = "ProxmityIntentService";
    private BroadcastReceiver yourReceiver;

    // ODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "com.estimote.proximitycontent.action.START";
    private static final String ACTION_STOP = "com.estimote.proximitycontent.action.STOP";

    // ODO: Rename parameters
//    private static final String EXTRA_RANGE = "com.estimote.proximitycontent.extra.RANGE";
//    private static final String EXTRA_PARAM2 = "com.estimote.proximitycontent.extra.PARAM2";



    public MyProximityIntentService() {
        super("MyProximityIntentService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION_START);
        this.yourReceiver = new BroadcastReceiver() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(ACTION_START))
                    startActionScan(getApplicationContext());
                else if (Objects.requireNonNull(intent.getAction()).equals(ACTION_STOP))
                    stopActionScan(getApplicationContext());
                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");

            }
        };
        this.registerReceiver(this.yourReceiver, theFilter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.yourReceiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_RANGE);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionStart(/*param1, param2*/);
            } else if (ACTION_STOP.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_RANGE);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionStop(/*param1, param2*/);
            }
        }
    }


    /**
     * Starts this service to perform action Scan with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     * @param context
     */
    // TODO: Customize helper method
    public static void startActionScan(Context context) {
        Intent intent = new Intent(context, MyProximityIntentService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void stopActionScan(Context context) {
        Intent intent = new Intent(context, MyProximityIntentService.class);
        intent.setAction(ACTION_STOP);
//        intent.putExtra(EXTRA_RANGE, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Handle action Start in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStart() {
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;

                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;

                    text = "You're in " + beaconDetails + "'s range!";
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
     * Handle action Stop in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStop() {
        // TODO: Handle action Stop
        proximityContentManager.stopContentUpdates();

        try {
            DeleteFile(FileUtils.filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "handleActionStop() called and File Deleted");
    }


}
