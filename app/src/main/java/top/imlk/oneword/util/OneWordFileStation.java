package top.imlk.oneword.util;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import top.imlk.oneword.bean.WordBean;

/**
 * Created by imlk on 2018/8/4.
 */

/**
 * 一言文件存储站
 */


public class OneWordFileStation {

    private static final String ONEWORD_FILE_NAME = "oneword.json";

    protected static String BASE_FILES_PATH;

    static {
        try {
            BASE_FILES_PATH = ActivityThread.currentApplication().createPackageContext("top.imlk.oneword", Context.CONTEXT_IGNORE_SECURITY).getExternalFilesDir(null).getAbsolutePath();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void saveOneWordJSON(WordBean wordBean) {

        if (wordBean == null) {
            return;
        }

        File files_dir = new File(BASE_FILES_PATH);
        if (!files_dir.exists()) {
            files_dir.mkdirs();
        }

        File oneword_json_file = new File(files_dir, ONEWORD_FILE_NAME);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(oneword_json_file);

            fileOutputStream.write(new Gson().toJson(wordBean, WordBean.class).getBytes());

        } catch (java.io.IOException e) {
            BugUtil.printAndSaveCrashThrow2File(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    BugUtil.printAndSaveCrashThrow2File(e);
                }
            }
        }

    }

    public static WordBean readOneWordJSON() {
        File files_dir = new File(BASE_FILES_PATH);
        if (!files_dir.exists()) {
            return null;
        }

        File oneword_json_file = new File(files_dir, ONEWORD_FILE_NAME);

        if (!oneword_json_file.exists()) {
            return null;
        }

        WordBean wordBean = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(oneword_json_file);

            byte[] bytes = new byte[((int) oneword_json_file.length())];

            fileInputStream.read(bytes);

            String json = new String(bytes);

            wordBean = new Gson().fromJson(json, WordBean.class);

        } catch (java.io.IOException e) {
            BugUtil.printAndSaveCrashThrow2File(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    BugUtil.printAndSaveCrashThrow2File(e);
                }
            }
        }

        return wordBean;
    }


}
