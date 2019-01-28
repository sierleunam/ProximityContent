package com.estimote.proximitycontent;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.estimote.proximitycontent.Services.MyBeaconService;

import org.json.JSONException;
import org.json.JSONObject;

import static com.estimote.proximitycontent.FileUtils.CreateDummyFile;
import static com.estimote.proximitycontent.FileUtils.filename;
import static com.estimote.proximitycontent.FileUtils.listfilename;
import static com.estimote.proximitycontent.FileUtils.writeJsonToFile;
import static com.estimote.proximitycontent.FileUtils.writeTextToFile;
//import static com.estimote.proximitycontent.FileUtils.CreateDummyFile;
//import static com.estimote.proximitycontent.FileUtils.writeJsonToFile;

public class Activity2 extends AppCompatActivity {

    private static final String TAG = "Activity2";
    Button btnStart, btnStop, btnSave, btnStartService, btnStopService, btnPlayBranco, btnPlayVerde, btnStopAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        btnStart = findViewById(R.id.btnStartId);
        btnStop = findViewById(R.id.btnStopId);
        btnSave = findViewById(R.id.buttonSaveId);
        btnStartService = findViewById(R.id.btnStartServiceId);
        btnStopService = findViewById(R.id.btnStopService);
        btnPlayBranco = findViewById(R.id.btn_playBrancoId);
        btnPlayVerde = findViewById(R.id.btn_playVerdeId);
        btnStopAudio = findViewById(R.id.btn_stopAudioId);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startScan();
                CreateDummyFile();

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.deleteFile(filename);
                FileUtils.deleteFile(MyApplication.FILE_START_SCAN);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == 1) {
                    try {
                        int obj = writeJsonToFile("beacon.json", new JSONObject("{\"title\":\"Titulo\"}"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onClick: JSON File Created");
                }
            }
        });

        btnPlayBranco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent audioIntent = new Intent(Activity2.this, MyAudioService.class);
//                audioIntent.putExtra(FILENAME_EXTRA,"start.branco.wav");
//                audioIntent.setAction(MyAudioService.ACTION_PLAY_AUDIO);
//                startService(audioIntent);

                writeTextToFile("start.branco.wav");
            }
        });

        btnPlayVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent audioIntent = new Intent(Activity2.this, MyAudioService.class);
//                audioIntent.putExtra(FILENAME_EXTRA,"start.verde.wav");
//                audioIntent.setAction(MyAudioService.ACTION_PLAY_AUDIO);
//                startService(audioIntent);

                writeTextToFile("start.verde.wav");
            }
        });

        btnStopAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent audioIntent = new Intent(Activity2.this, MyAudioService.class);
//                audioIntent.setAction(MyAudioService.ACTION_STOP_AUDIO);
//                startService(audioIntent);
                FileUtils.deleteFile(MyApplication.FILE_AUDIO_PLAY);
            }
        });
    }

    public void stopService(View view) {
        Intent serviceIntent = new Intent(this, MyBeaconService.class);
        stopService(serviceIntent);
        deleteFile(MyApplication.FILE_START_SCAN);
        deleteFile(filename);
        deleteFile(listfilename);
        Log.d(TAG, "stopService() called with: view = [" + view + "]");
    }

    public void startService(View view) {
        Intent serviceIntent = new Intent(Activity2.this, MyBeaconService.class);
//        serviceIntent.putExtra("config", 1);
        startService(serviceIntent);
    }
}
