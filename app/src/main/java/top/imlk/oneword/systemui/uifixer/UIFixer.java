package top.imlk.oneword.systemui.uifixer;

import android.widget.TextView;


/**
 * Created by imlk on 2018/7/30.
 */
public interface UIFixer {

    void fixUI(TextView onerInfoTextView);

    void onSetTextSize(int unit, float size);

    void onSetText(CharSequence text, TextView.BufferType type);

    void onSetTextColor(int color);

}
