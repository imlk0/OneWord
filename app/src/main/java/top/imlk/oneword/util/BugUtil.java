package top.imlk.oneword.util;

import android.os.Process;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;
import top.imlk.oneword.BuildConfig;


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


    public static void printThrowable(Throwable e) {

        if (hasXP) {
            XposedBridge.log(e);
        } else {
            CrashReport.postCatchedException(e);
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public static void printMessage(String str) {
        if (hasXP) {
            XposedBridge.log(str);
        } else {
            Log.e(TAG, str);
        }
    }

    public static String printAndSaveCrashThrow2File(Throwable th) {

        printThrowable(th);

        String base_file_path;
        if ((base_file_path = OneWordFileStation.getBaseFilesPath()) == null) {
            printMessage("BASE_FILES_PATH = null, unable to save log to file !!!!!");

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

            String path = base_file_path + "/crash_log/";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            path = path + logTime + ".log";
            FileOutputStream fos = new FileOutputStream(path, true);
            fos.write(logTime.getBytes());
            fos.write("\n".getBytes());
            fos.write(("FLAVOR：" + BuildConfig.FLAVOR + "\n").getBytes());
            fos.write(("VERSION_NAME：" + BuildConfig.VERSION_NAME + "\n").getBytes());
            fos.write(("VERSION_CODE：" + BuildConfig.VERSION_CODE + "\n").getBytes());
            fos.write(("ClassLoader：" + BugUtil.class.getClassLoader() + Process.myUid() + "\n").getBytes());
            fos.write(("Thread：" + Thread.currentThread() + "\n").getBytes());
            fos.write("\n\n".getBytes());
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
