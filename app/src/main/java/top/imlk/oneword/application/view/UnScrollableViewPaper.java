package top.imlk.oneword.application.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by imlk on 2018/5/23.
 */
public class UnScrollableViewPaper extends ViewPager {
    public UnScrollableViewPaper(@NonNull Context context) {
        super(context);
    }

    public UnScrollableViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d("onTouchEvent","onTouchEvent");
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d("onInterceptTouchEvent","onInterceptTouchEvent");
        return false;
    }
}