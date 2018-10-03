package top.imlk.oneword.systemui.injecter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by imlk on 2018/9/22.
 */
public class RomHelper {


    private static final String MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String CMD_GETPROP = "getprop";

    private static Boolean isMIUI;


    public synchronized static boolean isMIUI() {

        if (isMIUI == null) {
            String value = readProp(MIUI_VERSION_NAME);

            if (value == null || "".equals(value.trim())) {
                isMIUI = false;
            } else {
                isMIUI = true;
            }
        }

        return isMIUI;
    }

    private static String readProp(String propName) {// andorid 8 以上限制build.prop文件读取
        InputStream inputStream = null;
        try {
            inputStream = Runtime.getRuntime().exec(new String[]{CMD_GETPROP, propName}).getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }

            return byteArrayOutputStream.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }

        return null;
    }
}
