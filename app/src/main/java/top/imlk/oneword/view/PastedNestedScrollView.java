package top.imlk.oneword.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by imlk on 2018/5/23.
 */
public class PastedNestedScrollView extends NestedScrollView {
//    private static final long ANIMATED_SCROLL_GAP = 250;
//    private long mLastScroll;
//    private OverScroller mScroller;
//    private static final int DEFAULT_DURATION = 1000;


    public boolean canScroll = false;

    public PastedNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public PastedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PastedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return true;
        if (!canScroll) {
            return false;
        }

        Log.d("getScrollY()", getScrollY() + "");

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {


            if (getScrollY() > getHeight() / 3) {

                scrollToBottom();
                canScroll = true;
                return true;
            } else {
                scrollToTop();
                canScroll = false;
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void scrollToTop() {
        Log.d("scrollToTop", "scrollToTop");
        this.post(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                fling(0);
                smoothScrollTo(0, 0);
            }
        });
    }


    public void scrollToBottom() {
        Log.d("scrollToBottom", "scrollToBottom");

        this.post(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                fullScroll(NestedScrollView.FOCUS_DOWN);

                fling(0);
                smoothScrollTo(0, getHeight());
            }
        });
    }


    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        if (getScrollY() > 2 * getHeight() / 3) {
            scrollToBottom();
            canScroll = true;
        } else {
            scrollToTop();
            canScroll = false;
        }
    }


}
