package com.estimote.proximitycontent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import org.json.JSONException;
import org.json.JSONObject;

import static com.estimote.proximitycontent.FileUtils.DeleteFile;
import static com.estimote.proximitycontent.FileUtils.WriteJsonToFile;
import static com.estimote.proximitycontent.MyApplication.proximityContentManager;
import static com.estimote.proximitycontent.ProximityContentManagerController.startScan;
import static com.estimote.proximitycontent.ProximityContentManagerController.stopScan;


//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String FILE_START_SCAN = "start.scan";
    private static final String FILE_STOP_SCAN = "stop.scan";

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    MyFileObserver myFileObserver = null;

    Button btnStart, btnStop, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);


        btnStart = findViewById(R.id.btnStartId);
        btnStop = findViewById(R.id.btnStopId);
        btnSave = findViewById(R.id.buttonSaveId);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopScan();

                DeleteFile(FileUtils.filename);
                DeleteFile(FILE_START_SCAN);
                DeleteFile(FILE_STOP_SCAN);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                try {
                    int obj = WriteJsonToFile("beacon.json", new JSONObject("{\"title\":\"Titulo\"}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onClick: JSON File Created");
            }
        });
//        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
        }


        myFileObserver = new MyFileObserver(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).toString(),
                FileObserver.DELETE);
        myFileObserver.startWatching();
        Log.d(TAG, "onResume() called and File watched started");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        proximityContentManager.stopContentUpdates();
        proximityContentManager.destroy();

        myFileObserver.stopWatching();
        Log.d(TAG, "onDestroy() called and stopped file watch");
    }
}
