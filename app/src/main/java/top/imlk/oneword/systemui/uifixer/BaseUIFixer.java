package top.imlk.oneword.systemui.uifixer;

import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.systemui.view.OneWordView;


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

        if (oneWordView == null) {
            return;
        }

        WordBean wordBean = null;

        try {
            wordBean = new Gson().fromJson(text.toString(), WordBean.class);
        } catch (JsonSyntaxException e) {
            // TODO 增加软件更新后重启提示

            Toast.makeText(oneWordView.getContext(), "解析保存的一言失败，去设置一言？", Toast.LENGTH_SHORT).show();
        }

        oneWordView.setOneWord(wordBean);
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
