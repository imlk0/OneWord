package top.imlk.oneword.systemui.view;

import android.annotation.NonNull;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.BoringLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;

/**
 * Created by imlk on 2018/7/30.
 */
public class OneWordView extends LinearLayout {

    private LinearLayout llContent;

    private TextView tvContent;

    private LinearLayout llReference;

    private TextView tvReference;

    private WordBean wordBean;

    private WordViewConfig config;

    //<TextView android:textSize="@r$dimen/widget_label_font_size" android:textColor="@color/clock_gray" android:ellipsize="marquee" android:layout_gravity="center_horizontal" android:id="@r$id/owner_info" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_marginLeft="16dp" android:layout_marginTop="@dimen/date_owner_info_margin" android:layout_marginRight="16dp" android:singleLine="true" android:letterSpacing="0.05"/>
//    14sp

    public OneWordView(Context context) {
        super(context);

        this.setOrientation(LinearLayout.VERTICAL);


        // 正文View的样式
        tvContent = new TextView(context);
        tvContent.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams contentLP = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        // 正文外包裹一层LinearLayout
        llContent = new LinearLayout(context);
        llContent.setGravity(Gravity.CENTER);
        llContent.addView(tvContent, contentLP);

        LinearLayout.LayoutParams contentPaLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

//        contentPaLP.leftMargin = 40;
//        contentPaLP.rightMargin = 40;
//        contentPaLP.topMargin = 10;
//        contentPaLP.bottomMargin = 0;

        this.addView(llContent, contentPaLP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvContent.setElegantTextHeight(true);
        }
        tvContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        tvContent.setSingleLine(false);

        // 字体间距
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvContent.setLetterSpacing(0.05f);
        }


        // 设置来源TextView
        tvReference = new TextView(context);

        LinearLayout.LayoutParams referenceLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        // 来源TV外包裹一层LL

        llReference = new LinearLayout(context);
        llReference.setGravity(Gravity.RIGHT);
        llReference.addView(tvReference, referenceLP);

        LinearLayout.LayoutParams referencePaLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

//        referencePaLP.leftMargin = 40;
//        referencePaLP.rightMargin = 40;
//        referencePaLP.topMargin = 10;
//        referencePaLP.bottomMargin = 10;
        this.addView(llReference, referencePaLP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvReference.setElegantTextHeight(true);
        }
//        tvReference.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        tvReference.setSingleLine(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvReference.setLetterSpacing(0.05f);
        }


    }


    public void setOneWord(WordBean wordBean) {
        if (wordBean != null && wordBean.content != null) {
            this.wordBean = wordBean;

            setContent();
            setReference();

        } else {
            defaultSetOneWord();
        }
    }


    private void setContent() {

        boolean startLineInto = config == null ? true : config.startLineInto;


        if (startLineInto) {
            BoringLayout.Metrics metrics = BoringLayout.isBoring(wordBean.content, tvContent.getPaint());

//            Log.e("Boring imlk", metrics == null ? "no" : ("yes w:" + metrics.width));

            if (metrics != null && metrics.width >= llContent.getWidth()) {

                tvContent.setText("\u3000\u3000" + wordBean.content);
            } else {
                tvContent.setText(wordBean.content);
            }
        } else {
            tvContent.setText(wordBean.content);

        }


    }

    private void setReference() {

        if (TextUtils.isEmpty(wordBean.reference)) {
            llReference.setVisibility(GONE);
        } else {
            llReference.setVisibility(VISIBLE);

            boolean refAddLine = config == null ? true : config.refAddLine;
            if (refAddLine) {
                tvReference.setText("——" + wordBean.reference);
            } else {
                tvReference.setText(wordBean.reference);
            }
        }
    }


    private void defaultSetOneWord() {
        setOneWord(WordBean.generateDefaultBean());
    }


    public void applyWordViewConfig(@NonNull WordViewConfig config) {


        if (config != null) {

            this.config = config;

            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.textSize);

            this.setTextColor(config.textColor);

            //边距
            this.setPadding(config.disL, config.disT, config.disR, config.disB);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) llReference.getLayoutParams();
            layoutParams.topMargin = config.disC;
            llReference.setLayoutParams(layoutParams);

            //文本位置
            switch (config.conPos) {
                case 0:
                    llContent.setGravity(Gravity.LEFT);
                    break;
                default:
                case 1:
                    llContent.setGravity(Gravity.CENTER_HORIZONTAL);
                    break;
                case 2:
                    llContent.setGravity(Gravity.RIGHT);
                    break;
            }
            switch (config.refPos) {
                case 0:
                    llReference.setGravity(Gravity.LEFT);
                    break;
                case 1:
                    llReference.setGravity(Gravity.CENTER_HORIZONTAL);
                    break;
                default:
                case 2:
                    llReference.setGravity(Gravity.RIGHT);
                    break;
            }

            // Reference倾斜

            if (config.refItalic) {
                tvReference.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            } else {
                tvReference.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }

            setOneWord(this.wordBean);

        } else {
            defaultApplyWordViewConfig();
        }

    }

    private void defaultApplyWordViewConfig() {
        applyWordViewConfig(WordViewConfig.generateDefaultBean());
    }

    private void setTextSize(int unit, float size) {
        tvContent.setTextSize(unit, size);
        tvReference.setTextSize(unit, size);

    }

    private void setTextColor(int color) {
        tvContent.setTextColor(color);
        tvReference.setTextColor(color);
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
