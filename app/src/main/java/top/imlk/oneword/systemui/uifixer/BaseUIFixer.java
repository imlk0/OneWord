package top.imlk.oneword.systemui.uifixer;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.systemui.view.OneWordView;


/**
 * Created by imlk on 2018/7/30.
 */
public class BaseUIFixer {

    protected OneWordView oneWordView;

    public BaseUIFixer(TextView onerInfoTextView) {

        oneWordView = new OneWordView(onerInfoTextView.getContext());

        oneWordView.setTextSize(TypedValue.COMPLEX_UNIT_PX, onerInfoTextView.getTextSize());
        oneWordView.setTextColor(onerInfoTextView.getCurrentTextColor());

    }

    public void fixUI(TextView onerInfoTextView) {

        //添加到现有视图中

        ViewGroup parent = ((ViewGroup) onerInfoTextView.getParent());

        int index = parent.indexOfChild(onerInfoTextView);

        ViewGroup.LayoutParams layoutParams = onerInfoTextView.getLayoutParams();

        onerInfoTextView.setVisibility(View.GONE);

//        parent.removeView(onerInfoTextView);

//        XposedBridge.log("parent.addView(oneWordView, layoutParams);");

        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

        parent.addView(oneWordView, index + 1, layoutParams);


    }


    public void onSetOneWord(WordBean wordBean) {

        if (oneWordView == null) {
            return;
        }

        if (wordBean == null) {
            return;
        }

        // TODO 增加软件更新后重启提示


        oneWordView.setOneWord(wordBean);
    }

    public void onSetTextSize(int unit, float size) {
        oneWordView.setTextSize(unit, size);
    }

    public void onSetTextColor(int color) {
        oneWordView.setTextColor(color);
    }


}
