package com.estimote.proximitycontent;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.estimote.proximitycontent.estimote.BeaconID;
import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetails;
import com.estimote.proximitycontent.estimote.EstimoteCloudBeaconDetailsFactory;
import com.estimote.proximitycontent.estimote.ProximityContentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.estimote.proximitycontent.FileUtils.DeleteFile;
import static com.estimote.proximitycontent.FileUtils.WriteJsonToFile;
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


    // ODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "com.estimote.proximitycontent.action.START";
    private static final String ACTION_STOP = "com.estimote.proximitycontent.action.STOP";
    private String filename;

    // ODO: Rename parameters
//    private static final String EXTRA_RANGE = "com.estimote.proximitycontent.extra.RANGE";
//    private static final String EXTRA_PARAM2 = "com.estimote.proximitycontent.extra.PARAM2";



    public MyProximityIntentService() {
        super("MyProximityIntentService");
        filename = "beacon.json";
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
                    JSONObject json = new JSONObject();

                    try {
                        json.put("name", beaconDetails.getBeaconName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    WriteJsonToFile(filename,json);
                    Log.d(TAG, "onContentChanged() returned: " + text);
                } else {
                    text = "No beacons in range.";
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
        DeleteFile(filename);
        Log.d(TAG, "handleActionStop() called and File Deleted");
    }


}
