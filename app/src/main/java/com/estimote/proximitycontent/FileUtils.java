package com.estimote.proximitycontent;

import android.os.Environment;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = "FileUtils";
    public static String filename = "beacon.json";

    private static File getPublicDownloadsStorageFile(String fileName) {

        File file = null;
        if (isExternalStorageWritable()) {
            // Get the directory for the user's public downloads directory.
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);

        } else
            Log.d(TAG, "getPublicDownloadsStorageFile: Folder not Writable!!");

        return file;
    }

    public static int WriteJsonToFile(String fileName, @NotNull JSONObject obj) {

        File file = getPublicDownloadsStorageFile(fileName);
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(obj.toString());
            Log.d(TAG, "WriteJsonToFile: " + obj.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;

    }

    static void deleteFile(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        if (file.exists()) {
            boolean st = file.delete();
            Log.d(TAG, "deleteFile() called with: fileName = [" + fileName + "], Deleting: " + file.toString());
        }


    }

    public static boolean CheckFile(String fileName) {
        File file = getPublicDownloadsStorageFile(fileName);
        return file.exists();
    }

    static void CreateDummyFile(String fileName) {
        File file = getPublicDownloadsStorageFile(fileName);
        try {
            boolean st = file.createNewFile();
            Log.d(TAG, "CreateDummyFile: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}
