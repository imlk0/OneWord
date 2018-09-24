package top.imlk.oneword.systemui.injecter;

import android.os.Environment;
import android.os.SystemProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Properties;

/**
 * Created by imlk on 2018/9/22.
 */
public class RomHelper {

    private static WeakReference<Properties> propertiesWeakReference;

    public synchronized static boolean isMIUI() {
        return getProperties().contains("ro.miui.ui.version.name");
    }


    private synchronized static Properties getProperties() {

        Properties properties;
        if (propertiesWeakReference == null || (properties = propertiesWeakReference.get()) == null) {
            properties = new Properties();


            try {
                properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));

            } catch (IOException e) {
                e.printStackTrace();
            }

            propertiesWeakReference = new WeakReference<>(properties);
        }
        return properties;
    }
}
