package top.imlk.oneword.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.text.TextUtils;
import android.util.TypedValue;

import top.imlk.oneword.R;

/**
 * Created by imlk on 2018/6/2.
 */
public class AppStyleHelper {

    public static int getColorByAttributeId(Context context, @AttrRes int attrIdForColor) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attrIdForColor, typedValue, true);
        return typedValue.data;
    }



//    private static final int themes[] = {
//            R.style.RedTheme,
//            R.style.AmberTheme,
//            R.style.BlueTheme,
//            R.style.BlueGreyTheme,
//            R.style.BrownTheme,
//            R.style.CyanTheme,
//            R.style.DeepOrangeTheme,
//            R.style.DeepPurpleTheme,
//            R.style.GreenTheme,
//            R.style.GreyTheme,
//            R.style.IndigoTheme,
//            R.style.LightBlueTheme,
//            R.style.LimeTheme,
//            R.style.OrangeTheme,
//            R.style.PinkTheme,
//            R.style.PurpleTheme,
//            R.style.TealTheme,
//            R.style.YellowTheme,
//    };

//    /**
//     * {@link top.imlk.oneword.R.style#GreenTheme}
//     */
//    public static final String DEFAULT_THEME_NAME = "GreenTheme";
//
//    public static int getConfigedThemeId(Context context) {
//        String themeName = SharedPreferencesUtil.getSelectedThemeName(context);
//
//        int id = 0;
//        try {
//            id = (int) R.style.class.getDeclaredField(themeName).get(null);
//        } catch (Throwable throwable) {
//        }
//
//        return id == 0 ? R.style.GreenTheme : id;
//    }
//
}
