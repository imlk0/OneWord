package top.imlk.oneword.systemui.view;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.imlk.oneword.systemui.uifixer.UIFixer;

import static top.imlk.oneword.StaticValue.SPILITER;

/**
 * Created by imlk on 2018/5/28.
 */


/**
 * 用来放到KeyguardStatusView里作为成员变量
 */
@SuppressLint("AppCompatCustomView")
public class OwnerInfoTextViewProxy extends TextView {

    private UIFixer uiFixer;


    public OwnerInfoTextViewProxy(Context context) {
        super(context);
    }

    public OwnerInfoTextViewProxy(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OwnerInfoTextViewProxy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OwnerInfoTextViewProxy(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setTextSize(int unit, float size) {
        if (uiFixer != null) {
            uiFixer.onSetTextSize(unit, size);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (uiFixer != null) {
            uiFixer.onSetText(text, type);
        }
    }

    @Override
    public void setTextColor(int color) {
        if (uiFixer != null) {
            uiFixer.onSetTextColor(color);
        }
    }

//    @Override
//    public void setSelected(boolean selected) {
//        if (getProxyedTextView() != null) {
//            proxyedTextView.setSelected(selected);
//        }
//        if (getCustomTextView() != null) {
//            customTextView.setSelected(selected);
//        }
//    }

//    @Override
//    public void setVisibility(int visibility) {
//        if (getProxyedTextView() != null) {
//            proxyedTextView.setVisibility(visibility);
//        }
//
//        if (getCustomTextView() != null) {
//            customTextView.setVisibility(visibility);
//        }
//    }


    public void setUiFixer(UIFixer uiFixer) {
        this.uiFixer = uiFixer;
    }
}
