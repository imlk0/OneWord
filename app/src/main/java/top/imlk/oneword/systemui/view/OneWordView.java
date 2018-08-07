package top.imlk.oneword.systemui.view;

import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.imlk.oneword.bean.WordBean;

/**
 * Created by imlk on 2018/7/30.
 */
public class OneWordView extends LinearLayout {

    private LinearLayout contentPa;

    private TextView contentTextView;


    private LinearLayout referencePa;

    private TextView referenceTextView;


    //<TextView android:textSize="@r$dimen/widget_label_font_size" android:textColor="@color/clock_gray" android:ellipsize="marquee" android:layout_gravity="center_horizontal" android:id="@r$id/owner_info" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_marginLeft="16dp" android:layout_marginTop="@dimen/date_owner_info_margin" android:layout_marginRight="16dp" android:singleLine="true" android:letterSpacing="0.05"/>

    public OneWordView(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);


        // 正文View的样式
        contentTextView = new TextView(context);
        contentTextView.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams contentLP = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        // 正文外包裹一层LinearLayout
        contentPa = new LinearLayout(context);
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
        referenceTextView = new TextView(context);
        referenceTextView.setGravity(Gravity.RIGHT);

        LinearLayout.LayoutParams referenceLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 来源TV外包裹一层LL

        referencePa = new LinearLayout(context);

        referencePa.addView(referenceTextView, referenceLP);

        LinearLayout.LayoutParams referencePaLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        referencePaLP.leftMargin = 40;
        referencePaLP.rightMargin = 40;
        referencePaLP.topMargin = 10;
        referencePaLP.bottomMargin = 10;
        this.addView(referencePa, referencePaLP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            referenceTextView.setElegantTextHeight(true);
        }
        referenceTextView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        referenceTextView.setSingleLine(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            referenceTextView.setLetterSpacing(0.05f);
        }

    }


    public void setOneWord(WordBean wordBean) {
        if (wordBean != null) {
            contentTextView.setText("\u3000\u3000" + wordBean.content);
            if (TextUtils.isEmpty(wordBean.reference)) {
                referencePa.setVisibility(GONE);
            } else {
                referencePa.setVisibility(VISIBLE);
                referenceTextView.setText("——" + wordBean.reference);
            }
        } else {
            defaultSetOneWord();
        }
    }

    public void defaultSetOneWord() {
        setOneWord(new WordBean("句子正文，\n喵喵喵喵喵喵喵喵", "来源"));
    }

    public void setTextSize(int unit, float size) {
        contentTextView.setTextSize(unit, size);
        referenceTextView.setTextSize(unit, size);

    }

    public void setTextColor(int color) {
        contentTextView.setTextColor(color);
        referenceTextView.setTextColor(color);
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
