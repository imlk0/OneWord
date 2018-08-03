package top.imlk.oneword.application.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import top.imlk.oneword.application.client.activity.MainActivity;

/**
 * Created by imlk on 2018/5/23.
 */
public class PastedNestedScrollView extends NestedScrollView {
//    private static final long ANIMATED_SCROLL_GAP = 250;
//    private long mLastScroll;
//    private OverScroller mScroller;
//    private static final int DEFAULT_DURATION = 1000;

    private boolean canScroll = false;

    private boolean isOnTop = true;
    private Context context;
    private OnPasteListener mOnPasteListener;


    public interface OnPasteListener {

        void onPastedToTop();

        void onPastedToBottom();

    }

    public void setOnPasteListener(OnPasteListener listener) {
        this.mOnPasteListener = listener;
    }

    public void removeOnPasteListener(OnPasteListener listener) {
        if (this.mOnPasteListener == listener) {
            this.mOnPasteListener = null;
        }
    }

    public PastedNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public PastedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        updateContext(context);
    }

    public PastedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateContext(context);
    }

    public void updateContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return true;
        if (!canScroll) {
            return false;
        }

        Log.d("getScrollY()", getScrollY() + "");

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {


            if (getScrollY() > 2 * getHeight() / 3) {

                scrollToBottom();
                return true;
            } else {
                scrollToTop();
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
                canScroll = false;
                fling(0);
                smoothScrollTo(0, 0);

                if (mOnPasteListener != null) {
                    mOnPasteListener.onPastedToTop();
                }
            }
        });
        isOnTop = true;
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

                canScroll = true;
                fling(0);
                smoothScrollTo(0, getHeight());

                if (mOnPasteListener != null) {
                    mOnPasteListener.onPastedToBottom();
                }
            }
        });


        isOnTop = false;
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

    public boolean canScroll() {
        return canScroll;
    }

    public boolean isOnTop() {
        return isOnTop;
    }

}
