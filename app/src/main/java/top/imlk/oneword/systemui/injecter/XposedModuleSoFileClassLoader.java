package top.imlk.oneword.systemui.injecter;

import android.os.Build;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;
import top.imlk.oneword.util.OneWordFileStation;

/**
 * Created by imlk on 2018/8/23.
 */
public class XposedModuleSoFileClassLoader extends PathClassLoader {

    private String proxyedApkPath;

    public XposedModuleSoFileClassLoader(String proxyedApkPath, String dexPath, ClassLoader parent) {
        super(dexPath, parent);
        this.proxyedApkPath = proxyedApkPath;
    }

    public XposedModuleSoFileClassLoader(String proxyedApkPath, String dexPath, String librarySearchPath, ClassLoader parent) {
        super(dexPath, librarySearchPath, parent);
        this.proxyedApkPath = proxyedApkPath;
    }


//    private static final String LIBS_DIR_NAME = "lib";
//    private static boolean loaded = false;

    @Override
    public String findLibrary(String name) {

        File apkFile = new File(proxyedApkPath);

        File libDir = new File(apkFile.getParentFile(), "lib");

        for (File abiDir : libDir.listFiles()) {
            File soFile = new File(abiDir, System.mapLibraryName(name));
            if (soFile.exists()) {
                return soFile.getAbsolutePath();
            }
        }

//        String basePath = OneWordFileStation.getBaseFilesPath();
//
//        if (basePath != null) {
//
//            File libsPathFile = new File(basePath, LIBS_DIR_NAME);
//
//            if (!libsPathFile.exists()) {
//                libsPathFile.mkdirs();
//            }
//
//            if (!loaded) {
//
//                try {
//
//                    ZipFile apkFile = new ZipFile(proxyedApkPath);
//
//                    List<FileHeader> fileHeaders = apkFile.getFileHeaders();
//
//                    List<FileHeader> soFileHeaders = new ArrayList();
//
//                    for (int fileHeaderIndex = 0; fileHeaderIndex < fileHeaders.size(); fileHeaderIndex++) {
//
//                        if (fileHeaders.get(fileHeaderIndex).getFileName().startsWith("lib" + File.separator)) {
//                            soFileHeaders.add(fileHeaders.get(fileHeaderIndex));
//                        }
//                    }
//
//
//                    for (int abiIndex = 0; abiIndex < Build.SUPPORTED_ABIS.length; abiIndex++) {
//
//                        boolean got = false;
//
//                        for (int soFileHeaderIndex = 0; soFileHeaderIndex < soFileHeaders.size(); soFileHeaderIndex++) {
//
//                            FileHeader fileHeader = soFileHeaders.get(soFileHeaderIndex);
//                            String filePrefix = "lib" + File.separator + Build.SUPPORTED_ABIS[abiIndex] + File.separator;
//                            if (fileHeader.getFileName().startsWith(filePrefix)) {
//
//                                apkFile.extractFile(fileHeader, libsPathFile.getAbsolutePath(), null, fileHeader.getFileName().substring(filePrefix.length()));
//
//                                got = true;
//                            }
//                        }
//
//                        if (got) {
//                            break;
//                        }
//                    }
//
//                    loaded = true;
//                } catch (ZipException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            File soFile = new File(libsPathFile.getAbsoluteFile(), System.mapLibraryName(name));
//            if (soFile.exists()) {
//                return soFile.getAbsolutePath();
//            }
//        }


        return super.findLibrary(name);
    }
}
