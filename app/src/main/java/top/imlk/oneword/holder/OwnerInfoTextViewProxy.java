package top.imlk.oneword.holder;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import top.imlk.oneword.R;

import static top.imlk.oneword.common.StaticValue.SPILITER;

/**
 * Created by imlk on 2018/5/28.
 */
@SuppressLint("AppCompatCustomView")
public class OwnerInfoTextViewProxy extends TextView implements View.OnClickListener {

    private TextView proxyedTextView;
    private TextView customTextView;

    public OwnerInfoTextViewProxy(Context context, TextView proxyedTextView) {
        super(context);
        this.proxyedTextView = proxyedTextView;
        this.proxyedTextView.setOnClickListener(this);
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


    //<TextView android:textSize="@r$dimen/widget_label_font_size" android:textColor="@color/clock_gray" android:ellipsize="marquee" android:layout_gravity="center_horizontal" android:id="@r$id/owner_info" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_marginLeft="16dp" android:layout_marginTop="@dimen/date_owner_info_margin" android:layout_marginRight="16dp" android:singleLine="true" android:letterSpacing="0.05"/>
    public TextView getCustomTextView() {
        if (customTextView == null && getProxyedTextView() != null) {
            customTextView = new TextView(proxyedTextView.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(((ViewGroup.MarginLayoutParams) proxyedTextView.getLayoutParams()));
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            customTextView.setLayoutParams(layoutParams);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                customTextView.setElegantTextHeight(true);
//            }
//            customTextView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            customTextView.setSingleLine(true);
//            customTextView.setTextScaleX(-0.5f);//设置字体右斜,会造成错误
            customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, proxyedTextView.getTextSize());
            customTextView.setTextColor(proxyedTextView.getCurrentTextColor());
            customTextView.setGravity(Gravity.RIGHT);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                customTextView.setLetterSpacing(0.05f);
            }
            customTextView.setOnClickListener(this);
        }
        return customTextView;
    }

    @Override
    public void setTextSize(int unit, float size) {
        if (getProxyedTextView() != null) {
            proxyedTextView.setTextSize(unit, size);
        }

        if (getCustomTextView() != null) {
            customTextView.setTextSize(unit, size);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String[] strings = text.toString().split(SPILITER);
        if (getProxyedTextView() != null) {
            String strForProxy = strings[0];
            for (int i = 1; i < strings.length - 1; ++i) {
                strForProxy = strForProxy + SPILITER + strings[i];
            }
            proxyedTextView.setText(strForProxy);
        }

        if (strings.length == 1) {
            if (getCustomTextView() != null) {
                customTextView.setText("");
            }
        } else {
            if (getCustomTextView() != null) {
                customTextView.setText("——" + strings[strings.length - 1]);
            }
        }

//        if (getCustomTextView() != null && getProxyedTextView() != null) {
//            customTextView.setWidth(proxyedTextView.getWidth());
//            customTextView.setTextSize(proxyedTextView.getTextSize());
//        }
    }

    @Override
    public void setSelected(boolean selected) {
        if (getProxyedTextView() != null) {
            proxyedTextView.setSelected(selected);
        }
        if (getCustomTextView() != null) {
            customTextView.setSelected(selected);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (getProxyedTextView() != null) {
            proxyedTextView.setVisibility(visibility);
        }

        if (getCustomTextView() != null) {
            customTextView.setVisibility(visibility);
        }
    }

    public TextView getProxyedTextView() {
        return proxyedTextView;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("top.imlk.oneword", "top.imlk.oneword.client.MainActivity");
        intent.setComponent(cn);
        v.getContext().startActivity(intent);

    }
}
