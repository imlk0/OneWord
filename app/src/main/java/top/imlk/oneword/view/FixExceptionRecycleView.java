package top.imlk.oneword.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by imlk on 2018/6/2.
 */


public class FixExceptionRecycleView extends RecyclerView {
    private static final String LOG_TAG = "FixExceptionRecycleView";

    public FixExceptionRecycleView(Context context) {
        super(context);
    }

    public FixExceptionRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixExceptionRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(new FixExceptionOnTouchListener(l));
    }


    static class FixExceptionOnTouchListener implements OnTouchListener {

        private final OnTouchListener proxyedListener;

        public FixExceptionOnTouchListener(OnTouchListener proxyedListener) {
            this.proxyedListener = proxyedListener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                return this.proxyedListener.onTouch(v, event);
            } catch (IllegalArgumentException e) {
                Log.i(LOG_TAG, "IllegalArgumentException catched", e);
            }
            return false;
        }
    }
}


/**
 * 06-02 13:01:33.052 19045-19045/top.imlk.oneword E/AndroidRuntime: FATAL EXCEPTION: main
 * Process: top.imlk.oneword, PID: 19045
 * java.lang.IllegalArgumentException: pointerIndex out of range
 * at android.view.MotionEvent.nativeGetAxisValue(Native Method)
 * at android.view.MotionEvent.getX(MotionEvent.java:2122)
 * at co.dift.ui.SwipeToAction$1.onTouch(SwipeToAction.java:128)
 * at android.view.View.dispatchTouchEvent(View.java:10040)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2626)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2307)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2632)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at com.android.internal.policy.DecorView.superDispatchTouchEvent(DecorView.java:418)
 * at com.android.internal.policy.PhoneWindow.superDispatchTouchEvent(PhoneWindow.java:1809)
 * at android.app.Activity.dispatchTouchEvent(Activity.java:3119)
 * at android.support.v7.view.WindowCallbackWrapper.dispatchTouchEvent(WindowCallbackWrapper.java:68)
 * at com.android.internal.policy.DecorView.dispatchTouchEvent(DecorView.java:380)
 * at android.view.View.dispatchPointerEvent(View.java:10264)
 * at android.view.ViewRootImpl$ViewPostImeInputStage.processPointerEvent(ViewRootImpl.java:4468)
 * at android.view.ViewRootImpl$ViewPostImeInputStage.onProcess(ViewRootImpl.java:4336)
 * at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:3883)
 * at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:3936)
 * at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:3902)
 * at android.view.ViewRootImpl$AsyncInputStage.forward(ViewRootImpl.java:4029)
 * at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:3910)
 * at android.view.ViewRootImpl$AsyncInputStage.apply(ViewRootImpl.java:4086)
 * at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:3883)
 * at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:3936)
 * at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:3902)
 * at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:3910)
 * at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:3883)
 * at android.view.ViewRootImpl.deliverInputEvent(ViewRootImpl.java:6284)
 * at android.view.ViewRootImpl.doProcessInputEvents(ViewRootImpl.java:6258)
 * at android.view.ViewRootImpl.enqueueInputEvent(ViewRootImpl.java:6219)
 * at android.view.ViewRootImpl$WindowInputEventReceiver.onInputEvent(ViewRootImpl.java:6387)
 * at android.view.InputEventReceiver.dispatchInputEvent(InputEventReceiver.java:185)
 * at android.view.InputEventReceiver.nativeConsumeBatchedInputEvents(Native Method)
 * at android.view.InputEventReceiver.consumeBatchedInputEvents(InputEventReceiver.java:176)
 * at android.view.ViewRootImpl.doConsumeBatchedInput(ViewRootImpl.java:6358)
 * at android.view.ViewRootImpl$ConsumeBatchedInputRunnable.run(ViewRootImpl.java:6410)
 * at android.view.Choreographer$CallbackRecord.run(Choreographer.java:874)
 * at android.view.Choreographer.doCallbacks(Choreographer.java:686)
 * at android.view.Choreographer.doFrame(Choreographer.java:615)
 * at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:860)
 * at android.os.Handler.handleCallback(Handler.java:751)
 * at android.os.Handler.dispatchMessage(Handler.java:95)
 * at android.os.Looper.loop(Looper.java:154)
 * at android.app.ActivityThread.main(ActivityThread.java:6234)
 * at java.lang.reflect.Method.invoke(Native Method)
 * at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:891)
 * at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:781)
 * at de.robv.android.xposed.XposedBridge.main(XposedBridge.java:107)
 */
