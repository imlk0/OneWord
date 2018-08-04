package top.imlk.oneword.systemui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.systemui.uifixer.BaseUIFixer;


/**
 * Created by imlk on 2018/5/28.
 */


/**
 * 用来放到KeyguardStatusView里作为成员变量
 */
@SuppressLint("AppCompatCustomView")
public class OwnerInfoTextViewProxy extends TextView {

    private BaseUIFixer uiFixer;


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

    }

    @Override
    public void setTextColor(int color) {
        if (uiFixer != null) {
            uiFixer.onSetTextColor(color);
        }
    }

    public void setOneWord(WordBean word) {
        if (uiFixer != null) {
            uiFixer.onSetOneWord(word);
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


    public void setUiFixer(BaseUIFixer uiFixer) {
        this.uiFixer = uiFixer;
    }
}
