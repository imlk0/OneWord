package top.imlk.oneword.systemui.uifixer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by imlk on 2018/7/30.
 */
public class AOSPUIFixer extends BaseUIFixer {


    public AOSPUIFixer(TextView onerInfoTextView) {
        super(onerInfoTextView);
    }

    @Override
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


}
