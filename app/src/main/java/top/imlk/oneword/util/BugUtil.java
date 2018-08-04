package top.imlk.oneword.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;

import static top.imlk.oneword.util.OneWordFileStation.BASE_FILES_PATH;

/**
 * Created by imlk on 2018/7/30.
 */
public class BugUtil {

    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//    private static String logFileName = "module_log.txt";

    private static final String TAG = BugUtil.class.getSimpleName();


    private static boolean hasXP = false;

    static {
        try {
            XposedBridge.log(BugUtil.class.getSimpleName() + ":found XP");
            hasXP = true;
        } catch (Throwable e) {
            Log.i(TAG, ":there is No XP");
        }
    }

    public static String printAndSaveCrashThrow2File(Throwable th) {

        if (hasXP) {
            XposedBridge.log(th);
        } else {
            Log.e(TAG, "save Crashing", th);
        }

        if (BASE_FILES_PATH == null) {
            if (hasXP) {
                XposedBridge.log("BASE_FILES_PATH = null, unable to save log to file !!!!!");
            } else {
                Log.e(TAG, "BASE_FILES_PATH = null, unable to save log to file !!!!!");
            }

            return null;
        }


        StringBuffer sb = new StringBuffer();

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        th.printStackTrace(printWriter);
        Throwable cause = th.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);


        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String logTime = time + "-" + timestamp;
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String path = BASE_FILES_PATH + "/crash_log/";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            path = path + logTime + ".log";
            FileOutputStream fos = new FileOutputStream(path, true);
            fos.write(logTime.getBytes());
            fos.write("\n\n\n".getBytes());
            fos.write(sb.toString().getBytes());
//            fos.write("\n\n\n".getBytes());
            fos.close();

            return path;
//            }
        } catch (Exception e) {
            Log.e(BugUtil.class.getSimpleName(), "an error occured while writing file...", e);
        }
        return null;
    }


    public static String printAndSaveCrashThrowAndMsg2File(Throwable th, String msg) {

        th.addSuppressed(new Throwable("other info: " + msg));

        return printAndSaveCrashThrow2File(th);

    }

}
