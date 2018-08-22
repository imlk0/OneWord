package top.imlk.oneword.systemui.uifixer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.robv.android.xposed.XposedBridge;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.systemui.view.OneWordView;
import top.imlk.oneword.util.BroadcastSender;


/**
 * Created by imlk on 2018/7/30.
 */
public class BaseUIFixer implements View.OnClickListener {

    private OneWordView oneWordView;

    public BaseUIFixer(TextView onerInfoTextView) {

        oneWordView = new OneWordView(onerInfoTextView.getContext());
        oneWordView.setOnClickListener(this);
        XposedBridge.log("oneWordView.setOnClickListener");
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

    public void onSetTextSize(int unit, float size) {
//        oneWordView.setTextSize(unit, size);
    }

    public void onSetTextColor(int color) {
//        oneWordView.setTextColor(color);
    }


    public void setOneWord(WordBean word) {
        if (oneWordView != null) {
            oneWordView.setOneWord(word);
        }
    }

    public void applyWordViewConfig(WordViewConfig config) {
        if (oneWordView != null) {
            oneWordView.applyWordViewConfig(config);
        }
    }

    @Override
    public void onClick(View v) {
        WordBean wordBean = oneWordView.getCurWordBean();
        if (wordBean != null) {
            BroadcastSender.startMainActivityWhenClicked(v.getContext(), wordBean);
        }
    }
}
