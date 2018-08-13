package top.imlk.oneword.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;

import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;

/**
 * Created by imlk on 2018/8/7.
 */
public class ShareUtil {
    public static void shareAPI(Context context, ApiBean apiBean) {
        String result = Base64.encodeToString(new Gson().toJson(apiBean, ApiBean.class).getBytes(), Base64.DEFAULT);

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("锁屏一言API", result));
        Toast.makeText(context, "成功复制到剪切板，快去分享给别人吧", Toast.LENGTH_SHORT).show();

    }

    public static ApiBean parseReceivedAPI(Context context, String encoded) {
        ApiBean apiBean = null;
        try {
            apiBean = new Gson().fromJson(new String(Base64.decode(encoded, Base64.DEFAULT)), ApiBean.class);
            apiBean.id = -1;
            apiBean.enabled = true;
        } catch (Throwable t) {
            Toast.makeText(context, "API解析失败，因为：" + t.toString(), Toast.LENGTH_LONG).show();
        }
        return apiBean;
    }


    public static void shareOneWord(Context context, WordBean wordBean) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("锁屏一言", wordBean.content + "——" + wordBean.reference));
        Toast.makeText(context, "成功复制到剪切板", Toast.LENGTH_SHORT).show();

    }


}
