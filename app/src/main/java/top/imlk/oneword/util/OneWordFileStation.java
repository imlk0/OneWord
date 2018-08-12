package top.imlk.oneword.util;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;

/**
 * Created by imlk on 2018/8/4.
 */

/**
 * 一言文件存储站
 */


public class OneWordFileStation {

    private static final String ONEWORD_FILE_NAME = "oneword.json";
    private static final String WORDVIEW_CONFIG_FILE_NAME = "wordview_config.json";

    protected static String BASE_FILES_PATH;

    static {
        try {
            BASE_FILES_PATH = ActivityThread.currentApplication().createPackageContext("top.imlk.oneword", Context.CONTEXT_IGNORE_SECURITY).getExternalFilesDir(null).getAbsolutePath();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void saveOneWordJSON(WordBean wordBean) {
        saveObjJsonAtBasePath(ONEWORD_FILE_NAME, wordBean, WordBean.class);
    }

    public static WordBean readOneWordJSON() {
        return readObjJsonAtBasePath(ONEWORD_FILE_NAME, WordBean.class);
    }


    public static void saveWordViewConfigJSON(WordViewConfig wordViewConfig) {
        saveObjJsonAtBasePath(WORDVIEW_CONFIG_FILE_NAME, wordViewConfig, WordViewConfig.class);
    }

    public static WordViewConfig readWordViewConfigJSON() {
        return readObjJsonAtBasePath(WORDVIEW_CONFIG_FILE_NAME, WordViewConfig.class);
    }


    private static <T> void saveObjJsonAtBasePath(String filename, T bean, Class<T> tClass) {

        if (bean == null) {
            return;
        }

        File files_dir = new File(BASE_FILES_PATH);
        if (!files_dir.exists()) {
            files_dir.mkdirs();
        }

        File json_file = new File(files_dir, filename);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(json_file);

            fileOutputStream.write(new Gson().toJson(bean, tClass).getBytes());

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

    private static <T> T readObjJsonAtBasePath(String fileName, Class<T> tClass) {
        File files_dir = new File(BASE_FILES_PATH);
        if (!files_dir.exists()) {
            return null;
        }

        File json_file = new File(files_dir, fileName);

        if (!json_file.exists()) {
            return null;
        }

        T bean = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(json_file);

            byte[] bytes = new byte[((int) json_file.length())];

            fileInputStream.read(bytes);

            String json = new String(bytes);

            if (TextUtils.isEmpty(json)) {
                return null;
            }

            bean = new Gson().fromJson(json, tClass);

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

        return bean;
    }


}
