package com.estimote.proximitycontent;

import android.os.Environment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = "FileUtils";
    private static String fileName;
    private static JSONObject obj;

    public static File getPublicDownloadsStorageFile(String fileName) {

        File file = null;
        if (isExternalStorageWritable()) {
            // Get the directory for the user's public downloads directory.
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);

        }

        return file;
    }

    public static int WriteJsonToFile(String fileName, @NotNull JSONObject obj) {
        FileUtils.fileName = fileName;
        FileUtils.obj = obj;
        File file = getPublicDownloadsStorageFile(fileName);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(obj.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;

    }

    public static boolean DeleteFile(String fileName) {
        File file = getPublicDownloadsStorageFile(fileName);
        return file.delete();

    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}