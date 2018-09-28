package top.imlk.oneword.systemui.view;

import android.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.text.BoringLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

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

    private void defaultSetOneWord() {
        setOneWord(WordBean.generateDefaultBean());
    }


    private void setContent() {

        boolean startLineInto = config == null ? true : config.startLineInto;


        if (startLineInto) {
            BoringLayout.Metrics metrics = BoringLayout.isBoring(wordBean.content, tvContent.getPaint());

//            Log.e("Boring imlk", metrics == null ? "no" : ("yes w:" + metrics.width));

            ViewGroup stableParent = llContent;

            int widthSpace = llContent.getPaddingLeft() + llContent.getPaddingRight();

            while (true) {


                // 当前所在View的LP
                ViewGroup.LayoutParams layoutParams = stableParent.getLayoutParams();

                ViewParent nextParent = stableParent.getParent();

                if (layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT && stableParent != llContent) {// 如果当前view宽度已经固定
                    break;
                }


                if (nextParent instanceof ViewGroup && ((ViewGroup) nextParent).getWidth() > 0) {//判断是否需要往上走


                    stableParent = ((ViewGroup) nextParent);


                    if (layoutParams instanceof MarginLayoutParams) {//把前一个view的margin加上
                        widthSpace += ((MarginLayoutParams) layoutParams).leftMargin;
                        widthSpace += ((MarginLayoutParams) layoutParams).rightMargin;
                    }

                    //加上下一个的padding
                    widthSpace += stableParent.getPaddingLeft();
                    widthSpace += stableParent.getPaddingRight();

                } else {

                    break;
                }
            }


            if (metrics != null && metrics.width >= stableParent.getWidth() - widthSpace) {

                setContentInternal("\u3000\u3000" + wordBean.content);
            } else {
                setContentInternal(wordBean.content);
            }
        } else {
            setContentInternal(wordBean.content);

        }
    }

    private void setContentInternal(String str) {
        if (config != null && config.toTraditional) {
            str = ChineseConverter.convert(str, ConversionType.S2T, getContext());
        }
        tvContent.setText(str);
    }


    private void setReference() {

        if (TextUtils.isEmpty(wordBean.reference)) {
            llReference.setVisibility(GONE);
        } else {
            llReference.setVisibility(VISIBLE);

            boolean refAddLine = config == null ? true : config.refAddLine;
            if (refAddLine) {
                setReferenceInternal("——" + wordBean.reference);
            } else {
                setReferenceInternal(wordBean.reference);
            }
        }
    }

    private void setReferenceInternal(String str) {
        if (config != null && config.toTraditional) {
            str = ChineseConverter.convert(str, ConversionType.S2T, getContext());
        }
        tvReference.setText(str);
    }


    public void applyWordViewConfig(@NonNull WordViewConfig config) {


        if (config != null) {

            this.config = config;

            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.textSize);

            this.setTextColor(config.textColor);

            //边距
            this.setPadding(config.disL, config.disT, config.disR, config.disB);
            LinearLayout.LayoutParams llReferenceLP = (LayoutParams) llReference.getLayoutParams();
            llReferenceLP.topMargin = config.disC;
            llReference.setLayoutParams(llReferenceLP);

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
                    tvReference.setGravity(Gravity.LEFT);
                    break;
                case 1:
                    llReference.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvReference.setGravity(Gravity.CENTER_HORIZONTAL);
                    break;
                default:
                case 2:
                    llReference.setGravity(Gravity.RIGHT);
                    tvReference.setGravity(Gravity.RIGHT);
                    break;
            }

            // 保守宽度
            ViewGroup.LayoutParams oneWordViewLP = this.getLayoutParams();
            if (oneWordViewLP != null) {
                if (config.guardWidth) {
                    oneWordViewLP.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    oneWordViewLP.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                } else {
                    oneWordViewLP.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    oneWordViewLP.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                this.setLayoutParams(oneWordViewLP);
            }


            // Reference倾斜
            if (config.refItalic) {
                tvReference.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            } else {
                tvReference.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }

            post(new Runnable() {//推迟配置
                @Override
                public void run() {
                    setOneWord(OneWordView.this.wordBean);
                }
            });

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


    public WordBean getCurWordBean() {
        return wordBean;
    }

    public WordViewConfig getCurWordViewConfig() {
        return config;
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
