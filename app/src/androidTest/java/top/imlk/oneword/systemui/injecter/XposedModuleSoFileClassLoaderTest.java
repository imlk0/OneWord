package top.imlk.oneword.systemui.injecter;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by imlk on 2018/8/24.
 */
public class XposedModuleSoFileClassLoaderTest {

    @Test
    public void findLibrary() {
        XposedModuleSoFileClassLoader xposedModuleSoFileClassLoader = new XposedModuleSoFileClassLoader("", "", this.getClass().getClassLoader());

        xposedModuleSoFileClassLoader.findLibrary("c++_shared");

    }
}