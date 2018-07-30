package top.imlk.oneword.systemui.uifixer;

import android.annotation.CallSuper;
import android.util.TypedValue;
import android.widget.TextView;

import org.w3c.dom.Text;

import top.imlk.oneword.bean.Word;
import top.imlk.oneword.systemui.view.OneWordView;

import static top.imlk.oneword.StaticValue.SPILITER;

/**
 * Created by imlk on 2018/7/30.
 */
public class BaseUIFixer implements UIFixer {

    protected OneWordView oneWordView;

    public BaseUIFixer(TextView onerInfoTextView) {

        oneWordView = new OneWordView(onerInfoTextView.getContext());

        oneWordView.setTextSize(TypedValue.COMPLEX_UNIT_PX, onerInfoTextView.getTextSize());
        oneWordView.setTextColor(onerInfoTextView.getCurrentTextColor());

    }

    @Override
    public void fixUI(TextView onerInfoTextView) {


    }

    @Override
    public void onSetText(CharSequence text, TextView.BufferType type) {

        Word word = new Word();

        String[] strings = text.toString().split(SPILITER);
        String strForProxy = strings[0];
        for (int i = 1; i < strings.length - 1; ++i) {
            strForProxy = strForProxy + SPILITER + strings[i];
        }

        word.content = strForProxy;

        if (strings.length == 1) {
            word.from = "";
        } else {
            word.from = strings[strings.length - 1];
        }


        if (oneWordView != null) {
            oneWordView.setOneWord(word);
        }
    }

    @Override
    public void onSetTextSize(int unit, float size) {
        oneWordView.setTextSize(unit, size);
    }

    @Override
    public void onSetTextColor(int color) {
        oneWordView.setTextColor(color);
    }


}
