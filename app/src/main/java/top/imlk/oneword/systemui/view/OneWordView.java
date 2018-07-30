package top.imlk.oneword.systemui.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.imlk.oneword.bean.Word;

/**
 * Created by imlk on 2018/7/30.
 */
public class OneWordView extends LinearLayout {

    TextView contentTextView;

    TextView fromTextView;


    //<TextView android:textSize="@r$dimen/widget_label_font_size" android:textColor="@color/clock_gray" android:ellipsize="marquee" android:layout_gravity="center_horizontal" android:id="@r$id/owner_info" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_marginLeft="16dp" android:layout_marginTop="@dimen/date_owner_info_margin" android:layout_marginRight="16dp" android:singleLine="true" android:letterSpacing="0.05"/>

    public OneWordView(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);


        // 正文View的样式
        contentTextView = new TextView(context);
        contentTextView.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams contentLP = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        // 正文外包裹一层LinearLayout
        LinearLayout contentPa = new LinearLayout(context);
        contentPa.setGravity(Gravity.CENTER);
        contentPa.addView(contentTextView, contentLP);

        LinearLayout.LayoutParams contentPaLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        contentPaLP.leftMargin = 40;
        contentPaLP.rightMargin = 40;
        contentPaLP.topMargin = 10;
        contentPaLP.bottomMargin = 0;

        this.addView(contentPa, contentPaLP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            contentTextView.setElegantTextHeight(true);
        }
        contentTextView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        contentTextView.setSingleLine(false);

        // 字体间距
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            contentTextView.setLetterSpacing(0.05f);
        }


        // 设置来源TextView
        fromTextView = new TextView(context);
        fromTextView.setGravity(Gravity.RIGHT);

        LinearLayout.LayoutParams fromLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 来源TV外包裹一层LL
        LinearLayout fromPa = new LinearLayout(context);

        fromPa.addView(fromTextView, fromLP);

        LinearLayout.LayoutParams fromPaLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        fromPaLP.leftMargin = 40;
        fromPaLP.rightMargin = 40;
        fromPaLP.topMargin = 10;
        fromPaLP.bottomMargin = 10;
        this.addView(fromPa, fromPaLP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fromTextView.setElegantTextHeight(true);
        }
        fromTextView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        fromTextView.setSingleLine(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fromTextView.setLetterSpacing(0.05f);
        }

    }


    public void setOneWord(Word word) {
        contentTextView.setText(word.content);
        fromTextView.setText("——" + word.from);
    }

    public void setTextSize(int unit, float size) {
        contentTextView.setTextSize(unit, size);
        fromTextView.setTextSize(unit, size);

    }

    public void setTextColor(int color) {
        contentTextView.setTextColor(color);
        fromTextView.setTextColor(color);
    }

//    @Override
//    public void onClick(View v) {
//
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        ComponentName cn = new ComponentName("top.imlk.oneword", "top.imlk.oneword.application.client.activity.MainActivity");
//        intent.setComponent(cn);
//        v.getContext().startActivity(intent);
//
//    }


}
