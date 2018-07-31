package top.imlk.oneword.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by imlk on 2018/7/30.
 */
public class BugUtil {

    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//    private static String logFileName = "module_log.txt";

    public static String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
/*        ErrorLogBean errorLogBean = new ErrorLogBean();
        errorLogBean.saveInfo((String) SPUtils.get(AppContext.getInstance(), "POLICE_ID", "")
                , (String) SPUtils.get(AppContext.getInstance(), "TOKEN", "")
                , sb.toString()
                , "崩溃记录");*/
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String logTime = time + "-" + timestamp;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                String path = Environment.getExternalStorageDirectory() + "/Download/oneword_crash_log/";

                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                path = path + logTime + ".log";
                FileOutputStream fos = new FileOutputStream(path, true);
                fos.write(logTime.getBytes());
                fos.write("\n\n\n".getBytes());
                fos.write(sb.toString().getBytes());
                fos.write("\n\n\n".getBytes());
                fos.close();

                return path;
            }
        } catch (Exception e) {
            Log.e(BugUtil.class.getSimpleName(), "an error occured while writing file...", e);
        }
        return null;
    }

}
