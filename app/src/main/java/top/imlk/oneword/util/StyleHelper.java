package top.imlk.oneword.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

/**
 * Created by imlk on 2018/6/2.
 */
public class StyleHelper {
    public static int getColorByAttributeId(Context context, @AttrRes int attrIdForColor) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attrIdForColor, typedValue, true);
        return typedValue.data;
    }
}
